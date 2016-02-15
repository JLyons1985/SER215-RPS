package com.ser215.rps;

// This is the shell for the game server. 

// Imports
import java.io.*;
import java.net.*;
import java.util.*;

public class RPSGameServer {
	
	// Class Variables
	private String masterServerIp = "localhost";			// Holds the ip address to the master address, localhost for same computer
	private int masterServerPort = 90000;					// port  to the master server
	private int gameServerPort = 0;							// When a game server port is passed it goes here
	private String gameServerIp = "localhost";				// When a game server ip is passed it goes here
	private ServerSocket serverSocket;						// Holds the server socket for the game server
	
	// Class constructor
	public RPSGameServer() {
		// Creation of game server sockets and game loop go here.
	}

	public static void main(String[] args) {
		new RPSGameServer();
	}

}
