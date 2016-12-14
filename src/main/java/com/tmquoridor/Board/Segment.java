
package com.tmquoridor.Board;

/**
 * Segments are halves of walls.
 */
public class Segment {
  
  /** The coordinate of the segment (upper-left) */
  private final Coord pos;
  
  /** The orientation of the Segment (horizontal/vertical) */
  private final Orientation ort;
  
  /** Whether or not this Segment is an extension */
  private final boolean ext;
  
  /**
   * Default constructor
   * 
   * @param pos the Coord position of the Segment
   * @param ort the Orientation of the Segment
   * @param ext if the segment is an extension of a Wall
   */
  public Segment(Coord pos, Orientation ort, boolean ext) {
    this.pos = pos;
    this.ort = ort;
    this.ext = ext;
  }
  
  /**
   * Gets the position of the Segment
   * 
   * @return a Coord representing the position of the Segment.
   */
  public Coord getPos() {
    return this.pos;
  }
  
  /**
   * Gets the orientation of the Segment
   * 
   * @return Orientation.HORIZ if horizontal, Orientation.VERT if vertical
   */
  public Orientation getOrt() {
    return this.ort;
  }
  
  /**
   * Gets whether or not this Segment is an extension of a Wall (not the source position)
   * 
   * @return true if it is an extension
   */
  public boolean isExt() {
    return this.ext;
  }
}