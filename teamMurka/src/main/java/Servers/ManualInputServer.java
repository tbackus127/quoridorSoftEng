package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;

public class ManualInputServer {

    public final static int DEFAULT_PORT_NUMBER = 1478;
    public final static String DEFAULT_NAME = "mur:America";
    public final static String DEFAULT_PREFIX = "mur:";
    
    
    public final static String ARG_PORT = "--port";
    public final static String ARG_NAME = "--name";
    
    public final static String eoln = "\r\n";
    
    private int port;
    private String name;
    private int playerCount;
    private Board board;
    private int thisServersPlayerNumber;
    
    // Main that uses the command line arguments
    public static void main(String[] args) {
	// This sets the defaults
	int port = DEFAULT_PORT_NUMBER;
	String name = DEFAULT_NAME;
	
	int argNdx = 0;

        // This runs through all of the command line arguments and applies the proper ones
        while (argNdx < args.length) {
            String curr = args[argNdx];

            if (curr.equals(ARG_PORT)) {
                ++argNdx;

		String numberStr = args[argNdx];
		port = Integer.parseInt(numberStr);
	    } else if(curr.equals(ARG_NAME)){
		++argNdx;
		
		name = DEFAULT_PREFIX + args[argNdx];
	    } else {

                // if there is an unknown parameter, give usage and quit
                System.err.println("Unknown parameter \"" + curr + "\"");
                usage();
                System.exit(1);
            }

        ++argNdx;
        }

	ManualInputServer ms = new ManualInputServer(port, name);
	ms.run();
    }
    
    // Lets them know if they put in an invalid argument
    private static void usage() {
        System.err.print("usage: java BirthdayServer [options]\n" +
            "       where options:\n" + "       --port port\n");
    }
    
    // Constructor
    public ManualInputServer(int initPort, String initName){
	port = initPort;
	name = initName;
    }
    
    public void run(){
	try{
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
		Scanner console = new Scanner(System.in);
		
		establishProtocol(cin, cout);
		
		while(true){
		    String clientMessage = cin.nextLine();
		    if(clientMessage.startsWith("MYOUSHU")){
			sendMove(cout, console);
		    }
		}
	    }
	}catch(Exception e){
	    
	}
    }
    
    // This method handles the initial contact protocol
    // It gets passed a scanner on the client and a print stream to the client
    public void establishProtocol(Scanner cin, PrintStream cout){
	String clientMessage = cin.nextLine();
	if(!clientMessage.equals("HELLO")){
	    System.out.println("Incorrect contact protocol!");
	    cin.close();
	    cout.close();
	    return;
	}
	cout.print("IAM " + name + (char)10 + (char)13);
	clientMessage = cin.nextLine();
	String[] players = clientMessage.split(" ");
	playerCount = Integer.parseInt(players[1]);
	for(int i = 2; i < 1 + playerCount;i++){
	    if(players[i].equals(name)){
		thisServersPlayerNumber = i-1;
	    }
	}
	
	// Creates the board now
	board = new Board(playerCount);
    }
    /*
     * This takes a string in the format of  "t c r"
     * where t is the type of move being either m = move, v = verticle wall or
     * h = horizontal wall. It then returns the String in the correct format 
     * for the protocol 
    */
    public String moveWrapper(String move){
	String message = "TESUJI ";
	String[] splitMessage = move.split(" ");
	if(move.startsWith("m ")){
	    message += "(" + splitMessage[1] + ", " + splitMessage[2] + ")";
	    return message + "\n\r";
	}
	else if(move.startsWith("v ") || move.startsWith("h ")){
	    message += "[(" + splitMessage[1] + ", " + splitMessage[2] + ")";
	    message += " " + splitMessage[0] + "]";
	    return message + "\n\r";
	}
	return "";
    }
    
    public void sendMove(PrintStream cout, Scanner console){
	System.out.print("Enter \"m\" to move your peice, or w to place a wall: ");
	String moveType = console.next();
	if (moveType.equals("m")) {
	    String unwrappedMessage = moveType + " ";
	    System.out.print("What colum would you like to move to: ");
	    unwrappedMessage += console.next() + " ";
	    
	}
    }
}