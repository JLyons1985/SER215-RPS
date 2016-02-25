package com.ser215.rps;

// Game logic for each game goes here

// Imports
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import java.util.Random;

public class GameLogic {

	// Class Variables
	private boolean isSinglePlayer = false;                         // Is the game a single player game
        private int numOfPlayers = 0;                                   // Num of players in the game currently
        private int numOfMaxPlayers = 2;                                // Num of max players ithe game allows
        private Player playerOne = null, playerTwo = null;              // Reference to the players
        private int round = 0, turn = 0;                                // Rounds and turns
        private RPSClientApplication mainApp;                           // Holds reference to main app
	
        //new
        static  int  playerTies = 0,CPUTies = 0;
        static   int  playerWins = 0,CPUWins = 0;
        static int  playerlosses = 0,CPULosses = 0;
        
	// Constructors
	public GameLogic(boolean isSinglePlayer) {
            
            if (isSinglePlayer) {
		this.isSinglePlayer = isSinglePlayer;
                
                // Set player two as CPU
                this.playerTwo = new Player("CPU");
                
                this.numOfPlayers += 1;
            }
            
            this.playerOne = new Player();
            this.playerTwo = new Player();
            
	}
        
        public GameLogic(boolean isSinglePlayer, RPSClientApplication mainApp) {
            
            if (isSinglePlayer) {
		this.isSinglePlayer = isSinglePlayer;
                
                // Set player two as CPU
                this.playerTwo = new Player("CPU");
                
                this.numOfPlayers += 1;
            }
            
            this.mainApp = mainApp;
	}
        
        // Methods
        
        // Starts a new round resets the turns and options uses
        public void newRound() {
            this.round += 1;
            this.turn = 1;
            
            this.playerOne.setRPSUses(0, false);
            this.playerOne.setRPSUses(1, false);
            this.playerOne.setRPSUses(2, false);
            
            this.playerTwo.setRPSUses(0, false);
            this.playerTwo.setRPSUses(1, false);
            this.playerTwo.setRPSUses(2, false);
        }
        
        // New player joined game
        public void handleNewPlayer(Player player) {
            // Check if single player
            if (this.isSinglePlayer) {
                setPlayerSlot(1, player);
                this.numOfPlayers +=1;
            }
            else {      // Find out which slot is open
                if (this.playerOne.getPlayerId().equals("")){
                    // No player one add here
                    setPlayerSlot(1, player);
                    this.numOfPlayers += 1;
                }
                else {
                    // Player one taken add here
                    setPlayerSlot(2, player);
                    this.numOfPlayers += 1;
                }
            }
        }
        
        // Checks if player is allowed to make a throw
        public boolean canPlayerMakeThrow(String playerId, int throwType){
            
            // First has the player made a throw this turn
            if (this.playerOne.getPlayerId().matches(playerId)){  // Player one made the throw
                if (!this.playerOne.getCanMakeAThrow())
                    return false;
            }
            else {
                if (!this.playerTwo.getCanMakeAThrow())
                    return false;
            }
            
            // Alright if we made it this far then they can make a throw
            // but have they made this type of throw already this round
            if (this.playerOne.getPlayerId().matches(playerId)){  // Player one made the throw
                if (this.playerOne.getRPSUses(throwType))
                    return false;
            }
            else {
                if (this.playerTwo.getRPSUses(throwType))
                    return false;
            }
            
            return true;
        }
        
        // Handles when a player makes a throw
        public void playerMadeAThrow(String playerId, int throwType){
            
            // First was it player one or two
            if (this.playerOne.getPlayerId().matches(playerId)){  // Player one made the throw
                // Set the throw to true
                this.playerOne.setRPSUses(throwType, true);
            }
            else {
                // Set the throw to true
                this.playerTwo.setRPSUses(throwType, true);
            }
            
        }
        
        // Sets one of the player slots as the provided info
        private void setPlayerSlot(int slot, Player player) {
            switch (slot){
                case 1:
                    this.playerOne = player;
                    break;
                case 2:
                    this.playerTwo = player;
                    break;
            }
        }
        
        // Get the player player data
        public Player getPlayerData(int slot) {
            switch (slot){
                case 1:
                    return this.playerOne;
                case 2:
                    return this.playerTwo;
            }
            
            return new Player();
        }
        
        public void setGameLogicFromJson(JSONObject json){
            Gson gson = new Gson();
            
            // Now go through the task of creating the json
            this.isSinglePlayer = Boolean.getBoolean(json.get("isSinglePlayer").toString());
            this.numOfMaxPlayers = Integer.parseInt(json.get("numOfMaxPlayers").toString());
            this.numOfPlayers = Integer.parseInt(json.get("numOfPlayers").toString());
            this.playerOne.setPlayerDataFromJson(new JSONObject(gson.fromJson(json.get("playerOne").toString(), JSONObject.class)));
            this.playerTwo.setPlayerDataFromJson(new JSONObject(gson.fromJson(json.get("playerTwo").toString(), JSONObject.class)));
            this.round = Integer.parseInt(json.get("round").toString());
            this.turn = Integer.parseInt(json.get("turn").toString());
            
        }
        
        public void setCurrentPlayers(int currentPlayers){
            this.numOfPlayers = currentPlayers;
        }
        
        // Get round data
        public int getRound(){
            return this.round;
        }
        
        // Get rturn data
        public int getTurn(){
            return this.turn;
        }
        
        // Get max players
        public int getMaxPlayers(){
            return this.numOfMaxPlayers;
        }
        
        // Get current players nu,
        public int getCurrentPlayers(){
            return this.numOfPlayers;
        }
        
      
        public String getGameLogicAsJson(){
            
            // Create json object
            JSONObject json = new JSONObject();
            
            // Now go through the task of creating the json
            json.put("isSinglePlayer", String.valueOf(this.isSinglePlayer));
            json.put("numOfPlayers", String.valueOf(this.numOfPlayers));
            json.put("numOfMaxPlayers", String.valueOf(this.numOfMaxPlayers));
            json.put("playerOne", playerOne.getPlayerDataAsJson());
            json.put("playerTwo", playerTwo.getPlayerDataAsJson());
            json.put("round", String.valueOf(this.round));
            json.put("turn", String.valueOf(this.turn));
            
            // Return json
            return json.toJSONString();
        }
	
        
     
     public char ComputerMakeAThrow(){
   
       char CPU = 0;
       Random ran = new Random();
       
       int random = ran.nextInt(3);
       
       if(random == 0){CPU = 'R';}
       if(random == 1){CPU = 'P';}
       if(random == 2){CPU = 'S';}
   
   return CPU;
   } 
    
     public void rockButtonPushed(char CPU){
        
        
    if(CPU == 'R')
    {
       
      playerTies++;CPUTies++;
     this.  playerOne.setTiesThisGame(playerTies);
      this. playerTwo.setTiesThisGame(CPUTies);
     
    }
     
    if(CPU == 'P')
    {
        CPUWins++;
        playerlosses++;
       this. playerOne.setWinsThisGame(playerlosses);
       this. playerTwo.setLossesThisGame(CPUWins);
        
        
         
    }
    if(CPU == 'S')
    {
         playerWins++;
         CPULosses++;
       this.  playerOne.setWinsThisGame(playerWins);
      this.   playerTwo.setLossesThisGame(CPULosses);
    
    }
         
   
     
     
     }
}
