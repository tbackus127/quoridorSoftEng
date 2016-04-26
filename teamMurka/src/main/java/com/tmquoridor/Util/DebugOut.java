package com.tmquoridor.Util;

import java.util.Date;

import java.io.PrintStream;
import java.io.File;
import java.io.IOException;

import java.sql.Timestamp;

/**
 * This class performs writing debugging comments to a text file rather than spamming the console.
 */
public class DebugOut {
  
  /** The file name, minus ".txt" */
  private String name;
  
  /** Call signature, for finding out where the message came from*/
  private String sig;
  
  /** The file handle for this PrintStream */
  private File file;
  
  /** The PrintStream used for writing the file */
  private PrintStream fOut;
  
  /**
   * Default constructor
   * @param name the filename
   */
  public DebugOut(String name, String sig) {
      this.name = name;
      this.sig = sig;
      
      // Delete previous file (only keep most recent copy)
      try {
          this.file = new File(name);
          if(this.file.exists()) {
              this.file.delete();
          }
        
          this.fOut = new PrintStream(this.file);
      } catch (IOException ie) {
          System.err.println("DebugOut: Error while creating file " + name + ".txt");
          ie.printStackTrace();
      }
  }
  
  /**
   * Writes a timestamped message to the file
   * @param msg the String to write after the timestamp
   */
  public void write(String msg) {
      this.fOut.println(getTimestamp() + "<" + sig + ">:" + msg);
  }
  
  /**
   * Gets the current timestamp, minus the date
   * @return a String representation of the current time
   */
  private static String getTimestamp() {
    Date date = new Date();
    String stamp = (new Timestamp(date.getTime())).toString();
    return stamp.substring(11);
  }
}