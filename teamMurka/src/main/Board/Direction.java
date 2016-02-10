package com.tmquoridor.Board;

public enum Direction {
    NORTH {
        @Override
        public Orientation ort() {
            return Orientation.VERT;
        }
    }, EAST {
        @Override
        public Orientation ort() {
            return Orientation.HORIZ;
        }
    }, SOUTH {
        @Override
        public Orientation ort() {
            return Orientation.VERT;
        }
    }, WEST {
        @Override
        public Orientation ort() {
            return Orientation.HORIZ;
        }
    };
    
    public abstract Orientation ort();
    
    public static Orientation getOrientation(int d) {
        return (d % 2 == 0) ? Orientation.HORIZ : Orientation.VERT;
    }
    
    public Direction getDir(int d) {

    }
}