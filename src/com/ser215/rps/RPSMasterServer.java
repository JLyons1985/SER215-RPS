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

	// Class constructor
	public RPSMasterServer() {
		// Creation of master server sockets go here
	}
	
	public static void main(String[] args) {
		new RPSMasterServer();
	}

}
