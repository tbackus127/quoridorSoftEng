package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;
import java.util.*;

public class AIServer extends ManualInputServer {

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

      AIServer ai = new AIServer(port, name);
      ai.run();
    }


    // Lets them know if they put in an invalid argument
    private static void usage() {
        System.err.print("usage: java BirthdayServer [options]\n" +
            "       where options:\n" + "       --port port\n");
    }

    //constructor

    public AIServer(int port, String name) {
        super(port, name);
    }


    // @override
    public void sendMove(PrintStream cout) {
        System.err.println("AI.sendMove()");
        Random rand = new Random();
        int choice = rand.nextInt(2);
        int turnCount = 0;

        while (true) {
            // choice 0 is to make a move
            if (choice == 0) {
                // a random place (row / column) to move to

                int moveRow = rand.nextInt(9);
                int moveColumn = rand.nextInt(9);
                Coord dest = new Coord(moveRow, moveColumn);
                //if (turnCount == 0) {

                //}
                if (board.isLegalMove(thisServersPlayerNumber-1, dest) == true) {
                    String move = moveWrapper("m " + moveRow + " " + moveColumn);
                    System.err.print("Sending " + move);
                    cout.print(move);
                    turnCount++;

                    return;
                }
            }
            // choice 1 is to place a wall
            if (choice == 1) {
                // a random for the posible placements for a wall
                int wallRow = rand.nextInt(8);
                int wallColumn = rand.nextInt(8);
                Coord pos = new Coord(wallRow, wallColumn);
                Orientation ort1 = Orientation.HORIZ;
                Orientation ort2 = Orientation.VERT;

                //int orientWall = rand
                Wall horiz = new Wall(pos, ort1);
                Wall vert = new Wall(pos, ort2);


                if (board.wallsRemaining(thisServersPlayerNumber-1) == 0) {

                    choice = 0;
                    break;
                } else {
                    // wallOrient is the orientation of wall verticle / horizontal
                    int wallOrient = rand.nextInt(2);

                    try {
                        if (wallOrient == 1) {

                            if (board.isLegalWall(thisServersPlayerNumber-1, horiz) == true) {
                                String move = moveWrapper("v " + wallRow + " " + wallColumn);
                                System.err.print("Sending " + move);
                                cout.print(move);
                                turnCount++;
                                return;
                            }
                        }
                        if (wallOrient == 2) {
                            if (board.isLegalWall(thisServersPlayerNumber-1, vert) == true) {
                                String move = moveWrapper("h " + wallRow + " " + wallColumn);
                                System.err.print("Sending " + move);
                                cout.print(move);
                                turnCount++;
                                return;
                            }

                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    }
}



           // if (splitter[0].equals("m") {
              // turn off switch at current position
              // move pawn to the disired coordinate
              // update board
              // if the player 1 gets to row 0 player one wins game
              // if the player 2 gets to row 9 player one wins game
              // if the player 3 gets to column 9 player one wins game
              // if the player 4 gets to column 0 player one wins game

           // }
            //if (splitter[0].equals("v") {
              // if there still remains a path across after wall is placed
              // place verticle wall at chosen coordinate
              // update board
          //  }
          //  if (splitter[0].equals("h") {
              // if there still remains a path across after wall is placed
              // place horizontal wall at chosen coordinate
              // update board

  //public void thinker(code) {
    // Should we always make the same first move
    // (A wall one space to the right and forward from the opponent)
    // *Should we develop something to guard against that move*
    // if there is a possible move to increase the oponents shortest path
    // choose the move that will increase their path by the most
  //}
//}

