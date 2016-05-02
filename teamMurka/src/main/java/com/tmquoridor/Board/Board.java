package com.tmquoridor.Board;


import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
 
/**
 * Contains all data for player positions and walls.
 */
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
    private final int numOfPlayers;
    
    /** ith index contains the ith player's position */
    private Coord[] playerPositions;
    
    /** Contains all walls that have been placed */
    private HashSet<Wall> placedWalls;
    
    /** Contains the PID of all kicked players */
    private HashSet<Integer> kickedPlayers;
    
    /** Contains how many walls each player has left */
    private int[] wallsLeft;
    
    /** If the game has a winner yet */
    private boolean hasWinner;
    
    private HashMap<String, Direction> dirMap;
    private HashMap<String, Orientation> ortMap;
    private HashMap<Integer, ArrayList<Coord>> winningPos;
    
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
        for(int i = 0; i < playerPositions.length; i++)
            playerPositions[i] = startPos[i];
        placedWalls = new HashSet<Wall>();
        kickedPlayers = new HashSet<Integer>();
        hasWinner = false;
        buildMaps();
        wallsLeft = new int[numOfPlayers];
        for(int i = 0; i < wallsLeft.length; i++) {
            wallsLeft[i] = 20 / numOfPlayers;
        }
    }
    
    /**
     * Gets the player IDs that have been kicked
     * @return the IDs of the kicked players as a HashSet of Integers
     */
    public HashSet<Integer> getKickedPlayers() {
        return kickedPlayers;
    }
    
    /**
     * Tests whether or not a player has been kicked
     * @param id the player ID to check
     * @return true if the player has been kicked; false otherwise
     */
    public boolean isPlayerKicked(int id) {
        return kickedPlayers.contains(id);
    }
    
    /**
     * Gets the number of players at the beginning of the match
     * @return the number of players as an int.
     */    
    public int getTotalPlayers() {
        return numOfPlayers;
    }
    
    /**
     * Gets the number of players still playing
     * @return the number of players as an int.
     */
    public int getNumOfPlayers() {
        return numOfPlayers - kickedPlayers.size();
    }
    
    /**
     * Removes a player from the Board
     * @param plNum the player ID to remove
     */
    public void removePlayer(int plNum) {
        playerPositions[plNum] = null;
        kickedPlayers.add(plNum);
        wallsLeft[plNum] = 0;
    }
    
    /**
     * Converts a string to a Direction
     * @param s the string
     * @return the Direction equivalent (Ex: "WEST" for Direction.WEST)
     */
    public Direction toDir(String s) {
        return dirMap.get(s);
    }
    
    /**
     * Converts a string to an Orientation
     * @param s the string
     * @return the Orientation equivalent (Ex: "HORIZ" for Orientation.HORIZ)
     */
    public Orientation toOrt(String s) {
        return ortMap.get(s);
    }
    
    /**
     * Gets all walls that would block a player's path
     * @param pid the player ID to run against
     * @param path the path returned from getShortestPath()
     */
    public HashSet<Wall> getBlockingWalls(int pid, ArrayList<Coord> path) {
      HashSet<Wall> result = new HashSet<Wall>();
      
      // For testing
      result.add(new Wall(new Coord(0,0), Orientation.VERT));
      return result;
    }
    
    /**
     * Gets the shortest path with a wall tested
     * @param pid the player ID to check
     * @param w the wall to check shortest path with (must be a legal wall)
     * @return the shortest path as an ArrayList of Coords
     */
    public ArrayList<Coord> getShortestPath(int pid, Wall w) {
        Board b = copyOf();
        b.placeWall(w);
        return b.getShortestPath(pid);
    }
    
    /**
     * Gets the shortest path for any player to win
     * @param pid the player ID to check
     * @return the shortest path as an ArrayList of Coords
     */
    public ArrayList<Coord> getShortestPath(int pid) {
        if(kickedPlayers.contains(pid) || pid < 0) {
          System.err.println("Tried to get kicked or unknown player's path: PID=" + pid);
          return null;
        }
        ArrayList<Coord> path = null;
        ArrayList<Coord> temp = null;
        ArrayList<Coord> winPos = winningPos.get(pid);
        for(Coord move : winPos) {
            temp = getShortestPath(pid, move);
            if (temp != null) {
                if (path == null) {
                    path = temp;
                } else if (temp.size() < path.size()) {
                    path = temp;
                }
            }
        }
        return path;
    }
    
    /**
     * Gets the shortest path to a destination
     * @param pid the player ID to use
     * @param dest the Coord to path to
     * @return an ArrayList of Coords that is the shortest path; null if it doesn't exist
     */
    public ArrayList<Coord> getShortestPath(int pid, Coord dest) {
      PathFinder finder = new PathFinder(pid, this);
      return finder.getPath(dest);
    }
    
    /**
     * Performs a deep copy of this Board and returns it
     * @return a copy of this Board object
     */
    public Board copyOf() {
      
      // System.err.println("Board.copyOf()");
      Board b = new Board(numOfPlayers);
      
      // Player copy operations
      for(int i = 0; i < numOfPlayers; i++) {
        if(kickedPlayers.contains(i)) {
          // System.err.println("  Skipped kicked player " + i);
          b.removePlayer(i);
        } else {
          Coord pos = getPlayerPos(i);
          b.movePlayer(i, pos);
          // System.err.println("  Copying Player " + i + " at " + pos + " -> " + b.getPlayerPos(i));
        }
      }
      
      // Wall copy operations
      for(Wall w : getWalls()) {
        b.placeWall(w);
      }
      
      // Walls remaining
      for(int i = 0; i < wallsLeft.length; i++) {
        b.setWallsRemaining(i, wallsRemaining(i));
      }
      
      return b;
    }
    
    /**
     * Prints the Board's data
     */
    public void printBoard() {
        System.out.println("\n:: BOARD ::\n");
        for(int i = 0; i < numOfPlayers; i++) {
            if(playerPositions[i] != null) {
                System.out.println("Player " + (i+1) + ": " + getPlayerPos(i));
                System.out.println("  Moves: " + getLegalMoves(i));
            }
        }
        System.out.println();
        for(Wall w : placedWalls) {
            System.out.println("Wall@" + w);
        }
        
    }
    
    /**
     * toString method
     * @return a String representation of the Board
     */
    public String toString() {
        String message = "";
        message += ("\n:: BOARD ::\n");
        for(int i = 0; i < numOfPlayers; i++) {
            if(playerPositions[i] != null) {
                message += ("Player " + (i + 1) + ": " + getPlayerPos(i)) + "\n";
                // message += ("  Moves: " + getLegalMoves(i)) + "\n";
            }
        }
        System.out.println();
        for(Wall w : placedWalls) {
            message += ("Wall@" + w) + "\n";
        }
        return message;
    }
    
    /**
     * Gets the winner (if any yet)
     * @return 0 if no winner; returns the PID of the winner otherwise.
     */
    public int getWinner() {
        
        if(getNumOfPlayers() == 1) {
          for(int i = 0; i < numOfPlayers; i++) {
            if(!isPlayerKicked(i)) {
              hasWinner = true;
              return i + 1;
            }
          }
        }
        
        // Go through each player
        for(int i = 0; i < numOfPlayers; i++) {
            if(kickedPlayers.contains(i)) continue;
            
            // Get player coord
            Coord pCoord = getPlayerPos(i);
            int px = pCoord.getX();
            int py = pCoord.getY();
            
            // If any are on their far side, return the winner
            if(i == 0 && py == 8) return 1;
            if(i == 1 && py == 0) return 2;
            if(i == 2 && px == 8) return 3;
            if(i == 3 && px == 0) return 4;
        }
        
        return 0;
    }
    
    /**
     * Needed by the GUI to stop painting
     * @return if there was a winner
     */
    public boolean wasWinner() {
      return hasWinner;
    }
    
    /**
     * Checks if a wall is legal
     * @param pid the player ID placing the wall
     * @param w the Wall to check
     * @return true if legal; false otherwise
     * @throws RuntimeException if the internal Orientation model returns a null pointer
     */
    public boolean isLegalWall(int pid, Wall w) throws RuntimeException {
        
        if(wallsLeft[pid] <= 0)
            return false;
        
        System.err.println("w: " + w);
        
        // Get the wall we're placing's data
        Coord wPos = w.getPos();
        Orientation wOrt = w.getOrt();
        int wx = wPos.getX();
        int wy = wPos.getY();
        
        // Check board bounds (within 0-8)
        if (wOrt == Orientation.HORIZ) {
          if(wx <= -1 || wx >= 8 || wy <= 0 || wy >= 9)
            return false;
        } else {
          if(wy <= -1 || wy >= 8 || wx <= 0 || wx >= 9)
            return false;
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
        
        // Checks to make sure all players can still win the game
        for(int i = 0; i < numOfPlayers; i++) {
            if(!isPlayerKicked(i)) {
                ArrayList<Coord> temp = getShortestPath(i, w);
                if(temp == null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    /**
     * Tests if a player's move is legal
     * @param pid the player ID
     * @param dest the player's destination to check validity
     * @return true if it's a legal move; false otherwise
     */
    public boolean isLegalMove(int pid, Coord dest) {
        HashSet<Coord> legalMoves = getLegalMoves(pid);
        for(Coord c : legalMoves) {
            if(dest.equals(c)) return true;
        }
        return false;
    }
    
    
    /**
     * Gets the legal moves at a Coordinate.
     * @param pid the player ID to check.
     * @return a HashSet of Coords that can be moved to.
     */
    public HashSet<Coord> getLegalMoves(int pid) {
        if(kickedPlayers.contains(pid)) {
            System.err.println("Tried to get legal moves of kicked player.");
            return new HashSet<Coord>();
        }
        MoveTrace mt = new MoveTrace();
        mt.addPlayer(pid);
        Coord c = getPlayerPos(pid);
        if(c == null)
          System.err.println("getLegalMoves(int):c is null! PID=" + pid);
        return getLegalMoves(mt, c).getMoves();
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
        
        HashSet<Segment> segs = getSegments();
        
        // Go through all placed walls
        for(Segment s : segs) {
            
            // Check if we need to actually check the segment
            if(isRelevantWall(s, src, dir)) {
                int wx = s.getPos().getX();
                int wy = s.getPos().getY();
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
        if(dest == null) {
          System.err.println("movePlayer(): dest == null!");
          return;
        }
        if(kickedPlayers.contains(plNum)) {
            System.err.println("Tried to move a player that was kicked!");
            return;
        }
        playerPositions[plNum] = dest;
    }
    
    // DEBUG
    public void movePlayer(int pid, Coord c, String callFrom) {
      System.out.println("Called from " + callFrom + ":");
      movePlayer(pid, c);
    }
    
    /**
     * Places a wall on the board with a length of 2 and subtracts one from the player's wall count.
     * @param pid the player who placed the wall.
     * @param pos a Coordinate of the position to place the wall (upper-left of tile)
     * @param ort the Orientation of the wall HORIZ or VERT).
     */
    public void placeWall(int pid, Coord pos, Orientation ort) {
        if(wallsLeft[pid] <= 0)
            System.err.println("Placed too many walls! (Player: " + pid);
        wallsLeft[pid]--;
        placeWall(pos, ort);
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
        Coord c = w.getPos();
        int x = c.getX();
        int y = c.getY();
        Orientation ort = w.getOrt();
        placedWalls.add(new Wall(new Coord(x, y), ort));
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
        if(kickedPlayers.contains(plNum)) {
            System.err.println("Tried to get kicked player's coordinates!");
            return null;
        }
        return playerPositions[plNum];
    }
    
    /**
     * Gets the player ID at a Coord
     * @param c the Coord to check the player at
     * @return the player ID found at the Coord c (-1 if no player)
     */
    public int getPlayerAtCoord(Coord c) {
        if(c == null) {
          System.err.println("!! getPlayerAtCoord(): c is null");
          return -1;
        }
        int cx = c.getX();
        int cy = c.getY();
        for(int i = 0; i < playerPositions.length; i++) {
            if(kickedPlayers.contains(i)) continue;
            int px = playerPositions[i].getX();
            int py = playerPositions[i].getY();
            if(px == cx && py == cy)
                return i;
        }
        return -1;
    }
    
    /**
     * Gets all placed walls in the game
     * @return a HashSet of all Walls placed
     */
    public HashSet<Wall> getWalls() {
        return placedWalls;
    }
    
        /**
     * Legal moves helper method.
     * @param mt the MoveTrace so far
     * @param curr the current Coord we're checking
     * @return the MoveTrace thus far
     */
    private MoveTrace getLegalMoves(MoveTrace mt, Coord curr) {
        
        // if(curr == null)
          // System.err.println("getLegalMoves(MoveTrace, Coord): curr == null!");
        
        // For all four directions
        for(Direction dir : Direction.values()) {
            Coord c2 = null;
            try {
                c2 = curr.translate(dir);
            } catch (Exception e) {
                // System.err.println("!! TRANSLATION FAILED!");
                // e.printStackTrace();
                // c2 = curr;
                break;
            }
            int pid = getPlayerAtCoord(c2);
            
            // If it's not blocked
            if(!isBlocked(curr, dir)) {
              
                // If there's a player present
                if(pid >= 0 && !mt.isSeen(pid)) {
                    mt.addPlayer(pid);
                    mt.add(getLegalMoves(mt, c2));
                } else {
                  mt.addMove(c2);
                }
              
            }
            
/*            
            
            // If there is a player in this direction
            if(pid >= 0) {
                
                // If they haven't been seen yet, add their legal moves to this
                if(!mt.isSeen(pid)) {
                    mt.addPlayer(pid);
                    mt.add(getLegalMoves(mt, c2));
                }
                
            // If no player and not blocked, add it
            } else if(!isBlocked(curr, dir)) {
                mt.addMove(c2);
            }
*/
            
            
        }
        return mt;
    }
    
    /**
     * Checks if a wall is relevant for blocking paths.
     * @param w the Wall to check
     * @param plPos the player's position to check
     * @param dir the Direction the player is moving
     * @return true if the wall is relevant for collision checking
     */
    private boolean isRelevantWall(Segment s, Coord plPos, Direction dir) {
        
        // Get the playerPos's data
        int px = plPos.getX();
        int py = plPos.getY();
        
        // Get the Segment's data
        Coord sCoord = s.getPos();
        int sx = sCoord.getX();
        int sy = sCoord.getY();
        
        // Get orientation data
        Orientation mOrt = dir.ort();
        Orientation sOrt = s.getOrt();
        
        switch(mOrt) {
            case VERT:
                if(px != sx || sy - py > 1 || sy - py < 0 || mOrt == sOrt) {
                    // System.err.println("Irr:" + sOrt + "@" + sCoord + " when moving " + dir);
                    return false;
                }
            break;
            case HORIZ:
                if(py != sy || sx - px > 1 || sx - px < 0 || mOrt == sOrt)  {
                    // System.err.println("Irr:" + sOrt + "@" + sCoord + " when moving " + dir);
                    return false;
                }
        }
        // System.err.println("  Rel:" + sOrt + "@" + sCoord + " when moving " + dir);
        return true;
    }
    
    /**
     * Get remaining walls for a player
     * @param plNum the player to get the remaining walls for.
     * @return an int for the number of walls left
     */
    public int wallsRemaining(int plNum){
        return wallsLeft[plNum];
    }
    
    /**
     * Sets the number of walls remaining for a player
     * @param plNum the player number to edit
     * @param n the new number of walls left for that player
     */
    public void setWallsRemaining(int plNum, int n) {
      wallsLeft[plNum] = n;
    }
    
    private void buildMaps() {
        dirMap = new HashMap<String, Direction>();
        dirMap.put("n", Direction.NORTH);
        dirMap.put("s", Direction.SOUTH);
        dirMap.put("e", Direction.EAST);
        dirMap.put("w", Direction.WEST);
        dirMap.put("N", Direction.NORTH);
        dirMap.put("S", Direction.SOUTH);
        dirMap.put("E", Direction.EAST);
        dirMap.put("W", Direction.WEST);
        dirMap.put("north", Direction.NORTH);
        dirMap.put("south", Direction.SOUTH);
        dirMap.put("east", Direction.EAST);
        dirMap.put("west", Direction.WEST);
        dirMap.put("up", Direction.NORTH);
        dirMap.put("down", Direction.SOUTH);
        dirMap.put("right", Direction.EAST);
        dirMap.put("left", Direction.WEST);
        
        ortMap = new HashMap<String, Orientation>();
        ortMap.put("h", Orientation.HORIZ);
        ortMap.put("v", Orientation.VERT);
        ortMap.put("H", Orientation.HORIZ);
        ortMap.put("V", Orientation.VERT);
        ortMap.put("horiz", Orientation.HORIZ);
        ortMap.put("HORIZ", Orientation.HORIZ);
        ortMap.put("vert", Orientation.VERT);
        ortMap.put("VERT", Orientation.VERT);
  
        winningPos = new HashMap<Integer, ArrayList<Coord>>();
        ArrayList<Coord> list = new ArrayList<Coord>();
        for(int i = 0; i <= 8; i++){
            list.add(new Coord(i,8));
        }
        winningPos.put(0,list);
        
        list = new ArrayList<Coord>();
        for(int i = 0; i <= 8; i++){
            list.add(new Coord(i,0));
        }
        winningPos.put(1,list);
        
        list = new ArrayList<Coord>();
        for(int i = 0; i <= 8; i++){
            list.add(new Coord(8,i));
        }
              winningPos.put(2,list);
        
        list = new ArrayList<Coord>();
        for(int i = 0; i <= 8; i++){
            list.add(new Coord(0,i));
        }
        winningPos.put(3,list);
    }
}
