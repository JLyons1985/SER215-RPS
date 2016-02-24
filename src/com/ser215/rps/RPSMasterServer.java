package com.ser215.rps;

// Shell for master server. This will hold all master server Methods

//Imports
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RPSMasterServer extends RPSNetworkingParent {
	
    // Class Variables
    private ServerSocket serverSocket;					// Holds the server socket for the master server
    private boolean masterServerRunning = true;                         // Holds a reference to if the master server is running.
    private final int PORTSTART = 9001, PORTEND = 9999;                 // Star and end ports to search for availability
    
    private ArrayList gameServers;                                      // Holds all the game servers that are active

    // Class constructor
    public RPSMasterServer() {
        
        // Setup the logs
	log = new RPSLog("MasterServer");
	// Now print to the log saying the master server is starting up
	log.printToLog("LOG", "MasterServer starting up.");
        
        // Create initial sockets list
        this.sockets = new ArrayList(0);
        // Create initial game server array
        this.gameServers = new ArrayList(0);
            
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
    public String startNewGameServer() {
		
        // First find the first available port
        int availPort = 0;                                              // Available port holder  
        String tmpGameId = "";
                
        for (int i = PORTSTART; i <= PORTEND; i++) {
            
            if (portAvailable(i)) {
                availPort = i;
                
                // Create the date objects so we can record the date info
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		//get current date time with Date()
		Date date = new Date();
                
                tmpGameId = dateFormat.format(date) + "-" + availPort;
                
                // Now start the game server and return the port
                
                    RPSGameServer tmpServer = new RPSGameServer(availPort, tmpGameId, this);
                    
                    // Start the new thread
                    new Thread(tmpServer).start(); 
                    
                    // Add game server port to game servers
                    this.gameServers.add(tmpServer);
                    return tmpGameId;
            }
        }
          
	return tmpGameId;			
    }
    
    
    public JSONObject createGameServersList() {
        
        // Variables
        JSONObject json = new JSONObject();
        
        json.put("numOfGames", String.valueOf(gameServers.size()));
        
        // Now loop through adding each
        for (int i = 0; i < gameServers.size(); i++){
            json.put("server" + i, ((RPSGameServer)gameServers.get(i)).getGameSessionId());
        }
        
        return json;
    }
    
    // Checks if a port is freee
    private boolean portAvailable(int port) {
        
        // Variables
        Socket tmpSocket = null;
        
        try {
            tmpSocket = new Socket("localhost", port);
            
            // If we make it here then port is in use and is listening
            tmpSocket.close();
            return false;
                        
        } catch (IOException e) {
            // Port not in use
            return true;
        } finally {
                // Close socket in case
                if (tmpSocket != null){
                    try {
                        tmpSocket.close();
                    } catch (IOException e) {
                        log.printToLog("ERROR", e.getMessage());
                    }
                }
        }
    }
        
    // Shuts down the server
    public void shutdown() {
        // First send out message to all that the server is shutting down
        broadcastMessage("Shutdown", "");
            
        // Now close server socket
        try {
            
            // First loop through all game sessions and tell them to shut down
            for (int i = 0; i < gameServers.size(); i++) {
                
                ((RPSGameServer) gameServers.get(i)).shutdown();

            }
            
            // Clear out game servers
            gameServers.clear();
            
            serverSocket.close();
            masterServerRunning = false;
            System.exit(0);
        }
        catch(IOException e) {
            log.printToLog("ERROR", e.toString());
        }
    }
    
    // Remove a gameserver
    public void removeGameSession(RPSGameServer rpsGameServer){
        gameServers.remove(rpsGameServer);
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
                sendMessageToClient("Info", "Connected to master server.", this.socket);

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
                
                else if (json.get("message").toString().equals("CreateGameServer")) {   // Client says create a new game server
                    log.printToLog("INFO", "Received create game session: " + json.toJSONString());
                    if (json.get("password").toString().equals(masterServerPassword)){ // We trust them
                        // Send the client the game port that a new game session is created on
                        sendMessageToClient("GameSessionCreated", startNewGameServer(), this.socket);
                    }
                    else {
                        // Send password wrong
                        sendMessageToClient("Info", "Password is incorrect.", this.socket);
                    }
                }
                
                else if (json.get("message").toString().equals("RequestGames")) {   // Send the list of games to client
                    
                    // Send the list of games to the player
                    sendMessageToClient("Action", "RequestedGames", createGameServersList().toJSONString(), this.socket);
                    
                }
            }
                
            else if (json.get("messageType").toString().equals("ChatMessage"))    // Chat message retrieved broadcast to all
                broadcastMessage("ChatMessage", json.get("message").toString());
                    
                
                
        }
        
    }
	
        
	

}
