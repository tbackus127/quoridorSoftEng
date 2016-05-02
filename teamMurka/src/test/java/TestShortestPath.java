import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;
import static org.junit.Assert.*;

import com.tmquoridor.Board.*;

public class TestShortestPath {
  
    // Complex test for shortest path algorithm (Lee)
    @Test
    public void testBoardsShortest() {
        Board b = new Board();
    
        Wall[] testWalls = {
          new Wall(new Coord(4, 2), Orientation.HORIZ),
          new Wall(new Coord(6, 2), Orientation.HORIZ),
          new Wall(new Coord(8, 2), Orientation.VERT),
          new Wall(new Coord(4, 3), Orientation.HORIZ),
          new Wall(new Coord(4, 3), Orientation.VERT),
          new Wall(new Coord(2, 5), Orientation.HORIZ),
          new Wall(new Coord(8, 6), Orientation.VERT),
          new Wall(new Coord(6, 3), Orientation.VERT),
          new Wall(new Coord(6, 5), Orientation.VERT),
          new Wall(new Coord(6, 7), Orientation.HORIZ),
          new Wall(new Coord(7, 8), Orientation.HORIZ),
          new Wall(new Coord(2, 6), Orientation.VERT),
          new Wall(new Coord(3, 7), Orientation.HORIZ),
          new Wall(new Coord(5, 7), Orientation.VERT),
          new Wall(new Coord(2, 7), Orientation.HORIZ),
        };
         
        // Add walls
        for(int i = 0; i < testWalls.length; i++) {
          b.placeWall(testWalls[i]);
        }
        
        // Get the shortest path to (7, 8) (will ALWAYS be 17 in length).
        ArrayList<Coord> path = b.getShortestPath(0, new Coord(7, 8));
        
        // Debug info
        System.err.println("Final path: ");
        for(Coord c : path) {
            System.err.print(c + " ");
        } 
        
        System.err.println();
        assertNotNull(b);
        assertNotNull(path);
        assertEquals("Path size is not the shortest it can be!" ,path.size(), 17);
    }
    
    // Tests blocking walls for given path
    @Test
    public void testBlockingWalls() {
      Board b = new Board(4);
      
      // Setup player positions
      Coord[] plPos = {
        new Coord(3,2),
        new Coord(5,5),
        new Coord(4,2),
        new Coord(5,6)
      };
      
      for(int i = 0; i < plPos.length; i++)
        b.movePlayer(i, plPos[i]);
        
      // Setup walls
      Wall[] walls = {
        new Wall(new Coord(4,2), Orientation.HORIZ),
        new Wall(new Coord(0,3), Orientation.HORIZ),
        new Wall(new Coord(2,3), Orientation.HORIZ),
        new Wall(new Coord(4,3), Orientation.VERT),
        new Wall(new Coord(5,3), Orientation.HORIZ),
        new Wall(new Coord(7,3), Orientation.HORIZ),
        new Wall(new Coord(3,4), Orientation.VERT),
        new Wall(new Coord(0,6), Orientation.HORIZ),
        new Wall(new Coord(3,6), Orientation.HORIZ),
        new Wall(new Coord(7,6), Orientation.HORIZ),
        new Wall(new Coord(2,8), Orientation.HORIZ),
        new Wall(new Coord(4,8), Orientation.HORIZ),
        new Wall(new Coord(6,8), Orientation.HORIZ)
      };
      
      for(int i = 0; i < walls.length; i++) {
        b.placeWall(walls[i]);
      }
      
      // Blocking walls
      Wall[] expectedWalls = {
        new Wall(new Coord(5,3), Orientation.VERT),
        new Wall(new Coord(5,4), Orientation.VERT),
        new Wall(new Coord(4,5), Orientation.HORIZ),
        new Wall(new Coord(5,5), Orientation.HORIZ),
        new Wall(new Coord(5,6), Orientation.HORIZ),
        new Wall(new Coord(5,7), Orientation.HORIZ),
        new Wall(new Coord(6,6), Orientation.VERT),
        new Wall(new Coord(4,7), Orientation.HORIZ),
        new Wall(new Coord(7,6), Orientation.VERT),
        new Wall(new Coord(8,6), Orientation.VERT),
        new Wall(new Coord(8,7), Orientation.VERT)
      };
      
      ArrayList<Coord> path = b.getShortestPath(0);
      HashSet<Wall> result = b.getBlockingWalls(0, path);
      
      assertNotNull(path);
      assertNotNull(result);
    }
}