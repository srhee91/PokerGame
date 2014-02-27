package Host;

import GameState.PlayerInfo;
//this is pot
public class Pot {

	public Pot insidePot;
	public int totalPot;
	public PlayerInfo playerInvolved[];

	//Constructor
	public Pot() {
		totalPot = 0;
		insidePot = null;
	}
	public Pot(int leftover) {
		totalPot = leftover;
		insidePot = null;
	}
	
	//gathersPots at the end of each round
	//parameter player[] is the array of players. 
	public void gatherPots(PlayerInfo player[]) {
		
		if(insidePot != null)	insidePot.gatherPots(player);
		
		else {
			for(int i=0; i<player.length; i++) {
				helloworld
			}
		}
	}
}
