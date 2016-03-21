package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;
import java.util.*;

public class FranksAIServer extends ManualInputServer {
    // @override  
    public void sendMove(PrintStream cout) {
        Random rand = new Random();
        int choice = rand.nextInt(3);
        int moves = rand.nextInt(9);
        int walls = rand.nextInt(8);
        if (choice == 0) {
            if (isLegalMove == true) {
            cout.print(moveWrapper("m " + moves + " " + walls));
            }
        }
        if (choice == 1) {
            if (isLegalWall == true) {
                
            }
            
        }
        if (choice == 2) {
            
        }
        int m = 0;
        int h = 1; 
        int v = 2; 
        // checks all possible moves
        // wall or advance 
        // make one 
        
        cout.print(moveWrapper("v h m"));
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
   
  public void thinker(code) {
    // Should we always make the same first move 
    // (A wall one space to the right and forward from the opponent)
    // *Should we develop something to guard against that move* 
    // if there is a possible move to increase the oponents shortest path
    // choose the move that will increase their path by the most
  } 
}
            