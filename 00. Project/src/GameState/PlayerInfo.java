package GameState;

import Host.CalculateRank;
import Host.*;



public class PlayerInfo {

	public Card cards[];
	public int totalChip;
	public int betAmount;
	
	public boolean hasFolded, hasLeft;

	//Constructor - gets 2 cards and initialize chip amount
	public PlayerInfo(Card cards[]) {
		this.cards = cards;
		totalChip = PokerInfo.INIT_CHIP;
		betAmount = 0;
		hasFolded = false;
		hasLeft = false;
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
	
	//setter
	public void left()
	{	hasLeft = true;	}
	public void folded()
	{	hasFolded = true; }
}