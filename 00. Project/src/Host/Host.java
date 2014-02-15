package Host;

public class Host {
	public void init()
	{
		//make host
		
	}
	
	public void startNewGame()
	{
		
	}
	
	public void startNewTurn()
	{
		
	}
	
	public void sendGamestate()
	{
		
	}
	
	public void wait()
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
	
	public void run(){
		//before the game starts
		init();
		
		
		//when the game starts
		GameSet(); //not necessary
		
		while(!onePlayerLeft){
			startNewGame();
			
			for(int i=0; i<4; i++){
				startNewTurn();
				
				while(!turnEnds){
					sendGameState();
					wait();
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
