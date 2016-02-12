package com.tmquoridor.Board;

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