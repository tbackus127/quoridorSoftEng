package Servers;

public interface MoveServer{
    
    // Fields
    private String machine;
    private int port;
    private String serversName;
    private int playerNumber;
    private char[][] board;
    private int wallCount;
    
    // Methods
    public static void main(String[] args);
    private void run();
    private String getMove();
    private void placeMoveOnBoard();
    
}