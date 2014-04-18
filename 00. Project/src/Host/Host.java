package Host;

import java.util.*;

import GameState.*;
import GameState.Gamestate;
import Host.GameSystem.GameSystem;
import Host.GameSystem.Pot;
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
			
			game.newHand();			// flopstate = 0
			
			game.showdown = false;
			
			
			//each round
			for(int i=0; i<4; i++){

				game.newRound();
				

				// send a pre-round extra gamestate:
				
				// triggers deal cards/flop cards revealed.  allows first player to have some time
				// before his turn starts
				int temp = game.whoseTurn;
				game.whoseTurn = -3;
				sendGameState();
				try {
					Thread.sleep(1600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game.whoseTurn = temp;
				
				
				
				
				// no turns happen this round if we're already in showdown
				if (game.showdown) {
					game.flopState++;
					continue;
				}
				
				
				//each turn
				do{

					sendGameState();
					UserAction ua = receiveUserAction();
					updateAction(ua);

				}while(game.nextTurn() != game.highestBetter && game.whoseTurn != -1
						&& game.potTotal.getCurrentPot().winnerByFold == -1);
				
				
				
				// send a post-round gamestate:
				// allows GUI to show last player action and update his bet for this round
				temp = game.whoseTurn;
				game.whoseTurn = -1;
				sendGameState();
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game.whoseTurn = temp;
				
				
				
				
				// send another post-round gamestate
				// causes GUI to show animation of bets being collected
				temp = game.whoseTurn;
				game.whoseTurn = -2;
				sendGameState();
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game.whoseTurn = temp;
				
				
				
				
				
				game.updateRound();		// flopstate++
				
				
				
				
						
				//testing
				game.potTotal.printPot();
				
				/*special case handling*/
				//if everyone folds
				if(game.potTotal.winnerByFold != -1)
					break;
				
				//if everyone went all in in current pot
				Pot currentPot = game.potTotal.getCurrentPot();
				int notAllIn = 0;
				for(int j=0; j<GameSystem.MAXPLAYER; j++)
					if(currentPot.playerInvolved[j] && !game.player[j].isAllIn())
						notAllIn++;
				
				if(notAllIn <= 1){
					game.showdown = true;
				}
			}
			
			

			
			
			
			//TODO need to figure out what to send, what the gui need to show winning hands
			//		examples : pot, showdown, ...
			game.updateHand();		// flopstate = 4
			//TODO send gamestate and receive if anyone left the game.
			
			
			
			
			// send a post-hand gamestate to reveal everyone's cards (if needed),
			// distribute each pot to its winners, and gather pot leftovers into the main pot
			int temp = game.whoseTurn;
			game.whoseTurn = -4;
			sendGameState();
			try {
				Thread.sleep(game.potTotal.countPots(1)*100 + 5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.whoseTurn = temp;
			
			
			
			game.nullLosers();
						
		}
		//celebrate the winner
		//losers will just become a spectator without any notification
		//celebrateWinner();
		System.out.println("----- THE END ------");
		//TODO what to do when the game ends?
		
		
		
		// trigges game-lost popup for everyone who lost/won
		int temp = game.whoseTurn;
		game.whoseTurn = -5;
		game.flopState = 0;
		sendGameState();
		try {
			Thread.sleep(game.potTotal.countPots(1)*100 + 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		game.whoseTurn = temp;

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
		Gamestate gameState = game.getGamestate();
		
		/*
		// DEBUG: print game state
		System.out.println("flopstate = "+gameState.flopState);
		for(int k=0; k<8; k++){
			if(gameState.player[k] != null){
				if(gameState.dealer == k)	System.out.println("***Dealer***");
				if(gameState.whoseTurn == k)	System.out.println("---Your Turn---");
				System.out.println("Player "+k+":\n" + gameState.player[k]);
			}
		}
		System.out.println("It's player " + gameState.whoseTurn +"'s turn!");
		// DONE printing game state
		*/
		System.out.println("\n\n\nSending flopstate = "+game.flopState+", whoseturn = "+game.whoseTurn
				+ "*****************************************************");
		System.out.println("highest bet: "+gameState.highestBet);
		
		// print out pot winnerbyFolds
		Pot pot = gameState.potTotal;
		for (int i=0; i<8; i++) {
			if (pot==null)
				break;
			System.out.println("Pot "+i+" winnerByFold = "+pot.winnerByFold);
			pot = pot.splitPot;
		}
		
		System.out.println("showdown = "+gameState.showdown);
		
		hmh.sendAll(gameState);	
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

		switch (ua.action) {
		
		case FOLD:
			game.player[game.whoseTurn].fold();
			game.potTotal.fold(game.whoseTurn);
			
			//Special 
			Pot currentPot = game.potTotal.getCurrentPot();
			int numPlaying = 0;
			for(int i=0; i<GameSystem.MAXPLAYER; i++){
				if(currentPot.playerInvolved[i]){
					numPlaying++;
					currentPot.winnerByFold = i;
				}
			}
			if(numPlaying > 1){
				currentPot.winnerByFold = -1;
			}
			
			break;
		
		case CHECK:
		case CALL:
			game.player[game.whoseTurn].bet(ua.raiseAmount);
			break;
		
		case BET:
		case RAISE:
		case ALL_IN:
			game.player[game.whoseTurn].bet(ua.raiseAmount);
			game.highestBetter = game.whoseTurn;
			// all-in from poor player could result in a "raise" of lower than highest bet
			if (ua.raiseAmount > game.highestBet)
				game.highestBet = ua.raiseAmount;
			break;
		default:
			break;
		}
		
		game.player[game.whoseTurn].latestAction = ua;
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
		host.hmh.gameStart();
		
		host.hb.close();
		
		
		host.startGame();	
		//host.endGame();
		 
		host.hmh.gameEnd();
		
		
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