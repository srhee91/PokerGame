package Host;

import java.util.*;

import GameState.PlayerInfo;

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
	public int compareHands(Card[]card){
		int rank=0;						//See the Rank.java
		return rank;
	}
	
	//finds the best hands of the player
	public void findBestHand(Card[] cards){
		
		//combine cards
		
		//find the best hand
		int highHand[] = null;
		
		if(isRoyalStraightFlush(cards)!=null){
			System.out.println("RoyalStraightFlush");
			highHand = isRoyalStraightFlush(cards);
		}
		else if(isStraightFlush(cards)!=null){
			System.out.println("StraightFlush");
			highHand = isStraightFlush(cards);
		}
		else if(isFourCard(cards)!=null){
			System.out.println("FourCard");
			highHand = isFourCard(cards);
		}
		else if(isFullHouse(cards)!=null){
			System.out.println("FullHouse");
			highHand = isFullHouse(cards);
		}
		else if(isFlush(cards)!=0){
			System.out.println("Flush");
			//highHand = isFlush(cards);
		}
		else if(isStraight(cards)!=null){
			System.out.println("Straight");
			highHand = isStraight(cards);
		}
		else if(isThreeOfKind(cards)!=null){
			System.out.println("ThreeOfKind");
			highHand = isThreeOfKind(cards);
		}
		else if(isTwoPair(cards)!=null){
			System.out.println("TwoPair");
			highHand = isTwoPair(cards);
		}
		else if(isOnePair(cards)!=null){
			System.out.println("OnePair");
			highHand = isOnePair(cards);
		}
		else{	//No Pair
			System.out.println("noPair");
			highHand=noPair(cards);
		}
		System.out.println(Arrays.toString(highHand));
	}
	public int[] isRoyalStraightFlush(Card cards[]){
		int royal_flush_helper=0;
		int[]best_set=new int[5];
		if(isFlush(cards)!=0){
			for(int i=0;i<7;i++){			//checks for royal_flush
				if(cards[i].getKind()==isFlush(cards)&&(cards[i].getNumber()==1
					||cards[i].getNumber()==10||cards[i].getNumber()==11
					||cards[i].getNumber()==12||cards[i].getNumber()==13)){
					best_set[royal_flush_helper++]=cards[i].getNumber();
					
				}
			}
				if(royal_flush_helper==5){
					return best_set;
				}else{
					return null;
				}
		}
		return null;
	}
	public int[] isStraightFlush(Card cards[]){
		if(isFlush(cards)!=0&&isStraight(cards)!=null){
			return isStraight(cards);
		}
		return null;
	}
	public int[] isFourCard(Card cards[]){
		 int temp[];
		 int best_set[]=new int[5];
		 int high_num;
		 int pop_num;
		 temp=sort_toIntArray(cards);
		 
		if(findPair(cards)==4){
			pop_num=getMostPopularElement(temp);
			
			int i;
			for(i=6;temp[i]==pop_num;i--){}
				
			high_num=temp[i];
			
			if(high_num>pop_num){
				for(int j=0;j<4;j++){
					best_set[j]=pop_num;
				}
				best_set[4]=high_num;
			}else{
				best_set[0]=high_num;
				for(int j=1;j<5;j++){
					best_set[j]=pop_num;
				}
			}
			return best_set;
		}
		return null;
	}
	public int[] isFullHouse(Card cards[]){
		int temp[];
		int best_set[]=new int[5];
		int pop_num1;
		int pop_num2=0;
		temp=sort_toIntArray(cards);
		
		if(findPair(cards)==3){
			pop_num1=getMostPopularElement(temp);
			for(int i=0;i<6;i++){
				if((temp[i]==temp[i+1])&&(temp[i]!=pop_num1)){
					pop_num2=temp[i];
					break;
				}
			}
			if(pop_num2!=0){
				if(pop_num1>pop_num2){
					for(int j=0;j<2;j++){
						best_set[j]=pop_num2;
					}
					for(int j=2;j<5;j++){
						best_set[j]=pop_num1;
					}
				}else{
					for(int j=0;j<3;j++){
						best_set[j]=pop_num1;
					}
					for(int j=3;j<5;j++){
						best_set[j]=pop_num2;
					}
				}
				return best_set;
			}
		}
		return null;
	}
	
	public int isFlush(Card[]card){
		
		int isFlush=0;					
		int clover_num=0;				//CLOVER = 1;
		int heart_num=0;				//HEART = 2;
		int diamond_num=0;				//DIAMOND = 3;
		int spade_num=0;				//SPADE = 4;
		int royal_flush_helper=0;		//helper variable
		

		for(int i=0;i<7;i++){
			switch(card[i].getKind()){
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
		//System.out.println("clover:"+clover_num+" "+"heart:"+heart_num+" "+"diamond:"+diamond_num+" "+"spade"+spade_num); 	
		if(clover_num>=5){
			isFlush=1;
		}else if(heart_num>=5){
			isFlush=2;
		}else if(diamond_num>=5){
			isFlush=3;
		}else if(spade_num>=5){
			isFlush=4;
		}
		return isFlush; 
	}
	
	public int[] isStraight(Card[]cards){
		 int isStraight=0;		
		    int temp[];			//temporary array that will hold 1 set of 7 cards
		    int count=0;
		        
		   temp=sort_toIntArray(cards);
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
		        	//System.out.println(Arrays.toString(straight)); 
		        	return straight;
		        }
		    }
		    
		    return null;
	}

	public int[] isThreeOfKind(Card cards[]){
		 int temp[];
		 int pop_num;
		 int high_num1;
		 int high_num2;
		 int best_set[]=new int[5];
		 temp=sort_toIntArray(cards);
		 pop_num=getMostPopularElement(temp);
		 
		if(findPair(cards)==3&&isFullHouse(cards)==null){
			int i;
			for(i=6;pop_num==temp[i];i--){}
				high_num1=temp[i];
				
			for(;pop_num==temp[i];i--){}
				high_num2=temp[i];
				
			for(int j=0;j<3;j++){
				best_set[j]=pop_num;
			}
				best_set[3]=high_num1;
				best_set[4]=high_num2;
				Arrays.sort(best_set);
				return best_set;
		}
		return null;
	}
	public int[] isTwoPair(Card cards[]){			//fail case for three pairs
		 int temp[];
		 int pop_num1;
		 int pop_num2=0;
		 int high_num=0;
		 int best_set[]=new int[5];
		 temp=sort_toIntArray(cards);
		 pop_num1=getMostPopularElement(temp);
		 
		if(findPair(cards)==2){
			for(int i=0;i<6;i++){
				if((temp[i]==temp[i+1])&&temp[i]!=pop_num1){
					pop_num2=temp[i];
					break;
				}
			}
			if(pop_num2!=0){
				int i;
				high_num=temp[6];
				for(i=6;(temp[i]==pop_num1||temp[i]==pop_num2);i--){
					high_num=temp[i];
				}
				
				for(int j=0;j<2;j++){
					best_set[j]=pop_num2;
				}
				for(int j=2;j<4;j++){
					best_set[j]=pop_num1;
				}
				best_set[4]=high_num;
				Arrays.sort(best_set);
				return best_set;
			}
		}
		return null;
	}
	public int[] isOnePair(Card cards[]){
		 int temp[];
		 int pop_num;
		 int high_num1=0;
		 int high_num2=0;
		 int high_num3=0;
		 int best_set[]=new int[5];
		 temp=sort_toIntArray(cards);
		 pop_num=getMostPopularElement(temp);
		if(findPair(cards)==2&&isTwoPair(cards)!=null){
			int i;
			for(i=6;temp[i]==pop_num;i--){}
			high_num1=temp[i];
			for(;temp[i]==pop_num;i--){}
			high_num2=temp[i];
			for(;temp[i]==pop_num;i--){}
			high_num3=temp[i];
			
			for(int j=0;j<2;j++){
				best_set[i]=pop_num;
			}
			best_set[2]=high_num1;
			best_set[3]=high_num2;
			best_set[4]=high_num3;
			Arrays.sort(best_set);
			return best_set;
		}
		return null;
	}
	public int[] noPair(Card cards[]){
		 int temp[];
		 int best_set[]=new int[5];
		 temp=sort_toIntArray(cards);
		 for(int i=6,j=0;j<5;i--,j++){
			 best_set[j]=temp[i];
		 }
		 Arrays.sort(best_set);
		 return best_set;
	}

	
	private static int findPair(Card[]cards){
		int temp[];//new int[]{1,1,6,7,7,7,7};
		int max_pair=0;
		int pair_helper=0;
	
		temp=sort_toIntArray(cards);
		
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
				if(max_pair<pair_helper){
					max_pair=pair_helper;
				}
			}	
		}
		return max_pair;
	}

    private static int getMostPopularElement(int[] a){

        int maxElementIndex = getArrayMaximumElementIndex(a); 
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
    private static int[] sort_toIntArray(Card[]cards){
    	int temp[]=new int[7];
    	for(int i=0;i<7;i++){
	        temp[i]=cards[i].getNumber();
	    }
	    Arrays.sort(temp);
	    return temp;
    }
	
}
