package com.tmquoridor.Client;

import java.net.Socket;

import com.tmquoridor.Board.*;

public class GameClient implements Runnable {
    
    private boolean running;
    private Board board;
    
    public GameClient() {
        start();
    }
    
    public void start() {
        running = true;
        run();
    }
    
    public void run() {
        
    }
}