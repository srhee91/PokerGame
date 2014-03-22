package Host;

public class TestRank {
	public TestRank(){
	}
	// fullhouse check
	public static void fullhouse_check(CalculateRank calrank){
		System.out.println("Testing Fullhouse");
		Card[]temp=new Card[7];
		temp[0]=new Card(1,1);
		temp[1]=new Card(2,1);
		temp[2]=new Card(3,1);
		temp[3]=new Card(4,3);
		temp[4]=new Card(1,9);
		temp[5]=new Card(1,3);
		temp[6]=new Card(1,10);
		calrank.findBestHand(temp);
	}
	public static void fourcard_check(CalculateRank calrank){
		System.out.println("Testing FourCard");
		Card[]temp=new Card[7];
		temp[0]=new Card(1,3);
		temp[1]=new Card(2,3);
		temp[2]=new Card(3,3);
		temp[3]=new Card(4,3);
		temp[4]=new Card(4,9);
		temp[5]=new Card(3,2);
		temp[6]=new Card(2,10);
		calrank.findBestHand(temp);
	}
	
	// straighflush check
	public static void straightflush_check(CalculateRank calrank){
		System.out.println("Testing StraightFlush");
		Card[]temp=new Card[7];
		temp[0]=new Card(1,2);
		temp[1]=new Card(1,3);
		temp[2]=new Card(1,4);
		temp[3]=new Card(2,3);
		temp[4]=new Card(3,9);
		temp[5]=new Card(1,5);
		temp[6]=new Card(1,6);
		calrank.findBestHand(temp);
	}
	// flush check
	public static void flush_check(CalculateRank calrank){
		System.out.println("Testing Flush");
		Card[]temp=new Card[7];
		temp[0]=new Card(1,2);
		temp[1]=new Card(1,3);
		temp[2]=new Card(1,8);
		temp[3]=new Card(2,3);
		temp[4]=new Card(3,9);
		temp[5]=new Card(1,5);
		temp[6]=new Card(1,6);
		calrank.findBestHand(temp);
	}
	// straight check
	public static void straight_check(CalculateRank calrank){
		System.out.println("Testing Straight");
		Card[]temp=new Card[7];
		temp[0]=new Card(1,8);
		temp[1]=new Card(1,3);
		temp[2]=new Card(4,7);
		temp[3]=new Card(2,3);
		temp[4]=new Card(3,9);
		temp[5]=new Card(3,5);
		temp[6]=new Card(1,6);
		calrank.findBestHand(temp);
	}
	// three of kinds
	public static void threeofkind_check(CalculateRank calrank){
		System.out.println("Testing Three of Kinds");
		Card[]temp=new Card[7];
		temp[0]=new Card(1,2);
		temp[1]=new Card(1,7);
		temp[2]=new Card(4,7);
		temp[3]=new Card(2,7);
		temp[4]=new Card(3,3);
		temp[5]=new Card(1,5);
		temp[6]=new Card(1,8);
		calrank.findBestHand(temp);
	}
	// two pairs
	public static void twopair_check(CalculateRank calrank){
		System.out.println("Testing Two Pair");
		Card[]temp=new Card[7];
		temp[0]=new Card(4,4);
		temp[1]=new Card(2,8);
		temp[2]=new Card(4,9);
		temp[3]=new Card(2,9);
		temp[4]=new Card(3,10);
		temp[5]=new Card(1,8);
		temp[6]=new Card(1,11);
		calrank.findBestHand(temp);
	}
	// one pair
	public static void onepair_check(CalculateRank calrank){
		System.out.println("Testing OnePair");
		Card[]temp=new Card[7];
		temp[0]=new Card(1,2);
		temp[1]=new Card(4,1);
		temp[2]=new Card(4,7);
		temp[3]=new Card(2,7);
		temp[4]=new Card(3,3);
		temp[5]=new Card(1,5);
		temp[6]=new Card(1,6);
		calrank.findBestHand(temp);
	}
	// high card
	public static void highcard_check(CalculateRank calrank){
		System.out.println("Testing HighCard");
		Card[]temp=new Card[7];
		temp[0]=new Card(1,2);
		temp[1]=new Card(4,1);
		temp[2]=new Card(4,12);
		temp[3]=new Card(2,11);
		temp[4]=new Card(3,9);
		temp[5]=new Card(1,5);
		temp[6]=new Card(1,6);
		calrank.findBestHand(temp);
	}
	public static void main(String[] args){
		CalculateRank obj1=new CalculateRank();
		fullhouse_check(obj1); // fullhouse
		fourcard_check(obj1); // four_card
		straightflush_check(obj1); // straightflush		
		flush_check(obj1); // flush
		straight_check(obj1); // straight
		threeofkind_check(obj1); // threeofKind
		twopair_check(obj1); // two pair
		onepair_check(obj1); // one pair
		highcard_check(obj1); // high card
		
	}
}
