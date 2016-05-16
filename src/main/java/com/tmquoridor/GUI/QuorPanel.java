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
    private static final int MARGIN_TEXT_LEFT = 6;
    
    /** How far the player info is indented from the top */
    private static final int MARGIN_TEXT_TOP = 138;
    
    /** How far down the next line of info is */
    private static final int MARGIN_TEXT_LINE = 28;
    
    /** Pixels to shrink the pawn circle */
    private static final int PADDING_PAWN = 2;
    
    /** Pixels to shrink the pathing circle */
    private static final int PADDING_PATH = 15;
    
    /** Pixels to offset player number pawn label (x) */
    private static final int MARGIN_PNUM_X = 10;
    
    /** Pixels to offset player number pawn label (y) */
    private static final int MARGIN_PNUM_Y = 18;
    
    /** So path lines don't overlap, extra pixels to push points to the side */
    private static final int PATH_OFFSET = 2;
    
    /** Spacing for label X-Offset */
    private static final int PADDING_LABEL = 8;
    
    /** Spacing for results lines */
    private static final int MARGIN_RESLINE = 32;

    /** Background color */
    private static final Color COLOR_BG = Color.BLACK;
    
    /** Tile color */
    private static final Color COLOR_TILE[] = {
                                            new Color(128, 0, 0),
                                            new Color(128, 128, 128),
                                            new Color(0, 0, 128)
    };
    
    /** Walls' color */
    private static final Color COLOR_WALL = new Color(255, 255, 0);
    
    /** The pawn's color */
    private static final Color COLOR_PAWN = COLOR_WALL;
    
    /** Path colors */
    private static final Color COLOR_PATH[] = {
                                            new Color(0, 255, 0),
                                            new Color(255, 255, 0),
                                            new Color(0, 255, 255),
                                            new Color(255, 0, 255)
    };
    
    /** Results screen text color */
    private static final Color COLOR_SCRIPT = new Color(32, 10, 10);
    
    /** Results screen winner color */
    private static final Color COLOR_WINNER = new Color(160, 32, 32);
    
    /** Font for player status headers */
    private static Font fontHeaders;
    
    /** Font for player status labels */
    private static Font fontLabels;
    
    /** Font for results screen headers */
    private static Font fontResHeaders;
    
    /** Font for pawn name labels */
    private static Font fontPawnLabels;
    
    /** Font for results screen text */
    private static Font fontResText;
    
    /** Board to get info from */
    private final Board board;
    
    /** Player Names */
    private final String[] playerNames;
    
    /** In-game background */
    private BufferedImage bgImage;
    
    /** Results screen background */
    private BufferedImage winImage;
    
    /** The results of the match, after it is over, with the winner being in index 0. */
    private String[] matchResults;
    
    /** Resource directory */
    private String resDir = "res/";
    
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
        
        // Check if we're executing this from a level back
        if(!(new File(resDir + "img/murkaBG.png")).exists()) {
          resDir = "teamMurka/res/";
        }
        
        // Set background image and fonts
        try {
          
          File fileBGMain = new File(resDir + "img/murkaBG.png");
          File fileBGRes = new File(resDir + "img/declBG.jpg");
          File fileFontMain = new File(resDir + "fonts/freedom.ttf");
          File fileFontRes = new File(resDir + "fonts/declScript.ttf");
          File fileFontPLabels = new File(resDir + "fonts/plabel.ttf");
          
          // Load background images
          bgImage = ImageIO.read(fileBGMain);
          winImage = ImageIO.read(fileBGRes);
          
          // Load and set up fonts
          fontHeaders = Font.createFont(Font.TRUETYPE_FONT, fileFontMain).deriveFont(38f);
          fontLabels = Font.createFont(Font.TRUETYPE_FONT, fileFontMain).deriveFont(32f);
          fontResHeaders = Font.createFont(Font.TRUETYPE_FONT, fileFontRes).deriveFont(38f);
          fontResText = Font.createFont(Font.TRUETYPE_FONT, fileFontRes).deriveFont(26f);
          fontPawnLabels = Font.createFont(Font.TRUETYPE_FONT, fileFontPLabels).deriveFont(10f);
          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
          ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontMain));
          ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontRes));
          ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fileFontPLabels));
        } catch (IOException fnf) {
          System.err.println("  !! Could not find one or more background images!");
        } catch (FontFormatException ffe) {
          System.err.println("  !! Something blew up with the font!");
        }
        
        // Print player names (debug)
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
        if(matchResults == null) {
          g.drawImage(bgImage, 0, 0, null);
          if(board.wasWinner())
            return;
        } else {
          g.drawImage(winImage, 0, 0, null);
        }
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
          
          // Match still going on
          if(matchResults == null) {
            paintGrid(g);
            paintPawns(g);
            paintWalls(g);
            paintInfo(g);
            
            if(!board.wasWinner())
              paintPaths(g);
            else
              System.err.println("Game is over");
          
          // Match over
          } else {
            paintResults(g);
          }
        } else {
          System.err.println("Board is null.");
        }
    }
    
    /**
     * Sets the match results. Causes the GUI to display the results screen
     * @param names server names, with the winner being in index 0.
     */
    public void setResult(String[] names) {
      matchResults = names;
    }
    
    /**
     * Draws the results screen
     * @param g the Graphics object
     */
    private void paintResults(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      
      // Enable antialiasing
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      // Draw header of independenceDecls
      g2.setFont(fontResHeaders);
      g2.setColor(COLOR_SCRIPT);
      g2.rotate(Math.toRadians(-4));
      String resHeaderString = "IN CONGRESS, May 17, 2016.";
      g2.drawString(resHeaderString, (640 - g2.getFontMetrics(fontResHeaders).stringWidth(resHeaderString)) / 2, MARGIN_RESLINE * 2);
      
      // Draw line 1 of colonial babble
      g2.setFont(fontResText);
      String resLine1 = "The unanimous declaration of victory between";
      g2.drawString(resLine1, (640 - g2.getFontMetrics(fontResText).stringWidth(resLine1)) / 2, MARGIN_RESLINE * 4);
      
      // Get list of players, calculate width and draw
      String resPlayerList = matchResults[0];
      for(int i = 1; i < matchResults.length; i++) {
        if(matchResults.length > 2)
          resPlayerList += ",";
        if(i == matchResults.length - 1)
          resPlayerList += " and";
        resPlayerList += " " + matchResults[i];
      }
      g2.drawString(resPlayerList, (640 - g2.getFontMetrics(fontResText).stringWidth(resPlayerList)) / 2, MARGIN_RESLINE * 6);
      
      // Draw line 2 of colonial babble
      String resLine2 = "Henceforth, on this day it shall be knownst that";
      g2.drawString(resLine2, (640 - g2.getFontMetrics(fontResText).stringWidth(resLine2)) / 2, MARGIN_RESLINE * 8);
      
      // Draw winner's name
      String winName = matchResults[0];
      g2.setFont(fontResHeaders);
      g2.setColor(COLOR_WINNER);
      g2.drawString(winName, (640 - g2.getFontMetrics(fontResText).stringWidth(winName)) / 2, MARGIN_RESLINE * 10);
      
      // Draw last line of colonial babble
      g2.setFont(fontResText);
      g2.setColor(COLOR_SCRIPT);
      String resLine3 = "has triumphed against all odds and secured victory.";
      g2.drawString(resLine3, (640 - g2.getFontMetrics(fontResText).stringWidth(resLine3)) / 2, MARGIN_RESLINE * 12);
      
      String resLine4 = "You have our deepest thanks in making America great again!";
      g2.drawString(resLine4, (640 - g2.getFontMetrics(fontResText).stringWidth(resLine4)) / 2, MARGIN_RESLINE * 14);
      
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
                g2.setColor(COLOR_TILE[row % 3]);
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
            
            // Get player names and calculate where to place them
            g2.setFont(fontPawnLabels);
            g2.setColor(Color.BLACK);
            String pcode = playerNames[i].split(":")[0];
            int pLabelPadding = ((TILE_SIZE - PADDING_PAWN * 2) - g2.getFontMetrics(fontPawnLabels).stringWidth(pcode)) / 2;
            g2.drawString(pcode, gx + pLabelPadding, gy + MARGIN_PNUM_Y);
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
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(fontHeaders);
        
        for(int i = 0; i < playerNames.length; i++) {
            
            g2.setFont(fontHeaders);
            // Draw labels
            int labelx = (i % 2 == 0) ? (BOARD_SIZE * (i % 2)) : ((BOARD_SIZE + MARGIN_BOARD_LEFT) * (i % 2)) - 8;
            labelx += MARGIN_TEXT_LEFT;
            int labely = (i < 2) ? MARGIN_TEXT_TOP : (MARGIN_TEXT_TOP << 1) + 26;
            
            // If the player ID is kicked
            if(board.isPlayerKicked(i)) {
                g2.drawString("Deported!", labelx, labely);
            
            // Still playing
            } else {
                
                
                g2.drawString("Citizen " + (i+1), labelx, labely);
                
                // Draw player names
                g2.setFont(fontLabels);
                int textx = labelx + MARGIN_TEXT_LEFT + 2;
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
