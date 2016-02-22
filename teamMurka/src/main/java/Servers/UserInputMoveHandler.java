package com.tmquoridor.Server;

import java.io.IOException;

import java.util.*;
import java.io.PrintStream;
import java.net.Socket;

import com.tmquoridor.Board.*;


public class UserInputMoveHandler implements MoveServer, Runnable {
   
    private static final String DEFAULT_MACHINE_NAME = "localhost";
    private static final int DEFAULT_PORT = 6478;
   
    // Fields
    private String serversName;
    private int port;
    private int playerNumber;
    private Board board;
    private int wallCount;
    
    private Socket socket;
    private Scanner consoleIn;
    
    // Methods
    /* This is the constructor to the move server. It can be passed a string 
     * which is the of the move server.
     */
    public UserInputMoveHandler(int initPort, String initName){
        port = initPort;
        serversName = initName;
        consoleIn = new Scanner(System.in);
    }
    
    
    public void placeMoveOnBoard() {
        System.err.println("place");
    }
    
    @Override
    public String getMove() {
        return "move";
    }
    
    /* This is here to allow for the server to be ran without some outside 
     * class. It also allows for you to input the port you would like and also
     * allows for you to override the default name given to the server.
     */
    public static void main(String[] args){
        int ixargs = 0;
        int portValue = DEFAULT_PORT;
        String name = DEFAULT_MACHINE_NAME;
        // While loop  to run through all of the command line arguments
        while(ixargs > args.length){
            if(args[ixargs].equals("--port")){
                ixargs++;
                try {
                    portValue = Integer.parseInt(args[ixargs]);
                } catch(Exception e) {
                    System.out.println("After the port argument you entered" +
                                       " a non-numerical character");
                    System.exit(0);
                }
            }
            else if(args[ixargs].equals("--name")) {
                ixargs++;
                name = args[ixargs];
            }
        ixargs++;
        }
        
        // Makes the instance of the move server
        new Thread(new UserInputMoveHandler(portValue, name)).start();
    }
    
    public void run() {
        try {
            socket = new Socket(serversName, port);
            PrintStream serverOut = new PrintStream(socket.getOutputStream());
            Scanner serverIn = new Scanner(socket.getInputStream());
            
            String clientMsg = "";
            System.err.print("Client connected.\n\n> ");
            while(consoleIn.hasNextLine()) {
                String msg = consoleIn.nextLine();
                serverOut.println(msg);
                clientMsg = serverIn.nextLine();
                System.err.println("C.Resp: " + clientMsg + "\n\n> ");
            }
            
            serverOut.close();
            serverIn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}