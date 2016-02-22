package com.ser215.rps;

// This is the shell for the game server. 

// Imports
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONObject;

public class RPSGameServer extends RPSNetworkingParent{
	
    // Class Variables
    private ServerSocket serverSocket;                                  // Holds the server socket for the game server
    private String gameSessionId;					// The game sessions unique identifier
    private GameLogic gameLogic;					// Reference to the game logic for this game
    private Player[] players;                                           // Reference to the lRPSLog class for printing to log files
    private boolean gameServerRunning = true;                           // Holds a reference to if the master server is running.
	
    // Class constructor
    public RPSGameServer(int port) {
            
        // Setup
        this.gameServerPort = port;
            
        // Setup the logs
        log = new RPSLog("GameServer");
        // Now print to the log saying the master server is starting up
        log.printToLog("LOG", "GameServer starting up.");
        
        // Create the gamesession id
        this.gameSessionId = new Date() + "-" + gameServerPort;
            
        // Create initial sockets list
        this.sockets = new ArrayList(0);
            
        try {
            // Create a server socket
            this.serverSocket = new ServerSocket(gameServerPort); 
            // Tell the log the socket was created
            log.printToLog("LOG", "Game Server socket created. GameSessionId: " + this.gameSessionId);
            
            // Loop through while master server is running to listen for clients
            while (gameServerRunning) {
                
                // Listen for a new connection
                Socket socket = serverSocket.accept();
                
                // Tell the log we have a new connection
                log.printToLog("LOG", "New client connected on IP: " + socket.getInetAddress().getHostAddress() + 
                        " with HOSTNAME: " + socket.getInetAddress().getHostName() + ". Creating handler thread. ");
                
                // Add the socket to the socket array list
                this.sockets.add(socket);
                
                // Create a handler thread
                HandleAClient task = new HandleAClient(socket);
                
                // Start the new thread
                new Thread(task).start();   
            }
            
            
        }
        catch(IOException ex) {
            log.printToLog("ERROR", ex.toString());
        }    
    }

    // Main entry
    public static void main(String[] args) {
	new RPSGameServer(Integer.parseInt(args[0]));
    }

    // Methods
    // Shuts down the server
    public void shutdown() {
        // First send out message to all that the server is shutting down
        broadcastMessage("Shutdown", "");
            
        // Now close server socket
        try {
            serverSocket.close();
            gameServerRunning = false;
            System.exit(0);
        }
        catch(IOException e) {
            log.printToLog("ERROR", e.toString());
        }
    }
    
    // Inner class for HandleAClient
    class HandleAClient implements Runnable {
            
        // Variables
        private Socket socket;                              // A connected socket
        private boolean clientConnected = true;             // Is this client connected
        private DataInputStream inputFromClient;            // input stream
            
        // Constructor
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }
            
        /** Run a thread */
        public void run() {
                
            try {
 
                // Create data input and output streams
                this.inputFromClient = new DataInputStream(this.socket.getInputStream());
                    
                log.printToLog("LOG", "Started Thread");
                    
                // Tell the client that they were connected
                sendMessageToClient("Info", "Connected to server.", this.socket);

                // Continuously serve the client
                while (clientConnected) {
                    
                    // Check for data from the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputFromClient));
                    JSONObject json;
                    Gson gson;
                    
                    if (in.ready()) {
                        gson = new Gson();
                
                        json = new JSONObject(gson.fromJson(in.readLine(), JSONObject.class));
                
                        if (!json.isEmpty())
                            handleDataFromClient(json);           
                        }
                    }
                    
                    // If socket has been closed by the client then end this thread
                    if (socket.isClosed()) {
                        clientConnected = false;
                    }
                    
                }
            catch(IOException e) {
                log.printToLog("ERROR", e.toString());
            }
                
        }
            
        // Handles data from the client
        public void handleDataFromClient(JSONObject json) {
                
            // Determine how to handle the message
            if (json.get("messageType").toString().equals("Test"))                  // Test Message
                log.printToLog("TEST", json.get("message").toString());
                
            else if (json.get("messageType").toString().equals("Action")) {         // Action Performed from client
                    
                if (json.get("message").toString().equals("ClosingConnection")) {   // Client Closing Connection
                    try {
                            
                        //Remove this socket from socket list first find it
                        sockets.remove(socket);
                        this.socket.close();
                        this.inputFromClient.close();
                                                        
                        clientConnected = false;
                        // Log the disconnect
                        log.printToLog("INFO", "Client disconnected.");
                    }
                    catch(IOException e) {
                        log.printToLog("ERROR", e.toString());
                    }
                }
                    
                else if (json.get("message").toString().equals("Shutdown")) {   // Client says to shut down server
                    // First can we trust the client
                    if (json.get("password").toString().equals(masterServerPassword)){ // We trust them
                        shutdown();
                    }
                    else {
                        // Send password wrong
                        sendMessageToClient("Info", "Password is incorrect.", this.socket);
                    }
                }
                
                
            }
                
            else if (json.get("messageType").toString().equals("ChatMessage")) {   // Chat message retrieved broadcast to all
                broadcastMessage("ChatMessage", json.get("message").toString());
            }              
                
                
        }
        
    }
}
