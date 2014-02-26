package GameState;

import Host.*;
import java.util.*;


public class CalculateRank {
	public static Card[][] merge_arr;
	public static final int PLAYER_MAX=3;		//temporary exist for player number
	
	public CalculateRank(){
		merge_arr=new Card[PLAYER_MAX][7];//7 = player cards (2) + flop cards (5)
	}
	
	public void merge(){		//merge player array and flop array. [0-1] will be player card [2-6] will be flop card (same with other player)
		int k=0;
		for(int i=0;i<PLAYER_MAX;i++){
			for(int j=0;j<2;j++,k++){
				merge_arr[i][j]=Deck.player[k];		
			}
			for(int j=2,m=0;j<7;j++,m++){
				merge_arr[i][j]=Deck.flop[m];
			}
		}
    }
	
	public void printMerge(){			//exist for debugging
		for(int i=0;i<PLAYER_MAX;i++){
			for(int j=0;j<7;j++){
				System.out.println("merge_arr["+i+"]["+j+"]: "+merge_arr[i][j].toString());
			}
		}
	}

	public void find_Match(){			//Will find the best Rank.
		
	}
	
	public int find_Rank(Card[]play){
		int rank=0;						//See the Rank.java
		return rank;
	}
	public int isFlush(Card[]play){
		
		int isFlush=0;					
		int clover_num=0;				//CLOVER = 1;
		int heart_num=0;				//HEART = 2;
		int diamond_num=0;				//DIAMOND = 3;
		int spade_num=0;				//SPADE = 4;
		
		
		for(int i=0;i<7;i++){
			switch(play[i].getKind()){
			case 1:	clover_num++;
					break;
			case 2:	heart_num++;
					break;
			case 3:	diamond_num++;
					break;
			case 4: spade_num++;
					break;
			}
		}
		//for debugging
		//System.out.println("clover:"+clover_num+" "+"heart:"+heart_num+" "+"diamond:"+diamond_num+" "+"spade"+spade_num); 
		
		if(clover_num>=5||heart_num>=5||diamond_num>=5||spade_num>=5){
			isFlush=1;
		}
		
		return isFlush; 
	}
	public int isStraight(Card[]play){
		int isStraight=0;
		int arr[]=new int[7];
		
		return isStraight;
	}
	
	

	
	

}
