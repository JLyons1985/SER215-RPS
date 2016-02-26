package com.ser215.rps;

// This is the shell for the game server. 

// Imports
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONObject;

public class RPSGameServer extends RPSNetworkingParent implements Runnable{
	
    // Class Variables
    private ServerSocket serverSocket;                                  // Holds the server socket for the game server
    private String gameSessionId;					// The game sessions unique identifier
    private GameLogic gameLogic;					// Reference to the game logic for this game
    private Player[] players;                                           // Reference to the lRPSLog class for printing to log files
    private boolean gameServerRunning = true;                           // Holds a reference to if the master server is running.
    private RPSMasterServer masterServerRef;                            // Holds a reference to master server
	
    // Class constructor
    public RPSGameServer(int port, String gameId, RPSMasterServer masterServer) {
            
        // Setup
        this.gameServerPort = port;
        players = new Player[2];
        players[0] = new Player();
        players[1] = new Player();
        this.masterServerRef = masterServer;
            
        // Setup the logs
        log = new RPSLog("GameServer");
        // Now print to the log saying the master server is starting up
        log.printToLog("LOG", "GameServer starting up.");
        
        // Create the gamesession id
        this.gameSessionId = gameId;
        
        // Create the gameLogic object
        this.gameLogic = new GameLogic(false, this);
            
        // Create initial sockets list
        this.sockets = new ArrayList(0);
            
    }

    // Run
    public void run(){
        try {
            // Create a server socket
            this.serverSocket = new ServerSocket(gameServerPort); 
            // Tell the log the socket was created
            this.log.printToLog("LOG", "Game Server socket created. GameSessionId: " + this.gameSessionId);
            
            
            // Loop through while master server is running to listen for clients
            while (gameServerRunning) {
                
                // Listen for a new connection
                Socket socket = serverSocket.accept();
                
                // Tell the log we have a new connection
                this.log.printToLog("LOG", "New client connected on IP: " + socket.getInetAddress().getHostAddress() + 
                        " with HOSTNAME: " + socket.getInetAddress().getHostName() + ". Creating handler thread. ");
                
                // Add the socket to the socket array list
                this.sockets.add(socket);
                
                // Create a handler thread
                HandleAClientOnGameServer task = new HandleAClientOnGameServer(socket);
                
                // Start the new thread
                new Thread(task).start();   
            }
 
        }
        catch(IOException ex) {
            this.log.printToLog("ERROR", ex.toString());
        }    
    }

    // Methods
    // Shuts down the server
    public void shutdown() {
        // First send out message to all that the server is shutting down
        broadcastMessage("Shutdown", "");
        this.log.printToLog("INFO", "Shutting down session: " + this.gameSessionId);
            
        // Now close server socket
        try {
            this.serverSocket.close();
            this.gameServerRunning = false;
            
            // Tell master server to remove me
            masterServerRef.removeGameSession(this);
        }
        catch(IOException e) {
            this.log.printToLog("ERROR", e.toString());
        }
    }
    
    // Send game logic data to players
    public void sendGameLogicToPlayers() {
        // Send logic to all players
        broadcastMessage("UpdateGameLogic", gameLogic.getGameLogicAsJson());
    }
    
    // Get the log
    public RPSLog getGameLog(){
        return this.log;
    }
    
    // Get the session id
    public String getGameSessionId() {
        return this.gameSessionId;
    }
    
    // Inner class for HandleAClient
    class HandleAClientOnGameServer implements Runnable {
            
        // Variables
        private Socket socket;                              // A connected socket
        private boolean clientConnected = true;             // Is this client connected
        private DataInputStream inputFromClient;            // input stream
            
        // Constructor
        public HandleAClientOnGameServer(Socket socket) {
            this.socket = socket;
        }
            
