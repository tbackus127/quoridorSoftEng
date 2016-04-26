package com.tmquoridor.Client;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.net.*;

import com.tmquoridor.Board.*;
import com.tmquoridor.GUI.*;

public class GameClient {
    
    /** Timeout (in seconds) for any socket */
    private static final int DEFAULT_SOCKET_TIMEOUT = 600;
    
    /** End-of-line string */
    private static final String EOLN = "\r\n";
    
    /** Handshake response regex */
    private static final String HANDSHAKE_RESP_REGEX = "IAM\\s+\\w*:*\\w+\\s*";
    
    /** Move validation regex */
    private static final String MOVE_VAL_REGEX = "TESUJI\\s+\\(\\s*[0-8],\\s*[0-8]\\s*\\)\\s*\\n*";
    
    /** Wall validation regex */
    private static final String WALL_VAL_REGEX = "TESUJI\\s+\\[\\s*\\(\\s*[0-7]\\s*,\\s*[0-7]\\s*\\)\\s*,\\s*[hv]\\s*\\]\\s*\\n*";
    // This is only slightly obnoxious...
    
    /** The internal Board instance */
    private Board board;
    
    /** Number of players */
    private int numOfPlayers;
    
    /** Socket array */
    private Socket[] socks;
    
    /** Server name array */
    private String[] srvNames;
    
    /** GUI */
    private QuorGUI gui;
    
