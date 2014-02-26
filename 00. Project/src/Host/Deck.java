package Host;




public class Deck {
	final int CARD_NUM = 52;
	Card[] deck = new Card[CARD_NUM];
		
	
	public Deck(){					//initialize card 
		int i = 0;
		
		for(int j = 1; j < Card.KIND_MAX + 1; j++) {
			for(int k = 1; k < Card.NUM_MAX + 1; k++) {
				deck[i++] = new Card(j, k);
				//System.out.println(deck[i].toString());
			}
		}
	}
	
}

