package Host;

import GameState.*;


public class Host
{
	public GameSystem game;
	
	public static void main()
	{
		Host host = new Host();
		
		host.setup();
		host.waitToStart();
		host.startGame();	
		//host.endGame();
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
		game = new GameSystem();
		
		//when the game starts
		game.setup();
		
		while(!game.onePlayerLeft()){
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
		celebrateWinner();
	}
}
