package com.tmquoridor.GUI;

import java.awt.*;
import javax.swing.*;

import com.tmquoridor.Board.*;

public class QuorPanel extends JPanel {
    
    private static final int TILE_SIZE = 32;
    private static final int WALL_SIZE = 8;
    
    private static final Color COLOR_BG = new Color(180, 24, 24);
    private static final Color COLOR_TILE = new Color(24, 0, 0);
    private static final Color COLOR_WALL = new Color(255, 236, 160);
    
    private final Board board;
    
    public QuorPanel(Board b) {
        super();
        board = b;
        setSize(QuorGUI.FRAME_WIDTH, QuorGUI.FRAME_HEIGHT);
        setBackground(COLOR_BG);
        setLayout(new BorderLayout());
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateGUI(g);
        
    }
    
    private void updateGUI(Graphics g) {
        paintGrid(g);
        paintPawns(g);
        paintWalls(g);
    }
    
    private void paintGrid(Graphics g) {
        
    }
    
    private void paintPawns(Graphics g) {
        
    }
    
    private void paintWalls(Graphics g) {
        
    }
    
}