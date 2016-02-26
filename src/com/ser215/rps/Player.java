package com.ser215.rps;

// Player data goes here

// Imports
import org.json.simple.JSONObject;
import com.google.gson.Gson;

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
        
        public void setPlayerDataFromJson(JSONObject json) {
            
            // Now add all the data from the JSON
            this.setPlayerUsername(json.get("playerUsername").toString());
            this.setPlayerId(json.get("playerId").toString());
            this.setWinsTotal(Integer.parseInt(json.get("winsTotal").toString()));
            this.setTiesTotal(Integer.parseInt(json.get("tiesTotal").toString()));
            this.setLossesTotal(Integer.parseInt(json.get("lossesTotal").toString()));
            this.setWinsThisGame(Integer.parseInt(json.get("winsThisGame").toString()));
            this.setTiesThisGame(Integer.parseInt(json.get("tiesThisGame").toString()));
            this.setLossesThisGame(Integer.parseInt(json.get("lossesThisGame").toString()));
            this.setCanMakeAThrow(Boolean.parseBoolean(json.get("canMakeAThrow").toString()));
            
            for (int i = 0; i < 3; i++){
                this.setRPSUses(i, Boolean.getBoolean(json.get("rpsUses" + i).toString()));
            }
            
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
	
        public String getPlayerDataAsJson() {
            
            // Make json
            JSONObject json = new JSONObject();
            
            // Now add all the dat to the JSON
            json.put("playerUsername", this.getPlayerUsername());
            json.put("playerId", this.getPlayerId());
            json.put("winsTotal", String.valueOf(this.getWinsTotal()));
            json.put("tiesTotal", String.valueOf(this.getTiesTotal()));
            json.put("lossesTotal", String.valueOf(this.getLossesTotal()));
            json.put("winsThisGame", String.valueOf(this.getWinsThisGame()));
            json.put("tiesThisGame", String.valueOf(this.getTiesThisGame()));
            json.put("lossesThisGame", String.valueOf(this.getLossesThisGame()));
            json.put("canMakeAThrow", String.valueOf(this.getCanMakeAThrow()));
            
            for (int i = 0; i < 3; i++){
                json.put("rpsUses" + i, String.valueOf(this.getRPSUses(i)));
            }
            
            // return json
            return json.toJSONString();
        }
}
