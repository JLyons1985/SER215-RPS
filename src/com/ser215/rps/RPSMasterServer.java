package com.ser215.rps;

// Shell for master server. This will hold all master server Methods

//Imports
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;

public class RPSMasterServer extends RPSNetworkingParent {
	
    // Class Variables
    private ServerSocket serverSocket;					// Holds the server socket for the master server
    private Thread thread;						// Reference to a thread
    private String masterServerPassword = "";				// Used for verifying commands from the client that affect the server
    private RPSLog log;							// Reference to the RPSLog class for printing to log files
    private boolean masterServerRunning = true;                         // Holds a reference to if the master server is running.

    // Class constructor
    public RPSMasterServer() {
        
        // Setup the logs
	log = new RPSLog("MasterServer");
	// Now print to the log saying the master server is starting up
	log.printToLog("LOG", "MasterServer starting up.");
            
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(masterServerPort); 
            // Tell the log the socket was created
            log.printToLog("LOG", "Server socket created.");
            
            // Loop through while master server is running to listen for clients
            while (masterServerRunning) {
            
                // Listen for a new connection
                Socket socket = serverSocket.accept();
                
                // Tell the log we have a new connection
                log.printToLog("LOG", "New client connected on IP: " + socket.getInetAddress().getHostAddress() + 
                        " with HOSTNAME: " + socket.getInetAddress().getHostName() + ". Creating handler thread. ");
                
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
        
        
        
        // Inner class for HandleAClient
        class HandleAClient implements Runnable {
            
            // Variables
            private Socket socket; // A connected socket
            
            // Constructor
            public HandleAClient(Socket socket) {
                this.socket = socket;
            }
            
             /** Run a thread */
            public void run() {
                
                try {
 
                    // Create data input and output streams
                    DataInputStream inputFromClient = new DataInputStream(this.socket.getInputStream());
                    DataOutputStream outputToClient = new DataOutputStream(this.socket.getOutputStream());
                    
                    log.printToLog("LOG", "Started Thread");
                    
                    // Tell the client that they were connected
                    JSONObject json = new JSONObject();
                    json.put("messageType", "Info");
                    json.put("message", "Connected to server.");
                    
                    // Gson to convert from json to string
                    Gson gson = new Gson();
                    PrintWriter pw = new PrintWriter(outputToClient);
                    
                    pw.println(gson.toJson(json));
                    pw.flush();

                    // Continuously serve the client
                    while (true) {
                    
                    // Check for data from the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputFromClient));
                    
                    if (in.ready()) {
                        gson = new Gson();
                
                        json = new JSONObject(gson.fromJson(in.readLine(), JSONObject.class));
                
                        if (!json.isEmpty())
                            handleDataFromClient(json);           
                        }
                    }                
                }
                catch(IOException e) {
                    log.printToLog("ERROR", e.toString());
                }
                
            }
            
            // Handles data from the client
            public void handleDataFromClient(JSONObject json) {
                
                // Determine how o handle the message
                if (json.get("messageType").toString().equals("Test"))
                    log.printToLog("TEST", json.get("message").toString());
                
            }
        
        }
	
        
	

}
