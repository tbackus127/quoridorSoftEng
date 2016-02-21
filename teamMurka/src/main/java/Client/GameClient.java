package com.tmquoridor.Client;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.tmquoridor.Board.*;

public class GameClient implements Runnable {
    
    private static final Executor exec = Executors.newFixedThreadPool(4);
    
    private boolean running;
    private Board board;
    private int portNumber;
    
    public GameClient(int portNumber) {
        this.portNumber = portNumber;
    }
    
    public void start() {
        running = true;
        run();
    }
    
    public void run() {
        
        while(running) {
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
    }
}