    /** Playing flag */
    private boolean playing = false;
    
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
            try {
              Thread.sleep(1000);
            } catch(InterruptedException ign) {}
            sendStartMsgs();
            setupBoard();
            gui = new QuorGUI(board, srvNames);
            doGameLoop();
        }
        
    }
    
    /**
     * Sets up the internal Board instance
     */
    private void setupBoard() {
        System.err.println("Setting up...");
        board = new Board(socks.length);
    }
    
    /**
     * Performs the game loop
     */
    private void doGameLoop() {
        System.err.println("Entering game loop...");
        board.printBoard();
        playing = true;
        while(playing) {
            for(int i = 0; i < socks.length; i++) {
                PrintStream cout = null;
                Scanner cin = null;
                
                int pnum = translateSocket(i);
                
                // Skip over kicked players
                if(socks[pnum] == null) continue;
                
                try {
                    
                    // Send GO (see what I did there?) message and await response.
                    cout = new PrintStream(socks[pnum].getOutputStream());
                    cin = new Scanner(socks[pnum].getInputStream());
                    
                    cout.print("MYOUSHU" + EOLN);
                    System.err.println("Sent: MYOUSHU");
                    String srvMove = cin.nextLine();
                    System.err.println("  Msg: " + srvMove);
                    
                    // If the server's move syntax is correct
                    if(Pattern.matches(MOVE_VAL_REGEX, srvMove)) {
                        
                        // Check move legality
                        Coord mCoord = extractCoord(srvMove);
                        // System.err.println("GameClient.Coord: " + mCoord);
                        if(!board.isLegalMove(pnum, mCoord)) {
                            System.err.println("Player " + (pnum+1) + " made illegal move!:\n    Illegal destination: " + mCoord.getX() + "," + mCoord.getY());
                            madeIllegalMove(pnum);
                            continue;
                        }
                        board.movePlayer(pnum, mCoord, "Client.move");
                        srvMove = srvMove.substring(7);
                        broadcastAll("ATARI" + " " + (pnum + 1) + " " + srvMove);
                        board.printBoard();
                        
                    // If the server's wall placement syntax is correct
                    } else if(Pattern.matches(WALL_VAL_REGEX, srvMove)) {
                        
                        // Check move legality
                        Coord wCoord = extractCoord(srvMove);
                        Orientation wOrt = extractOrt(srvMove);
                        if(wOrt == Orientation.HORIZ){
                            wCoord = new Coord(wCoord.getX(), wCoord.getY() + 1);
                        } else {
                            wCoord = new Coord(wCoord.getX() + 1, wCoord.getY());
                        }
                        if(!board.isLegalWall(pnum, new Wall(wCoord, wOrt))) {
                            System.err.println("Player " + (pnum + 1) + " made illegal move!:\n    Illegal wall at " + wCoord.getX() + "," + wCoord.getY());
                            madeIllegalMove(pnum);
                            continue;
                        }
                        board.placeWall(pnum, wCoord, wOrt);
                        srvMove = srvMove.substring(7);
                        broadcastAll("ATARI " + (pnum + 1) + " " + srvMove);
                        board.printBoard();
                    
                    // If it matches no valid syntax, it's illegal
                    } else {
                        System.err.println("  Matched no valid syntaxes.");
                        madeIllegalMove(pnum);
                        continue;
                    }
                    
                    // If there is a winner, announce it
                    if(board.getWinner() > 0) {
                        broadcastAll("KIKASHI " + board.getWinner());
                        playing = false;
                        gui.repaintGUI();
                        break;
                    }
                    
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                    playing = false;
                }
                gui.repaintGUI();
            }
        }
        
    }
    
    /**
     * Broadcasts illegal move made.
     * @param pid the player ID
     */
    private void madeIllegalMove(int pid) {
        broadcastAll("GOTE" + " " + (pid + 1));
        board.removePlayer(pid);
        try {
            socks[pid].close();
        } catch (IOException e) {
            System.err.println("Socket closed");
        }
        socks[pid] = null;
        int winID = board.getWinner();
        if(winID > 0) {
          broadcastAll("KIKASHI " + winID);
          playing = false;
        }
        gui.repaintGUI();
    }
    
    /**
     * Extracts the Orientation from a move string.
     * @param msg the move string to extract from
     * @return a Orientation representing the desired move. Returns null if in the improper format.
     */
    private Orientation extractOrt(String msg) {
        
        return (msg.contains("h")) ? Orientation.HORIZ : (msg.contains("v")) ? Orientation.VERT : null;
    }
    
    /**
     * Extracts the Coord from a move string.
     * @param msg the move string to extract from
     * @return a Coord representing the desired move. Returns null if in the improper format.
     */
    private Coord extractCoord(String msg) {
        int[] res = new int[2];
        int cnt = 0;
        for(int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);
            if(c >= '0' && c <= '9') {
                res[cnt++] = c - '0';
            }
        }
        return new Coord(res[0], res[1]);
    }
    
    
    /**
     * Translates [0, 1, [2, 3]] to [0, [3], 1, [2]]. Be aware this is zero-based.
     * @param pid the player turn number
     * @return the socket ID for who's turn it is
     */
    private int translateSocket(int pid) {
        switch(pid) {
            case 0: return 0;
            case 1: return (numOfPlayers > 2) ? 3 : 1;
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
        System.err.println("Broadcast: \"" + msg + "\"");
        try {
            for(int i = 0; i < socks.length; i++) {
                if(socks[i] == null) continue;
                PrintStream ps = new PrintStream(socks[i].getOutputStream());
                ps.print(msg + EOLN);
            }
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
            String bcMsg = "GAME " + i + " " + msgSrvNames;
            try {
                PrintStream cout = new PrintStream(socks[i - 1].getOutputStream());
                cout.print(bcMsg + EOLN);
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
                System.out.println("Handshaking with " + i);
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
        
        
            try {
                cout = new PrintStream(sock.getOutputStream());
                cin = new Scanner(sock.getInputStream());
                
                // Send and receive
                cout.print("HELLO" + EOLN);
                System.err.println("Sent \"HELLO\"");
                srvResp = cin.nextLine();
                System.err.println("Resp: " + srvResp);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        
        // If the server's response isn't in the form: "IAM <NAME>", return false
        if(!Pattern.matches(HANDSHAKE_RESP_REGEX, srvResp)) {
            System.err.println("  !! Handshake response mismatch:\n  \"" + srvResp + "\"");
            return null;
        }
        
        // Return the server's name
        return srvResp.substring(srvResp.indexOf(' ') + 1);
    }
    
    /**
     * Sets up the Sockets for each connected Move Server
     * @param args the <machine>:<port> pairs passed at runtime
     * @return an array of Socket objects cooresponding to each player (zero-based, in proper turn order)
     */
    private Socket[] buildSocks(String[] args) {
        
        System.err.println("Building sockets...");
        
        // Check for only 2 or 4 players
        int argc = args.length;
        if(argc != 2 && argc != 4)
            throw new IllegalArgumentException("Only 2 or 4 players are allowed.");
        
        numOfPlayers = argc;
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
                
                System.err.println("  Creating new socket: " + machine + ":" + port);
                sk = new Socket(machine, port);
                sk.setSoTimeout(DEFAULT_SOCKET_TIMEOUT * 1000);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Machine:port pair syntax incorrect (port must be a valid int).");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            result[translateSocket(plNum++)] = sk;
        }
        return result;
    }
    
    
    /**
     * Main method
     * @param args the runtime arguments ("machine:port" pairs.)
     */
    public static void main(String[] args) {
        GameClient gc = new GameClient(args);
    }
}








