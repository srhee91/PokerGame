package Host.GameSystem;

import java.io.Serializable;

import Host.*;

public class Pot implements Serializable  {

	public Pot splitPot;
	public int totalPot;
	public boolean playerInvolved[];

	//Constructor
	public Pot() {
		totalPot = 0;
		splitPot = null;
		
		playerInvolved = new boolean[GameSystem.MAXPLAYER];
		for(int i=0; i< GameSystem.MAXPLAYER; i++){	playerInvolved[i] = true;	}
	}
	public Pot(int leftover) {
		totalPot = leftover;
		splitPot = null;
		
		playerInvolved = new boolean[GameSystem.MAXPLAYER];
		for(int i=0; i< GameSystem.MAXPLAYER; i++){	playerInvolved[i] = true;	}
	}
	
	//gathersPots at the end of each round
	//parameter player[] is the array of players. 
	public void gatherPots(Player player[], int highestBet) {
		
		if(splitPot != null)	splitPot.gatherPots(player, highestBet);
		
		else {
			
			//check for someone who folded/left OR didn't bet
			for(int i=0; i<player.length; i++) {
				if(player[i] != null){
				if(player[i].hasFolded || player[i].hasLeft || player[i].betAmount == 0) {
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
		
		Card hands[][] = new Card[GameSystem.MAXPLAYER][2];
		
		for(int i=0; i<GameSystem.MAXPLAYER; i++){
			if(playerInvolved[i])
				hands[i] = game.player[i].hand;
			else
				hands[i] = null;
		}
		
		//int[] winner = (new Rank()).findWinner(game.flops, hands);
		
		totalPot += game.leftover;
		/*
		for(int i=0; i<winner.length; i++)
		{
			game.player[winner[i]].totalChip += (int) (totalPot/winner.length);
		}
		game.leftover = totalPot%winner.length;*/
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
