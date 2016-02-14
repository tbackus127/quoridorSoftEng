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
        int wsize = 6;
        assertEquals("Three (actual: 6) walls were not placed.", wallSize, wsize);
        
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
}