package GameState;

import Host.*;

public class TableInfo {

	Card flops[];
	int flopState;
	int whoseTurn;		//player index
	int dealer;			//player index
	int bigBlinder;		//player index
	int smallBlinder;	//player index
	Pot potTotal;
	//Pot potThisTurn;  Pot.java handles this
	public int highestBet;
	//public int currentBetAmount;	??
	public int flopTurnRiverState;
	
	public int me;		//player index
	
	public TableInfo(Card flops[])
	{
		this.flops = flops;
	}
	public TableInfo() {
		// TODO Auto-generated constructor stub
	}
	public void dealerPlayer(int player){
		player = dealer;
	}
	public void bigblindPlayer(int player){
		player = bigBlinder;
	}
	public void smallblindPlayer(int player){
		player = smallBlinder;
	}
	public void whoseTurn(int player){
		player = whoseTurn;
	}
}
