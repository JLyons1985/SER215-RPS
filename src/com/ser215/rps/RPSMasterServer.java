package com.ser215.rps;

// Shell for master server. This will hold all master server Methods

//Imports
import java.io.*;
import java.net.*;
import java.util.*;

public class RPSMasterServer {
	
	// Class Variables
	private String masterServerIp = "localhost";			// Holds the ip address to the master address, localhost for same computer
	private int masterServerPort = 90000;					// port  to the master server
	private int gameServerPort = 0;							// When a game server port is passed it goes here
	private String gameServerIp = "localhost";				// When a game server ip is passed it goes here
	private ServerSocket serverSocket;						// Holds the server socket for the master server

	// Class constructor
	public RPSMasterServer() {
		// Creation of master server sockets go here
	}
	
	public static void main(String[] args) {
		new RPSMasterServer();
	}

}
