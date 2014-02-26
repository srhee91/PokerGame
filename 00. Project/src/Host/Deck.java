package Host;

import java.util.Random;




public class Deck {
	final int CARD_NUM = 52;
	Card[] deck = new Card[CARD_NUM];
	Random generator = new Random();
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
	        for (int i=0; i<52; i++)
	        {
	            ran1 = generator.nextInt(deck.length-1);
	            ran2 = generator.nextInt(deck.length-1);
	            temp1 = deck[ran2];
	            deck[ran2]=deck[ran1];
	            deck[ran1]=temp1;
	        }
	}
	public static void main(String[] args){
		Deck obj=new Deck();
		obj.shuffle();
		for(int i=0;i<52;i++){
		 System.out.println(obj.deck[i].toString());
		}
	}
	
}

