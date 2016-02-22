package com.ser215.rps;

// Shell for master server. This will hold all master server Methods

//Imports
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RPSMasterServer extends RPSNetworkingParent {
	
    // Class Variables
    private ServerSocket serverSocket;					// Holds the server socket for the master server
    private String masterServerPassword = "shuttlebutt";		// Used for verifying commands from the client that affect the server
    private RPSLog log;							// Reference to the RPSLog class for printing to log files
    private boolean masterServerRunning = true;                         // Holds a reference to if the master server is running.
    private ArrayList sockets;                                          // Holds all the connected sockets

    // Class constructor
    public RPSMasterServer() {
        
        // Setup the logs
	log = new RPSLog("MasterServer");
	// Now print to the log saying the master server is starting up
	log.printToLog("LOG", "MasterServer starting up.");
        
        // Create initial sockets list
        this.sockets = new ArrayList(0);
            
        try {
            // Create a server socket
            this.serverSocket = new ServerSocket(masterServerPort); 
            // Tell the log the socket was created
            log.printToLog("LOG", "Server socket created.");
            
            // Loop through while master server is running to listen for clients
            while (masterServerRunning) {
            
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
		new RPSMasterServer();
	}
	
	// Methods
	
	// Starts a new game server
	public boolean startNewGameServer() {
		
		return false;			// Change this!!
	}
        
        // Loops through all sockets and sends out the chat message
        public void broadcastMessage(String messageType, String message) {
            
            for (int i = 0; i < this.sockets.size(); i++) {
                
                if (messageType.equals("ChatMessage")) {
                    // Create the date objects so we can record the date info
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    //get current date time with Date()
                    Date date = new Date();
			
                    // Construct temp string
                    String tmpMessage = "[" + dateFormat.format(date) + "] " + message;
                    // Write the message
                    sendMessageToClient("ChatMessage", tmpMessage, (Socket) this.sockets.get(i));
                }
                
                else if (messageType.equals("Shutdown")) {
                    sendMessageToClient("Info", "Server closing", (Socket) this.sockets.get(i));
                }
                
            }
            
        }
        
        // Sends a message to a specific client
        public void sendMessageToClient(String messageType, String message, Socket socket) {
            
            try {
                // Variables
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());            // output stream
            
                // Create JSON and GSon objects
                JSONObject json = new JSONObject();
                Gson gson = new Gson();
            
                // Put message into JSON
                json.put("messageType", messageType);
                json.put("message", message);
            
                //Make printer writer
                PrintWriter pw = new PrintWriter(outputToClient);
            
                // Send the message
                pw.println(gson.toJson(json));
                pw.flush();
            }
            catch(IOException e) {
                log.printToLog("ERROR", e.toString());
            }
                        
    }
        
        // Shuts down the server
        public void shutdown() {
            // First send out message to all that the server is shutting down
            broadcastMessage("Shutdown", "");
            
            // Now close server socket
            try {
                serverSocket.close();
                masterServerRunning = false;
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
                    
                    else if (json.get("message").toString().equals("Shutdown")) {   // Client syas to shut down server
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
                
                else if (json.get("messageType").toString().equals("ChatMessage"))    // Chat message retrieved broadcast to all
                    broadcastMessage("ChatMessage", json.get("message").toString());
                    
                
                
            }
        
        }
	
        
	

}
