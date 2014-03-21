package GameState;

import Host.*;
import Host.GameSystem.Card;
import Host.GameSystem.Pot;


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
	}

	//every new hand
	public void newFlops(Card flops[])
	{
		this.flops = flops;
		flopState = 0;
	}
	public void newPot(int leftover)
	{
		potTotal = new Pot(leftover);
	}
	public void nextDealer()
	{
		//change dealer, bigBlinder, smallBlinder
	}
	
	
	
}
