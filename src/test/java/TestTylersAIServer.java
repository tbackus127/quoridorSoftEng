import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Test;

import com.tmquoridor.Board.Board;
import com.tmquoridor.Board.Coord;
import com.tmquoridor.Server.TylersAIServer;

public class TestTylersAIServer {
  
  @Test
  public void testConstructor() throws Exception {
    // String serverName = "America";
    int port = 1478;
    TylersAIServer user = new TylersAIServer(port, "mur:America", 250);
    
    assertNotNull("Nothing was constructed", user);
  }
  
  @Test
  public void testUpdateBoard() {
    String fakedMove = "ATARI 1 (4, 1)";
    
    String x = "HELLO\r\nGAME 2 abc:One mur:America\r\n";
    Scanner incomingReader = new Scanner(x);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream outGoingFromClient = new PrintStream(baos);
    
    TylersAIServer ms = new TylersAIServer(1478, "mur:America", 250);
    
    ms.establishProtocol(incomingReader, outGoingFromClient);
    ms.updateBoard(fakedMove);
    
    Board expectedB = new Board();
    Coord move = new Coord(4, 1);
    expectedB.movePlayer(0, move);
    
    assertEquals("It did not move the player Properly", expectedB.toString(), ms.getBoard().toString());
  }
}