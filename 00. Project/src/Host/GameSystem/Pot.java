package Host.GameSystem;

import java.io.Serializable;

import Host.*;

public class Pot implements Serializable  {

	public Pot splitPot;
	public int totalPot;
	public boolean playerInvolved[];
	
	public boolean winner[];
	
	//special case
	public int winnerByFold;

	//Constructor
	public Pot() {
		totalPot = 0;
		splitPot = null;
		winnerByFold = -1;
		
		playerInvolved = new boolean[GameSystem.MAXPLAYER];
		for(int i=0; i< GameSystem.MAXPLAYER; i++)	playerInvolved[i] = true;
	}
	public Pot(int leftover) {
		totalPot = leftover;
		splitPot = null;
		winnerByFold = -1;
		
		playerInvolved = new boolean[GameSystem.MAXPLAYER];
		for(int i=0; i< GameSystem.MAXPLAYER; i++)	playerInvolved[i] = true;
	}
	
	//copy constructor
	public Pot(Pot pot) {
		totalPot = pot.totalPot;
		winnerByFold = pot.winnerByFold;
		playerInvolved = new boolean[pot.playerInvolved.length];
		for (int i=0; i<GameSystem.MAXPLAYER; i++)
			playerInvolved[i] = pot.playerInvolved[i];
		
		if(pot.winner!=null){
			for(int i=0; i<GameSystem.MAXPLAYER; i++)
				winner[i] = pot.winner[i];
		}
		
		if (pot.splitPot!=null)
			splitPot = new Pot(pot.splitPot);
		else
			splitPot = null;
	}
	
	public void fold(int playerIndex){
		if(splitPot != null)	splitPot.fold(playerIndex);
		
		playerInvolved[playerIndex] = false;
	}

	//gathersPots at the end of each round
	//parameter player[] is the array of players. 
	public void gatherPots(Player player[], int highestBet) {
		
		if(splitPot != null)	splitPot.gatherPots(player, highestBet);
		
		else {
			
			//check for someone who folded/left OR didn't bet
			for(int i=0; i<player.length; i++) {
				if(player[i] != null){
				if(player[i].hasFolded || player[i].betAmount < highestBet) {
					totalPot += player[i].betAmount;
					player[i].betAmount = 0;
					playerInvolved[i] = false;
				}}
				else{
					playerInvolved[i] = false;
				}
			}
			
			//check if someone went all in, and find the lowest all in
			int lowestBet = GameSystem.INIT_CHIP*8 + 1; 
			for(int i=0; i<player.length; i++)
			{
				if(player[i] != null){
				if(player[i].betAmount > 0 && highestBet > player[i].betAmount){
					if(lowestBet > player[i].betAmount) lowestBet = player[i].betAmount;
				}}
			}
			//if someone went all in.....
			if(lowestBet != (GameSystem.INIT_CHIP*8 + 1))
			{
				//subtract the all in amount and add it to this totalPot; Then, splitPot the rest;
				for(int i=0; i<player.length; i++)
				{
					if(player[i] != null){
					if(player[i].betAmount > 0){
						totalPot += lowestBet;
						player[i].betAmount -= lowestBet;
					}}
				}
				//splitPot the rest;
				splitPot = new Pot();
				splitPot.gatherPots(player, highestBet-lowestBet);
			}
			
			
			//if it gets to this point its normal pot (no all ins)
			//save the total pot
			for(int i=0; i<player.length; i++)
			{
				if(player[i] != null){
					totalPot += player[i].betAmount;
					player[i].betAmount = 0;
				}
			}
			
		}
	}

	public void potToWinner(GameSystem game)
	{
		if(splitPot != null)	splitPot.potToWinner(game);
		
		if(winnerByFold == -1) {
			Card hands[][] = new Card[GameSystem.MAXPLAYER][2];
			
			for(int i=0; i<GameSystem.MAXPLAYER; i++){
				if(playerInvolved[i])
					hands[i] = game.player[i].hand;
				else
					hands[i] = null;
			}
			
			winner = (new Rank()).findWinner(game.flops, hands);
			int winnerCount = 0;
			
			totalPot += game.leftover;
			
			for(int i=0; i<GameSystem.MAXPLAYER; i++){
				if(winner[i]) winnerCount++;
			}
			for(int i=0; i<GameSystem.MAXPLAYER; i++)
			{
				if(winner[i]){
					game.player[i].totalChip += (int) (totalPot/winnerCount);
				}
			}
			game.leftover = totalPot%winnerCount;
		}
		else {
			game.player[winnerByFold].totalChip += totalPot;
		}
	}
	
	public Pot getCurrentPot() {
		if(splitPot == null)	return this;
		else					return splitPot.getCurrentPot();
	}
	public void printPot()
	{
		if(splitPot != null)	splitPot.printPot();
		
		System.out.println("totalPot : " + totalPot);
		System.out.print("Splitted to : Player ");
		for(int i=0; i<GameSystem.MAXPLAYER; i++){
			if(playerInvolved[i])	System.out.print(i + " ");
		}
		System.out.println("\n");
	}
}
