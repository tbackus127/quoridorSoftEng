import java.util.HashSet;

import org.junit.Test;
import static org.junit.Assert.*;

import com.tmquoridor.Board.*;

public class TestBoard {
    
    @Test
    public void testBoard() {
        int playerCount = 2;
        Board b = new Board(playerCount);
        
        assertNotNull("Board is null", b);
        
        // Set up box with Direction.WEST open
        Wall w1 = new Wall(new Coord(2, 2), Orientation.HORIZ);
        Wall w2 = new Wall(new Coord(2, 3), Orientation.HORIZ);
        Wall w3 = new Wall(new Coord(3, 2), Orientation.VERT);
        
        assertNotNull("One or more walls are null.", w1);
        assertNotNull("One or more walls are null.", w2);
        assertNotNull("One or more walls are null.", w3);
        
        b.placeWall(w1);
        b.placeWall(w2);
        b.placeWall(w3);
        
        int wallSize = b.getWalls().size();
        int wsize = 3;
        assertEquals("Three walls were not placed.", wallSize, wsize);
        
        System.err.println();
        
        // DEBUG
        for(Wall w : b.getWalls()) {
            System.err.print(w.toString());
        }
        
        System.err.println();
        
        Coord testPos = new Coord(2, 2);
        
        boolean blockedNorth = b.isBlocked(testPos, Direction.NORTH);
        boolean blockedEast = b.isBlocked(testPos, Direction.EAST);
        boolean blockedSouth = b.isBlocked(testPos, Direction.SOUTH);
        boolean blockedWest = b.isBlocked(testPos, Direction.WEST);
        
        assertTrue("Blocked move @ NORTH allowed!", blockedNorth);
        assertTrue("Blocked move @ EAST allowed!", blockedEast);
        assertTrue("Blocked move @ SOUTH allowed!", blockedSouth);
        assertFalse(blockedWest);

    }
    
    @Test
    public void testWalls() throws Exception {
        Board b = new Board();
        assertNotNull("Board is null!", b);
        
        // Set up legal walls
        Wall w1 = new Wall(new Coord(4, 2), Orientation.HORIZ);
        Wall w2 = new Wall(new Coord(5, 2), Orientation.VERT);
        Wall w3 = new Wall(new Coord(5, 3), Orientation.HORIZ);
        Wall w4 = new Wall(new Coord(6, 4), Orientation.VERT);
        Wall w5 = new Wall(new Coord(4, 2), Orientation.VERT);
        
        assertTrue("Wall 1 is not legal and should be!", b.isLegalWall(w1));
        b.placeWall(w1);
        assertTrue("Wall 2 is not legal and should be!", b.isLegalWall(w2));
        b.placeWall(w2);
        assertTrue("Wall 3 is not legal and should be!", b.isLegalWall(w3));
        b.placeWall(w3);
        assertTrue("Wall 4 is not legal and should be!", b.isLegalWall(w4));
        b.placeWall(w4);
        assertTrue("Wall 5 is not legal and should be!", b.isLegalWall(w5));
        b.placeWall(w5);
        
        // Illegal wall checking
        Wall w6 = new Wall(new Coord(5, 5), Orientation.HORIZ);
        assertFalse("Wall 6 is legal and should not be! (Problem with crossing)", b.isLegalWall(w6));
        Wall w7 = new Wall(new Coord(4, 2), Orientation.HORIZ);
        assertFalse("Wall 6 is legal and should not be! (Problem with first segment overlap)", b.isLegalWall(w7));
        Wall w8 = new Wall(new Coord(6, 3), Orientation.VERT);
        assertFalse("Wall 6 is legal and should not be! (Problem with extension segment overlap)", b.isLegalWall(w8));
    }
    
    @Test
    public void testPlayerSetup() {
        Board b1 = new Board(4);
        Coord b1p1pos = b1.getPlayerPos(0);
        assertNotNull("Player 1 position is null!", b1p1pos);
        Coord b1p2pos = b1.getPlayerPos(1);
        assertNotNull("Player 2 position is null!", b1p2pos);
        Coord b1p3pos = b1.getPlayerPos(2);
        assertNotNull("Player 3 position is null!", b1p3pos);
        Coord b1p4pos = b1.getPlayerPos(3);
        assertNotNull("Player 4 position is null!", b1p4pos);
        
        Coord b1p1comp = new Coord(4, 0);
        Coord b1p2comp = new Coord(4, 8);
        Coord b1p3comp = new Coord(0, 4);
        Coord b1p4comp = new Coord(8, 4);
        
        assertTrue("Player 1 position was not set at 4, 0!", b1p1pos.equals(b1p1comp));
        assertTrue("Player 2 position was not set at 4, 8!", b1p2pos.equals(b1p2comp));
        assertTrue("Player 3 position was not set at 0, 4!", b1p3pos.equals(b1p3comp));
        assertTrue("Player 4 position was not set at 8, 4!", b1p4pos.equals(b1p4comp));
        
    }
    
    @Test
    public void testPlayerMovement() {
        Board b = new Board();
        
        assertTrue("North should be blocked!", b.isBlocked(b.getPlayerPos(0), Direction.NORTH));
        // HashSet<Coord> legalMoves = b.getLegalMoves(b.getPlayerPos(0));
        // System.err.println("\n\n" + legalMoves.size() + "\n\n");
        // assertEquals("There are not 3 legal moves for player 1!", legalMoves.size(), 3);
    }
}