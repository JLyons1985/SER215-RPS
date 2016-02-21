package com.ser215.rps;

// Parent class for networking 

// Imports
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.*;
import java.net.*;


public class RPSNetworkingParent {
	
	// Variables
	protected String masterServerIp = "localhost";			// Holds the ip address to the master address, localhost for same computer
	protected String gameServerIp = "";				// When a game server ip is passed it goes here					
	protected int masterServerPort = 9000;				// port  to the master server
	protected int gameServerPort = 0;                               // When a game server port is passed it goes here
	
	
	// Constructors
	public RPSNetworkingParent() {
		
	}

}
