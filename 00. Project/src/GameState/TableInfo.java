package GameState;

import Host.*;

public class TableInfo {

	Card flops[];
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
	
	public TableInfo(Card cards[])
	{
		this.flops = flops;
	}
}
