package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;

public class AIServer extends ManualInputServer {

    /** How many more moves the opponent has before they win before we start preventing them */
    private int attackThreshold = 5;
    
    /** How much of a difference a wall would have to make to have us counter it */
    private int wallCounterThreshold = 5;

    // Main that uses the command line arguments
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


    // Lets them know if they put in an invalid argument
    private static void usage() {
        System.err.print("usage: java BirthdayServer [options]\n" +
            "       where options:\n" + "       --port port\n");
    }

    //constructor
    // Uses internal wall placement (W:[(4,7),V] would place a wall on the LEFT of (4,7)
    public AIServer(int port, String name, int delay) {
        super(port, name, delay, true);
    }

    
    public void sendMove(PrintStream cout) {
      System.err.println("Generating move...");
      Random rand = new Random();
      int pid = thisServersPlayerNumber - 1;
      boolean noWallsLeft = (board.wallsRemaining(pid) <= 0);
      String move = "";
      
      // Get all players' shortest paths
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
        
        // Go through the set of potential walls that could screw us over
        Wall worstWall = null;
        int worstPathLength = currPathLength;
        for(Wall w : blockingWalls) {
          ArrayList<Coord> theoreticalPath = board.getShortestPath(pid, w);
          
          // If this wall would REALLY screw us over, keep track of it
          if(theoreticalPath.size() - currPathLength >= wallCounterThreshold && theoreticalPath.size() > worstPathLength) {
            
            // If the anti-Wall is legal
            if(board.isLegalWall(pid, worstWall.getAntiWall())) {
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
        
      // If an opponent has the shortest path
      } else {
        
        System.err.println("Opponent with PID=" + pidWithShortestPath + " has the shortest path.");
        
        // If the opponent is close enough to bother blocking them off
        if(currPathLength <= attackThreshold) {
          System.err.println("Opponent is close to winning!");
        
          Wall bestWall = null;
          int bestWallPathLength = currPathLength;
          
          // Find the wall that would do the most damage
          for(Wall w : blockingWalls) {
            ArrayList<Coord> theoreticalPath = board.getShortestPath(pidWithShortestPath, w);
          
            // If this wall is better, track it as the best
            if(theoreticalPath.size() > bestWallPathLength) {
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
      }
      
      System.err.println("    Sending \"" + move + "\"");
      cout.println(move);
    }
}








