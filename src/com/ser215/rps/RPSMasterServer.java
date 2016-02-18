package com.ser215.rps;

// Shell for master server. This will hold all master server Methods

//Imports
import java.io.*;
import java.net.*;
import java.util.*;

public class RPSMasterServer {
	
	// Class Variables
	private ServerSocket serverSocket;						// Holds the server socket for the master server
	private Thread thread;									// Reference to a thread
	private String masterServerPassword = "";				// Used for verifying commands from the client that affect the server
	private RPSLog log;										// Reference to the lRPSLog class for printing to log files

	// Class constructor
	public RPSMasterServer() {
		// Creation of master server sockets go here
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
		
	

}
