package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;
import java.util.NoSuchElementException;
import java.lang.String;

public class ManualInputServer {

    public final static int DEFAULT_PORT_NUMBER = 1478;
    public final static String DEFAULT_NAME = "mur:America";
    public final static String DEFAULT_PREFIX = "mur:";
    
    
    public final static String ARG_PORT = "--port";
    public final static String ARG_NAME = "--name";
    public final static String ARG_INTNL_WALL = "--intwalls";
    
    public final static String eoln = "\r\n";
    
    private int port;
    private String name;
    private int playerCount;
    protected Board board;
    protected int thisServersPlayerNumber;
    protected boolean useInternalWallPos;
    
    // Main that uses the command line arguments
    public static void main(String[] args) {
          
        // This sets the defaults
        int port = DEFAULT_PORT_NUMBER;
        String name = DEFAULT_NAME;
        int argNdx = 0;
        boolean intnlWalls = false;

        System.err.println("argl=" + args.length);
        // This runs through all of the command line arguments and applies the proper ones
        while (argNdx < args.length) {
            String curr = args[argNdx];

            if (curr.equals(ARG_PORT)) {
                ++argNdx;

                String numberStr = args[argNdx];
                port = Integer.parseInt(numberStr);
            } else if(curr.equals(ARG_NAME)) {
                ++argNdx;
            
                name = DEFAULT_PREFIX + args[argNdx];
                
            } else if(curr.equals(ARG_INTNL_WALL)) {
                intnlWalls = true;
              
            } else {

                // if there is an unknown parameter, give usage and quit
                System.err.println("Unknown parameter \"" + curr + "\"");
                usage();
                System.exit(1);
            }

            ++argNdx;
        }

        ManualInputServer ms = new ManualInputServer(port, name, intnlWalls);
        ms.run();
    }
    
    
    // Lets them know if they put in an invalid argument
    private static void usage() {
        System.err.print("usage: java ManualInputServer [options]\n" +
            "       where options:\n" + "       --port port\n");
    }
    
    // Constructor
    public ManualInputServer(int initPort, String initName, boolean intWalls){
        port = initPort;
        name = initName;
        useInternalWallPos = intWalls;
    }
    
