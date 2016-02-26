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
        private RPSGameServer rpsGameServer;                            // Holds a reference to the game server
        private boolean gameHasStarted;
        private int playerOneThrow, playerTwoThrow;                     // Holds the temp throw for this turn
        private String gameMessage = "";                                // Holds a message to pass to players
	
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
            this.isSinglePlayer = isSinglePlayer;         
	}
        
	public GameLogic(boolean isSinglePlayer, RPSGameServer rpsGameServer) {
            
            if (isSinglePlayer) {
		this.isSinglePlayer = isSinglePlayer;
                
                // Set player two as CPU
                this.playerTwo = new Player("CPU");
                
                this.numOfPlayers += 1;
            }
            
            this.playerOne = new Player();
            this.playerTwo = new Player();
            this.isSinglePlayer = isSinglePlayer;
            this.rpsGameServer = rpsGameServer;            
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
        
        // Get message
        public String getMessage() {
            return this.gameMessage;
        }
        
        // Starts a new round resets the turns and options uses
        public void newRound() {
            this.round += 1;
            this.turn = 0;
            
            newTurn();
            
            this.playerOne.setRPSUses(0, false);
            this.playerOne.setRPSUses(1, false);
            this.playerOne.setRPSUses(2, false);
            this.playerOne.setTiesThisGame(0);
            this.playerOne.setWinsThisGame(0);
            this.playerOne.setLossesThisGame(0);
            
            this.playerTwo.setRPSUses(0, false);
            this.playerTwo.setRPSUses(1, false);
            this.playerTwo.setRPSUses(2, false);
            this.playerTwo.setTiesThisGame(0);
            this.playerTwo.setWinsThisGame(0);
            this.playerTwo.setLossesThisGame(0);
            
        }
        
        // Starts new turn
        public void newTurn() {
            
            this.turn += 1;
            
            this.playerOneThrow = 9;
            this.playerTwoThrow = 9;
            this.playerOne.setCanMakeAThrow(true);
            this.playerTwo.setCanMakeAThrow(true);
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
                this.playerOneThrow = throwType;
                this.playerOne.setCanMakeAThrow(false);
            }
            else {
                // Set the throw to true
                this.playerTwo.setRPSUses(throwType, true);
                this.playerTwoThrow = throwType;
                this.playerTwo.setCanMakeAThrow(false);
            }
            
            // Check to see if both players made a turn yet.
            if (this.playerOneThrow == 9 || this.playerTwoThrow == 9){ // Waiting on someone to throw
                this.gameMessage = "Waiting for another player to throw....";
            }
            else { // Both made a throw so compare them
                
                // First check if the throws are the same
                if (this.playerOneThrow == this.playerTwoThrow) {
                    this.playerOne.setTiesThisGame(this.playerOne.getTiesThisGame() + 1);
                    this.playerTwo.setTiesThisGame(this.playerTwo.getTiesThisGame() + 1);
                    this.gameMessage = "This round is a tie.";
                }
                else { // Now check who wins
                    switch (this.playerOneThrow){
                        case 0: // Rock
                            if (this.playerTwoThrow == 1) { // Player 2 wins
                                this.playerOne.setLossesThisGame(this.playerOne.getLossesThisGame() + 1);
                                this.playerTwo.setWinsThisGame(this.playerTwo.getWinsThisGame() + 1);
                                this.gameMessage = this.playerTwo.getPlayerUsername() + " wins the turn.";
                            }
                            else if (this.playerTwoThrow == 2) { // Player 1 wins
                                this.playerOne.setWinsThisGame(this.playerOne.getWinsThisGame() + 1);
                                this.playerTwo.setLossesThisGame(this.playerTwo.getLossesThisGame() + 1);
                                this.gameMessage = this.playerOne.getPlayerUsername() + " wins the turn.";
                            }
                            break;
                        case 1: // Paper
                            if (this.playerTwoThrow == 2) { // Player 2 wins
                                this.playerOne.setLossesThisGame(this.playerOne.getLossesThisGame() + 1);
                                this.playerTwo.setWinsThisGame(this.playerTwo.getWinsThisGame() + 1);
                                this.gameMessage = this.playerTwo.getPlayerUsername() + " wins the turn.";
                            }
                            else if (this.playerTwoThrow == 0) { // Player 1 wins
                                this.playerOne.setWinsThisGame(this.playerOne.getWinsThisGame() + 1);
                                this.playerTwo.setLossesThisGame(this.playerTwo.getLossesThisGame() + 1);
                                this.gameMessage = this.playerOne.getPlayerUsername() + " wins the turn.";
                            }
                            break;
                        case 2: // Scissors
                            if (this.playerTwoThrow == 0) { // Player 2 wins
                                this.playerOne.setLossesThisGame(this.playerOne.getLossesThisGame() + 1);
                                this.playerTwo.setWinsThisGame(this.playerTwo.getWinsThisGame() + 1);
                                this.gameMessage = this.playerTwo.getPlayerUsername() + " wins the turn.";
                            }
                            else if (this.playerTwoThrow == 1) { // Player 1 wins
                                this.playerOne.setWinsThisGame(this.playerOne.getWinsThisGame() + 1);
                                this.playerTwo.setLossesThisGame(this.playerTwo.getLossesThisGame() + 1);
                                this.gameMessage = this.playerOne.getPlayerUsername() + " wins the turn.";
                            }
                            break;
                    }
                }
                
                // Now see what turn it is
                if (this.turn == 3){ // last turn so now decides who wins the round
                    
                    if ((this.playerOne.getWinsThisGame() - this.playerTwo.getWinsThisGame()) > 0){ // Player 1 has more wins
                        this.playerOne.setWinsTotal(this.playerOne.getWinsTotal() + 1);
                        this.playerTwo.setLossesTotal(this.playerTwo.getLossesTotal() + 1);
                        this.gameMessage = this.getMessage() + this.playerOne.getPlayerUsername() + " wins this round. Starting new round....";
                    }
                    else if ((this.playerOne.getWinsThisGame() - this.playerTwo.getWinsThisGame()) < 0){ // Player 2 has more wins
                        this.playerTwo.setWinsTotal(this.playerTwo.getWinsTotal() + 1);
                        this.playerOne.setLossesTotal(this.playerOne.getLossesTotal() + 1);
                        this.gameMessage = this.getMessage() + this.playerTwo.getPlayerUsername() + " wins this round. Starting new round....";
                    }
                    else { // Same amount so tie
                        this.playerTwo.setTiesTotal(this.playerTwo.getTiesTotal() + 1);
                        this.playerOne.setTiesTotal(this.playerOne.getTiesTotal() + 1);
                        this.gameMessage = this.getMessage() + "The round was a tie. Starting new round....";
                    }
                    this.newRound();
                }
                else {
                    this.newTurn();
                }
                    
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
        
        public static GameLogic setGameLogicFromJson(JSONObject json){
            Gson gson = new Gson();
            GameLogic tmpLogic = new GameLogic(Boolean.getBoolean(json.get("isSinglePlayer").toString()));
            
            // Now go through the task of creating the json
            tmpLogic.isSinglePlayer = Boolean.parseBoolean(json.get("isSinglePlayer").toString());
            tmpLogic.numOfMaxPlayers = Integer.parseInt(json.get("numOfMaxPlayers").toString());
            tmpLogic.numOfPlayers = Integer.parseInt(json.get("numOfPlayers").toString());
            tmpLogic.playerOne.setPlayerDataFromJson(new JSONObject(gson.fromJson(json.get("playerOne").toString(), JSONObject.class)));
            tmpLogic.playerTwo.setPlayerDataFromJson(new JSONObject(gson.fromJson(json.get("playerTwo").toString(), JSONObject.class)));
            tmpLogic.round = Integer.parseInt(json.get("round").toString());
            tmpLogic.turn = Integer.parseInt(json.get("turn").toString());
            tmpLogic.gameHasStarted = Boolean.parseBoolean(json.get("gameHasStarted").toString());
            tmpLogic.gameMessage = json.get("gameMessage").toString();

            return tmpLogic;
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
            json.put("gameHasStarted", String.valueOf(this.gameHasStarted));
            json.put("gameMessage", this.gameMessage);
            
            // Return json
            return json.toJSONString();
        }
        
        // Get if the game has started
        public boolean getHasGameStarted(){
            return this.gameHasStarted;
        }
        
        // Set game has started
        public void setGameHasStarted(boolean started){
            this.gameHasStarted = started;
        }
        
	// Remove the player from the game logic
        public void removePlayerFromGame(String playerId){
            // Which players is the player id associated
            if (this.playerOne.getPlayerId().equals(playerId)) { 
                this.playerOne = new Player();
                this.numOfPlayers -= 1;
            }
            else if (this.playerTwo.getPlayerId().equals(playerId)) {
                this.playerTwo = new Player();
                this.numOfPlayers -= 1;
            }
            else { // No one by that name
                this.rpsGameServer.getGameLog().printToLog("ERROR", "No one by the player id.");
            }
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
