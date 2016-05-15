package com.tmquoridor.Board;

import java.util.HashMap;

/**
 * Enum for directions
 * calling ort() will return the Directions respective Orientation
 * calling toString() will return the String representation
 */
public enum Direction {
    NORTH {
        @Override
        public Orientation ort() {
            return Orientation.VERT;
        }
        
        @Override
        public String toString() {
            return "N";
        }
    }, EAST {
        @Override
        public Orientation ort() {
            return Orientation.HORIZ;
        }
        
        @Override
        public String toString() {
            return "E";
        }
    }, SOUTH {
        @Override
        public Orientation ort() {
            return Orientation.VERT;
        }
        
        @Override
        public String toString() {
            return "S";
        }
    }, WEST {
        @Override
        public Orientation ort() {
            return Orientation.HORIZ;
        }
        
        @Override
        public String toString() {
            return "W";
        }
    };
    
    public abstract Orientation ort();
    
    /**
     * Converts even numbers to HORIZ and odd to VERT
     * @param d the number to convert
     * @return Orientation.HORIZ if even, Orientation.VERT otherwise
     */
    public static Orientation getOrientation(int d) {
    	// convert int to dir, if even Hor wall else odd vert wall
        return (d % 2 == 0) ? Orientation.HORIZ : Orientation.VERT;
    }
}