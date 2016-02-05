/**
 * Wall Class
 * <p>
 * Walls separate tiles on the board to prevent movement.
 */

public class Wall {
    
    /** Coordinate the wall is positioned at. Origin is upper-left */
    private final Coord pos;   

    /** true = vertical, false = horizontal. Spans two tiles */
    private final boolean dir;
    
    /** Enum for directions */
    public enum Dir {
        HORIZ(false),
        VERT(true)
    }
    
    /**
     * Default constructor
     * @param pos the origin Coord (from upper-left) the wall will be placed at
     * @param dir the direction the wall is set at. Use Dir.HORIZ or Dir.VERT for
     *        distinguishing the directions.
     */
    public Wall(Coord pos, boolean dir) {
        this.pos = pos;
        this.dir = dir;
    }
    
    /**
     * toString() method
     * @return a String representation of the Wall.
     */
    public String toString() {
        return "[" + pos + ":(" (dir) ? 
            pos.getX() + "," + pos.getY() + 2 :
            pos.getX() + 2 + "," + pos.getY()
        ];
    }
}