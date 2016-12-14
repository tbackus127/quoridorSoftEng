
package com.tmquoridor.Servers;

public interface MoveServer {
  
  // Methods
  public void run();
  
  public String getMove();
  
  public void placeMoveOnBoard();
  
}