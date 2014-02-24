package Host;

import GameState.*;


public class Host
{
	public static void main()
	{
		/*setup();
		waitToStart();
		startGame();	
		endGame();*/
	}
		
	public void setup()
	{
		//creates HostMessageHandler
	}
	
	public void waitToStart()
	{
		//wait to be started by hostplayer
	}
	
	public void startGame()
	{		
		/*//when the game starts
		gameSet();
		
		while(!onePlayerLeft){
			startNewGame();
			
			for(int i=0; i<4; i++){
				startNewTurn();
				
				while(!turnEnds){
					sendGameState();
					waitForAction();
					updateUserActivity();
				}
				updateTurn();
			}
			updateGame();
		}
		//celebrate the winner
		//losers will just become a spectator without any notification
		celebrateWinner();*/
	}
	
	public void gameSet()
	{
		//(optional) set games - initial chips for each player, initial blind, etc..
		//create GameSystem
	}
	
	public void startNewGame()
	{
		
	}
	
	public void startNewTurn()
	{
		
	}
	
	public void sendGameState()
	{
		
	}
	
	public void waitForAction()
	{
		
	}
	
	public void updateUserActivity()
	{
		
	}
	
	public void updateTurn()
	{
		
	}
	
	public void updateGame()
	{
		
	}
	
	public void update()
	{
		
	}
	
	public void celebrateWinner()
	{
		
	}
}
