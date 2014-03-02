package GameState;

import Host.*;



public class PlayerInfo {

	public Card hands[];
	public int totalChip;
	public int betAmount;
	
	public boolean hasFolded, hasLeft;

	//Constructor
	public PlayerInfo() {
		totalChip = PokerInfo.INIT_CHIP;
		hasLeft = false;
		betAmount = 0;
	}
	
	//Checks if the player went all in.
	public boolean isAllIn() {
		if(totalChip == 0)	return true;
		return false;
	}	 
	
	//the player bets the betAmount.
	//**parameter betAmount is NOT Cumulative** 
	//If the bet is higher than players totalChip, return false;
	//If successfully bet, return true;
	public boolean bet(int betAmount) {
		if(betAmount > totalChip)	return false;
		
		this.betAmount += betAmount;
		totalChip -= betAmount;
		
		return true;
	}
	
	public void dealHands(Card hands[]){
		this.hands = hands;
		hasFolded = false;
	}
	
	//setter
	public void left()
	{	hasLeft = true;	}
	public void folded()
	{	hasFolded = true; }
}