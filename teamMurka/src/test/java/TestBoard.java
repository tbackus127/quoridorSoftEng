import org.junit.Test;

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
        
        b.placeWall(w1);
        b.placeWall(w2);
        b.placeWall(w3);
        
        int wallSize = b.getWalls().size();
        assertEquals("Three walls were not placed.", wallSize, 3);
        
        testPos = new Coord(3, 3);
        
        boolean blockedNorth = isBlocked(testPos, Direction.NORTH);
        boolean blockedEast = isBlocked(testPos, Direction.EAST);
        boolean blockedSouth = isBlocked(testPos, Direction.SOUTH);
        boolean blockedWest = isBlocked(testPos, Direction.WEST);
        
        System.out.println("TEST");
        assertTrue("Blocked move @ NORTH allowed!", blockedNorth);
        assertTrue("Blocked move @ EAST allowed!", blockedEast);
        assertTrue("Blocked move @ NORTH allowed!", blockedSouth);
        assertTrue("Can't move WEST!", !blockedWest);
        
        
        
        
    }
}