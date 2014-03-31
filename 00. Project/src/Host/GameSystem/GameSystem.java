package Host.GameSystem;

import GameState.Gamestate;


public class GameSystem{

	public Player player[];

	//**Instance variables that hold throughout the entire game;
	public int whoseTurn;		//player index
	public int dealer;			//player index
	public int bigBlinder;		//player index
	public int smallBlinder;	//player index

	//**Instance variables that reset every hand;
	public Card flops[];
	public int flopState;
	
	public Pot potTotal;
	public int highestBet;
	//public int highestBetter; its currently in Host.java
	
	public Deck deck;
	
	public int blind;
	public int leftover;
		
	//constructor
	//What to initialize:
	//1. players
	//2. table
	public GameSystem(int numPlayer){
		
		player = new Player[MAXPLAYER];
		for(int i=0; i<numPlayer; i++){
			player[i] = new Player();
		}
		for(int i=numPlayer; i<MAXPLAYER; i++){
			player[i] = null;
		}
		
		blind = INIT_BLIND;
		dealer = INIT_DEALER;
		leftover = 0;
	}
	
	//Hand
	//1. new deck
	//2. assign cards
	public void newHand(){
		deck = new Deck();
		deck.shuffle();
		
		for(int i=0; i<MAXPLAYER; i++){
			if(player[i] != null)
				player[i].dealHands(deck.drawHands());
		}
		
		flops = deck.drawFlops();
		potTotal = new Pot(leftover);

		whoseTurn = dealer;
		highestBet = blind;
		initBlinds();
	}
	
	public void updateRound(){
		potTotal.gatherPots(player, highestBet);
		
		highestBet = 0;
		whoseTurn = dealer;
	}
	public void updateHand(){
		//find winner
		int winner = (new Rank()).findWinner(flops, player);
		
		//pot to winner
		
		//update dealer
		dealer = nextPlayer(dealer);
	}

	
	
	public void initBlinds(){
		//initial small/big blinds
		//updates
		player[nextTurn()].bet(blind/2);
		player[nextTurn()].bet(blind);
	}
	
	public int nextTurn(){
		whoseTurn = nextPlayer(whoseTurn);
		return whoseTurn;
	}
	public int nextPlayer(int currentPlayer){
		int nextP = currentPlayer+1;
		if(nextP == MAXPLAYER)	nextP = 0;
		
		while(true){
			if(player[nextP] != null && !player[nextP].hasFolded && !player[nextP].isAllIn())	break;
	
			nextP++;
			if(nextP == MAXPLAYER)	nextP = 0;

			if(nextP == currentPlayer) return -1;
		}
		
		return nextP;
	}

	public int playerCount(){
		
		int count = 0;
		for(int i=0; i<MAXPLAYER; i++){
			if(player[i]!=null)	count++;
		}
		
		return count;
	}
	
	public Gamestate getGamestate(int me){
		Gamestate gamestate = new Gamestate();
		
		gamestate.me = me;
		gamestate.player = player;
		gamestate.whoseTurn = whoseTurn;
		gamestate.dealer = dealer;
		gamestate.bigBlinder = bigBlinder;
		gamestate.smallBlinder = smallBlinder;
		gamestate.flops = flops;
		gamestate.flopState = flopState;
		gamestate.potTotal = potTotal;
		gamestate.highestBet = highestBet;
		gamestate.leftover = leftover;
		
		return gamestate;
	}

	public static int INIT_CHIP = 1000;
	public static int MAXPLAYER = 8;
	public static int INIT_BLIND = 20;
	public static int INIT_DEALER = 0;
}
