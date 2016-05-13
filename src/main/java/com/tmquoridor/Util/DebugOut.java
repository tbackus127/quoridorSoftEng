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
  
  /** Set to true to enable debug output */
  private static final boolean ENABLED = true;
  
  /** The file name, minus ".txt" */
  private String name;
  
  /** The file handle for this PrintStream */
  private File file;
  
  /** The PrintStream used for writing the file */
  private PrintStream fOut;
  
  /** Disable output for this particular instance */
  private boolean isEnabled;
  
  /**
   * Default constructor
   * @param name the filename
   */
  public DebugOut(String name, boolean en) {
      this.name = name;
      this.isEnabled = en;
      
      if(ENABLED && this.isEnabled) {
        
        // Delete previous file (only keep most recent copy)
        try {
            
            File dbDir = new File("debug");
            if(!dbDir.exists()) {
                if(!dbDir.mkdir()) {
                    System.err.println("DebugOut(): Failed to create debug directory!");
                }
            }
            
            // Clear all files in debug folder
            File[] dbFiles = dbDir.listFiles();
            for(File f : dbFiles) {
              if(f.exists())
                f.delete();
            }
            
            this.file = new File(dbDir + "/" + name + ".txt");
            if(this.file.exists()) {
                this.file.delete();
            }
          
            this.fOut = new PrintStream(this.file);
        } catch (IOException ie) {
            System.err.println("DebugOut: Error while creating file " + name + ".txt");
            ie.printStackTrace();
        }
        
      }
  }
  
  /**
   * Writes a timestamped message to the file
   * @param msg the String to write after the timestamp
   * @param sig the call signature ("ClassName.methodName()" recommended; for tracing purposes)
   */
  public void write(String sig, String msg) {
      if(ENABLED && this.isEnabled)
          this.fOut.println(getTimestamp() + "@" + sig + ": " + msg);
  }
  
  /**
   * Enables or disables debugging output for this particular instance
   * @param val what to set the isEnabled flag to
   */
  public void setEnabled(boolean val) {
      this.isEnabled = val;
  }
  
  /**
   * Gets the current timestamp, minus the date
   * @return a String representation of the current time
   */
  private static String getTimestamp() {
    Date date = new Date();
    String stamp = (new Timestamp(date.getTime())).toString();
    return stamp.substring(11, 19);
  }
}