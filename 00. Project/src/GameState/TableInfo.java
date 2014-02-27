package GameState;

import Host.Pot;

public class TableInfo {

	int flops[];
	int flopState;
	PlayerInfo whoseTurn;
	PlayerInfo dealer;
	PlayerInfo bigBlinder;
	PlayerInfo smallBlinder;
	Pot potTotal;
	Pot potThisTurn;
	int highestBet;
	public int flopTurnRiverState;
	
	PlayerInfo isMe;
	
	public TableInfo()
	{
		
	}
}
