package com.tmquoridor.GUI;

import java.util.ArrayList;

import java.io.File;
import java.io.PrintStream;
import java.io.IOException;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;

import com.tmquoridor.Board.*;

public class QuorPanel extends JPanel {
    
    /** Tile size in pixels */
    private static final int TILE_SIZE = 33;
    
    /** Wall size in pixels */
    private static final int WALL_SIZE = 8;
    
    /** Size of the board grid, in pixels */
    private static final int BOARD_SIZE = (TILE_SIZE + WALL_SIZE) * 9;
    
    /** How far the board starts drawing from the left */
    private static final int MARGIN_BOARD_LEFT = 142;
    
    /** How far the board starts drawing from the top */
    private static final int MARGIN_BOARD_TOP = 72;
    
    /** How far the info text is indented from the left */
    private static final int MARGIN_TEXT_LEFT = 10;
    
    /** How far the player info is indented from the top */
    private static final int MARGIN_TEXT_TOP = 138;
    
    /** How far down the next line of info is */
    private static final int MARGIN_TEXT_LINE = 24;
    
    /** Pixels to shrink the pawn circle */
    private static final int PADDING_PAWN = 2;
    
    /** Pixels to shrink the pathing circle */
    private static final int PADDING_PATH = 15;
    
    /** Pixels to offset player number pawn label (x) */
    private static final int MARGIN_PNUM_X = 11;
    
    /** Pixels to offset player number pawn label (y) */
    private static final int MARGIN_PNUM_Y = 19;
    
    /** So path lines don't overlap, extra pixels to push points to the side */
    private static final int PATH_OFFSET = 2;
    
    /** Spacing for label X-Offset */
    private static final int PADDING_LABEL = 8;

    // Various RGB colors for GUI components
    private static final Color COLOR_BG = new Color(180, 24, 24);
    private static final Color COLOR_TILE = new Color(24, 0, 0);
    private static final Color COLOR_WALL = new Color(255, 236, 160);
    private static final Color COLOR_PAWN = new Color(255, 236, 160);
    private static final Color COLOR_PATH[] = {
                                            new Color(0, 255, 0),
                                            new Color(255, 255, 0),
                                            new Color(0, 255, 255),
                                            new Color(255, 0, 255)
    };
    
    /** Font for drawing information labels */
    private static final Font FONT_LABELS = new Font("Serif", Font.PLAIN, 24);
    
    /** Font for drawing information text */
    private static final Font FONT_INFO = new Font("Serif", Font.PLAIN, 14);
    
    /** Board to get info from */
    private final Board board;
    
    /** Player Names */
    private final String[] playerNames;
    
    /** Background image */
    private BufferedImage bgImage;
    
    /** LCD font for headers */
    private static Font lcdFontHeaders;
    
    /** LCD font for headers */
    private static Font lcdFontLabels;
    
