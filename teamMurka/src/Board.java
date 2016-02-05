/**
 * Board Class
 * <p>
 * This class contains all data for player positions and walls.
 */

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
        playerPositions = new int[numOfPlayers];
    }
    
    // public HashSet<Coord> getLegalMoves(int plNum) {}
    // public boolean isMoveLegal(Coord src, Coord dest) {}
    
    /**
     * Checks if a direction is blocked at a Coordinate.
     * @param src the starting Coordinate that will be tested from
     * @param dir the direction to move (Use Coord.Direction enum)
     * @return true if the move is blocked
     */
    public void isBlocked(Coord src, int dir) {
        for(wall : placedWalls) {
            
            // Skip the wall if it's too far to matter
            if(Coord.getDistance(wall.getPos(), src) < 2) {
                
                // If we're moving East or West, horizontal walls have no effect,
                // and vice-versa
                if(Direction.getOrientation(w.getDir() != Direction.getOrientation(dir)) {
                    
                    // Check blocking for the corresponding direction
                    switch(dir) {
                        case Direction.EAST:
                            if(w.getPos().getX() + 1 == src.getX())
                                return true;
                        case Direction.WEST:
                            if(w.getPos().getX() == src.getX())
                                return true;
                        case Direction.NORTH:
                            if(w.getPos().getY() == src.getY())
                                return true;
                        case Direction.SOUTH:
                            if(w.getPos().getY() + 1 == src.getY())
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
    
    // public void placeWall(Coord pos, int dir) {}
    
}