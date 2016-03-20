package com.tmquoridor.Client;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.net.*;

import com.tmquoridor.Board.*;

public class GameClient {
    
    /** Timeout (in seconds) for any socket */
    private static final int DEFAULT_SOCKET_TIMEOUT = 5;
    
    /** Handshake response regex */
    private static final String HANDSHAKE_RESP_REGEX = "IAM\\s+\\w+\\s*";
    
    /** Move validation regex */
    private static final String MOVE_VAL_REGEX = "TESUJI\\s+Move\\s+\\(\\s*[0-8],\\s*[0-8]\\s*\\)\\s*";
    
    /** Wall validation regex */
    private static final String WALL_VAL_REGEX = "TESUJI\\s+Wall\\s+\\[\\s*\\(\\s*[0-8]\\s*,\\s*[0-8]\\s*\\)\\s*,\\s*[hv]\\s*\\]\\s*";
    // This is only slightly obnoxious...
    
    /** The internal Board instance */
    private Board board;
    
    /** Socket array */
    private Socket[] socks;
    
    /** Server name array */
    private String[] srvNames;
    
    /**
     * Default constructor
     * @param args runtime arguments passed in
     */
    public GameClient(String[] args) {
        
        // Set sockets.
        try {
            socks = buildSocks(args);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        
        // If handshaking is successfull
        if(handshake()) {
            sendStartMsgs();
            setupBoard();
            doGameLoop();
        }
        
    }
    
    /**
     * Sets up the internal Board instance
     */
    private void setupBoard() {
        board = new Board(socks.length);
    }
    
    /**
     * Performs the game loop
     */
    private void doGameLoop() {
        for(int i = 0; i < socks.length; i++) {
            PrintStream cout = null;
            Scanner cin = null;

            try {
                
                // Send GO (see what I did there?) message and await response.
                cout = new PrintStream(socks[i].getOutputStream());
                cin = new Scanner(socks[i].getInputStream());
                
                cout.println("MYOUSHU");
                String srvMove = cin.nextLine();
                
                // If the server's move syntax is correct
                if(Pattern.matches(MOVE_VAL_REGEX, srvMove)) {
                    
                    // Check move legality
                    Coord mCoord = extractCoord(srvMove);
                    if(!board.isLegalMove(i, mCoord)) {
                        System.err.println("Player " + i + " made illegal move!:\n    Illegal destination: " + mCoord.getX() + "," + mCoord.getY());
                        madeIllegalMove(i);
                        continue;
                    }
                    board.movePlayer(i, mCoord);
                    broadcastAll("ATARI" + (i + 1) + " " + srvMove);
                    
                // If the server's wall placement syntax is correct
                } else if(Pattern.matches(WALL_VAL_REGEX, srvMove)) {
                    
                    // Check move legality
                    Coord wCoord = extractCoord(srvMove);
                    Orientation wOrt = extractOrt(srvMove);
                    if(!board.isLegalWall(i, new Wall(wCoord, wOrt))) {
                        System.err.println("Player " + i + " made illegal move!:\n    Illegal wall at " + wCoord.getX() + "," + wCoord.getY());
                        madeIllegalMove(i);
                        continue;
                    }
                    board.placeWall(i, wCoord, wOrt);
                    broadcastAll("ATARI" + (i + 1) + " " + srvMove);
                
                // If it matches no valid syntax, it's illegal
                } else {
                    madeIllegalMove(i);
                    continue;
                }
                
                // If there is a winner, announce it
                if(board.getWinner() > 0)
                    broadcastAll("KIKASHI " + board.getWinner());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Broadcasts illegal move made.
     * @param pid the player ID
     */
    private void madeIllegalMove(int pid) {
        broadcastAll("GOTE" + (pid + 1));
        board.removePlayer(pid);
    }
    
    /**
     * Extracts the Orientation from a move string.
     * @param msg the move string to extract from
     * @return a Orientation representing the desired move. Returns null if in the improper format.
     */
    private Orientation extractOrt(String msg) {
        
        Scanner sc = new Scanner(msg);
        // Seek to ')'
        while(sc.next() != ")") {}
        
        // Get the char
        char c = sc.next().trim().toLowerCase().charAt(0);
        
        // Return HORIZ for 'h', VERT for 'v'
        return (c == 'h') ? Orientation.HORIZ : (c == 'v') ? Orientation.VERT : null;
    }
    
    /**
     * Extracts the Coord from a move string.
     * @param msg the move string to extract from
     * @return a Coord representing the desired move. Returns null if in the improper format.
     */
    private Coord extractCoord(String msg) {
        Scanner sc = new Scanner(msg);
        int x = -1;
        int y = -1;
        try {
            x = sc.nextInt();
            y = sc.nextInt();
        } catch (Exception e) {
            return null;
        }
        return new Coord(x, y);
    }
    
    
    /**
     * Translates [0, 1, [2, 3]] to [0, [3], 1, [2]]. Be aware this is zero-based.
     * @param pid the player turn number
     * @return the socket ID for who's turn it is
     */
    private int translateSocket(int pid) {
        switch(pid) {
            case 0: return 0;
            case 1: return (socks.length > 2) ? 3 : 1;
            case 2: return 1;
            case 3: return 2;
            default: return 0;
        } 
    }
    
    /**
     * Broadcasts a message to all clients
     * @param msg the message to broadcast
     */
    private void broadcastAll(String msg) {
        
        try {
            PrintStream ps1 = new PrintStream(socks[0].getOutputStream());
            PrintStream ps2 = new PrintStream(socks[1].getOutputStream());
            PrintStream ps3 = new PrintStream(socks[2].getOutputStream());
            PrintStream ps4 = new PrintStream(socks[3].getOutputStream());
            ps1.println(msg);
            ps2.println(msg);
            ps3.println(msg);
            ps4.println(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Broadcasts the start message to all connected Servers
     */
    private void sendStartMsgs() {
        
        // Build the names of the servers
        String msgSrvNames = srvNames[0];
        for(int i = 1; i < srvNames.length; i++)
            msgSrvNames += " " + srvNames[i];
        
        // Build the message and send it to each move server
        for(int i = 1; i <= socks.length; i++) {
            String bcMsg = "GAME " + i + "" + msgSrvNames;
            try {
                PrintStream cout = new PrintStream(socks[i].getOutputStream());
                cout.println(bcMsg);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * Performs the handshaking for sockets
     * @return true if handshaking was successfull, false if not.
     */
    private boolean handshake() {
        
        // Set server names
        srvNames = new String[socks.length];
        for(int i = 0; i < socks.length; i++) {
            String srvName = "";
            try {
                srvName = handshakeWithSocket(socks[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if(srvName == null) return false;
            srvNames[i] = srvName;
        }
        return true;
    }
    
    
    /**
     * Handshakes with the specified Move Server
     * @param sock the socket for connecting to the Move Server
     * @return the server's name (String) if handshaking is successfull, null if not.
     */
    private String handshakeWithSocket(Socket sock) throws IOException {
        
        PrintStream cout = null;
        Scanner cin = null;
        String srvResp = "";
        short toCount = 0;
        
        // Try 3 times to handshake.
        while(toCount < 3) {
            try {
                cout = new PrintStream(sock.getOutputStream());
                cin = new Scanner(sock.getInputStream());
                
                // Send and receive
                cout.println("HELLO");
                srvResp = cin.nextLine();
            } catch (SocketTimeoutException ste) {
                if(toCount >= 3)
                    throw new IOException("Client <-> Server handshake not successfull.");
                System.err.println("Socket timed out. Retrying...");
                toCount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // If the server's response isn't in the form: "IAM <NAME>", return false
        if(!Pattern.matches(HANDSHAKE_RESP_REGEX, srvResp))
            return null;
        
        // Return the server's name
        return srvResp.substring(srvResp.indexOf(' ') + 1);
    }
    
    /**
     * Sets up the Sockets for each connected Move Server
     * @param args the <machine>:<port> pairs passed at runtime
     * @return an array of Socket objects cooresponding to each player (zero-based, in proper turn order)
     */
    private Socket[] buildSocks(String[] args) {
        
        // Check for only 2 or 4 players
        int argc = args.length;
        if(argc != 2 || argc != 4)
            throw new IllegalArgumentException("Only 2 or 4 players are allowed.");
        
        Socket[] result = new Socket[argc];
        
        // Go through each pair and check syntax. If it checks out, construct a Socket.
        int plNum = 0;
        for(String arg : args) {
            String[] pair = arg.split(":");
            
            // Machine
            String machine = null;
            if(pair[0].length() > 0)
                machine = pair[0];
            else
                throw new IllegalArgumentException("Machine was not specified.");
            
            // Port
            int port = -1;
            Socket sk = null;
            try {
                port = Integer.parseInt(pair[1]);
                sk = new Socket(machine, port);
                sk.setSoTimeout(DEFAULT_SOCKET_TIMEOUT * 1000);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("machine:port pair syntax incorrect (port must be a valid int).");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            result[translateSocket(plNum++)] = sk;
        }
        return result;
    }
    
    
    /**
     * Main method
     * @param args the runtime arguments ("<machine>:<port>" pairs.)
     */
    public static void main(String[] args) {
        GameClient gc = new GameClient(args);
    }
}