        /** Run a thread */
        public void run() {
                
            try {
 
                // Create data input and output streams
                this.inputFromClient = new DataInputStream(this.socket.getInputStream());
                    
                log.printToLog("LOG", "Started Thread");
                    
                // Tell the client that they were connected
                sendMessageToClient("Info", "Connected to game server.", this.socket);
                
                // Check for data from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(this.inputFromClient));
                
                // Now request player data
                sendMessageToClient("Action", "SendPlayerData", this.socket);

                // Continuously serve the client
                while (clientConnected) {
                    
                    // Check for data from the server
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
                    if (this.socket.isClosed()) {
                        this.clientConnected = false;
                    }
                    
                }
            catch(IOException e) {
                log.printToLog("ERROR", e.toString());
            }
                
        }
            
        // Handles data from the client
        public void handleDataFromClient(JSONObject json) {
                
            // Determine how to handle the message
            switch(json.get("messageType").toString()){
                
                case "Test":
                    log.printToLog("TEST", json.get("message").toString());
                    break;
                    
                case "Action":
                    switch (json.get("message").toString()) {   
                        
                        case "PlayerMadeAThrow":
                            // First check that the player is allowed to make that throw
                            if (gameLogic.canPlayerMakeThrow(json.get("playerId").toString(), Integer.parseInt(json.get("throwType").toString()))){
                                // Player can make throw
                                log.printToLog("Info", json.get("playerId").toString() + " can make the throw of " + json.get("throwType").toString());
                                
                                gameLogic.playerMadeAThrow(json.get("playerId").toString(), Integer.parseInt(json.get("throwType").toString()));
                                // Send game logic
                                sendGameLogicToPlayers();
                            }
                            else { // Player cant make throw
                                log.printToLog("Info", json.get("playerId").toString() + " can NOT make the throw of " + json.get("throwType").toString());
                                sendMessageToClient("Info", "You can't make that throw try again.", this.socket);
                            }
                            break;
                        
                        case "ClosingConnection":
                            try {
                            
                                //Remove this socket from socket list first find it
                                sockets.remove(socket);
                                this.socket.close();
                                this.inputFromClient.close();
                                                        
                                clientConnected = false;
                                // Log the disconnect
                                log.printToLog("INFO", "Client disconnected.");
                                
                                
                                if (players[0].getPlayerId().equals(json.get("playerId").toString())){ 
                                    players[0] = new Player();
                                    gameLogic.removePlayerFromGame(json.get("playerId").toString());
                                }
                                else if (players[1].getPlayerId().equals(json.get("playerId").toString())){
                                    players[1] = new Player();
                                    gameLogic.removePlayerFromGame(json.get("playerId").toString());
                                }
                                else {
                                    log.printToLog("ERROR", "No one by the player id.");
                                }
                                
                                if (gameLogic.getCurrentPlayers() <= 0) {
                                    // Shut down
                                    shutdown();
                                }
                            }
                            catch(IOException e) {
                                log.printToLog("ERROR", e.toString());
                            }
                            break;
                            
                        case "Shutdown":
                          // First can we trust the client
                            if (json.get("password").toString().equals(masterServerPassword)){ // We trust them
                                shutdown();
                            }
                            else {
                                // Send password wrong
                                sendMessageToClient("Info", "Password is incorrect.", this.socket);
                            }
                            break;
                    }
                    break;
                
                case "NewPlayer":
                    log.printToLog("Info", "New player is joining....");
                    
                    // Make data into a player
                    Player tmpPlayer = new Player();
                    
                    // Load data into player
                    tmpPlayer.setPlayerId(json.get("playerId").toString());
                    tmpPlayer.setPlayerUsername(json.get("playerUsername").toString());
                    tmpPlayer.setWinsTotal(Integer.parseInt(json.get("totalWins").toString()));
                    tmpPlayer.setTiesTotal(Integer.parseInt(json.get("totalTies").toString()));
                    tmpPlayer.setLossesTotal(Integer.parseInt(json.get("totalLosses").toString()));
                    
                    // Add the player to the array and inside the game logic
                    if (players[0].getPlayerId().equals("")){  // Add here
                        players[0] = tmpPlayer;
                        gameLogic.handleNewPlayer(tmpPlayer);
                    }
                    else {
                        players[1] = tmpPlayer;
                        gameLogic.handleNewPlayer(tmpPlayer);
                    }
                    
                    // Now check if session is full or not
                    if (gameLogic.getCurrentPlayers() == gameLogic.getMaxPlayers()) { // Full session start game
                        
                        // Tell players the game has begun
                        broadcastMessage("Info", "The game has started.");
                        
                        gameLogic.newRound();
                        
                        // set game has started
                        gameLogic.setGameHasStarted(true);
                        
                        //System.out.println(gameLogic.getHasGameStarted());
                        
                        // send game logic data to players
                        sendGameLogicToPlayers();
                    }
                    else { // Waiting for another rplayer
                        
                        // Tell players waiting for another
                        broadcastMessage("Info", "Waiting on another player...");
                        
                        // set game has started
                        gameLogic.setGameHasStarted(false);
                        
                        //System.out.println(gameLogic.getHasGameStarted());
                        
                        sendGameLogicToPlayers();
                    }
                    break;
                    
                case "ChatMessage":
                    broadcastMessage("ChatMessage", json.get("message").toString());
                    break;
            }             
                
        }
        
    }
}
