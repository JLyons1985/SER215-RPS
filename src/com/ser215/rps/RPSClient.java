package com.ser215.rps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.*;
import java.net.*;

// Client Shell for Rock, Paper, Scissors

// Imports
import javax.swing.JFrame;

public class RPSClient extends JFrame {
	
	// Class variables
	private String masterServerIp = "localhost";			// Holds the ip address to the master address, localhost for same computer
	private String gameServerIp = "";						// When a game server ip is passed it goes here					
	private int masterServerPort = 90000;					// port  to the master server
	private int gameServerPort = 0;							// When a game server port is passed it goes here
	private DataOutputStream toServer;						// Output stream to master server or game server
	private DataInputStream fromServer;						// Input from either the game server or master server
	
	// Class constructor
	public RPSClient() {
		
		// Creation of gui, connection to master server at startup, and main program loop go in here
		
	}
	
	public static void main(String[] args) {
		new RPSClient();
	}

}
