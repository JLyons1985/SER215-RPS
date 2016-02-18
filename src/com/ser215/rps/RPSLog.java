package com.ser215.rps;

// This is the log writer for the RPS game. It determines which log to write to
// then formats the message based on passed category and message.

import java.io.File;

// Imports
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RPSLog {

	// Class Variables
	private String printTo;						// Holds a string that is checked when determining where to write the log file
	
	// Constructors
	public RPSLog() {
		this.printTo = "";
	}
	
	public RPSLog(String printTo) {
		this.printTo = printTo;
	}
	
	// Methods
	
	// Prints to the log file with a given category and message
	public void printToLog(String category, String message) {
		
		// Variables
		Path path;								// Holds the path to the log we will be writing to
		
		// Wrap below in a try for error catching
		try {
			// First check if the directory exists
			if (!Files.isDirectory(Paths.get("./logs/"))) {
				  // Create the directory
				Files.createDirectory(Paths.get("./logs/"));
				}
			
			// Determine that log based off printTo
			if (this.printTo == "Client")
				path = Paths.get("./logs/clientLog.txt");
			else if (this.printTo == "MasterServer")
				path = Paths.get("./logs/masterServerLog.txt");
			else if (this.printTo == "GameServer")
				path = Paths.get("./logs/gameServerLog.txt");
			else
				path = Paths.get("./logs/clientLog.txt");
		
			// Get the file
			File file = new File(path.toString());
		
			// Create the write
			FileWriter write = new FileWriter(file, true);
			PrintWriter printer = new PrintWriter(write);
			
			// Create the date objects so we can record the date info
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			//get current date time with Date()
			Date date = new Date();
						
			// Write the message
			printer.println(dateFormat.format(date));
			printer.println(category.toUpperCase() + ": " + message);
			
			// Flush then close
			write.flush();
			write.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
