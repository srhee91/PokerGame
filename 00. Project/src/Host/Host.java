package Host;

import GameState.*;
import Host.GameSystem.GameSystem;


//Host will do the followings:
//1. create host network
//2. wait for it to start
//3. start the game
//   - has all the flows here
//   - uses methods in GameSystem
public class Host{
	
	public GameSystem game;
	public int playerCount;
			
	public void createHost(){
		//creates HostMessageHandler
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
		
		//TEST TEST TEST//
		System.out.println("before the game start");
		for(int i=0; i<GameSystem.MAXPLAYER; i++)
			if(game.player[i] != null)	System.out.println("Player "+i+":\n" + game.player[i]);
			else						System.out.println("Player "+i+" Does not exist.");
		//TEST TEST TEST//
		
		//each hand
		while(game.playerCount() > 1){
			game.newHand();

			//TEST TEST TEST//
			System.out.println("\nwhen the hand is dealt");
			for(int i=0; i<GameSystem.MAXPLAYER; i++)
				if(game.player[i] != null)	System.out.println("Player "+i+":\n" + game.player[i]);
				else						System.out.println("Player "+i+" Does not exist.");
			System.out.println("Flops : ");
			for(int i=0; i<5; i++)	System.out.println(game.flops[i]);
			//TEST TEST TEST//
			
			//each round
			for(int i=0; i<4; i++){
				game.flopState = i;
				
				//TEST TEST TEST//
				System.out.println("Flop state : " + game.flopState);
				//TEST TEST TEST//

				//each turn
				while(game.nextTurn() != -1){
					sendGameState();
					updateAction();
					
					//TEST TEST TEST//
					System.out.println("It's " + game.whoseTurn +"'s turn!");
					System.out.println("Fold=0 Call=1 Bet=2");
					Scanner s = new Scanner();
					//TEST TEST TEST//

				}
				
				game.updateRound();
			}
			game.updateHand();
		}
		//celebrate the winner
		//losers will just become a spectator without any notification
		//celebrateWinner();
	}
	
	public void sendGameState(){
		
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

		Host host = new Host();
		
		//TEST TEST TEST//
		host.playerCount = 4;
		//TEST TEST TEST//
		
		host.createHost();
		host.waitToStart();
		host.startGame();	
		//host.endGame();
	}
}