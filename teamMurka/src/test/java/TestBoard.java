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
}