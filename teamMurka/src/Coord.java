/**
 * Coordinate Class
 * <p>
 * Contains a simple coordinate (x, y).
 */

public class Coord {
    
    // X and Y parts of the coordinate
    private final int x, y;

    /**
     * Default constructor
     * @param x the x component of the coordinate
     * @param y the y component of the coordinate
     */
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the distance between two Coords
     * @param a the first Coord to check
     * @param b the second Coord to check
     * @return a double of the distance between Coord a and Coord b
     */
    public static double getDistance(Coord a, Coord b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }
    
    /**
     * Gets the X component of the coordinate
     * @return an int representing the X component
     */
    public int getX() {
        return this.x;
    }
    
    /**
     * Gets the Y component of the coordinate
     * @return an int representing the Y component
     */
    public int getY() {
        return this.y;
    }
    
    /**
     * toString() method
     * @return a string representation of the Coord object
     */
    @Override
    public String toString() {
        return "(" + getX() + "," + getY() + ")";
    }
}