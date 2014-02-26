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
				//System.out.println(deck[i].toString());
			}
		}
	}
	public void shuffle(){    
		 int index_1, index_2;
		 Card temp1;
	        for (int i=0; i<1000; i++)
	        {
	            index_1 = generator.nextInt( deck.length );
	            index_2 = generator.nextInt( deck.length );
	            
	            temp1 = deck[index_2];
	            deck[index_2]=deck[index_1];
	            deck[index_1]=temp1;
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

