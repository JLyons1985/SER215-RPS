package com.ser215.rps;

// Game logic for each game goes here

// Imports

public class GameLogic {

	// Class Variables
	private boolean isSinglePlayer;					// Is the game a single player game
        private int numOfPlayers = 0;                                   // Num of players in the game currently
        private int numOfMaxPlayers = 2;                                // Num of max players ithe game allows
        private Player playerOne = null, playerTwo = null;              // Reference to the players
        private int round = 0, turn = 0;                                // Rounds and turns
        private RPSClientApplication mainApp;                           // Holds reference to main app
	
	// Constructors
	public GameLogic(boolean isSinglePlayer) {
            
            if (isSinglePlayer) {
		this.isSinglePlayer = isSinglePlayer;
                
                // Set player two as CPU
                this.playerTwo = new Player("CPU");
                
                this.numOfPlayers += 1;
            }
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
                if (this.playerOne == null){
                    // No player one add here
                    setPlayerSlot(1, player);
                    this.numOfMaxPlayers += 1;
                }
                else {
                    // Player one taken add here
                    setPlayerSlot(2, player);
                    this.numOfMaxPlayers += 1;
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
        
        // Get round data
        public int getRound(){
            return this.round;
        }
        
        // Get rturn data
        public int getTurn(){
            return this.turn;
        }
	
}
