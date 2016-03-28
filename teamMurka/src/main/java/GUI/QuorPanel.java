package com.tmquoridor.GUI;

import java.awt.*;
import javax.swing.*;

import com.tmquoridor.Board.*;

public class QuorPanel extends JPanel {
    
    /** Tile size in pixels */
    private static final int TILE_SIZE = 32;
    
    /** Wall size in pixels */
    private static final int WALL_SIZE = 8;
    
    /** Size of the board grid, in pixels */
    private static final int BOARD_SIZE = (TILE_SIZE + WALL_SIZE) * 9;
    
    /** How far the board starts drawing from the left */
    private static final int MARGIN_BOARD_LEFT = 136;
    
    /** How far the board starts drawing from the top */
    private static final int MARGIN_BOARD_TOP = 48;
    
    /** How far the info text is indented from the left */
    private static final int MARGIN_TEXT_LEFT = 4;
    
    /** How far the player info is indented from the top */
    private static final int MARGIN_TEXT_TOP = 64;
    
    /** How far down the next line of info is */
    private static final int MARGIN_TEXT_LINE = 16;
    
    /** Pixels to shrink the pawn circle */
    private static final int PADDING_PAWN = 2;
    
    /** Offset for Wall x placement */
    private static final int OFFSET_WALL_X = 0;
    
    /** Offset for Wall y placement */
    private static final int OFFSET_WALL_Y = 0;
    
    // Various RGB colors for GUI components
    private static final Color COLOR_BG = new Color(180, 24, 24);
    private static final Color COLOR_TILE = new Color(24, 0, 0);
    private static final Color COLOR_WALL = new Color(255, 236, 160);
    private static final Color COLOR_PAWN = new Color(255, 236, 160);
    
    /** Font for drawing information labels */
    private static final Font FONT_LABELS = new Font("Serif", Font.PLAIN, 24);
    
    /** Font for drawing information text */
    private static final Font FONT_INFO = new Font("Serif", Font.PLAIN, 14);
    
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
            
            // Calculate drawing coords
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
        
        g.setColor(COLOR_WALL);
        
        for(Wall w : board.getWalls()) {
            
            // Unpack Wall data
            Coord wCoord = w.getPos();
            int wx = wCoord.getX();
            int wy = wCoord.getY();
            Orientation wOrt = w.getOrt();
            
            // Drawing coords and spans
            int gx = MARGIN_BOARD_LEFT + (wx * (TILE_SIZE + WALL_SIZE));
            int gy = MARGIN_BOARD_TOP + (wy * (TILE_SIZE + WALL_SIZE)) - WALL_SIZE;
            int sx = TILE_SIZE * 2 + WALL_SIZE;
            int sy = WALL_SIZE;
            
            if(wOrt == Orientation.VERT) {
                
                // Swap sx and sy if vertical wall
                sx ^= sy;
                sy ^= sx;
                sx ^= sy;
                
                // Calculate position
                gx -= WALL_SIZE;
                gy += WALL_SIZE;
            }
            g.fillRect(gx, gy, sx, sy);
        }
    }
    
    /**
     * Updates the info for the various players
     * @param g the Graphics object
     */
    private void paintInfo(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(FONT_LABELS);
        
        for(int i = 0; i < playerNames.length; i++) {
            
            // Draw labels
            g2.setFont(FONT_LABELS);
            int labelx = (i % 2 == 0) ? (BOARD_SIZE * (i % 2)) : ((BOARD_SIZE + MARGIN_BOARD_LEFT) * (i % 2)) - 8;
            labelx += MARGIN_TEXT_LEFT;
            int labely = (i < 2) ? MARGIN_TEXT_TOP : MARGIN_TEXT_TOP << 2;
            g2.drawString("Player " + (i+1), labelx, labely);
            
            // Draw player names
            g2.setFont(FONT_INFO);
            int textx = labelx + MARGIN_TEXT_LEFT;
            int texty = labely + MARGIN_TEXT_LINE;
            g2.drawString("\"" + playerNames[i] + "\"", textx, texty);
            
            // Draw walls left
            texty += MARGIN_TEXT_LINE;
            g2.drawString("Walls: " + board.wallsRemaining(i), textx, texty);
            
        }
    }
    
}
