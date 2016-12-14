
package com.tmquoridor.Board;

import java.util.HashSet;

/**
 * Contains the information for recursively finding legal moves.
 */
public class MoveTrace {
  
  /** The players that have already been seen */
  private HashSet<Integer> seenPlayers;
  
  /** The set of legal moves so far */
  private HashSet<Coord> legalMoves;
  
  /**
   * Default constructor
   */
  public MoveTrace() {
    this.seenPlayers = new HashSet<Integer>();
    this.legalMoves = new HashSet<Coord>();
  }
  
  /**
   * Checks if there is a record of the passed player
   * 
   * @param pid the player to search for
   * @return true if the player ID is found; false otherwise
   */
  public boolean isSeen(int pid) {
    return (this.seenPlayers.contains(pid));
  }
  
  /**
   * Gets the legal moves
   * 
   * @return a HashSet of legal moves, in Coords
   */
  public HashSet<Coord> getMoves() {
    return this.legalMoves;
  }
  
  /**
   * Gets the players seen already
   * 
   * @return a HashSet of players, in Integers
   */
  public HashSet<Integer> getPlayers() {
    return this.seenPlayers;
  }
  
  /**
   * Adds a player to what we've seen
   * 
   * @param pid the player ID to add
   */
  public void addPlayer(int pid) {
    this.seenPlayers.add(pid);
  }
  
  /**
   * Adds a Coord to the list of legal moves
   * 
   * @param c the Coord to add
   */
  public void addMove(Coord c) {
    this.legalMoves.add(c);
  }
  
  /**
   * Adds the contents of another MoveTrace to this one
   * 
   * @param mt the other MoveTrace
   */
  public void add(MoveTrace mt) {
    this.legalMoves.addAll(mt.getMoves());
    this.seenPlayers.addAll(mt.getPlayers());
  }
}