    /**
     * Default constructor
     * @param b the Board object to update from
     * @param plNames the names of the players playing
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
        
        // Set background image and font
        try {
          switch(board.getTotalPlayers()) {
            case 4:
              bgImage = ImageIO.read(new File("res/img/QuoridorBG-4p.png"));
            break;
            case 2:
              bgImage = ImageIO.read(new File("res/img/QuoridorBG-2p.png"));
            break;
            default:
              bgImage = null;
          }
          
          // Load font
          lcdFontHeaders = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/digital.ttf")).deriveFont(34f);
          lcdFontLabels = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/digital.ttf")).deriveFont(28f);
          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
          ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/digital.ttf")));
        } catch (IOException fnf) {
          System.err.println("Could not find BG image.");
        } catch (FontFormatException ffe) {
          System.err.println("Something blew up with the font.");
        }
        
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
        g.drawImage(bgImage, 0, 0, null);
        if(board.wasWinner())
          return;
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
        if(board != null) {
          paintGrid(g);
          paintPawns(g);
          paintWalls(g);
          paintInfo(g);
          
          if(!board.wasWinner())
            paintPaths(g);
          else
            System.err.println("Game is over");
          
        } else {
          System.err.println("Board is null.");
        }
    }
    
    /**
     * Draws the tiles
     * @param g the Graphics object
     */
    private void paintGrid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        // Draw Tile rows
        for(int row = 0; row < 9; row++) {
            
            g2.setColor(COLOR_PAWN);
            int lrx = MARGIN_BOARD_LEFT - (TILE_SIZE / 2);
            int lry = MARGIN_BOARD_TOP + (TILE_SIZE / 2) + row * (TILE_SIZE + WALL_SIZE);
            // g2.drawString("" + row, lrx, lry);
            
            // Columns
            for(int col = 0; col < 9; col++) {
                int orgX = MARGIN_BOARD_LEFT + col * (TILE_SIZE + WALL_SIZE);
                int orgY = MARGIN_BOARD_TOP + row * (TILE_SIZE + WALL_SIZE);
                
                int lcx = MARGIN_BOARD_LEFT + col * (TILE_SIZE + WALL_SIZE) + (TILE_SIZE / 2);
                int lcy = MARGIN_BOARD_TOP - (TILE_SIZE / 2) + 8;
                
                g2.setColor(COLOR_PAWN);
                // g2.drawString("" + col, lcx, lcy);
                g2.setColor(COLOR_TILE);
                g2.fillRect(orgX, orgY, TILE_SIZE, TILE_SIZE);
            }
        }
    }
    
    /**
     * Draws circles for the pawns' locations
     * @param g the Graphics object
     */
    private void paintPawns(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        // Enable antialiasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for(int i = 0; i < board.getTotalPlayers(); i++) {
            g2.setColor(COLOR_PATH[i]);

            // If the player has already been kicked, skip drawing them.
            if(board.isPlayerKicked(i))
                continue;
            
            // Unpack player positions
            Coord pPos = board.getPlayerPos(i);
            int px = pPos.getX();
            int py = pPos.getY();
            
            // Calculate drawing coords
            int gx = PADDING_PAWN + MARGIN_BOARD_LEFT + px * (TILE_SIZE + WALL_SIZE);
            int gy = PADDING_PAWN + MARGIN_BOARD_TOP + py * (TILE_SIZE + WALL_SIZE);
            
            g2.fillOval(gx, gy, TILE_SIZE - PADDING_PAWN * 2, TILE_SIZE - PADDING_PAWN * 2);
            
            // Player numbers
            g2.setColor(Color.BLACK);
            g2.drawString("" + (i+1), gx + MARGIN_PNUM_X, gy + MARGIN_PNUM_Y);
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
        g2.setColor(COLOR_PATH[2]);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(lcdFontHeaders);
        
        for(int i = 0; i < playerNames.length; i++) {
            
            g2.setFont(lcdFontHeaders);
            // Draw labels
            int labelx = (i % 2 == 0) ? (BOARD_SIZE * (i % 2)) : ((BOARD_SIZE + MARGIN_BOARD_LEFT) * (i % 2)) - 8;
            labelx += MARGIN_TEXT_LEFT;
            int labely = (i < 2) ? MARGIN_TEXT_TOP : MARGIN_TEXT_TOP << 2;
            
            // If the player ID is kicked
            if(board.isPlayerKicked(i)) {
                g2.drawString("KICKED", labelx, labely);
            
            // Still playing
            } else {
                
                
                g2.drawString("Player " + (i+1), labelx, labely);
                
                // Draw player names
                g2.setFont(lcdFontLabels);
                int textx = labelx + MARGIN_TEXT_LEFT + 4;
                int texty = labely + MARGIN_TEXT_LINE;
                
                // Draw walls left
                g2.drawString("Walls: " + board.wallsRemaining(i), textx, texty);
            
            }
        }
    }
    
    /**
     * Displays pathing info for players
     * @param g the Graphics object
     */
    private void paintPaths(Graphics g) {
      
      if(board.wasWinner())
        return;
      
      ArrayList<Coord> path = null;
      
      for(int pid = 0; pid < board.getTotalPlayers(); pid++) {
        
        g.setColor(COLOR_PATH[pid]);
        if(board.isPlayerKicked(pid))
          continue;
        
        try {
          path = board.copyOf().getShortestPath(pid);
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
        
        
        // Don't paint if player was kicked
        if(path == null)
          return;
        
        // Only draw if the target is possible to move to
        if(path != null) {
          for(int i = 0; i < path.size(); i++) {
            Coord curr = path.get(i);
            int gx = PADDING_PATH + MARGIN_BOARD_LEFT + curr.getX() * (TILE_SIZE + WALL_SIZE);
            int gy = PADDING_PATH + MARGIN_BOARD_TOP + curr.getY() * (TILE_SIZE + WALL_SIZE);
            switch(pid) {
              case 1:
                gx += PATH_OFFSET;
                gy -= PATH_OFFSET;
              break;
              case 2:
                gx -= PATH_OFFSET;
                gy += PATH_OFFSET;
              break;
              case 3:
                gx += PATH_OFFSET;
                gy += PATH_OFFSET;
              break;
              default:
                gx -= PATH_OFFSET;
                gy -= PATH_OFFSET;
            }
            g.fillOval(gx, gy, TILE_SIZE - PADDING_PATH * 2, TILE_SIZE - PADDING_PATH * 2);
          }
          
        }
      }
      

    }
    
}
