package com.tmquoridor.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Queue;

import com.tmquoridor.Board.*;
import com.tmquoridor.Util.*;

public class PathFinder {
  
  private int pid;
  private Board board;
  private HashMap<Integer, Integer> marks;
  
  private DebugOut dout;
  
  public PathFinder(int pid, Board b) {
    this.pid = pid;
    this.marks = new HashMap<Integer, Integer>();
    this.board = b.copyOf();
    this.dout = new DebugOut("PathFinder_" + this.hashCode());
  }
  
  public ArrayList<Coord> getPath(Coord dest) {
    HashSet<Coord> seen = new HashSet<Coord>();
    Queue<Coord> queue = new ArrayDeque<Coord>();
    
    Coord initPos = board.getPlayerPos(pid);
    
    // Player was kicked; stop updating
    if(initPos == null)
      return null;
    
    // We're at the destination already
    if(initPos.equals(dest)) {
      dout.write("PathFinder.getPath", "At destination.");
      return null;
    }
    
    queue.add(initPos);
    int currentMark = 0;
    marks.put(initPos.id(), 0);
    dout.write("PathFinder.getPath", "Marked " + initPos + " with 0");
    boolean found = false;
    Coord curr = null;
    while(!queue.isEmpty()) {
      
      // Get the current position and add to visited
      curr = queue.remove();
      dout.write("PathFinder.getPath", "Processing " + curr + ":" + marks.get(curr.id()));
      if(marks.get(curr.id()) > currentMark) {
        currentMark = marks.get(curr.id());
        dout.write("PathFinder.getPath", "Update mark to " + currentMark);
      }
      if(curr == null)
        dout.write("PathFinder.getPath", "curr is null");
      marks.put(curr.id(), currentMark);
      board.movePlayer(pid, curr);
      dout.write("PathFinder.getPath", "Moved player to " + board.getPlayerPos(pid));
      seen.add(curr);
      
      // If we've found the target
      if(curr.equals(dest)) {
        dout.write("PathFinder.getPath", "FOUND");
        found = true;
        break;
      }
      
      for(Coord lc : board.getLegalMoves(pid)) {
        if(!isVisited(seen, lc)) {
          marks.put(lc.id(), currentMark + 1);
          queue.add(lc);
          seen.add(lc);
          dout.write("PathFinder.addToQ", "Added " + lc + ":" + marks.get(lc.id()) + " to queue.");
        } else {
          dout.write("PathFinder.addToQ", "No unvisited Coords for " + lc + ":" + marks.get(lc.id()));
        }
      }
      
    }
    
    // If found, build the path and return it.
    if(found) {
      dout.write("PathFinder.found", "Building path of length " + currentMark + "...");
      Coord[] temp = new Coord[currentMark];
      temp[temp.length - 1] = curr;
      
      // Build helper array
      for(int i = currentMark - 2; i >= 0; i--) {
        Coord next = null;
        dout.write("PathFinder.found", "Building for index " + i);
        HashSet<Coord> legalMoves = board.getLegalMoves(pid);
        if(legalMoves.isEmpty()) {
          dout.write("PathFinder.found", "PathFinder: Legalmoves is empty!");
          dout.write("PathFinder.found", board.toString());
          dout.write("PathFinder.found", "For PID=" + pid);
        }
        for(Coord c : legalMoves) {
          dout.write("PathFinder.found", "  Backtracing " + c);
          if(marks.containsKey(c.id())) {
            int cid = marks.get(c.id());
            dout.write("PathFinder.found", "    CID=" + cid);
            
            // Found a return path
            if(cid == i + 1) {
              dout.write("PathFinder.found", "    Path found: " + c);
              next = c;
              temp[i] = c;
              break;
              
            // This is not a return path
            } else {
              dout.write("PathFinder.found", "    " + c + " is not a return path");
            }
          } else {
            dout.write("PathFinder.found", "    No data for " + c);
          }

        }
        if(next == null)
            dout.write("PathFinder.found", "NEXT IS NULL");
        else {
            board.movePlayer(pid, next);
            dout.write("PathFinder.found", "Moving to next Coord @ " + board.getPlayerPos(pid));
        }
      }
      
      ArrayList<Coord> result = new ArrayList<Coord>();
      for(int i = 0; i < temp.length; i++) {
        result.add(temp[i]);
      }
      
      return result;
      
    // Processed all of queue; if not found, there is no path.
    } else {
      dout.write("PathFinder.end", "No path found for " + initPos);
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