package com.tmquoridor.Board;

/**
 * Wall Class
 * <p>
 * Walls separate tiles on the board to prevent movement.
 */

public class Wall {
    
    /** Coordinate the wall is positioned at. Origin is upper-left */
    private final Coord pos;   

    /** Orientation of the wall. Either HORIZ or VERT */
    private final Orientation dir;
    
    /**
     * Default constructor
     * @param pos the origin Coord (from upper-left) the wall will be placed at
     * @param dir the direction the wall is set at. Use Dir.HORIZ or Dir.VERT for
     *        distinguishing the directions.
     */
    public Wall(Coord pos, Orientation dir) {
        this.pos = pos;
        this.dir = dir;
    }
    
    /**
     * Gets the position of the Wall
     * @return a Coord for the Wall's position
     */
    public Coord getPos() {
        return pos;
    }
    
    /**
     * Gets the direction of the Wall
     * @return an Orientation of the direction
     */
    public Orientation getDir() {
        return dir;
    }
    
    /**
     * toString() method
     * @return a String representation of the Wall.
     */
    @Override
    public String toString() {
        return "[" + pos + ":(" + ((dir == Orientation.VERT) ? 
            pos.getX() + "," + pos.getY() + 1 :
            pos.getX() + 1 + "," + pos.getY() + "]");
    }
}