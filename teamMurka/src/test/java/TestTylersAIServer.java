import org.junit.Test;
import java.util.ArrayList;
import java.io.IOException;
import java.net.Socket;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;
import java.io.PrintStream;

import com.tmquoridor.Server.*;
import com.tmquoridor.Board.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith; 

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.testng.PowerMockTestCase;
import org.powermock.reflect.Whitebox;

public class TestTylersAIServer{
    
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
	String expected = "IAM mur:America\r\n";
	Scanner incomingReader = new Scanner(x);
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream outGoingFromClient = new PrintStream(baos);
	
	TylersAIServer ms = new TylersAIServer(1478,"mur:America", 250);

	ms.establishProtocol(incomingReader, outGoingFromClient);
	ms.updateBoard(fakedMove);
	
	Board expectedB = new Board();
	Coord move = new Coord(4, 1);
	expectedB.movePlayer(0, move);
	
	assertEquals("It did not move the player Properly", expectedB.toString(), ms.getBoard().toString());
	
	
    }
}