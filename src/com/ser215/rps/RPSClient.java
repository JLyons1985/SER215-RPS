package com.ser215.rps;

// Client Shell for Rock, Paper, Scissors

// Imports
import javax.swing.JFrame;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.*;
import java.net.*;


public class RPSClient extends JFrame {
	// Class variables
	private static DataOutputStream toServer;						// Output stream to master server or game server
	private static DataInputStream fromServer;						// Input from either the game server or master server
	private static Player player;									// Holds a reference to the player data for this client
	private static boolean isPlayingSinglePlayer;					// Is the player playing the computer?
	private static GameLogic gameLogic;								// Holds a reference to the game logic, only used during single player
	private static RPSLog log;										// Reference to the lRPSLog class for printing to log files
		
		
	// Main entry for the client
	public static void main(String[] args) {	
		// Setup the Client variables
		log = new RPSLog("Client");
		// Now print to the log saying the client is starting up
		log.printToLog("LOG", "Client starting up.");
		
		 
	}
	
	
	
	
	// Class Methods
	
	// Connects to the master server
	public static void connectToMasterServer() {
		
	}
	
	// Lists all the available game sessions
	// Mainly effects the gui as the list will populate a menu screen.
	// Returns a list of unique gamesession ids
	public static String[] listGameSessions() {
		
		return new String[0]; 		// Change this!!!
	}
	
	// Creates a game session by telling master server to create a new session
	// Master server then takes the supplied username and creates a unique gamesessionid
	public static boolean createGameSession() {
		
		return false;				// Change this!!
	}
	
	// Joins either a newly created game session or a session listed
	// in the game session list by suppling the unique game session id
	public static boolean joinGameSession(String gameSessionId) {
		
		return false;				// Change This!!
	}
	
	// Starts a single player game
	// Will create a gameLogic object which will act as the CPU
	public static void startSinglePlayerGame() {
		
	}
	
	// Quits the game, informs master server the client is shutting down, and cleans
	// up unneeded stuff.
	public static void quit() {
		
	}
	

}
