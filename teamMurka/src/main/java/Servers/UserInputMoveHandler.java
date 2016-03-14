package com.tmquoridor.Server;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintStream;

import java.util.*;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.tmquoridor.Board.*;


public class UserInputMoveHandler implements MoveServer, Runnable {
   
    private static final String DEFAULT_SERVER_NAME = "America";
    private static final int DEFAULT_PORT = 1478;
   
    // Fields
    private String serversName;
    private int port;
    private int playerNumber;
    private Board board;
    private int wallCount;
    
    private ServerSocket server;
    private Socket client;
    
    private Scanner console;
    
    // Methods
    /* This is the constructor to the move server. It can be passed a string 
     * which is the of the move server.
     */
    public UserInputMoveHandler(int initPort, String initName, Socket cSocket) throws Exception{
        port = initPort;
        serversName = "mur:" + initName;
        server = new ServerSocket(initPort);
        client = cSocket;
        console = new Scanner(System.in);
    }
    
    public UserInputMoveHandler(int initPort, String initName) throws Exception {
        this(initPort, initName, new Socket("localhost", initPort));
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
    public static void main(String[] args) throws Exception{
        int ixargs = 0;
        int portValue = DEFAULT_PORT;
        String name = DEFAULT_SERVER_NAME;
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
        UserInputMoveHandler mh = new UserInputMoveHandler(portValue, name);
        mh.run();
    }
    
    public void run() {
        try {
            System.out.println("Server now accepting connections "+
            "on port " + port);
            
            // This while loop allows for the client to connect
            while ((client = server.accept()) != null) {
                PrintStream serverOut = new PrintStream(client.getOutputStream());
                Scanner serverIn = new Scanner(client.getInputStream());
                
                serverOut.close();
                serverIn.close();
            }
            
            // need the actual client to make the streams
            
            
//             String clientMsg = "";
//             System.err.print("Client connected.\n\n> ");
//             while(console.hasNextLine()) {
//                 String msg = console.nextLine();
//                 serverOut.println(msg);
//                 clientMsg = serverIn.nextLine();
//                 System.err.println("C.Resp: " + clientMsg + "\n\n> ");
//             }
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}