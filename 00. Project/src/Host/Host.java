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
		
		//each hand
		while(game.playerCount() > 1){
			game.newHand();
			
			//each round
			for(int i=0; i<4; i++){
				game.flopState = i;
				
				//each turn
				while(game.nextTurn() != -1){
					sendGameState();
					updateAction();
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
		if(action)
		game.player[game.whoseTurn].bet(betAmount - game.player[game.whoseTurn].betAmount);
		game.highestBet = betAmount;
		game.player[game.whoseTurn].fold();
	}
	
	
	//main method - a process created by Poker.java or GUI
	public static void main(){

		Host host = new Host();
		
		host.createHost();
		host.waitToStart();
		host.startGame();	
		//host.endGame();
	}
}