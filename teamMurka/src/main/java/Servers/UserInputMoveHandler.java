package com.tmquoridor.Server;
import com.tmquoridor.Board.*;

import java.util.*;

public class UserInputMoveHandler implements MoveServer{
    // Fields
    private int port;
    private String serversName;
    private int playerNumber;
    private Board board;
    private int wallCount;
    
    
    // Methods
    /* This is the constructor to the move server. It can be passed a string 
     * which is the of the move server.
     */
    public UserInputMoveHandler(int initPort, String initName){
	port = initPort;
	serversName = initName;
    }
    
    
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
	int portValue = 1478; // This sets the default value
	int ixargs = 0;
	String name = "teamMurka"; // This sets the default name
	// While loop  to run through all of the command line arguments
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
		ixargs++;
		name = args[ixargs];
	    }
	    ixargs++;
	}
        
	// Makes the instance of the move server
	UserInputMoveHandler us = new UserInputMoveHandler(portValue, name);
    }
    
    public void run(){
	
    }
    
    
}