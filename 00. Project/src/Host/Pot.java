package Host;

import GameState.PlayerInfo;

public class Pot {

	public Pot splitPot;
	public int totalPot;
	public boolean playerInvolved[];

	//Constructor
	public Pot() {
		totalPot = 0;
		splitPot = null;
		
		playerInvolved = new boolean[PokerInfo.MAXPLAYER];
		for(int i=0; i< PokerInfo.MAXPLAYER; i++){	playerInvolved[i] = true;	}
	}
	public Pot(int leftover) {
		totalPot = leftover;
		splitPot = null;
		
		playerInvolved = new boolean[PokerInfo.MAXPLAYER];
		for(int i=0; i< PokerInfo.MAXPLAYER; i++){	playerInvolved[i] = true;	}
	}
	
	//gathersPots at the end of each round
	//parameter player[] is the array of players. 
	public void gatherPots(PlayerInfo player[], int highestBet) {
		
		if(splitPot != null)	splitPot.gatherPots(player);
		
		else {
			
			//check for someone who folded/left
			for(int i=0; i<player.length; i++) {
				if(player[i].hasFolded || player[i].hasLeft) {
					totalPot += player[i].betAmount;
					player[i].betAmount = 0;
					playerInvolved[i] = false;
				}
			}
			
			//check if someone went all in
			int lowestBet = PokerInfo.INIT_CHIP*8 + 1; 
			for(int i=0; i<player.length; i++)
			{
				if(player[i].betAmount > 0 && highestBet > player[i].betAmount){
					if(lowestBet > player[i].betAmount) lowestBet = player[i].betAmount;
				}
			}
			
			//if it gets to this point its normal pot (no all ins)
			//save the total pot
			for(int i=0; i<player.length; i++)
			{
				totalPot += player[i].betAmount;
				player[i].betAmount = 0;
			}
			
		}
	}
}
