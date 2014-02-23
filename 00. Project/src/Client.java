import GameState.*;

public class Client implements Runnable {

	public Client()
	{
		
	}
	
	public void run()
	{
		startGUI();
		joinGame();		
	}
	
	public void startGUI()
	{
		//start a GUI Thread
	}
	
	public void joinGame()
	{
		while(true)
		{
			waitToJoin();	//wait for the GUI to joinGame
			startGame();	//join the game and start
			waitToEnd();	//wait for the game to end
		}
	}
	
	public void waitToJoin()
	{
		
	}
	
	public void startGame()
	{
		//create a Thread of ClientMessageHandler
	}
	
	public void waitToEnd()
	{
		
	}
}