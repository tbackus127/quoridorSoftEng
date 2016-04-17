package com.tmquoridor.Board;

/**
 * Contains a simple coordinate (x, y).
 */
public class Coord {
    
    /** X component of the Coord */
    private final int x;
    
    /** Y component of the Coord */
    private final int y;

    /** Used for various computations */
    private int mark;
    
    /**
     * Default constructor
     * @param x the x component of the coordinate
     * @param y the y component of the coordinate
     */
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
        this.mark = -1;
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
     * Translates a Coord by Direction
     * @param dir the Direction to move
     * @return the resulting Coord (bounded if OoB) if the move would be made.
     * @throws RuntimeException if the internal Direction processing returns a null pointer
     */
    public Coord translate(Direction dir) throws RuntimeException {
        int x2 = this.x;
        int y2 = this.y;
        switch(dir) {
            case NORTH:
                y2--;
                if(y2 < 0) 
                    y2 = 0;
            break;
            case EAST:
                x2++;
                if(x2 > 8)
                    x2 = 8;
            break;
            case SOUTH:
                y2++;
                if(y2 > 8)
                    y2 = 8;
            break;
            case WEST:
                x2--;
                if(x2 < 0)
                    x2 = 0;
            break;
            default:
                System.err.println("Error while translating Coord");
                throw new RuntimeException("SOMETHING WENT HORRIBLY WRONG! (Coord 01)");
        }
        return new Coord(x2, y2);
    }
    
    /**
     * Checks if this Coord is equal to another Coord.
     * @param other the other Coord to check this Coord with
     * @return true if they are equal; false otherwise
     */
    public boolean equals(Coord other) {
        int ox = other.getX();
        int oy = other.getY();
        if(ox != this.x || oy != this.y)
            return false;
        return true;
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
     * Marks this Coord (for processing various things)
     * @param n the mark ID to give it
     */
    public void setMark(int n) {
      this.mark = n;
    }
    
    /**
     * Gets the mark of this Coord
     * @return the mark ID
     */
    public int getMark() {
      return this.mark;
    }
    
    /**
     * toString() method
     * @return a string representation of the Coord object
     */
    @Override
    public String toString() {
        return "(" + getX() + "," + getY() + ((mark >= 0) ? (":" + mark) : "") + ")";
    }
}