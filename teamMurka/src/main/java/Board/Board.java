/**
 * Board Class
 * <p>
 * This class contains all data for player positions and walls.
 */

package com.tmquoridor.Board;
 
import java.util.HashSet;
 
public class Board {
    
    /** Length and width of the board */
    private static final int BOARD_SIZE = 9;
    
    /** The number of players playing */
    int numOfPlayers;
    
    /** ith index contains the ith player's position */
    Coord[] playerPositions;
    
    /** Contains all walls that have been placed */
    HashSet<Wall> placedWalls;
     
    /**
     * Default constructor
     * @param plNum the number of players
     */
    public Board(int plNum) {
        numOfPlayers = plNum;
        playerPositions = new Coord[numOfPlayers];
        placedWalls = new HashSet<Wall>();
    }
    
    // public HashSet<Coord> getLegalMoves(int plNum) {}
    // public boolean isMoveLegal(Coord src, Coord dest) {}
    
    /**
     * Checks if a direction is blocked at a Coordinate.
     * @param src the starting Coordinate that will be tested from
     * @param dir the direction to move (Use Coord.Direction enum)
     * @return true if the move is blocked
     */
    public boolean isBlocked(Coord src, Direction dir) {
        for(Wall wall : placedWalls) {
            
            // Skip the wall if it's too far to matter
            if(Coord.getDistance(wall.getPos(), src) < 2) {
                
                // If we're moving East or West, horizontal walls have no effect,
                // and vice-versa
                if(wall.getOrt() != dir.ort()) {
                    
                    // Check blocking for the corresponding direction
                    switch(dir) {
                        case EAST:
                            if(wall.getPos().getX() + 1 == src.getX() && wall.getPos().getY() + 1 == src.getY())
                                return true;
                        case WEST:
                            if(wall.getPos().getX() == src.getX() && wall.getPos().getY() + 1 == src.getY())
                                return true;
                        case NORTH:
                            if(wall.getPos().getX() + 1 == src.getX() && wall.getPos().getY() == src.getY())
                                return true;
                        case SOUTH:
                            if(wall.getPos().getX() + 1 == src.getX() && wall.getPos().getY() + 1 == src.getY())
                                return true;
                    }
                }
            }
        }
        
        // If we haven't found any blocked directions, all is well
        return false;
    }
    
    /**
     * Moves a player to a destination. Does not check for collisions,
     * so an upper layer of control is needed.
     * @param plNum the player number to move
     * @param dest the destination Coordinate
     */
    public void movePlayer(int plNum, Coord dest) {
        playerPositions[plNum] = dest;
    }
    
    /**
     * Gets a player's position
     * @param plNum the player to get the position of
     * @return a Coord where the specified player is located
     */
    public Coord getPlayerPos(int plNum) {
        return playerPositions[plNum];
    }
    
    /**
     * Gets all placed walls in the game
     * @return a HashSet of all Walls placed
     */
    public HashSet<Wall> getWalls() {
        return placedWalls;
    }
    
    /**
     * Places a wall on the board.
     */
    public void placeWall(Coord pos, Orientation ort) {
        Wall w1 = new Wall(pos, ort);
        
        // Calculate wall 2's coordinate
        int spanX = (ort == Orientation.HORIZ) ? pos.getX() + 1 : pos.getX();
        int spanY = (ort == Orientation.HORIZ) ? pos.getY() : pos.getY() + 1;
        Coord spanPos = new Coord(spanX, spanY);
        Wall w2 = new Wall(spanPos, ort);
        
        // Add the walls to the HashSet
        placedWalls.add(w1);
        placedWalls.add(w2);
    }
    
    public void placeWall(Wall w) {
        placeWall(w.getPos(), w.getOrt());
    }
    
}
