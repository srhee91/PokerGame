package Host.GameSystem;

import java.util.Arrays;
import java.util.Random;



public class Deck {
	
	final int CARD_NUM = 52;
	Card[] deck = new Card[CARD_NUM];
	Card[] burn = new Card[3];
	Random generator = new Random();
	public static Card[] flop = new Card[5];
	public static Card[] player;
	public static Card[] player_1 = new Card[7];
	public static Card[] player_2 = new Card[7];
	public static Card[] player_3 = new Card[7];
	public static Card[] player_4 = new Card[7];
	public static Card[] player_5 = new Card[7];
	public static Card[] player_6 = new Card[7];
	public static Card[] player_7 = new Card[7];
	public static Card[] player_8 = new Card[7];
	
	
	private static int deckcount=0,burncount=0,flopcount=0;
	
	
	public Deck(){					//initialize card 
		int i = 0;
		for(int j = 1; j <= Card.KIND_MAX; j++) {
			for(int k = 1; k <= Card.NUM_MAX; k++) {
				deck[i++] = new Card(j, k);
			}
		}
	}
	
	public void shuffle(){    
		int ran1, ran2;
		Card temp1;
        for (int i=0; i<1000; i++)
        {
            ran1 = generator.nextInt(deck.length);
            ran2 = generator.nextInt(deck.length);
            temp1 = deck[ran2];
            deck[ran2]=deck[ran1];
            deck[ran1]=temp1;
        }
	}
	
	public Card[] drawHands(){
		Card[] hands = new Card[2];
		hands[0] = deck[deckcount++];
		hands[1] = deck[deckcount++];
		return hands;
	}
	
	public Card[] drawFlops(){
		for(int i=0; i<5; i++)
		{
			flop[i] = deck[deckcount++];
		}
		
		return flop;
	}
	
	public void drawcardsToPlayer(final int PLAYER_MAX){
		int i=0;
        player = new Card[2*PLAYER_MAX]; 
		while(i<PLAYER_MAX*2){
			player[i]=deck[deckcount];
			int j=0;
			if(i==2){
				player_1[j]=player[i-1];
				player_1[j+1]=player[i];
			}
			if(i==4){
				player_2[j]=player[i-1];
				player_2[j+1]=player[i];
			}
			if(i==6){
				player_3[j]=player[i-1];
				player_3[j+1]=player[i];
			}
			if(i==8){
				player_4[j]=player[i-1];
				player_4[j+1]=player[i];
			}
			if(i==10){
				player_5[j]=player[i-1];
				player_5[j+1]=player[i];
			}
			if(i==12){
				player_6[j]=player[i-1];
				player_6[j+1]=player[i];
			}
			if(i==14){
				player_7[j]=player[i-1];
				player_7[j+1]=player[i];
			}
			if(i==16){
				player_8[j]=player[i-1];
				player_8[j+1]=player[i];
			}
			deckcount++;
			i++;
		}
		burnCard();
	}
	
	public void burnCard(){
		while(burncount<3){
			burn[burncount]=deck[deckcount];
			flipCard(flop,deck,burncount);
			burncount++;
			deckcount++;
		}
	}
	
	private void flipCard(Card[] flop1, Card[] deck1,int burncount){
		if(burncount==0){
			flop[flopcount]=deck[++deckcount];
			flop[++flopcount]=deck[++deckcount];
			flop[++flopcount]=deck[++deckcount];
		}
		else if(burncount==1){
			flop[++flopcount]=deck[++deckcount];
		}
		else if(burncount==2){
			flop[++flopcount]=deck[++deckcount];
		}
	}

	public static void main(String[] args){
//<<<<<<< HEAD:00. Project/src/Host/Deck.java
		Deck obj=new Deck();
		obj.shuffle();
		obj.drawcardsToPlayer(8);
		Rank obj1=new Rank();
		obj1.merge();
		Rank calrank=new Rank();
		for(int i=0; i<5;i++){
			System.out.println(flop[i]);
			player_1[i+2]=flop[i];
			player_2[i+2]=flop[i];
			player_3[i+2]=flop[i];
			player_4[i+2]=flop[i];
			player_5[i+2]=flop[i];
			player_6[i+2]=flop[i];
			player_7[i+2]=flop[i];
			player_8[i+2]=flop[i];
		}
		for(int i=0;i<7;i++){
			System.out.println("Player 1" + player_1[i]);
		}
		for(int i=0;i<7;i++){
			System.out.println("Player 2" + player_2[i]);
		}
		for(int i=0;i<7;i++){
			System.out.println("Player 3" + player_3[i]);
		}
		for(int i=0;i<7;i++){
			System.out.println("Player 4" + player_4[i]);
		}
		calrank.findBestHand(player_1);
		calrank.findBestHand(player_2);
		calrank.findBestHand(player_3);
		calrank.findBestHand(player_4);
	
		//for(int i=0;i<16;i+=2){
			//System.out.println("player "+ j + " = " +Deck.player[i].toString());
			//System.out.println("player "+ j + " = " +Deck.player[i+1].toString());
			
			//calrank.findBestHand(Deck.player);
		//	j++;
	//	}
		/*
=======
		
		Deck obj=new Deck();
		obj.shuffle();
		
		Deck obj2 = new Deck();
		obj2.shuffle();
		
		for(int i=0; i<52; i++)
		{
			System.out.printf("%2d : " + obj.deck[i] + "\t\t " + obj2.deck[i] + "\n", (i+1));
		}
		
		obj.drawcardsToPlayer(3);
		Rank obj1=new Rank();
		obj1.merge();
		int test[];
		obj1.printMerge();
		
>>>>>>> FETCH_HEAD:00. Project/src/Host/GameSystem/Deck.java
		Card[]temp=new Card[7];
		temp[0]=new Card(1,1);
		temp[1]=new Card(2,1);
		temp[2]=new Card(3,1);
		temp[3]=new Card(4,3);
		temp[4]=new Card(1,9);
		temp[5]=new Card(1,2);
		temp[6]=new Card(1,10);
		for(int i=0;i<Rank.PLAYER_MAX;i++){
		
			
			obj1.findBestHand(Rank.merge_arr[i]);
		}
	
		
		for(int i=0;i<3;i++){
			 System.out.println("burn" + obj.burn[i].toString());
			}
		for(int i=0;i<5;i++){
			System.out.println("flop" + obj.flop[i].toString());
			}
		for(int i=0;i<2;i++){
			System.out.println("player "+ i + " = " + obj.player[i].toString());
			System.out.println("player "+ i + " = " + obj.player[i+2].toString());
		}
		for(int i=0;i<52;i++){
		 System.out.println(obj.deck[i].toString());
		}
				int j=1;
		for(int i=0;i<6;i+=2){
			System.out.println("player "+ j + " = " +Deck.player[i].toString());
			System.out.println("player "+ j + " = " +Deck.player[i+1].toString());
			j++;
		}*/
		
	}
}

