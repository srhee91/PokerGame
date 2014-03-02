package GameState;

import Host.*;


public class TableInfo {

	//**Instance variables that hold throughout the entire game;
	public int whoseTurn;		//player index
	public int dealer;			//player index
	public int bigBlinder;		//player index
	public int smallBlinder;	//player index

	//**Instance variables that reset every hand;
	public Card flops[];
	public int flopState;
	
	public Pot potTotal;
	public int highestBet;	
		
	//Constructor
	public TableInfo() {
		potTotal = new Pot();
	}

	//reset Every new hand
	public void reset(int leftover)
	{
		
	}
	
}
