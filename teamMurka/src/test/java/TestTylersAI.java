import org.junit.Test;
import java.util.ArrayList;
import java.io.IOException;
import java.net.Socket;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;
import java.io.PrintStream;

import com.tmquoridor.Server.*;
import com.tmquoridor.Board.*;
import com.tmquoridor.AI.*;

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

public class TestTylersAI{
    
    @Test
    public void testConstructor(){
	int port = 1478;
        TylersAIServer user = new TylersAIServer(port, "mur:America");
        assertNotNull("Nothing was constructed", user);
    }
}