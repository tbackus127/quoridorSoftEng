import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

import com.tmquoridor.Board.*;

public class TestShortestPath {
  
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
        
        for(int i = 0; i < testWalls.length; i++) {
          b.placeWall(testWalls[i]);
        }
        
        ArrayList<Coord> path = b.getShortestPath(0, new Coord(7, 8));
        
        System.err.println("Final path: ");
        for(Coord c : path) {
            System.err.print(c + " ");
        } 
        
        System.err.println();
        assertNotNull(b);
    }
}