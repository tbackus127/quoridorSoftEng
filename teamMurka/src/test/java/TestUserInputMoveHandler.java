import org.junit.Test;
import java.util.ArrayList;

import com.tmquoridor.Server.*;

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

import static org.mockito.Mockito.times;


/* This tester is for testing each component of the user inputed moves.
 */
public class TestUserInputMoveHandler{
    
    @Test
    public void testConstructor() {        
        String serverName = "America";
        int port = 1478;
	Socket socket = Mockito.mock(Socket.class);
        UserInputMoveHandler user = new UserInputMoveHandler(port, "Murka", socket);
        assertNotNull("Nothing was constructed", user);
    }
    
//     @Test
//     public
}