package com.tmquoridor.Client;

import java.io.IOException;
import java.io.PrintStream;

import java.net.Socket;

import java.util.Scanner;

import com.tmquoridor.Board.*;

public class GameClient implements Runnable {
    
    private Board board;
    
    private Socket[] playerSockets;
    
    public GameClient(Socket[] argSocks) {
        playerSockets = argSocks;
    }
    
    public void run() {
        if(!handshake()) {
          System.err.println("Something went wrong while handshaking!");
          System.exit(0);
        }
        doGameLoop();
    }
    
    private void doGameLoop() {
        board = new Board(playerSockets.length);
        board.printBoard();
        while(true) {
            for(int i = 0; i < playerSockets.length; i++) {
              
                try {
                    // Set up communication
                    PrintStream cout = new PrintStream(playerSockets[i].getOutputStream());
                    Scanner cin =  new Scanner(playerSockets[i].getInputStream());
                    
                    cout.println("turn");
                    String move = cin.nextLine();
                    
                    // Check move legality
                    while(!isMoveLegal(i, move)) {
                        cout.println("invalid");
                        System.out.println("Player " + i + " made an illegal move!");
                        move = cin.nextLine();
                    }
                    
                    doMove(i, move);
                    board.printBoard();
                    
                } catch (Exception e) {
                    System.err.println("Server DC'd");
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }
    
    private void doMove(int pid, String move) {
        String[] tokens = move.split(" ");
        
        switch(tokens[0]) {
            case "move":
                Coord pPos = board.getPlayerPos(pid);
                Direction mDir = board.toDir(tokens[1]);
                try {
                    board.movePlayer(pid, pPos.translate(mDir));
                } catch (Exception e) {
                  e.printStackTrace();
                }
            break;
            case "wall":
                try {
                    int wx = Integer.parseInt(tokens[1]);
                    int wy = Integer.parseInt(tokens[2]);
                    Coord wPos = new Coord(wx, wy);
                    Orientation wOrt = board.toOrt(tokens[3]);
                    board.placeWall(wPos, wOrt);
                } catch (Exception e) {
                    System.err.println("Wall legality checking doesn't work for some reason!");
                    e.printStackTrace();
                    return;
                }
        }
    }
    
    private boolean isMoveLegal(int pid, String move) {
        String[] tokens = move.split(" ");
        
        switch(tokens[0]) {
            case "move":
                Coord pPos = board.getPlayerPos(pid);
                Direction mDir = board.toDir(tokens[1]);
                return !board.isBlocked(pPos, mDir);
            case "wall":
                try {
                    int wx = Integer.parseInt(tokens[1]);
                    int wy = Integer.parseInt(tokens[2]);
                    Coord wPos = new Coord(wx, wy);
                    Orientation wOrt = board.toOrt(tokens[3]);
                    return board.isLegalWall(new Wall(wPos, wOrt));
                } catch (Exception e) {
                    return false;
                }
            default:
                return false;
        }
    }
    
    private boolean handshake() {
        System.out.println("Client attempting connections...");
        for(int i = 0; i < playerSockets.length; i++) {
            
            try {
                PrintStream cout = new PrintStream(playerSockets[i].getOutputStream());
                Scanner cin =  new Scanner(playerSockets[i].getInputStream());
            
                // Send PID to each Server
                cout.println(i);
                if(cin.nextLine().toLowerCase().equals("ok"))
                    System.out.println("Player " + (i+1) + " OK");
              
            } catch (Exception e) {
                System.err.println("Something blew up.");
                return false;
            }
            
        }
        return true;
    }
    
    public static void main(String[] args) {
        int ixargs = 0;
        int seenPlayers = 0;
        // String[] playerNames = {null, null, null, null};
        int[] playerPorts = {-1, -1, -1, -1};
        
        // Parse runtime args
        while(ixargs < args.length) {
            String arg = args[ixargs++];
            switch(arg) {
                case "--port":
                    try {
                        int port = Integer.parseInt(args[ixargs]);
                        playerPorts[seenPlayers] = port;
                        System.out.println("Set port of P" + seenPlayers + " to " + port);
                        seenPlayers++;
                    } catch(Exception e) {
                        System.err.println("Illegal port number.");
                        System.exit(0);
                    }
            }
        }
        
        Socket[] socks = new Socket[seenPlayers];
        for(int i = 0; i < socks.length; i++) {
            try {
                socks[i] = new Socket("localhost", playerPorts[i]);
                System.out.println("New socket@localhost:" + playerPorts[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Create a new thread of the Game Client
        new Thread(new GameClient(socks)).start();
    }
}