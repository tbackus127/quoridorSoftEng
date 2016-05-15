package com.tmquoridor.Board;

/**
 * Enum for orientations
 * calling toString() will return the String representation
 */
public enum Orientation {
    HORIZ {
        @Override
        public String toString() {
        	// H for horizontal
            return "H";
        }
        
        @Override
        public Orientation neg() {
        	//Negate current wall orientation
          return Orientation.VERT;
        }
    }, VERT {
        @Override
        public String toString() {
        	// V for vertical
            return "V";
        }
        
        @Override
        public Orientation neg() {
        	// negate current object's orientation
          return Orientation.HORIZ;
        }
    };
    
	// abstract method for a null orientation
    public abstract Orientation neg();
}