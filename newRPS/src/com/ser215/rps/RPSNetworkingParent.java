package com.ser215.rps;

// Parent class for networking 

// Imports
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.simple.JSONObject;


public class RPSNetworkingParent {
	
	// Variables
	protected String masterServerIp = "localhost";			// Holds the ip address to the master address, localhost for same computer
	protected String gameServerIp = "localhost";			// When a game server ip is passed it goes here					
	protected int masterServerPort = 9000;				// port  to the master server
	protected int gameServerPort = 0;                               // When a game server port is passed it goes here
        protected ArrayList sockets;                                    // Holds all the connected sockets
        protected RPSLog log;                                           // Reference to the RPSLog class for printing to log files
        protected String masterServerPassword = "shuttlebutt";		// Used for verifying commands from the client that affect the server
	
	
	// Constructors
	public RPSNetworkingParent() {
		
	}
        
        // Methods
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

}
