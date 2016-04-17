import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

import com.tmquoridor.Board.*;

public class TestShortestPath {
  
    @Test
    public void testBoardsShortest() {
        Board b = new Board();
        ArrayList<Coord> path = b.getShortestPath(0, new Coord(4, 4));
        for(Coord c : path) {
            System.err.print(c + " ");
        } 
        
        System.err.println();
        assertNotNull(b);
    }
}