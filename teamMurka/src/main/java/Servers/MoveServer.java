package com.tmquoridor.Server;

public interface MoveServer {
    
    // Fields
    // private String machine;
    // private int port;
    // private String serversName;
    // private int playerNumber;
    // private char[][] board;
    // private int wallCount;
    
    // Methods
    // public static void main(String[] args);
    // public void run();
    public String getMove();
    public void placeMoveOnBoard();
    
}