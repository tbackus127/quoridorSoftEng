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
    public final int numOfPlayers;
    
    /** ith index contains the ith player's position */
    private Coord[] playerPositions;
    
    /** Contains all walls that have been placed */
    private HashSet<Wall> placedWalls;
     
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
     * Checks if a wall is relevant for blocking paths.
     * @param w the Wall to check
     * @param plPos the player's position to check
     * @param dir the Direction the player is moving
     * @return true if the wall is relevant for collision checking
     */
    private boolean isRelevantWall(Wall w, Coord plPos, Direction dir) {
        int px = plPos.getX();
        int py = plPos.getY();
        Coord wCoord = w.getPos();
        int wx = wCoord.getX();
        int wy = wCoord.getY();
        Orientation mOrt = dir.ort();
        Orientation wOrt = w.getOrt();
        switch(mOrt) {
            case VERT:
                if(px != wx || wy - py > 1 || wy - py < 0 || mOrt == wOrt) {
                    System.err.println("Irr:" + wOrt + "@" + wCoord + " when moving " + dir);
                    return false;
                }
            break;
            case HORIZ:
                if(py != wy || wx - px > 1 || wx - px < 0 || mOrt == wOrt)  {
                    System.err.println("Irr:" + wOrt + "@" + wCoord + " when moving " + dir);
                    return false;
                }
        }
        System.err.println("  Rel:" + wOrt + "@" + wCoord + " when moving " + dir);
        return true;
    }
    
    /**
     * Checks if a direction is blocked at a Coordinate.
     * @param src the starting Coordinate that will be tested from
     * @param dir the direction to move (Use Coord.Direction enum)
     * @return true if the move is blocked
     */
    public boolean isBlocked(Coord src, Direction dir) {
        
        // Go through all placed walls
        for(Wall wall : placedWalls) {
            
            // Check if we need to actually check the wall
            if(isRelevantWall(wall, src, dir)) {
                int wx = wall.getPos().getX();
                int wy = wall.getPos().getY();
                int sx = src.getX();
                int sy = src.getY();
                
                // Check for four directions
                switch(dir) {
                    case NORTH:
                        if(wx == sx && wy == sy) return true;
                    break;
                    case EAST:
                        if(wx - 1 == sx && wy == sy) return true;
                    break;
                    case SOUTH:
                        if(wx == sx && wy - 1 == sy) return true;
                    break;
                    case WEST:
                        if(wx == sx && wy == sy) return true;
                    break;
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
     * Places a wall on the board with a length of 2.
     * @param pos a Coordinate of the position to place the wall (upper-left of tile)
     * @param ort the Orientation of the wall HORIZ or VERT).
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
    
    /**
     * Places a wall on the board with a length of 2.
     * @param w the Wall to place on the board.
     */
    public void placeWall(Wall w) {
        placeWall(w.getPos(), w.getOrt());
    }
}
