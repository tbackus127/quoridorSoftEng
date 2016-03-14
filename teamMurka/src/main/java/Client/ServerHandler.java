// package com.tmquoridor.Client;

// import java.util.Scanner;
// import java.io.PrintStream;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.io.IOException;
// import java.net.Socket;

// public class ServerHandler implements Runnable {

    // private final Scanner clientIn;
    // private final PrintStream serverOut;
    
    // private final Socket clientSocket;
    
    // private ServerHandler(OutputStream out, InputStream in, Socket sock) {
      // clientIn = new Scanner(in);
      // serverOut = new PrintStream(out);
      // this.clientSocket = sock;
    // }
    
    // public ServerHandler(Socket socket) throws IOException {
        // this(socket.getOutputStream(), socket.getInputStream(), socket);
    // }
    
    // public void run() {
        // String clientMessage = "";
        // while(clientIn.hasNextLine()) {
            // clientMessage = clientIn.nextLine();
            // System.err.println("S.Rec: " + clientMessage);
            // serverOut.println(clientMessage);
        // }
        
        // System.err.println("Disconnected from client.");
        // clientIn.close();
        // serverOut.close();
    // }

// }