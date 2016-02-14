package Servers;

public class UserInputMoveHandler implements MoveServer{
    // Fields
    private int port;
    private String serversName;
    private int playerNumber;
    private char[][] board;
    private int wallCount;
    
    
    // Methods
    
    /* This is the constructor to the move server. It can be passed a string which is 
     */
    public UserInputMoveHandler(int initPort){
	this.port = initPort;
    }
    
    public static void main(String[] args){
	
    }
    
    
}