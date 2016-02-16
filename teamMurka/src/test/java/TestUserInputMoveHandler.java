import org.junit.Test;
import java.util.ArrayList;

import com.tmquoridor.Server.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/* This tester is for testing each component of the user inputed moves.
 */
public class TestUserInputMoveHandler{
    
    @Test
    public void testNotNull() {        
        String machine = "localhost";
        int port = 1478;
        UserInputMoveHandler user = new UserInputMoveHandler(port);
        assertNotNull("Empty Hand constructed", user);
    }
}