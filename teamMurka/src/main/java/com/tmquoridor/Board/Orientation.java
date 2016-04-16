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
    }, VERT {
        @Override
        public String toString() {
            return "V";
        }
    };
}