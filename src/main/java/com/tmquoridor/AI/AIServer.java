package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;

public class AIServer extends ManualInputServer {

    /** How many more moves the opponent has before they win before we start preventing them */
    private int remainingPathThreshold = 5;
    
    /** How much of a difference in path length a wall would make to attack with */
    private int attackDeltaThreshold = 5;
    
    /** How much of a difference a wall would have to make to have us counter it */
    private int wallCounterThreshold = 5;
    
    /** The chance we will place a wall anyways, even if it doesn't screw the opponent much */
    private double randomWallChance = 0.15D;
    
    /** Chance to do hard-coded opening */
    private double openingChance = 0.8D;
    
    /** Turn counter */
    private int turnCount = 0;

    /**
     * Main method. Creates a new AIServer object with the specified runtime arguments
     * @param args runtime args as a String array
     */
    public static void main(String[] args) {
          
        // This sets the defaults
        int port = DEFAULT_PORT_NUMBER;
        String name = DEFAULT_NAME;
        int delay = DEFAULT_DELAY;
      
        int argNdx = 0;

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
    
            } else if(curr.equals(ARG_DELAY)) {
                ++argNdx;
                delay = Integer.parseInt(args[argNdx]);
    
            } else {

                // if there is an unknown parameter, give usage and quit
                System.err.println("Unknown parameter \"" + curr + "\"");
                usage();
                System.exit(1);
            }

