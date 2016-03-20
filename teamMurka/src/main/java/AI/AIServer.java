package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;

public interface MoveServer {
  public Move requestMove() 
  
}

public class AIServer extends ManualInputServer {
  // @override  
  public Move sendMove(PrintStream cout) {
      
    // the while loop checks for in valid input and breaks if so 
    // then if the argument is in the correct format executes move
    
    while (cout.hasNextLine()) {
      moveComand = cout.nextLine();
      String[] splitter = moveComand.split(" ");
      if (splitter.length != 2) {
        cout.println(moveComand + " - invalid format ");
        break;
      } else {
        if (!(splitter[0].equals("m" || "v" || "h"))) {
          cout.println("You must enter a command for a move(m) or a wall(v or h)");
          break;
        } else {
          columnSel = Interger.parseInt(splitter[1]);
          if (columnSel < 0 || columnSel > 8) {
            cout.println("Your selection is out of bounds choose between 0 & 8");
            break;
          }
          rowSel = Interger.parseInt(splitter[2]);
          if (rowSel < 0 || rowSel > 8) {
            cout.println("Your selection is out of bounds choose between 0 & 8");
          } else {
            system.out.println("Works");
            if (splitter[0].equals("m") {
              // turn off switch at current position 
              // move pawn to the disired coordinate 
              // update board
              // if the player 1 gets to row 0 player one wins game
              // if the player 2 gets to row 9 player one wins game
              // if the player 3 gets to column 9 player one wins game
              // if the player 4 gets to column 0 player one wins game

            }
            if (splitter[0].equals("v") {
              // if there still remains a path across after wall is placed
              // place verticle wall at chosen coordinate 
              // update board
            }
            if (splitter[0].equals("h") {
              // if there still remains a path across after wall is placed
              // place horizontal wall at chosen coordinate
              // update board 
            }
          } 
        }
      }
    }
  }
  public void thinker(code) {
    // Should we always make the same first move 
    // (A wall one space to the right and forward from the opponent)
    // *Should we develop something to guard against that move* 
    // if there is a possible move to increase the oponents shortest path
    // choose the move that will increase their path by the most
  } 
}
            