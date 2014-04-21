package Host.Test;

import java.util.Arrays;

import Host.GameSystem.Card;
import Host.GameSystem.Rank;

public class TestRank {
	public TestRank(){
	}
	
	// fullhouse check
	public static void fullhouse_check(Rank calrank){
		System.out.println("Testing Fullhouse");
		
		Card[]temp=new Card[7];
		for(int i=1;i<14;i++){			
			for(int j=1;j<14;j++){
				if(j!=i){
				temp[0]=new Card(1,i);
				temp[1]=new Card(2,i);
				temp[2]=new Card(3,i);
				temp[3]=new Card(4,j);
				temp[4]=new Card(1,j);
				temp[5]=new Card(1,0);
				temp[6]=new Card(1,0);
				
				calrank.findBestHand(temp);
				}
			}
		}

		
		
		//calrank.findWinner(flop, hand);
		
	}
	public static void fourcard_check(Rank calrank){
		System.out.println("Testing FourCard");
		Card[]temp=new Card[7];
		for(int i=1;i<14;i++){			
			for(int j=1;j<14;j++){
				if(j!=i){
				temp[0]=new Card(1,i);
				temp[1]=new Card(2,i);
				temp[2]=new Card(3,i);
				temp[3]=new Card(4,i);
				temp[4]=new Card(1,j);
				temp[5]=new Card(1,0);
				temp[6]=new Card(1,0);
				calrank.findBestHand(temp);
				}
			}
		}

		
		
		//calrank.findWinner(flop, hand);
	}
	
	// straighflush check
	public static void straightflush_check(Rank calrank){
		System.out.println("Testing StraightFlush");
		Card[]temp=new Card[7];
		for(int kind=1;kind<5;kind++){
			for(int i=1;i<10;i++){
				temp[0]=new Card(kind,i+4);
				temp[1]=new Card(kind,i+3);
				temp[2]=new Card(kind,i+2);
				temp[3]=new Card(kind,i+1);
				temp[4]=new Card(0,1);
				temp[5]=new Card(kind,i);
				temp[6]=new Card(0,1);
				calrank.findBestHand(temp);
			}
		}

		
		
		//calrank.findWinner(flop, hand);
	}
	// flush check
	public static void flush_check(Rank calrank){
		System.out.println("Testing Flush");
		Card[]flop=new Card[5];
		flop[0]=new Card(1,1);
		flop[1]=new Card(3,6);
		flop[2]=new Card(1,7);
		flop[3]=new Card(2,10);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,6);
		hand[0][1]=new Card(1,9);
		hand[1]=null;
		hand[2][0]=new Card(1,2);
		hand[2][1]=new Card(1,4);
		hand[3][0]=new Card(2,2);
		hand[3][1]=new Card(2,5);
		hand[4][0]=new Card(4,11);
		hand[4][1]=new Card(3,5);
		hand[5]=null;
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	// straight check
	public static void straight_check(Rank calrank){
		System.out.println("Testing Straight");
		Card[]temp=new Card[7];
		for(int i=1;i<10;i++){
			temp[0]=new Card(1,i+4);
			temp[1]=new Card(3,i+3);
			temp[2]=new Card(1,i+2);
			temp[3]=new Card(3,i+1);
			temp[4]=new Card(0,1);
			temp[5]=new Card(1,i);
			temp[6]=new Card(0,1);
			calrank.findBestHand(temp);
		}

		
		
		//calrank.findWinner(flop, hand);
	}
	// three of kinds
	public static void threeofkind_check(Rank calrank){
		System.out.println("Testing Three of Kinds");
		Card[]temp=new Card[7];
		for(int i=1;i<14;i++){			
			for(int j=1;j<13;j++){
				if(j!=i){					//There are some case where it is four card. Usually the pair will be always x,x,x,21,20
				temp[0]=new Card(1,i);
				temp[1]=new Card(2,i);
				temp[2]=new Card(3,i);
				temp[3]=new Card(4,j);
				temp[4]=new Card(1,j+1);
				temp[5]=new Card(1,20);
				temp[6]=new Card(1,21);
				calrank.findBestHand(temp);
				}
			}
		}

		
		
		//calrank.findWinner(flop, hand);
	}
	// two pairs
	public static void twopair_check(Rank calrank){
		System.out.println("Testing Two Pair");
		Card[]temp=new Card[7];
		for(int i=1;i<14;i++){			
			for(int j=1;j<14;j++){
				for(int k=1;k<14;k++){
					if(j!=i&&k!=i&&k!=j){
					temp[0]=new Card(1,i);
					temp[1]=new Card(2,i);
					temp[2]=new Card(3,k);
					temp[3]=new Card(4,j);
					temp[4]=new Card(1,j);
					temp[5]=new Card(1,0);
					temp[6]=new Card(1,0);
					calrank.findBestHand(temp);
					}
				}
			}
		}

		
		
		//calrank.findWinner(flop, hand);
	}
	// one pair
	public static void onepair_check(Rank calrank){
		System.out.println("Testing OnePair");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,2);
		flop[1]=new Card(3,4);
		flop[2]=new Card(1,6);
		flop[3]=new Card(2,8);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,1);
		hand[0][1]=new Card(1,2);
		hand[1]=null;
		hand[2][0]=new Card(1,1);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,1);
		hand[3][1]=new Card(2,6);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,8);
		hand[5]=null;
		hand[6][0]=new Card(4,1);
		hand[6][1]=new Card(3,10);
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	// high card
	public static void highcard_check(Rank calrank){
		System.out.println("Testing HighCard");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,2);
		flop[1]=new Card(3,4);
		flop[2]=new Card(1,6);
		flop[3]=new Card(2,8);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,11);
		hand[0][1]=new Card(1,12);
		hand[1]=null;
		hand[2][0]=new Card(1,11);
		hand[2][1]=new Card(3,12);
		hand[3][0]=new Card(1,12);
		hand[3][1]=new Card(2,13);
		hand[4][0]=new Card(4,12);
		hand[4][1]=new Card(1,13);
		hand[5][0]=new Card(4,11);
		hand[5][1]=new Card(3,12);
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	public static void main(String[] args){
		Rank obj1=new Rank();
		System.out.println("ROYAL_STRAIGHT_FLUSH(9) STRAIGHT_FLUSH(8) FOURCARD(7) FULLHOUSE(6) FLUSH(5) STRAIGHT(4) THREEPAIR(3) TWOPAIR(2) ONEPAIR(1) NOPAIR(0)");
		straightflush_check(obj1);
		fullhouse_check(obj1);
		fourcard_check(obj1);
		straight_check(obj1);
		//flush_check(obj1);
		threeofkind_check(obj1);
		//twopair_check(obj1);
		//onepair_check(obj1);
		//highcard_check(obj1);
		
		
		
		
		
		

	}
}
