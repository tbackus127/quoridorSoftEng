package com.tmquoridor.GUI;

import java.awt.*;
import javax.swing.*;

import com.tmquoridor.Board.*;

public class QuorPanel extends JPanel {
    
    /** Tile size in pixels */
    private static final int TILE_SIZE = 32;
    
    /** Wall size in pixels */
    private static final int WALL_SIZE = 8;
    
    /** How far the board starts drawing from the left */
    private static final int MARGIN_BOARD_LEFT = 136;
    
    /** How far the board starts drawing from the top */
    private static final int MARGIN_BOARD_TOP = 48;
    
    /** Pixels to shrink the pawn circle */
    private static final int PADDING_PAWN = 2;
    
    // Various RGB colors for GUI components
    private static final Color COLOR_BG = new Color(180, 24, 24);
    private static final Color COLOR_TILE = new Color(24, 0, 0);
    private static final Color COLOR_WALL = new Color(255, 236, 160);
    private static final Color COLOR_PAWN = new Color(255, 236, 160);
    
    /** Font for drawing information text */
    private static final Font FONT_INFO = new Font("Serif", Font.PLAIN, 24);
    
    /** Board to get info from */
    private final Board board;
    
    /** Player Names */
    private final String[] playerNames;
    
    /**
     * Default constructor
     * @param b the Board object to update from
     */
    public QuorPanel(Board b, String[] plNames) {
        super();
        board = b;
        playerNames = plNames;
        setSize(QuorGUI.FRAME_WIDTH - 32, QuorGUI.FRAME_HEIGHT - 32);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(COLOR_BG);
        System.err.println("Panel constructed.");
        System.err.print("Players: ");
        for(int i = 0; i < playerNames.length; i++) {
            System.err.print(playerNames[i] + " ");
        }
        System.err.println();
        
    }
    
    /**
     * Paints the panel
     * @param g Graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.err.println("paint() called");
        updateGUI(g);
    }
    
    /**
     * Gets the panel's preferred size (prevents swing from having a fit)
     * @return Dimension(640x480)
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }
    
    /**
     * Summons a 100-foot tall unicorn covered in chainsaws
     * @param g the Graphics object
     */
    private void updateGUI(Graphics g) {
        paintGrid(g);
        paintPawns(g);
        paintWalls(g);
        paintInfo(g);
    }
    
    /**
     * Draws the tiles
     * @param g the Graphics object
     */
    private void paintGrid(Graphics g) {
        g.setColor(COLOR_TILE);
        
        // Draw Tile rows
        for(int row = 0; row < 9; row++) {
            
            // Columns
            for(int col = 0; col < 9; col++) {
                int orgX = MARGIN_BOARD_LEFT + col * (TILE_SIZE + WALL_SIZE);
                int orgY = MARGIN_BOARD_TOP + row * (TILE_SIZE + WALL_SIZE);
                g.fillRect(orgX, orgY, TILE_SIZE, TILE_SIZE);
            }
        }
    }
    
    /**
     * Draws circles for the pawns' locations
     * @param g the Graphics object
     */
    private void paintPawns(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(COLOR_PAWN);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(int i = 0; i < board.getNumOfPlayers(); i++) {
            Coord pPos = board.getPlayerPos(i);
            int px = pPos.getX();
            int py = pPos.getY();
            int gx = PADDING_PAWN + MARGIN_BOARD_LEFT + px * (TILE_SIZE + WALL_SIZE);
            int gy = PADDING_PAWN + MARGIN_BOARD_TOP + py * (TILE_SIZE + WALL_SIZE);
            g2.fillOval(gx, gy, TILE_SIZE - PADDING_PAWN * 2, TILE_SIZE - PADDING_PAWN * 2);
        }
    }
    
    /**
     * Draws thin rectangles between the grid tiles
     * @param g the Graphics object
     */
    private void paintWalls(Graphics g) {
        
        //TODO: This
    }
    
    /**
     * Updates the info for the various players
     * @param g the Graphics object
     */
    private void paintInfo(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(FONT_INFO);
        
        for(int i = 0; i < playerNames.length; i++) {
            g2.drawString(playerNames[i], 10, 40 * (i+1));
        }
    }
    
}