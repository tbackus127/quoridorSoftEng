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
    
    /** Player Starting Positions */
    private static final Coord[] startPos = {
        new Coord(4, 0),
        new Coord(4, 8),
        new Coord(0, 4),
        new Coord(8, 4)
    };
    
    /** The number of players playing */
    public final int numOfPlayers;
    
    /** ith index contains the ith player's position */
    private Coord[] playerPositions;
    
    /** Contains all walls that have been placed */
    private HashSet<Wall> placedWalls;
    
    /**
     * Default contructor
     * Defaults to two players
     */
    public Board() {
        this(2);
    }
    
    /**
     * Alternative constructor
     * @param plNum the number of players
     */
    public Board(int plNum) {
        numOfPlayers = plNum;
        playerPositions = new Coord[numOfPlayers];
        for(int i = 0; i < playerPositions.length; i++) {
            playerPositions[i] = startPos[i];
        }
        placedWalls = new HashSet<Wall>();
    }
    
    public boolean isLegalWall(Wall w) throws Exception {
        
        // Get the wall we're placing's data
        Coord wPos = w.getPos();
        Orientation wOrt = w.getOrt();
        int wx = wPos.getX();
        int wy = wPos.getY();
        
        // Check board bounds (within 0-8)
        switch(wOrt) {
            case HORIZ:
                if(wy <= 0 || wy >= 9) 
                    return false;
            break;
            case VERT:
                if(wx <= 0 || wx >= 9) 
                    return false;
            break;
            default:
                throw new Exception("SOMETHING WENT HORRIBLY WRONG!\nIn Board:isLegalWall00");
        }
        
        // Break the walls into segments
        HashSet<Segment> segs = getSegments();
        for(Segment s : segs) {
            
            // Get each Segment's data
            Coord sPos = s.getPos();
            int sx = sPos.getX();
            int sy = sPos.getY();
            Orientation sOrt = s.getOrt();
            
            // If it overlaps with the wall we're placing's first segment, illegal
            if(sPos.equals(wPos) && sOrt == wOrt)
                return false;
            
            // Get the wall we're placing's extension (second segment)
            Segment wExt = w.getSegment(1);
            Coord wExtPos = wExt.getPos();
            
            // If it overlaps with any segment, illegal
            if(sPos.equals(wExtPos) && sOrt == wOrt)
                return false;
            
            // If the wall crosses another perpendicular to it, illegal (same midpoint, different orientations)
            if(s.isExt() && sPos.equals(wExtPos))
                return false;
        }
        return true;
    }
    
    /**
     * Gets the legal moves of a specified player
     * @param plNum the player number to get legal moves of
     * @return a HashSet of Coords that can be moved to.
     */
    public HashSet<Coord> getLegalMoves(int plNum) {
        return getLegalMoves(getPlayerPos(plNum));
    }
    
    /**
     * Gets the legal moves at a Coordinate.
     * @param pos Coord to check.
     * @return a HashSet of Coords that can be moved to.
     */
    public HashSet<Coord> getLegalMoves(Coord pos) {
        HashSet<Coord> result = new HashSet<Coord>();
        
        //TODO: Take adjacent players into account
        // Check each cardinal direction
        if(isBlocked(pos, Direction.NORTH))
            result.add(new Coord(pos.getX(), pos.getY() - 1));
        if(isBlocked(pos, Direction.SOUTH))
            result.add(new Coord(pos.getX(), pos.getY() + 1));
        if(isBlocked(pos, Direction.WEST))
            result.add(new Coord(pos.getX() - 1, pos.getY()));
        if(isBlocked(pos, Direction.EAST))
            result.add(new Coord(pos.getX() + 1, pos.getY()));
        
        return result;
    }
    
    /**
     * Checks if a wall is relevant for blocking paths.
     * @param w the Wall to check
     * @param plPos the player's position to check
     * @param dir the Direction the player is moving
     * @return true if the wall is relevant for collision checking
     */
    private boolean isRelevantWall(Wall w, Coord plPos, Direction dir) {
        
        // Get the playerPos's data
        int px = plPos.getX();
        int py = plPos.getY();
        
        // Get the Wall's data
        Coord wCoord = w.getPos();
        int wx = wCoord.getX();
        int wy = wCoord.getY();
        
        // Get orientation data
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
        
        // Check Board bounds
        if(src.getY() <= 0 && dir == Direction.NORTH)
            return true;
        if(src.getY() >= 8 && dir == Direction.SOUTH)
            return true;
        if(src.getX() <= 0 && dir == Direction.WEST)
            return true;
        if(src.getX() >= 8 && dir == Direction.EAST)
            return true;
        
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
                        if(wx == sx && wy == sy) 
                            return true;
                    break;
                    case EAST:
                        if(wx - 1 == sx && wy == sy) 
                            return true;
                    break;
                    case SOUTH:
                        if(wx == sx && wy - 1 == sy) 
                            return true;
                    break;
                    case WEST:
                        if(wx == sx && wy == sy) 
                            return true;
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
     * Places a wall on the board with a length of 2.
     * @param pos a Coordinate of the position to place the wall (upper-left of tile)
     * @param ort the Orientation of the wall HORIZ or VERT).
     */
    public void placeWall(Coord pos, Orientation ort) {
        placedWalls.add(new Wall(pos, ort));
    }
    
    /**
     * Places a wall on the board with a length of 2.
     * @param w the Wall to place on the board.
     */
    public void placeWall(Wall w) {
        placedWalls.add(w);
    }
    
    /**
     * Gets a copy of the placed walls as individual Segments.
     * @return a HashSet of Segments
     */
    public HashSet<Segment> getSegments() {
        HashSet<Segment> result = new HashSet<Segment>();
        for(Wall w : placedWalls) {
            result.add(w.getSegment(0));
            result.add(w.getSegment(1));
        }
        return result;
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
}
