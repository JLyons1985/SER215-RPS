package com.ser215.rps;

// This is the shell for the game server. 

// Imports
import java.io.*;
import java.net.*;
import java.util.*;

public class RPSGameServer {
	
	// Class Variables
	private ServerSocket serverSocket;						// Holds the server socket for the game server
	private String gameSessionId;							// The game sessions unique identifier
	private GameLogic gameLogic;							// Reference to the game logic for this game
	private Player[] players;								// Array of players in this game session
	private Thread thread;									// Thread reference
	
	// Class constructor
	public RPSGameServer() {
		// Creation of game server sockets and game loop go here.
	}

	public static void main(String[] args) {
		new RPSGameServer();
	}

}
