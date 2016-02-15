package com.tmquoridor.Server;

public class UserInputMoveHandler implements MoveServer{
    // Fields
    private int port;
    private String serversName;
    private int playerNumber;
    private char[][] board;
    private int wallCount;
    
    
    // Methods
    
    /* This is the constructor to the move server. It can be passed a string 
     * which is 
     */
    public UserInputMoveHandler(int initPort){
        this.port = initPort;
    }
    
    @Override
    public void placeMoveOnBoard() {
        System.err.println("place");
    }
    
    @Override
    public String getMove() {
        return "move";
    }
    
    /* This is here to allow for the server to be ran without some outside 
     * class. It also allows for you to input the port you would like and also
     * allows for you to override the default name given to the server.
     */
    public static void main(String[] args){
	int portValue;
	int ixargs = 0;
	// While loop  to run through all of trhe command line arguments
	while(ixargs > args.length){
	    if(args[ixargs].equals("--port")){
		ixargs++;
		try {
		    portValue = Integer.parseInt(args[ixargs]);
		} catch(Exception e) {
		    System.out.println("After the port argument you entered" +
		                       " a non-numerical character");
		    System.exit(0);
		}
	    }
	    else if(args[ixargs].equals("--name")){
		
	    }
	    ixargs++;
	}
    }
    
    
}