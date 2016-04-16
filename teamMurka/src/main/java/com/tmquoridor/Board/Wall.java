package com.tmquoridor.Board;

/**
 * Wall Class
 * <p>
 * Walls separate tiles on the board to prevent movement.
 */

public class Wall {
    
    /** Coordinate the wall is positioned at. Origin is upper-left of the corresponding tile */
    private final Coord pos;   

    /** Orientation of the wall. Either HORIZ or VERT */
    private final Orientation ort;
    
    /**
     * Default constructor
     * @param pos the origin Coord (from upper-left) the wall will be placed at
     * @param ort the direction the wall is set at. Use Orientation.HORIZ or Orientation.VERT for
     *        distinguishing the directions.
     */
    public Wall(Coord pos, Orientation ort) {
        this.pos = pos;
        this.ort = ort;
    }
    
    /**
     * Gets the corresponding segment (half) of the wall.
     * @param sid the Segment ID. 0 = Wall origin, 1 = Wall extension
     * @return the desired Segment.
     */
    public Segment getSegment(int sid) {
        switch(sid) {
            case 0:
                return new Segment(pos, ort, false);
            case 1:
                int wx = pos.getX();
                int wy = pos.getY();
                int sx = (ort == Orientation.HORIZ) ? wx + 1 : wx;
                int sy = (ort == Orientation.VERT) ? wy + 1 : wy;
                return new Segment(new Coord(sx, sy), ort, true);
        }
        return null;
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
    public Orientation getOrt() {
        return ort;
    }
    
    /**
     * toString() method
     * @return a String representation of the Wall.
     */
    @Override
    public String toString() {
        return "[" + pos + ":" + ((ort == Orientation.VERT) ? "V" : "H") + "]";
    }
}