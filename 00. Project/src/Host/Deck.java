package Host;

import java.util.Random;




public class Deck {
	final int CARD_NUM = 52;
	Card[] deck = new Card[CARD_NUM];
	Card[] flop = new Card[5];
	Card[] burn = new Card[3];
	Card[] player;
	Random generator = new Random();
	private static int i=0,j=0,k=0,f=0;
	
	
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
	public void drawcardsToPlayer(int numberofplayer){
        player = new Card[2*numberofplayer]; 
		while(i<numberofplayer*2){
			player[i]=deck[j];
			j++;
			i++;
		}
		burnCard();
	}
	public void burnCard(){
		while(k<3){
			burn[k]=deck[j];
			flipCard(flop,deck,k);
			k++;
			j++;
		}
		
	}
	private void flipCard(Card[] flop1, Card[] deck1,int k){
		if(k==0){
			flop[f]=deck[++j];
			flop[++f]=deck[++j];
			flop[++f]=deck[++j];
		}
		else if(k==1){
			flop[++f]=deck[++j];
		}
		else if(k==2){
			flop[++f]=deck[++j];
		}
	}
	public int handcalcalculater(Card flop[], Card[] player){
		
		return 0;
	}
	public static void main(String[] args){
		Deck obj=new Deck();
		obj.shuffle();
		obj.drawcardsToPlayer(2);
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
	}
	
}

