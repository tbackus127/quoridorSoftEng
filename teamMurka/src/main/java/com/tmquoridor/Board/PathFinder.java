package com.tmquoridor.Board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Queue;

import com.tmquoridor.Board.*;

public class PathFinder {
  
  private int pid;
  private Board board;
  
  public PathFinder(int pid, Board b) {
    this.pid = pid;
    board = b.copyOf();
  }
  
  public ArrayList<Coord> getPath(Coord dest) {
    HashSet<Coord> visited = new HashSet<Coord>();
    Queue<Coord> queue = new ArrayDeque<Coord>();
    
    Coord initPos = board.getPlayerPos(pid);
    
    queue.add(initPos);
    
    int currentMark = 0;
    boolean found = false;
    Coord curr = null;
    while(!queue.isEmpty()) {
      
      // Get the current position and add to visited
      curr = queue.remove();
      if(curr.getMark() > currentMark)
        currentMark = curr.getMark();
      System.err.println("Processing " + curr);
      curr.setMark(currentMark);
      board.movePlayer(pid, curr);
      visited.add(curr);
      
      // If we've found the target
      if(curr.equals(dest)) {
        System.err.println("FOUND");
        found = true;
        break;
      }
      
      for(Coord lc : board.getLegalMoves(pid)) {
        if(!isVisited(visited, lc)) {
          lc.setMark(currentMark + 1);
          System.err.println("  Unvisited coord: " + lc);
          queue.add(lc);
        }
      }
      
    }
    
    // If found, build the path and return it.
    if(found) {
      Coord[] temp = new Coord[currentMark];
      temp[temp.length - 1] = curr;
      temp[0] = initPos;
      
      // Build helper array
      for(int i = currentMark - 1; i > 0; i--) {
        for(Coord c : board.getLegalMoves(pid)) {
          if(c.getMark() == i - 1) {
            temp[i] = c;
          }
        }
      }
      
      ArrayList<Coord> result = new ArrayList<Coord>();
      for(int i = 0; i < temp.length; i++) {
        result.add(temp[i]);
      }
      
      return result;
    }
    
    return null;
  }
  
  private static boolean isVisited(HashSet<Coord> set, Coord chk) {
    for(Coord c : set)
      if(c.equals(chk))
        return true;
    return false;
  }
  
}