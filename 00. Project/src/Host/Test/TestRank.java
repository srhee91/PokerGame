package Host.Test;

import Host.GameSystem.Card;
import Host.GameSystem.Rank;

public class TestRank {
	public TestRank(){
	}
	
	// fullhouse check
	public static void fullhouse_check(Rank calrank){
		
		System.out.println("Testing Fullhouse");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,7);
		flop[1]=new Card(3,7);
		flop[2]=new Card(1,7);
		flop[3]=new Card(2,10);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,10);
		hand[0][1]=new Card(1,2);
		hand[1]=null;
		hand[2][0]=new Card(1,11);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,2);
		hand[3][1]=new Card(2,5);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,5);
		hand[5][0]=new Card(4,2);
		hand[5][1]=new Card(3,5);
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
		
	}
	public static void fourcard_check(Rank calrank){
		System.out.println("Testing FourCard");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,10);
		flop[1]=new Card(3,10);
		flop[2]=new Card(2,10);
		flop[3]=new Card(2,2);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,3);
		hand[0][1]=new Card(1,2);
		hand[1]=null;
		hand[2][0]=new Card(1,1);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,2);
		hand[3][1]=new Card(2,12);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,13);
		hand[5]=null;
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	
	// straighflush check
	public static void straightflush_check(Rank calrank){
		System.out.println("Testing StraightFlush");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,7);
		flop[1]=new Card(1,2);
		flop[2]=new Card(1,3);
		flop[3]=new Card(1,4);
		flop[4]=new Card(1,5);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,10);
		hand[0][1]=new Card(1,1);
		hand[1]=null;
		hand[2][0]=new Card(1,6);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,13);
		hand[3][1]=new Card(2,5);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,7);
		hand[5][0]=new Card(4,1);
		hand[5][1]=new Card(3,5);
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
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
		Card[]flop=new Card[5];
		flop[0]=new Card(4,7);
		flop[1]=new Card(3,7);
		flop[2]=new Card(1,7);
		flop[3]=new Card(2,10);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,10);
		hand[0][1]=new Card(1,2);
		hand[1]=null;
		hand[2][0]=new Card(1,1);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,2);
		hand[3][1]=new Card(2,5);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,5);
		hand[5][0]=new Card(4,1);
		hand[5][1]=new Card(3,5);
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	// three of kinds
	public static void threeofkind_check(Rank calrank){
		System.out.println("Testing Three of Kinds");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,7);
		flop[1]=new Card(3,7);
		flop[2]=new Card(1,7);
		flop[3]=new Card(2,10);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,10);
		hand[0][1]=new Card(1,2);
		hand[1]=null;
		hand[2][0]=new Card(1,1);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,2);
		hand[3][1]=new Card(2,5);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,5);
		hand[5][0]=new Card(4,1);
		hand[5][1]=new Card(3,5);
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	// two pairs
	public static void twopair_check(Rank calrank){
		System.out.println("Testing Two Pair");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,7);
		flop[1]=new Card(3,7);
		flop[2]=new Card(1,7);
		flop[3]=new Card(2,10);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,10);
		hand[0][1]=new Card(1,2);
		hand[1]=null;
		hand[2][0]=new Card(1,1);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,2);
		hand[3][1]=new Card(2,5);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,5);
		hand[5][0]=new Card(4,1);
		hand[5][1]=new Card(3,5);
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	// one pair
	public static void onepair_check(Rank calrank){
		System.out.println("Testing OnePair");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,7);
		flop[1]=new Card(3,7);
		flop[2]=new Card(1,7);
		flop[3]=new Card(2,10);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,10);
		hand[0][1]=new Card(1,2);
		hand[1]=null;
		hand[2][0]=new Card(1,1);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,2);
		hand[3][1]=new Card(2,5);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,5);
		hand[5][0]=new Card(4,7);
		hand[5][1]=new Card(3,5);
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	// high card
	public static void highcard_check(Rank calrank){
		System.out.println("Testing HighCard");
		Card[]flop=new Card[5];
		flop[0]=new Card(4,7);
		flop[1]=new Card(3,7);
		flop[2]=new Card(1,7);
		flop[3]=new Card(2,10);
		flop[4]=new Card(1,10);
		Card[][]hand=new Card[8][2];
		hand[0][0]=new Card(1,10);
		hand[0][1]=new Card(1,2);
		hand[1]=null;
		hand[2][0]=new Card(1,1);
		hand[2][1]=new Card(3,4);
		hand[3][0]=new Card(1,2);
		hand[3][1]=new Card(2,5);
		hand[4][0]=new Card(4,1);
		hand[4][1]=new Card(1,5);
		hand[5][0]=new Card(4,1);
		hand[5][1]=new Card(3,5);
		hand[6]=null;
		hand[7]=null;
		
		
		calrank.findWinner(flop, hand);
	}
	public static void main(String[] args){
		Rank obj1=new Rank();
		highcard_check(obj1);
		fourcard_check(obj1);
		onepair_check(obj1);
		twopair_check(obj1);
		threeofkind_check(obj1);
		straight_check(obj1);
		flush_check(obj1);
		fullhouse_check(obj1);
		straightflush_check(obj1);

	}
}
