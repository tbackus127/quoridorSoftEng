/**
 * Wall Class
 * <p>
 * Walls separate tiles on the board to prevent movement.
 */

public class Wall {
    
    /** Coordinate the wall is positioned at. Origin is upper-left */
    private final Coord pos;   

    /** true = vertical, false = horizontal. Spans ONE tile */
    private final boolean dir;
    
    /** Enum for orientation */
    public enum Orientation {
        HORIZ(0), VERT(1)
    }
    
    /** Enum for directions */
    public enum Direction {
        NORTH(0), EAST(1), SOUTH(2), WEST(3)
        
        public boolean getOrientation(int d) {
            return (d % 2 == 0) ? Orientation.HORIZ : Orientation.VERT;
        }
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
        return "[" + pos + ":(" (dir == Orientation.VERT) ? 
            pos.getX() + "," + pos.getY() + 1 :
            pos.getX() + 1 + "," + pos.getY()
        ];
    }
}