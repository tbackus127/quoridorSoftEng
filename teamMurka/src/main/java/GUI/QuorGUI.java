package com.tmquoridor.GUI;

import java.awt.*;
import javax.swing.*;

import com.tmquoridor.Board.*;

public class QuorGUI {
    
    public static final int FRAME_WIDTH = 640;
    public static final int FRAME_HEIGHT = 480;
    
    public QuorPanel panel = null;
    
    public QuorGUI(Board b) {
        
        SwingUtilities.invokeLater(new Runnable() {
           
            public void run() {
                System.err.println("GUI Launched");
                JFrame frame = new JFrame("Quoridor");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
                frame.setLayout(new BorderLayout(0, 0));
                
                panel = new QuorPanel(b);
                frame.add(panel, BorderLayout.PAGE_START);
                frame.validate();
                
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });
        System.err.println("GUI Constructed.");
    }
    
    public void repaintGUI() {
        panel.repaint();
    }
}