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

public class TestManualServer{
    
    @Test
    public void testConstructor() throws Exception {        
        // String serverName = "America";
        int port = 1478;
        ManualInputServer user = new ManualInputServer(port, "mur:America");
        assertNotNull("Nothing was constructed", user);
    }
    
    @Test
    public void testEstablishProtocol(){
	
	// Sets up the variables
	String x = "HELLO\r\nGAME 2 abc:One mur:America\r\n";
	String expected = "IAM mur:America\r\n";
	Scanner incomingReader = new Scanner(x);
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream outGoingFromClient = new PrintStream(baos);
	
	ManualInputServer ms = new ManualInputServer(1478,"mur:America");
	ms.establishProtocol(incomingReader, outGoingFromClient);
	
	String messagesFromClient = baos.toString();
	
	assertEquals("The out going message from the client is not correct", 
							expected, messagesFromClient);
    }
    
    @Test
    public void testMoveWrapper(){
	String move = "m 5 6";
	String wallv = "v 5 6";
	String wallh = "h 5 6";
	String expectedMove = "TESUJI (5, 6)\r\n";
	String expectedWallV = "TESUJI [(5, 6), v]\r\n";
	String expectedWallH = "TESUJI [(5, 6), h]\r\n";
	
	ManualInputServer ms = new ManualInputServer(1478,"mur:America");
	
	assertEquals("The move wrapper did not wrap the move Properly",expectedMove,ms.moveWrapper(move));
	assertEquals("The move wrapper did not wrap the wall placement Properly",
		     expectedWallV ,ms.moveWrapper(wallv));
	assertEquals("The move wrapper did not wrap the wall placement Properly",
		     expectedWallH ,ms.moveWrapper(wallh));
    }
    
    @Test
    public void testSendMove() {
	// Tests pawn movement
	String input = "m 5 6";
	Scanner fakeConsole = new Scanner(input);
	
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream outGoingFromClient = new PrintStream(baos);
	
	String expected = "TESUJI (5, 6)\r\n";
	
	ManualInputServer ms = new ManualInputServer(1478,"mur:America");
	ms.sendMove(outGoingFromClient, fakeConsole);
	
	assertEquals("The move did not format correctly", expected, baos.toString());
	
	// Tests wall placement
	input = "w h 5 6";
	fakeConsole = new Scanner(input);
	
	baos = new ByteArrayOutputStream();
	outGoingFromClient = new PrintStream(baos);
	
	expected = "TESUJI [(5, 6), h]\r\n";
	
	ms.sendMove(outGoingFromClient, fakeConsole);
	
	assertEquals("The wall did not format correctly", expected, baos.toString());
    }
    
    @Test
    public void testUpdateBoard() {
	String fakedMove = "ATARI 1 (4, 1)";
	
	String x = "HELLO\r\nGAME 2 abc:One mur:America\r\n";
	String expected = "IAM mur:America\r\n";
	Scanner incomingReader = new Scanner(x);
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream outGoingFromClient = new PrintStream(baos);
	
	ManualInputServer ms = new ManualInputServer(1478, "mur:America");
	ms.establishProtocol(incomingReader, outGoingFromClient);
	ms.updateBoard(fakedMove);
	
	Board expectedB = new Board();
	Coord move = new Coord(4, 1);
	expectedB.movePlayer(0, move);
	
	assertEquals("It did not move the player Properly", expectedB.toString(), ms.getBoard().toString());
    }
    
    @Test
    public void testRemovePlayer(){
	String fakedMove = "GOTE 1";
	
	Board expectedB = new Board();
	expectedB.removePlayer(0);
	
	String x = "HELLO\r\nGAME 2 abc:One mur:America\r\n";
	String expected = "IAM mur:America\r\n";
	Scanner incomingReader = new Scanner(x);
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream outGoingFromClient = new PrintStream(baos);
	
	ManualInputServer ms = new ManualInputServer(1478, "mur:America");
	ms.establishProtocol(incomingReader, outGoingFromClient);
	
	ms.removePlayer(fakedMove);
	
	assertEquals("It did not move the player Properly", expectedB.toString(), ms.getBoard().toString());
    }
    
    @Test
    public void testWinner() {
	String fakedMove = "KIKASHI 2";
	String expected = "Congratulations you have won the game!";
	
	String x = "HELLO\r\nGAME 2 abc:One mur:America\r\n";
	Scanner incomingReader = new Scanner(x);
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream outGoingToConsole = new PrintStream(baos);
	
	ManualInputServer ms = new ManualInputServer(1478, "mur:America");
	ms.establishProtocol(incomingReader, outGoingToConsole);
	
	baos = new ByteArrayOutputStream();
	outGoingToConsole = new PrintStream(baos);
	
	ms.winnerDeclared(outGoingToConsole, fakedMove);
	
	assertEquals("The winner was not declared properly", expected, baos.toString());
    }
}