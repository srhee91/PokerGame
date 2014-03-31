package Host;

import java.util.Scanner;

import sun.font.CreatedFontTracker;
import GameState.*;
import Host.GameSystem.GameSystem;
import Network.HostBroadcaster;
import Network.HostMessageHandler;
import Network.HostBroadcaster.Listening;
import Network.UserAction;


//Host will do the followings:
//1. create host network
//2. wait for it to start
//3. start the game
//   - has all the flows here
//   - uses methods in GameSystem
public class Host{
	
	public Object objReceived;
	public HostBroadcaster hb;
	public HostMessageHandler hmh;
	
	public GameSystem game;
	public int playerCount;
	public int port;
	
	public String hostname;
	
	public Host(int port, String hostname){
		this.port=port;
		this.hostname=hostname;
	}
			
	public void createHost(){
		HostBroadcaster hb=new HostBroadcaster(port-1,hostname);
		hb.lobbyState=new LobbyState("hostname");
		hb.new Listening().start();
		HostMessageHandler hmh = new HostMessageHandler(port, this, Thread.currentThread());
		
		
	}
	
	
	
	public void waitToStart(){
		//wait to be started by hostplayer
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
		
		//TEST TEST TEST// V_01
//		System.out.println("before the game start");
//		for(int i=0; i<GameSystem.MAXPLAYER; i++)
//			if(game.player[i] != null)	System.out.println("Player "+i+":\n" + game.player[i]);
//			else						System.out.println("Player "+i+" Does not exist.");
		//TEST TEST TEST// V_01
		
		//each hand
		while(game.playerCount() > 1){
			game.newHand();

			//TEST TEST TEST// V_01
//			System.out.println("\nwhen the hand is dealt");
//			for(int i=0; i<GameSystem.MAXPLAYER; i++)
//				if(game.player[i] != null)	System.out.println("Player "+i+":\n" + game.player[i]);
//				else						System.out.println("Player "+i+" Does not exist.");
//			System.out.println("Flops : ");
//			for(int i=0; i<5; i++)	System.out.println(game.flops[i]);
			//TEST TEST TEST// V_01
			
			//each round
			for(int i=0; i<4; i++){
				game.flopState = i;
				int highestBetter = game.nextTurn();
				
				//TEST TEST TEST// V_01
//				System.out.println("Flop state : " + game.flopState);
				//TEST TEST TEST// V_01

				//each turn
				do{
					sendGameState();
					receiveAction();
					updateAction();

					//TEST V_02!!//
					System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					
					for(int k=0; k<5; k++)	System.out.println(game.flops[k]);
					
					System.out.println();
					
					for(int k=0; k<GameSystem.MAXPLAYER; k++)
						if(game.player[k] != null)	System.out.println("Player "+k+":\n" + game.player[k]);

					System.out.println("It's player " + game.whoseTurn +"'s turn!");
					System.out.print("Fold=0 Call=1 Bet=2\n:");
					Scanner s = new Scanner(System.in);
					int input = s.nextInt();
					if(input == 0){
						game.player[game.whoseTurn].fold();
						
						//if every1 folds the hand ends
						//
					}
					else if(input == 1)	game.player[game.whoseTurn].bet(game.highestBet);
					else {
						System.out.print("How much you want to bet? :");
						input = s.nextInt();
						game.player[game.whoseTurn].bet(input);
						highestBetter = game.whoseTurn;
						game.highestBet = input;
					}
					//TEST V_02!!//
					
					//TEST TEST TEST// V_01
//					System.out.println("It's player " + game.whoseTurn +"'s turn!");
//					System.out.print("Fold=0 Call=1 Bet=2\n:");
//					Scanner s = new Scanner(System.in);
//					int input = s.nextInt();
//					if(input == 0)		game.player[game.whoseTurn].fold();
//					else if(input == 1)	game.player[game.whoseTurn].bet(game.highestBet);
//					else {
//						System.out.print("How much you want to bet? :");
//						input = s.nextInt();
//						game.player[game.whoseTurn].bet(input);
//						highestBetter = game.whoseTurn;
//						game.highestBet = input;
//					}
//					
//					for(int k=0; k<GameSystem.MAXPLAYER; k++)
//						if(game.player[k] != null)	System.out.println("Player "+k+":\n" + game.player[k]);
//						else						System.out.println("Player "+k+" Does not exist.");
					//TEST TEST TEST// V_01

				}while(game.nextTurn() != highestBetter);
				
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
		
		for(int i=0; i<GameSystem.MAXPLAYER; i++){
		
			if(game.player[i] != null){
				game.getGamestate(i);
				
			}
		
		}
		
	}
	
	
	
	public void receiveAction(){
		//request and receive user action from "game.whoseTurn"
		//user action can be the enum we made last sunday
		try {
			synchronized(this){
				this.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Host class receive"+(UserAction)objReceived);
	}
	
	
	
	
	public void updateAction(){
		//bet/fold/call/raise...
//		if(action)?
//		game.player[game.whoseTurn].bet(betAmount - game.player[game.whoseTurn].betAmount);
//		game.highestBet = betAmount;
//		game.player[game.whoseTurn].fold();
	}
	
	
	//main method - a process created by Poker.java or GUI
	public static void main(String args[]){

		// start host, hostmessagehandler, hostbroadcaster
		Host host = new Host(4321,"hostname");
		host.createHost();
		
		System.out.println("********waiting to receive hostname...");
		
		// wait for host player name 
		host.receiveAction();
		String hostName = (String)host.objReceived;
		System.out.println("********host name: "+hostName);
		
		
		
		/*
		Host host = new Host(4321);
		
		//TEST TEST TEST// V_01
		host.playerCount = 4;
		//TEST TEST TEST// V_01
		
		host.createHost();
		host.waitToStart();
		host.startGame();	
		//host.endGame();
		 * */
	}	
}