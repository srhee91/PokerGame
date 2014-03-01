package Host;

import java.util.Random;



public class Deck {
	final int CARD_NUM = 52;
	Card[] deck = new Card[CARD_NUM];
	Card[] burn = new Card[3];
	Random generator = new Random();
	public static Card[] flop = new Card[5];
	public static Card[] player;
	
	private static int deckcount=0,burncount=0,flopcount=0;
	
	
	public Deck(){					//initialize card 
		int i = 0;
		for(int j = 1; j < Card.KIND_MAX + 1; j++) {
			for(int k = 1; k < Card.NUM_MAX + 1; k++) {
				deck[i++] = new Card(j, k);
			}
		}
	}
	
	public void shuffle(){    
		int ran1, ran2;
		Card temp1;
        for (int i=0; i<1000; i++)
        {
            ran1 = generator.nextInt(deck.length-1);
            ran2 = generator.nextInt(deck.length-1);
            temp1 = deck[ran2];
            deck[ran2]=deck[ran1];
            deck[ran1]=temp1;
        }
	}
	
	public void drawcardsToPlayer(final int PLAYER_MAX){
		int i=0;
        player = new Card[2*PLAYER_MAX]; 
		while(i<PLAYER_MAX*2){
			player[i]=deck[deckcount];
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
		Deck obj=new Deck();
		obj.shuffle();
		obj.drawcardsToPlayer(3);
		CalculateRank obj1=new CalculateRank();
		obj1.merge();
		//obj1.printMerge();
		
		Card[]temp=new Card[7];
		temp[0]=new Card(1,8);
		temp[1]=new Card(1,10);
		temp[2]=new Card(1,11);
		temp[3]=new Card(1,12);
		temp[4]=new Card(1,13);
		temp[5]=new Card(1,7);
		temp[6]=new Card(1,9);
		for(int i=0;i<CalculateRank.PLAYER_MAX;i++){
			System.out.println("Flush Player "+(i+1)+": "+obj1.isFlush(temp));
			System.out.println("Straight Player "+(i+1)+": "+obj1.isStraight(CalculateRank.merge_arr[i]));
			obj1.findPair(temp);
		}
		
		/*
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
		}*/
		/*
		int j=1;
		for(int i=0;i<6;i+=2){
			System.out.println("player "+ j + " = " +Deck.player[i].toString());
			System.out.println("player "+ j + " = " +Deck.player[i+1].toString());
			j++;
		}*/
		
	}
}

