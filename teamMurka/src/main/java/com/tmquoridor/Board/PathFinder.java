package com.tmquoridor.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Queue;

import com.tmquoridor.Board.*;

public class PathFinder {
  
  private int pid;
  private Board board;
  private HashMap<Integer, Integer> marks;
  
  public PathFinder(int pid, Board b) {
    this.pid = pid;
    this.marks = new HashMap<Integer, Integer>();
    this.board = b.copyOf();
  }
  
  public ArrayList<Coord> getPath(Coord dest) {
    HashSet<Coord> seen = new HashSet<Coord>();
    Queue<Coord> queue = new ArrayDeque<Coord>();
    
    Coord initPos = board.getPlayerPos(pid);
    
    queue.add(initPos);
    int currentMark = 0;
    marks.put(initPos.id(), 0);
    boolean found = false;
    Coord curr = null;
    while(!queue.isEmpty()) {
      
      // Get the current position and add to visited
      curr = queue.remove();
      if(marks.get(curr.id()) > currentMark)
        currentMark = marks.get(curr.id());
      // System.err.println("Processing " + curr);
      marks.put(curr.id(), currentMark);
      board.movePlayer(pid, curr);
      seen.add(curr);
      
      // If we've found the target
      if(curr.equals(dest)) {
        // System.err.println("FOUND");
        found = true;
        break;
      }
      
      for(Coord lc : board.getLegalMoves(pid)) {
        if(!isVisited(seen, lc)) {
          marks.put(lc.id(), currentMark + 1);
          // System.err.println("  Unvisited coord: " + lc);
          queue.add(lc);
          seen.add(lc);
        }
      }
      
    }
    
    // If found, build the path and return it.
    if(found) {
      // System.err.println("\nBuilding path of length " + currentMark + "...");
      Coord[] temp = new Coord[currentMark];
      temp[temp.length - 1] = curr;
      
      // System.err.println("Initial array: " + Arrays.toString(temp));
      
      // Build helper array
      for(int i = currentMark - 2; i >= 0; i--) {
        Coord next = null;
        // System.err.println("Building for index " + i);
        HashSet<Coord> legalMoves = board.getLegalMoves(pid);
        for(Coord c : legalMoves) {
          // System.err.println("  Backtracing " + c);
          if(marks.containsKey(c.id())) {
            int cid = marks.get(c.id());
            // System.err.println("    CID=" + cid);
            if(cid == i + 1) {
              // System.err.println("    Path found: " + c);
              next = c;
              temp[i] = c;
              break;
            }
          } else {
            // System.err.println("    No data for " + c);
          }

        }
        // if(next == null) System.err.println("NEXT IS NULL");
        board.movePlayer(pid, next);
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