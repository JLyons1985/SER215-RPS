package com.ser215.rps;

// Client Shell for Rock, Paper, Scissors

// Imports
import javax.swing.JFrame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.*;
import java.net.*;

public class RPSClient extends RPSNetworkingParent {
	// Class variables
	private DataOutputStream toServer;						// Output stream to master server or game server
	private DataInputStream fromServer;						// Input from either the game server or master server
	private Player player;									// Holds a reference to the player data for this client
	private boolean isPlayingSinglePlayer;					// Is the player playing the computer?
	private GameLogic gameLogic;							// Holds a reference to the game logic, only used during single player
	private RPSLog log;										// Reference to the lRPSLog class for printing to log files
	
	// Class constructor
	public RPSClient() {
		
		// Creation of gui, connection to master server at startup, and main program loop go in here
		
		// Create the client log
		log = new RPSLog("Client");
		// Now print to the log saying the client is starting up
		log.printToLog("LOG", "Client starting up.");
		
	}
	
	// Main entry for the client
	public static void main(String[] args) {
		new RPSClient();
	}
	
	// Class Methods
	
	// Connects to the master server
	public void connectToMasterServer() {
		
	}
	
	// Lists all the available game sessions
	// Mainly effects the gui as the list will populate a menu screen.
	// Returns a list of unique gamesession ids
	public String[] listGameSessions() {
		
		return new String[0]; 		// Change this!!!
	}
	
	// Creates a game session by telling master server to create a new session
	// Master server then takes the supplied username and creates a unique gamesessionid
	public boolean createGameSession() {
		
		return false;				// Change this!!
	}
	
	// Joins either a newly created game session or a session listed
	// in the game session list by suppling the unique game session id
	public boolean joinGameSession(String gameSessionId) {
		
		return false;				// Change This!!
	}
	
	// Starts a single player game
	// Will create a gameLogic object which will act as the CPU
	public void startSinglePlayerGame() {
		
	}
	
	// Quits the game, informs master server the client is shutting down, and cleans
	// up unneeded stuff.
	public void quit() {
		
	}
	

}
