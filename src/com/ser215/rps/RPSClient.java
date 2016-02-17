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
	
	// Class constructor
	public RPSClient() {
		
		// Creation of gui, connection to master server at startup, and main program loop go in here
		
	}
	
	public static void main(String[] args) {
		new RPSClient();
	}

}