    public void run() {
        try {
            // This sets up the socket the server will listen at
            ServerSocket server = new ServerSocket(port);
            
            // There to let you know its working
            System.out.println("Server now accepting connections "+
            "on port " + port);
            
            // There for when the client does connects
            Socket client;
            
            // Here it waits for a connection to be made
            while((client = server.accept()) != null) {
                System.out.println("Connection from " + client);
                
                // Makes the in and out functions of the server
                Scanner cin = new Scanner(client.getInputStream());
                PrintStream cout = new PrintStream(client.getOutputStream());
                
                Thread.sleep(100);
                
                establishProtocol(cin, cout);
                
                Thread.sleep(100);
                
                while(true) {
                    String clientMessage = cin.nextLine();
                    System.err.println("Recieved: \"" + clientMessage + "\"");
                    if(clientMessage.startsWith("MYOUSHU")){
                        sendMove(cout);
                        Thread.sleep(1000);
                    }
                    else if(clientMessage.startsWith("ATARI")){
                        updateBoard(clientMessage);
                    }
                    else if(clientMessage.startsWith("GOTE")) {
                        removePlayer(clientMessage);
                    }
                    else if(clientMessage.startsWith("KIKASHI")){
                        winnerDeclared(System.out, clientMessage);
                    }
                }
            }
        } catch(NoSuchElementException ign) {
          // Ignored
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    // This method handles the initial contact protocol
    // It gets passed a scanner on the client and a print stream to the client
    public void establishProtocol(Scanner cin, PrintStream cout) {
        String clientMessage = cin.nextLine();
        System.err.println("Recieved: \"" + clientMessage + "\"");
        if(!clientMessage.equals("HELLO")){
            System.out.println("Incorrect contact protocol!");
            cin.close();
            cout.close();
            return;
        }
        cout.print("IAM " + name + "\r\n");
        System.err.println("Sent: \"IAM " + name + "\"");
        clientMessage = cin.nextLine();
        System.err.println("Recieved: \"" + clientMessage + "\"");
        String[] players = clientMessage.split(" ");
        thisServersPlayerNumber = Integer.parseInt(players[1]);
        playerCount = players.length - 2;
        System.err.println("Player number: " + thisServersPlayerNumber);
        
        // Creates the board now
        board = new Board(playerCount);
    }
    
    /*
     * This takes a string in the format of  "t c r"
     * where t is the type of move being either m = move, v = verticle wall or
     * h = horizontal wall. It then returns the String in the correct format 
     * for the protocol 
    */
    public String moveWrapper(String move) {
        // System.err.println("moveWrapper received:" + move);
        String message = "TESUJI ";
        String[] splitMessage = move.split(" ");
        if(move.startsWith("m ")){
            message += "(" + splitMessage[1] + ", " + splitMessage[2] + ")";
            return message + "\r\n";
        }
        else if(move.startsWith("v ") || move.startsWith("h ")) {
            int wx = Integer.parseInt(splitMessage[1]);
            int wy = Integer.parseInt(splitMessage[2]);
            
            if(this.useInternalWallPos) {
                if(splitMessage[0].charAt(0) == 'h') {
                  wy -= 1;
                } else {
                  wx -= 1;
                }
                message += "[(" + (wx) + ", " + (wy) + ")";
            } else {
                message += "[(" + (wx) + ", " + (wy) + ")";
            }
            
            message += ", " + splitMessage[0] + "]";
            return message + "\r\n";
        }
        return "";
    }
    
    public void sendMove(PrintStream cout) throws Exception{
        Scanner console = new Scanner(System.in);
        sendMove(cout, console);
    }
    
    public void sendMove(PrintStream cout, Scanner console) {
        System.out.print("Enter \"m\" to move your peice, or \"w\" to place a wall: ");
        String moveType = console.next();
	String unwrappedMessage = "";
        if (moveType.equals("m")) {
            unwrappedMessage = moveType + " ";
            System.out.print("What column would you like to move to: ");
            unwrappedMessage += console.next() + " ";
            System.out.print("What row would you like to move to: ");
            unwrappedMessage += console.next();
        }
        else if(moveType.equals("w")){
            System.out.print("Enter \"h\" for a horizontal wall or \"v\" for" +
                                   " a vertical wall: ");
            unwrappedMessage = console.next() + " ";
            System.out.print("Enter the column: ");
            unwrappedMessage += console.next() + " ";
            System.out.print("Enter the row: ");
            unwrappedMessage += console.next();
        }
        
        System.out.println(moveWrapper(unwrappedMessage));
        cout.print(moveWrapper(unwrappedMessage));
    }
    
    public void updateBoard(String message) {
        // System.err.println("updateBoard():" + message);
        message = message.substring(6).replaceAll("\\s","");
        // System.err.println("  upd0:" + message);
        int playerNumber = message.charAt(0) - '0';
        // System.err.println("  pid:" + playerNumber);
        
        message = message.substring(1);
        // System.err.println("  upd1:" + message);
        if(message.startsWith("[")) {
            Orientation ort = null;
            int column = message.charAt(2) - '0';
            int row = message.charAt(4) - '0';
            if(message.charAt(7) == 'h') {
                ort = Orientation.HORIZ;
                row += 1;
            } else {
                ort = Orientation.VERT;
                column += 1;
            }
            Coord coord = new Coord(column, row);
            board.placeWall(playerNumber - 1, coord, ort);
        }
        else if(message.startsWith("(")){
            int column = message.charAt(1) - '0';
            int row = message.charAt(3) - '0';
            Coord coord = new Coord(column, row);
            board.movePlayer(playerNumber - 1, coord);
        }
        
    }
    
    //
    public Board getBoard(){
        return board;
    }
    
    // 
    public void removePlayer(String message){
        message = message.substring(5).replaceAll("\\s","");
        board.removePlayer((int)message.charAt(0)-(int)'0'-1);
    }
    
    public void winnerDeclared(PrintStream console, String message){
        message = message.substring(7).replaceAll("\\s","");
        if((int)message.charAt(0)-(int)'0' == thisServersPlayerNumber) {
            console.print("Congratulations you have won the game!\n");
        } else{
            console.print("Sorry you didn't win this time, better luck next time.\n");
        }
    }
}