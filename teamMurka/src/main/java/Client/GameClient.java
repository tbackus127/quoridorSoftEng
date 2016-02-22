package com.tmquoridor.Client;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.tmquoridor.Board.*;

public class GameClient implements Runnable {
    
    private static final Executor exec = Executors.newFixedThreadPool(4);
    private static final int DEFAULT_PORT = 6478;
    private static final String DEFAULT_MACHINE_NAME = "localhost";
    
    private boolean running;
    private Board board;
    private int portNumber;
    private String machineName;
    
    public GameClient(int portNumber, String machineName) {
        this.portNumber = portNumber;
        this.machineName = machineName;
    }
    
    public void run() {
        try {
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("Server accepting connections on port " + portNumber);

            Socket client = null;

            while ((client = server.accept()) != null) {
                ServerHandler sHand = new ServerHandler(client);
                exec.execute(sHand);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        int ixargs = 0;
        int portValue = DEFAULT_PORT;
        String name = DEFAULT_MACHINE_NAME;
        // While loop  to run through all of the command line arguments
        while(ixargs > args.length) {
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
        
        // Create a new thread of the Game Client
        new Thread(new GameClient(portValue, name)).start();
    }
}