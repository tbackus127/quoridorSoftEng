import org.junit.Test;

public class TestBoard {
    
    @Test
    public void testBoardConstructor() {
        int playerCount = 2;
        Board b = new Board(playerCount);
        
        assertNotNull("Board is null", b);
        
    }
}