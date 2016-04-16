import java.util.ArrayList;

import com.tmquoridor.Board.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

public class TestShortestPath {
  
  @Test
  public void testBoardsShortest() {
    Board b = new Board();
    ArrayList<Coord> path = b.getShortestPath(0, new Coord(4, 4));
    for(Coord c : path) {
      System.out.print(c + " ");
    }
  }
}