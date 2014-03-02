package Host;

import GameState.Gamestate;

//library of game algorithm
public class GameSystem {
	
	public Gamestate state;
	
	//Constructor
	public GameSystem(){
	
		state = new Gamestate();
		
		
		
		
		Deck obj=new Deck();
		obj.shuffle();
		obj.drawcardsToPlayer(3);
		
		TableInfo table=new TableInfo(null);
		if(table.flopTurnRiverState==3){
			CalculateRank obj1=new CalculateRank();
			obj1.merge();
		}
		
	}
	
	public void setup()
	{
		//(optional) set games - initial chips for each player, initial blind, etc..
		//create GameSystem
		PokerInfo.INIT_CHIP = 1000;
		PokerInfo.MAXPLAYER = 8;
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
		gatherPots();
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
