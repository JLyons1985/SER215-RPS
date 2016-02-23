package com.ser215.rps;

// Player data goes here

// Imports

public class Player {
	
	// Class Variables
	private String playerId;					// Holds the players unique id
	private String playerUsername;                                  // Holds the players display name
	private String playerIp;					// Holds the players ip to make sure it really is them
	private int tiesTotal;						// Holds the total ties this player has accumulated
	private int winsTotal;						// Holds the total wins this player has accumulated
	private int lossesTotal;					// Holds the total losses this player has accumulated
	private int tiesThisGame;					// Holds the total ties this player has accumulated this game
	private int winsThisGame;					// Holds the total wins this player has accumulated this game
	private int lossesThisGame;					// Holds the total losses this player has accumulated this game
        private boolean[] rpsUses = new boolean[3];                     // Holds the array for if the player used any options
        private boolean canMakeAThrow = true;                           // Can the playe make a throw or have they already gone.
	
	// Constructors
	public Player() {
		this.playerId = "";
		this.playerUsername = "";
		this.playerIp = "";
		this.tiesTotal = 0;
		this.winsTotal = 0;
		this.lossesTotal = 0;
		this.tiesThisGame = 0;
		this.winsThisGame = 0;
		this.lossesThisGame = 0;
                this.rpsUses[0] = false;
                this.rpsUses[1] = false;
                this.rpsUses[2] = false;
	}
	
	public Player (String username) {
		this.playerId = "";
		this.playerUsername = username;
		this.playerIp = "";
		this.tiesTotal = 0;
		this.winsTotal = 0;
		this.lossesTotal = 0;
		this.tiesThisGame = 0;
		this.winsThisGame = 0;
		this.lossesThisGame = 0;
                this.rpsUses[0] = false;
                this.rpsUses[1] = false;
                this.rpsUses[2] = false;
	}
	
	// Methods
	
	// Setters for the various attributes
	public void setPlayerId(String playerId){
		this.playerId = playerId;
	}
	
	public void setPlayerUsername(String username){
		this.playerUsername = username;
	}
	
	public void setPlayerIp(String playerIp){
		this.playerIp = playerIp;
	}
	
	public void setTiesTotal(int tiesTotal){
		this.tiesTotal = tiesTotal;
	}
	
	public void setWinsTotal(int winsTotal){
		this.winsTotal = winsTotal;
	}
	
	public void setLossesTotal(int lossesTotal){
		this.lossesTotal = lossesTotal;
	}
	
	public void setTiesThisGame(int tiesThisGame){
		this.tiesThisGame = tiesThisGame;
	}
	
	public void setWinsThisGame(int winsThisGame){
		this.winsThisGame = winsThisGame;
	}
	
	public void setLossesThisGame(int lossesThisGame){
		this.lossesThisGame = lossesThisGame;
	}
        
        public void setCanMakeAThrow(boolean canMakeAThrow){
		this.canMakeAThrow = canMakeAThrow;
	}
        
        public void setRPSUses(int rpsArraySlot, boolean used) {
            this.rpsUses[rpsArraySlot] = used;
        }
	
	// Getters for the various attributes
	public String getPlayerId() {
		return this.playerId;
	}
	
	public String getPlayerUsername() {
		return this.playerUsername;
	}
	
	public String getPlayerIp() {
		return this.playerIp;
	}
	
	public int getTiesTotal(){
		return this.tiesTotal;
	}
	
	public int getWinsTotal(){
		return this.winsTotal;
	}
	
	public int getLossesTotal(){
		return this.lossesTotal;
	}
	
	public int getTiesThisGame(){
		return this.tiesThisGame;
	}
	
	public int getWinsThisGame(){
		return this.winsThisGame;
	}
	
	public int getLossesThisGame(){
		return this.lossesThisGame;
	}
        
        public boolean getRPSUses(int rpsArraySlot) {
            return this.rpsUses[rpsArraySlot];
        }
        
        public boolean getCanMakeAThrow(){
		return this.canMakeAThrow;
	}
	
}
