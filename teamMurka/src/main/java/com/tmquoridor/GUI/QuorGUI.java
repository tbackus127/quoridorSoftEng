package com.tmquoridor.GUI;

import java.awt.*;
import javax.swing.*;

import com.tmquoridor.Board.*;

public class QuorGUI {
    
    /** Width of the JFrame */
    public static final int FRAME_WIDTH = 640;
    
    /** Height of the JFrame */
    public static final int FRAME_HEIGHT = 480;
    
    /** Reference to QuorPanel (JPanel subclass) */
    public QuorPanel panel = null;
    
    /**
     * Default constructor
     * @param b a reference to the Board to draw the info of
     * @param plNames the names of the players playing
     */
    public QuorGUI(Board b, String[] plNames) {
        
        // Do painting on the Event Dispatch Queue instead of the main thread
        SwingUtilities.invokeLater(new Runnable() {
           
            public void run() {
                System.err.println("GUI Launched");
                
                // Setup frame layouts
                JFrame frame = new JFrame("Quoridor");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
                frame.setLayout(new BorderLayout(0, 0));
                
                // Add the "canvas" and revalidate
                panel = new QuorPanel(b, plNames);
                frame.add(panel, BorderLayout.PAGE_START);
                frame.validate();
                
                // Finish setup and display GUI
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });
        System.err.println("GUI Constructed.");
    }
    
    /**
     * Repaints the GUI, called in the client after a move is made.
     */
    public void repaintGUI() {
        if(panel != null)
          panel.repaint();
    }
}