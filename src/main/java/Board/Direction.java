package com.tmquoridor.Board;

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
    
    public static Orientation getOrientation(int d) {
        return (d % 2 == 0) ? Orientation.HORIZ : Orientation.VERT;
    }
}