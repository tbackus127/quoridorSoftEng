import org.junit.Test;
import static org.junit.Assert.*;

import com.tmquoridor.Client.*;
import com.tmquoridor.Server.*;

public class TestCommunication {
  
  @Test
  public void testClient() {
      GameClient gc = new GameClient(6478, "localhost");
      assertNotNull("Game client is null!", gc);
  }
  
  @Test
  public void testServer() {
      UserInputMoveHandler mh = new UserInputMoveHandler(6478, "localhost");
      assertNotNull("Move server is null!", mh);
  }
  
  // @Test
  // public void testCommunication() {
    
  // }
}