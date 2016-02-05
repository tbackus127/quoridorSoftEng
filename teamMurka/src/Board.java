/**
 * Board Class
 * <p>
 * This class contains all data for player positions and walls.
 */

import java.util.HashSet;
 
public class Board {
    
    private static final int BOARD_SIZE = 9;
    
    int numOfPlayers;
    
    Coord[] playerPositions;
    HashSet<Wall> placedWalls;
     
    public Board(int plNum) {
        numOfPlayers = plNum;
        playerPositions = new int[4];
    }
    
    // public HashSet<Coord> getLegalMoves(int plNum) {}
    // public boolean isMoveLegal(Coord src, Coord dest) {}
    
    public void movePlayer(int plNum, Coord dest) {
        playerPositions[plNum] = dest;
    }
    
    public Coord getPlayerPos(int plNum) {
        return playerPositions[plNum];
    }
    
    public HashSet<Wall> getWalls() {
        return placedWalls;
    }
    
}