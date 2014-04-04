package Host;

import java.util.*;

import GameState.*;
import Host.GameSystem.GameSystem;
import Network.*;


//Host will do the followings:
//1. create host network
//2. wait for it to start
//3. start the game
//   - has all the flows here
//   - uses methods in GameSystem
public class Host{
	
	// for communicating with hmh
	public boolean isWaiting = false;
	public String objSender;
	public Object objReceived;
	
	
	public HostBroadcaster hb;
	public HostMessageHandler hmh;
	
	public GameSystem game;
	
	public int playerCount;
	public String players[];
	
	
	public int port;
	
	public String hostname;
	
	public Host(int port){
		this.port=port;
		hmh = new HostMessageHandler(port, this, Thread.currentThread());
		
		players = new String[GameSystem.MAXPLAYER];
		playerCount = 0;
	}			
	
	public void waitForHostClientConnection() {
		// wait for connection from host client
		while (hmh.getConnectedPlayerNames().isEmpty());
		hostname = hmh.lastJoinedPlayer;
		System.out.println("Host client has joined: "+hostname);
	}
	
	public void createBroadcaster(){
		hb = new HostBroadcaster(port-1, hostname);
	}
	
	
	public void waitToStart(){
		//wait to be started by hostplayer
		
		// wait for start msg from host client
		while (true) {
			receiveObject();
			if (objSender.equals(hostname) &&
					objReceived instanceof String &&
					((String)objReceived).equals("start")) {
				break;
			}
		}
		
		hmh.sendAll("start");
	}
	
	

	public void receiveObject(){
		try {
			synchronized(this){
				isWaiting = true;
				this.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/************** Game Flow **************/
	//each game
	//1. players, table created
	//
	//each hand
	//1. new deck, new card
	//2. rank at the end
	//3. small/big blind
	//each round
	//1. pot
	//
	//each turn
	//1. turn change
	//2. player action
	public void startGame(){
	
		//each game
		game = new GameSystem(playerCount);
		
		//each hand
		while(game.playerCount() > 1){
			game.newHand();
			
			//each round
			for(int i=0; i<4; i++){
				game.flopState = i;
				game.highestBetter = game.nextTurn();
				
				//each turn
				do{
					sendGameState();
					UserAction ua = receiveUserAction();
					updateAction(ua);

				}while(game.nextTurn() != game.highestBetter);
				
				game.updateRound();
				game.potTotal.printPot();
			}
			game.updateHand();
		}
		//celebrate the winner
		//losers will just become a spectator without any notification
		//celebrateWinner();
	}
	
	
	
	
	
	public void sendGameState(){
		//send "game.getGamestate();"  <- this will be an object of "Gamestate"
		//to every player "game.player[]" (you have to check if player!=null)
		/*
		for(int i=0; i<GameSystem.MAXPLAYER; i++){
		
			if(game.player[i] != null){
				game.getGamestate(i);
				
			}
		
		}*/
		
		hmh.sendAll(game.getGamestate());	
	}
	
	

	public UserAction receiveUserAction() {
		String playerName = players[game.whoseTurn];
		while (true) {
			receiveObject();
			if (objSender.equals(playerName) &&
					objReceived instanceof UserAction) {
				break;
			}
		}
		UserAction ret = (UserAction)objReceived;
		System.out.println("action received: "+ret.action.name());
		return ret;
	}
	
	
	public void updateAction(UserAction ua){
		//bet/fold/call/raise...
//		if(action)?
//		game.player[game.whoseTurn].bet(betAmount - game.player[game.whoseTurn].betAmount);
//		game.highestBet = betAmount;
//		game.player[game.whoseTurn].fold();

		
		switch (ua.action) {
		case CHECK_CALL:
			game.player[game.whoseTurn].bet(ua.raiseAmount);
			break;
		case FOLD:
			game.player[game.whoseTurn].fold();
			break;
		case RAISE_BET:
			game.player[game.whoseTurn].bet(ua.raiseAmount);
			game.highestBetter = game.whoseTurn;
			game.highestBet = ua.raiseAmount;
			break;
		case START_GAME:
			break;
		}
	}
	
	
	//main method - a process created by Poker.java or GUI
	public static void main(String args[]){

		// start host, hostmessagehandler, hostbroadcaster
		Host host = new Host(4321);
		
		//host.hostname = "test_hostname";
		//host.createBroadcaster();
		
		// wait for connection from client who started this host
		host.waitForHostClientConnection();
		
		// start broadcaster
		host.createBroadcaster();
		
		// wait for start msg from host client
		host.waitToStart();
		
		
		host.hb.close();
		
		
		host.startGame();	
		//host.endGame();
		 
		
		
		
		/*
		while (true) {
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			host.hmh.removeDeadConnections();
			System.out.println("\n\nConnected players:");
			for (String s : host.hmh.getConnectedPlayerNames())
				System.out.println(s);
		}
		*/
		
		// wait for players to join, wait for start-game message from client 0
		
		
		
		// close broadcaster when game starts
		//host.hb.close();
		
		// close hostmessagehandler last
		//host.hmh.close();
		
		
		
	}	
}