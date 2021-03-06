
package com.tmquoridor.Servers;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import com.tmquoridor.Board.Coord;

public class RandomMoveServer extends ManualInputServer {
  
  // Main that uses the command line arguments
  public static void main(String[] args) {
    
    // This sets the defaults
    int port = DEFAULT_PORT_NUMBER;
    String name = DEFAULT_NAME;
    int delay = DEFAULT_DELAY;
    
    @SuppressWarnings("unused")
    boolean intnlWalls = false;
    
    int argNdx = 0;
    
    System.err.println("argl=" + args.length);
    // This runs through all of the command line arguments and applies the proper ones
    while (argNdx < args.length) {
      String curr = args[argNdx];
      
      if (curr.equals(ARG_PORT)) {
        ++argNdx;
        
        String numberStr = args[argNdx];
        port = Integer.parseInt(numberStr);
      } else if (curr.equals(ARG_NAME)) {
        ++argNdx;
        name = DEFAULT_PREFIX + args[argNdx];
        
      } else if (curr.equals(ARG_DELAY)) {
        ++argNdx;
        delay = Integer.parseInt(args[argNdx]);
        
      } else if (curr.equals(ARG_INTNL_WALL)) {
        intnlWalls = true;
        
      } else {
        
        // if there is an unknown parameter, give usage and quit
        System.err.println("Unknown parameter \"" + curr + "\"");
        usage();
        System.exit(1);
      }
      
      ++argNdx;
    }
    
    RandomMoveServer ms = new RandomMoveServer(port, name, delay);
    ms.run();
  }
  
  // Lets them know if they put in an invalid argument
  private static void usage() {
    System.err.print("usage: java BirthdayServer [options]\n" + "       where options:\n" + "       --port port\n");
  }
  
  // Constructor
  public RandomMoveServer(int port, String name, int delay) {
    super(port, name, delay, false);
  }
  
  @Override
  public void sendMove(PrintStream cout) throws Exception {
    HashSet<Coord> legalMoves = board.getLegalMoves(thisServersPlayerNumber - 1);
    String unwrappedMessage = "m ";
    for (Coord move : legalMoves) {
      unwrappedMessage += move.getX() + " " + move.getY();
      TimeUnit.MILLISECONDS.sleep(delay);
      cout.print(moveWrapper(unwrappedMessage));
      return;
    }
  }
}