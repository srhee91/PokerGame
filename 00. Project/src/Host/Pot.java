package Host;

import GameState.PlayerInfo;

public class Pot {

	public Pot splitPot;
	public int totalPot;
	public PlayerInfo playerInvolved[];

	//Constructor
	public Pot() {
		totalPot = 0;
		splitPot = null;
	}
	public Pot(int leftover) {
		totalPot = leftover;
		splitPot = null;
	}
	
	//gathersPots at the end of each round
	//parameter player[] is the array of players.
	public void gatherPots(PlayerInfo player[]) {
		
		if(splitPot != null)	splitPot.gatherPots(player);
		
		else {
			int lowestBet = 
			for(int i=0; i<player.length; i++) {
				helloworld
			}
		}
	}
}
