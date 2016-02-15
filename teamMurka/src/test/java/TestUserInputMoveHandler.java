import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/* This tester is for testing each component of the user inputed moves.
 */
public class TestUserInputMoveHandler{
    String machine = "localhost";
    int port = 1478;
    UserInputMoveHandler user = new UserInputMoveHandler();
    assertNotNull("Empty Hand constructed", user);
}