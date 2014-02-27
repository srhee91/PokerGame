package Host;

import java.util.*;

//For debugging Goto Deck.java
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

	//finds the best hand out of all players
	public void findWinner(){
			
	}
	//compare hands between two players?
	public int compareHands(Card[]play){
		int rank=0;						//See the Rank.java
		return rank;
	}
	
	//finds the best hands of the player
	public void findBestHand(PlayerInfo player){
		
		//combine cards
		
		//find the best hand
		int highHand[];
		if(highHand = isRoyalStraightFlush(cards)){
			
		}
		else if(highHand = isStraightFlush(cards)){
			
		}
		else if(highHand = isFourCard(cards)){
			
		}
		else if(highHand = isFullHouse(cards)){
			
		}
		else if(highHand = isFlush(cards)){
			
		}
		else if(highHand = isStraight(cards)){
			
		}
		else if(highHand = isThreeOfKind(cards)){
			
		}
		else if(highHand = isTwoPair(cards)){
			
		}
		else if(highHand = isOnePair(cards)){
			
		}
		else{	//No Pair
			
		}
	}
	
	public int[] isRoyalStraightFlush(Card cards[]){
		return null;
	}
	public int[] isStraightFlush(Card cards[]){
		return null;
	}
	public int[] isFourCard(Card cards[]){
		return null;
	}
	public int[] isFullHouse(Card cards[]){
		return null;
	}
	
	public int isFlush(Card[]play){
		
		int isFlush=0;					
		int clover_num=0;				//CLOVER = 1;
		int heart_num=0;				//HEART = 2;
		int diamond_num=0;				//DIAMOND = 3;
		int spade_num=0;				//SPADE = 4;
		int royal_flush_helper=0;		//helper variable
		

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
		
		//for debugging goto Deck.java
		System.out.println("clover:"+clover_num+" "+"heart:"+heart_num+" "+"diamond:"+diamond_num+" "+"spade"+spade_num); 
		
		if(clover_num>=5){
			isFlush=1;
		}else if(heart_num>=5){
			isFlush=2;
		}else if(diamond_num>=5){
			isFlush=3;
		}else if(spade_num>=5){
			isFlush=4;
		}
		
		if(isFlush!=0){
				
			for(int i=0;i<7;i++){			//checks for royal_flush
				if(play[i].getKind()==isFlush&&(play[i].getNumber()==1
					||play[i].getNumber()==10||play[i].getNumber()==11
					||play[i].getNumber()==12||play[i].getNumber()==13)){
					royal_flush_helper++;
				}
			}
			
			if(royal_flush_helper==5){			//is royal flush
				isFlush=Rank.ROYAL_STRAIGHT_FLUSH;
			}
			
			if(isStraight(play)!=null){			//is straight flush
				isFlush=Rank.STRAIGHT_FLUSH;
			}
		}
		
		return isFlush; 
	}
	
	public int[] isStraight(Card[]play){
		 int isStraight=0;		
		    int temp[]=new int[7];			//temporary array that will hold 1 set of 7 cards
		    int count=0;
		        
		    for(int i=0;i<7;i++){
		        temp[i]=play[i].getNumber();
		    }
		    
		    Arrays.sort(temp); 			//sort
		 //  System.out.println(Arrays.toString(temp)); //for debugging

		    for(int i=2; i>=0;i--){
		        count=0;
		        int k=i;
		        int straight[] = new int[5];
		        straight[count++] = temp[i+4];
		        for(int j=i+4;j>k;j--){
		            if (temp[j-1] + 1 == temp[j]) {
		                straight[count++] = temp[j-1];
		            }else if (temp[j-1] == temp[j]) {
		            	k--;
		            }
		            
		            if (k < 0) break;
		        }
		        if(count==5){
		        	System.out.println(Arrays.toString(straight)); 
		        	return straight;
		        }
		    }
		    
		    return null;
	}

	public int[] isThreeOfKind(Card cards[]){
		return null;
	}
	public int[] isTwoPair(Card cards[]){
		return null;
	}
	public int[] isOnePair(Card cards[]){
		return null;
	}

	
	public int[] findPair(Card[]play){
		int temp[]=new int[]{1,1,1,1,1,1,3};
		int max_pair=0;
		int max_element=0;
		int high_num=0;
		int pair_helper=0;
		for(int i=0;i<7;i++){
	        //temp[i]=play[i].getNumber();
	    }
		
		Arrays.sort(temp); 
		for(int i=0;i<6;i++){
			if(temp[i]==temp[i+1]){
				int j=i;
				pair_helper=1;
				while(temp[j]==temp[j+1]){
					pair_helper++;
					j++;
					if(j==6){
						break;
					}
				}
				i=j;
				//System.out.println("helper:"+pair_helper);
				if(max_pair<pair_helper){
					max_pair=pair_helper;
				}
			}
			
		}
		if(max_pair==4){
			max_element=getMostPopularElement(temp);
			for(int i=6;i>=0;i--){
				if(temp[i]==max_element){
					
				}
			}
		}
		
		return null;
	}

    private static int getMostPopularElement(int[] a){

        int maxElementIndex = getArrayMaximumElementIndex(a); 
        System.out.println("maxElementIndex:"+maxElementIndex);
        int[] b= new int[a[maxElementIndex]+1];
        for(int i = 0; i<a.length;i++){
               ++b[a[i]];
        }
        return getArrayMaximumElementIndex(b);
    }

    private static int getArrayMaximumElementIndex(int[] a) {
        int maxElementIndex = 0;
        for(int i = 1;i<a.length;i++){
            if(a[i]>=a[maxElementIndex]){
                maxElementIndex = i;
            }
        }
        return maxElementIndex;
    }
	
}
