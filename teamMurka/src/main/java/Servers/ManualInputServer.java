package com.tmquoridor.Server;

import java.io.IOException;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;

public class ManualInputServer{

    public final static int DEFAULT_PORT_NUMBER = 1478;
    public final static String DEFAULT_NAME = "America";
    public final static String ARG_PORT = "--port";
    
    
    
    public final static String eoln = "\r\n";
    
    private int port;
    
    
    // Main that uses the command line arguments
    public static void main(String[] args) {
	// This sets the defaults
	int port = DEFAULT_PORT_NUMBER;
	
	int argNdx = 0;

	// This runs through all of the command line arguments and applies the proper ones
	while (argNdx < args.length) {
	    String curr = args[argNdx];

	    if (curr.equals(ARG_PORT)) {
		++argNdx;

		String numberStr = args[argNdx];
		port = Integer.parseInt(numberStr);
	    } else {

	    // if there is an unknown parameter, give usage and quit
	    System.err.println("Unknown parameter \"" + curr + "\"");
	    usage();
	    System.exit(1);
	    }

	++argNdx;
	}

	ManualInputServer ms = new ManualInputServer(port);
	ms.run();
    }
    
    // Lets them know if they put in an invalid argument
    private static void usage() {
	System.err.print("usage: java BirthdayServer [options]\n" +
	    "       where options:\n" + "       --port port\n");
    }
    
    private ManualInputServer(int initPort){
	port = initPort;
    }
    
    public void run(){
	try{
	    // This sets up the socket the server will listen at
	    ServerSocket server = new ServerSocket(port);
	    
	    // There to let you know its working
	    System.out.println("Server now accepting connections "+
	    "on port " + port);
	    
	    // There for when the client does connects
	    Socket client;
	    
	    // Here it waits for a connection to be made
	    while((client = server.accept()) != null) {
		System.out.println("Connection from " + client);
		
		// Makes the in and out functions of the server
		Scanner cin = new Scanner(client.getInputStream());
		PrintStream cout = new PrintStream(client.getOutputStream());
		Scanner console = new Scanner(System.in);
		while(true){
		    String clientMessage = cin.nextLine();
		    System.out.println(clientMessage);
		
		    cout.print(console.nextLine() + eoln);
		}
	    }
	}catch(Exception e){
	    
	}
    }
    
}