            ++argNdx;
        }

        AIServer ms = new AIServer(port, name, delay);
        ms.run();
    }


    /**
     * Prints the usage message if the user entered invalid runtime arguments
     */
    private static void usage() {
        System.err.print("usage: \"java AIServer [--port <port>] [--name <name>] [--delay <delay>]\"");
    }

    /**
     * Default constructor. Uses internal wall placement!
     * @param port the port number to use
     * @param name the server's name
     * @param delay the delay to wait until making a move (in milliseconds)
     */
    public AIServer(int port, String name, int delay) {
        super(port, name, delay, true);
    }

    
    /**
     * Generates and sends a move across the wire
     * @param cout the PrintStream hooked to the client socket
     */
    public void sendMove(PrintStream cout) {
      System.err.println("Generating move...");
      
      //this ibject generates random numbers based on a seed
      Random rand = new Random();
      int pid = thisServersPlayerNumber - 1;
      boolean noWallsLeft = (board.wallsRemaining(pid) <= 0);
      String move = "";
      
      // 80% chance to do Schiller's opening
      if(rand.nextDouble() <= openingChance && board.getTotalPlayers() == 2) {
        System.err.println("Doing schiller's opening.");
        int swx = -1;
        int swy = -1;
        Orientation swOrt = Orientation.VERT;
        if(turnCount <= 1) {
          switch(pid) {
            
            // Player 2
            case 1:
              if(turnCount == 0) {
                swx = 3;
                swy = 2;
              } else {
                swx = 6;
                swy = 2;
                swOrt = Orientation.HORIZ;
              }
            break;
            
            // Player 1
            default:
              if(turnCount == 0) {
                swx = 4;
                swy = 5;
              } else {
                swx = 1;
                swy = 5;
                swOrt = Orientation.HORIZ;
                
              }
          }
          
          Wall sw = new Wall(new Coord(swx, swy), swOrt);
          System.err.println("Generated wall " + sw);
          
          if(board.isLegalWall(pid, sw)) {
            System.err.println("Wall is legal");
            move = moveWrapper("" + swOrt.toString() + " " + swx + " " + swy);
            cout.print(move);
            turnCount++;
            return;
          } else {
            System.err.println("Wall isn't legal: " + sw);
          }
        }
          
      }
      
      // Get all players' shortest paths
      System.err.println("Computing paths...");
      ArrayList<ArrayList<Coord>> paths = new ArrayList<ArrayList<Coord>>();
      int pidWithShortestPath = -1;
      int pathSizeOfLeader = 9001;
      for(int i = 0; i < board.getTotalPlayers(); i++) {
        if(!board.isPlayerKicked(pid)) {
          paths.add(board.getShortestPath(i));
          
          // Store PID with shortest path
          if(paths.get(i).size() < pathSizeOfLeader) {
            pidWithShortestPath = i;
            pathSizeOfLeader = paths.get(i).size();
          }
        }
      }
      
      HashSet<Wall> blockingWalls = board.getBlockingWalls(pidWithShortestPath, paths.get(pidWithShortestPath));
      ArrayList<Coord> currPath = paths.get(pidWithShortestPath);
      int currPathLength = currPath.size();
      
      // If we have the shortest path
      if(pidWithShortestPath == pid) {
        
        System.err.println("  We have the shortest path.");
        
        // If we have walls to place
        if(!noWallsLeft) {
          
          // Go through the set of potential walls that could screw us over
          Wall worstWall = null;
          int worstPathLength = currPathLength;
          for(Wall w : blockingWalls) {
            ArrayList<Coord> theoreticalPath = board.getShortestPath(pid, w);
            
            // If this wall would REALLY screw us over, keep track of it
            if(theoreticalPath.size() - currPathLength >= wallCounterThreshold && theoreticalPath.size() > worstPathLength) {
              
              // If the anti-Wall is legal
              if(board.isLegalWall(pid, w.getAntiWall())) {
                worstWall = w;
                worstPathLength = theoreticalPath.size();
              }
            }
          }
          
          // If we've decided we need to place an anti-wall
          if(worstWall != null) {
            
            // Get the worst Wall's anti-wall and place it
            Wall antiWall = worstWall.getAntiWall();
            Coord wPos = antiWall.getPos();
            System.err.println("    Using anti-wall: " + antiWall);
            move = moveWrapper(antiWall.getOrt().toString() + " " + wPos.getX() + " " + wPos.getY());
          
          // If any potential walls aren't that big of a deal, or we can't place any anti-wall
          } else {
          
            // Continue along our path
            Coord next = paths.get(pid).get(0);
            System.err.println("    Continuing along path " + next);
            move = moveWrapper("m " + next.getX() + " " + next.getY());
        
          }
          
        // If we've ran out of walls
        } else {
          
          // Continue along our path
          Coord next = paths.get(pid).get(0);
          System.err.println("    Continuing along path " + next);
          move = moveWrapper("m " + next.getX() + " " + next.getY());
        }
        
      // If an opponent has the shortest path
      } else {
        
        System.err.println("Opponent with PID=" + pidWithShortestPath + " has the shortest path.");
        
        // If we have walls to spare
        if(!noWallsLeft) {
                  
          // If the opponent is close enough to bother blocking them off
          if(currPathLength <= remainingPathThreshold || rand.nextDouble() <= randomWallChance) {
            System.err.println("Opponent is close to winning!");
          
            Wall bestWall = null;
            int bestWallPathLength = currPathLength;
            
            // Find the wall that would do the most damage
            for(Wall w : blockingWalls) {
              ArrayList<Coord> theoreticalPath = board.getShortestPath(pidWithShortestPath, w);
              
              int theoreticalDelta = theoreticalPath.size() - currPathLength;
            
              // If this wall is better, track it as the best (if we care about it)
              if(theoreticalPath.size() > bestWallPathLength && theoreticalDelta + (7 - currPathLength) >= attackDeltaThreshold) {
                System.err.println("  Found new best wall: " + w);
                bestWall = w;
                bestWallPathLength = theoreticalPath.size();
              }
            }
          
            // If we found a good wall to place
            if(bestWall != null) {
              Coord wPos = bestWall.getPos();
              move = moveWrapper(bestWall.getOrt().toString() + " " + wPos.getX() + " " + wPos.getY());
              
            // Otherwise, continue
            } else {
              Coord next = paths.get(pid).get(0);
              System.err.println("    Continuing along path " + next);
              move = moveWrapper("m " + next.getX() + " " + next.getY());
            }
            
            
          // If the opponent is still far away
          } else {
            
            // Continue along our path
            Coord next = paths.get(pid).get(0);
            System.err.println("    Continuing along path " + next);
            move = moveWrapper("m " + next.getX() + " " + next.getY());
          }
        
        // If we're out of walls
        } else {
          
          // Continue along our path
          Coord next = paths.get(pid).get(0);
          System.err.println("    Continuing along path " + next);
          move = moveWrapper("m " + next.getX() + " " + next.getY());
        }
      }
      
      System.err.println("    Sending \"" + move + "\"");
      cout.print(move);
      turnCount++;
    }
}








