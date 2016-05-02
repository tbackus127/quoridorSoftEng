package com.tmquoridor.Board;

/**
 * Enum for orientations
 * calling toString() will return the String representation
 */
public enum Orientation {
    HORIZ {
        @Override
        public String toString() {
            return "H";
        }
        
        @Override
        public Orientation neg() {
          return Orientation.VERT;
        }
    }, VERT {
        @Override
        public String toString() {
            return "V";
        }
        
        @Override
        public Orientation neg() {
          return Orientation.HORIZ;
        }
    };
    
    public abstract Orientation neg();
}