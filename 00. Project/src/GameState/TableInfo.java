package GameState;

import Host.*;

public class TableInfo {

	Card flops[];
	int flopState;
	int whoseTurn;
	int dealer;
	int bigBlinder;
	int smallBlinder;
	Pot potTotal;
	Pot potThisTurn;
	int highestBet;
	public int flopTurnRiverState;
	
	PlayerInfo isMe;
	
	public TableInfo(Card flops[])
	{
		this.flops = flops;
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
	public void potTotal(){
		
	}
	public void whoseTurn(int player){
		player = whoseTurn;
	}
}
