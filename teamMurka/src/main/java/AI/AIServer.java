package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;
import java.util.*;

public class AIServer extends ManualInputServer {
    
    public AIServer(int port, String name) {
        super(port, name);
    }
    
    // @override  
    public void sendMove(PrintStream cout) {
        Random rand = new Random(); 
        int choice = rand.nextInt(2);        
        
        while (true) {
            // choice 0 is to make a move            
            if (choice == 0) {
                // a random place (row / column) to move to 
                int move1 = rand.nextInt(9);
                int move2 = rand.nextInt(9);
                Coord dest = new Coord(move1, move2);
                if (board.isLegalMove(thisServersPlayerNumber - 1, dest) == true) {
                    cout.print(moveWrapper("m " + move1 + " " + move2));
                    return;
                }
            }
            // choice 1 is to place a wall 
            if (choice == 1) {     
                // a random for the posible placements for a wall 
                int wall1 = rand.nextInt(8);
                int wall2 = rand.nextInt(8);
                Coord pos = new Coord(wall1, wall2);
                Orientation ort1 = Orientation.HORIZ;
                Orientation ort2 = Orientation.VERT;
                if (board.wallsRemaining(thisServersPlayerNumber) == 0) {
                    choice = 0; 
                    break;
                } else {
                    // wallOrient is the orientation of wall verticle / horizontal
                    int wallOrient = rand.nextInt(2);
                    if (wallOrient == 1) {
                        
                        try {
                            if (board.isLegalWall(thisServersPlayerNumber-1, new Wall(pos, ort1)) == true) {
                                cout.print(moveWrapper("v" + wall1 + " " + wall2));
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (wallOrient == 2) {
                        
                        try {
                            if (board.isLegalWall(thisServersPlayerNumber-1, new Wall(pos, ort2)) == true) {
                                cout.print(moveWrapper("h" + wall1 + " " + wall2));
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
            
