package GameState;

import Host.CalculateRank;
import Host.*;



public class PlayerInfo {

	private Card cardsInHand[];
	private int chipAmount;
	private int pot;
	boolean cardsVisible;
	boolean betThisTurn;
	public PlayerInfo()
	{
		cardsInHand=new Card[2];
	}
	public void setCards(int playerID){
		
	}
	public void setChipAmount(int chip){
		chipAmount=chip;
	}
	public void setPot(int pot){
		this.pot=pot;
	}
	public Card getFirstCard(){
		return cardsInHand[0];
	}
	public Card getSecondCard(){
		return cardsInHand[1];
	}
	public int getChipAmount(){
		return chipAmount;
	}
	public int getPot(){
		return pot;
	}
	 
	
}
