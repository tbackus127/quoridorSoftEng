package com.tmquoridor.GUI;

import java.awt.*;
import javax.swing.*;

import com.tmquoridor.Board.*;

public class QuorPanel extends JPanel {
    
    private static final int TILE_SIZE = 32;
    private static final int WALL_SIZE = 8;
    private static final int MARGIN_BOARD_LEFT = 120;
    private static final int MARGIN_BOARD_TOP = 48;
    private static final int PADDING_PAWN = 2;
    
    private static final Color COLOR_BG = new Color(180, 24, 24);
    private static final Color COLOR_TILE = new Color(24, 0, 0);
    private static final Color COLOR_WALL = new Color(255, 236, 160);
    private static final Color COLOR_PAWN = new Color(255, 236, 160);
    
    private final Board board;
    
    public QuorPanel(Board b) {
        super();
        board = b;
        setSize(QuorGUI.FRAME_WIDTH - 32, QuorGUI.FRAME_HEIGHT - 32);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(COLOR_BG);
        System.err.println("Panel constructed.");
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.err.println("paint() called");
        updateGUI(g);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }
    
    private void updateGUI(Graphics g) {
        paintGrid(g);
        paintPawns(g);
        paintWalls(g);
    }
    
    private void paintGrid(Graphics g) {
        g.setColor(COLOR_TILE);
        
        // Draw Tiles
        // Rows
        for(int row = 0; row < 9; row++) {
            
            // Columns
            for(int col = 0; col < 9; col++) {
                int orgX = MARGIN_BOARD_LEFT + col * (TILE_SIZE + WALL_SIZE);
                int orgY = MARGIN_BOARD_TOP + row * (TILE_SIZE + WALL_SIZE);
                g.fillRect(orgX, orgY, TILE_SIZE, TILE_SIZE);
            }
        }
    }
    
    private void paintPawns(Graphics g) {
        g.setColor(COLOR_PAWN);
        for(int i = 0; i < board.getNumOfPlayers(); i++) {
            Coord pPos = board.getPlayerPos(i);
            int px = pPos.getX();
            int py = pPos.getY();
            int gx = PADDING_PAWN + MARGIN_BOARD_LEFT + px * (TILE_SIZE + WALL_SIZE);
            int gy = PADDING_PAWN + MARGIN_BOARD_TOP + py * (TILE_SIZE + WALL_SIZE);
            g.fillOval(gx, gy, TILE_SIZE - PADDING_PAWN * 2, TILE_SIZE - PADDING_PAWN * 2);
        }
    }
    
    private void paintWalls(Graphics g) {
        
    }
    
}