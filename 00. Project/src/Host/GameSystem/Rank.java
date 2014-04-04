package Host.GameSystem;

import java.util.*;



//For debugging Goto Deck.java
public class Rank {
	public static Card[][] merge_arr;
	public static final int PLAYER_MAX=3;		//temporary exist for player number
	public static int final_rank = 0; 
	public static boolean Ace = false;
	public static boolean Ace_Pair;
	
	
	public Rank(){
		merge_arr=new Card[PLAYER_MAX][7];//7 = player cards (2) + flop cards (5)
	}
	
	public void merge(Card[]flop, Card[][]hand){		//merge player array and flop array. [0-1] will be player card [2-6] will be flop card (same with other player)
		for(int i=0;i<PLAYER_MAX;i++){
			if(hand[i]!=null){
				for(int j=0;j<2;j++){
					merge_arr[i][j]=hand[i][j];
				}
				for(int j=2,m=0;j<7;j++,m++){
					merge_arr[i][j]=flop[m];
				}
			}
			else{
				merge_arr[i]=null;
			}
		}
    }
	
	public void printMerge(){			//exist for debugging
		
		for(int i=0;i<PLAYER_MAX;i++){
			if(merge_arr[i]!=null){
				for(int j=0;j<7;j++){
					
					System.out.println("i:"+i+"j:"+j);
					System.out.println("merge_arr["+i+"]["+j+"]: "+merge_arr[i][j].toString());
				}
			}
			
		}
	}

	//split pot winner not handled yet.
	public boolean[] findWinner(Card[] flop, Card[][]hand){
		int rank[]=new int[8];
		boolean winner[]=new boolean[8];
		int max_rank=0;
		int max_rank_count=0;  //number of players who is max_rank
		merge(flop,hand);
		//printMerge();
		for(int i=0;i<PLAYER_MAX;i++){			//find players rank and store their rank to the rank array
			if(merge_arr[i]!=null){
				findBestHand(merge_arr[i]);
				rank[i]=final_rank;
			}
		}
		
		for(int i=0;i<7;i++){			//gets max_rank
			if(rank[i]>max_rank){
				max_rank=rank[i];
			}
		}
		
		for(int i=0;i<PLAYER_MAX;i++){
			if(rank[i]==max_rank){
				max_rank_count++;
				merge_arr[i]=null;
				winner[i]=false;
			}else{
				winner[i]=true;
			}
		}
		//printMerge();
		System.out.println(max_rank_count);
		if(max_rank_count>1){
			winner = compareHands(merge_arr,max_rank);
			for(int i=0;i<8;i++){
				System.out.println("compare "+ winner[i]);
			}
			return winner;
		}else{
			for(int i=0;i<8;i++){
				System.out.println("no compare " + winner[i]);
			}
			return winner;
		}
	}
	//compare hands between two players?
	public boolean[] compareHands(Card[][]merge_arr,int max_rank){
		boolean tie[]=new boolean[8];
		for(int i=0;i<8;i++){
			tie[i]=false;
		}
		if(max_rank==STRAIGHT_FLUSH){
			System.out.println("inside of SF");
			for(int i=0;i<2;i++){
				System.out.println("inside of i for loop");
				System.out.println(merge_arr[i]);
				if(merge_arr[i]!=null){
					System.out.println("inside of i for loop null");
					int[] rank1 = findBestHand(merge_arr[i]);
					for(int l=i+1;l<3;l++){
						System.out.println(merge_arr[l]);
						if(merge_arr[l]!=null){
							int[] rank2 = findBestHand(merge_arr[l]);
							System.out.println("rank1 :" + rank1[0] + "rank2 : " + rank2[0]);
							if(rank1[0]>rank2[0]){
								tie[i]=true;
							}else if(rank1[0]<rank2[0]){
								tie[l]=true;
							}else if(rank1[0]==rank2[0]){
								tie[i]=true;
								tie[l]=true;
							}
						}
					}
				}
			}
			return tie;
		}
		return tie;
	}
				//players have same rank
/*
		if(rank_1 > rank_2)	return p1;
		else if(rank_1 < rank_2)	return p2;
		else{
			//if they have same rank, compare 5 best cards 
			if(rank_1==ROYAL_STRAIGHT_FLUSH){
				//players have same rank
			}
			else if(rank_1==STRAIGHT_FLUSH){
				if(rank1[0]>rank2[0]){
					return p1;
				}else if(rank1[0]<rank2[0]){
					return p2;
				}else{
					//players have same rank
				}
			}
			else if(rank_1==FOURCARD){
				if(rank1[0]>rank2[0]){
					return p1;
				}
				else if(rank1[0]==rank2[0] && rank1[4]>rank2[4]){
					return p1;
				}
			}
			else if(rank_1==FULLHOUSE){
				if(rank1[0]>rank2[0]){
					return p1;
				}
				else if(rank1[0]==rank2[0] && rank1[3]>rank2[3]){
					return p1;
				}
				else{
					//players have same hand
				}
				
			}
			else if(rank_1==FLUSH){
				for(int i=0;i<5;i++){
					if(rank1[i]>rank2[i]){
						return p1;
					}
					else if(rank1[i]<rank2[i]){
						return p2;
					}
					else{
						if(i==4){
							//players have same hands.
						}
					}
				}
			}
			else if(rank_1==STRAIGHT){
				if(rank1[0]>rank2[0]){
					return p1;
				}
			}
			else if(rank_1==THREEPAIR){
				for(int i=0;i<=2;i++){
					if(rank1[i]>rank2[i]){
						return p1;
					}
					else if(rank1[i]<rank2[i]){
						return p2;
					}
					else{
						if(i==4){
							// player has same hand.
						}
					}
				}
			}
			else if(rank_1==TWOPAIR){
				for(int i=0;i<=4;i=i+2){
					if(rank1[i]>rank2[i]){
						return p1;
					}
					else if(rank1[i]<rank2[i]){
						return p2;
					}
					else{
						if(i==4){
							// player has same hand.
						}
					}
				}
			}
			else if(rank_1==ONEPAIR){
				if(rank1[0]>rank2[0]){
					return p1;
				}
				else if(rank1[0]==rank2[0]){
					for(int i=2;i<5;i++){
						if(rank1[i]>rank2[i]){
							return p1;
						}
						else if(rank1[i]<rank2[i]){
							return p2;
						}
					}
					//player have same hands.
				}
				return p2;
			}
			else if(rank_1==NOPAIR){
				for(int i=0; i<5; i++){
					if(rank1[i]>rank2[i]){
						return p1;
					}
					else if(rank1[i]<rank2[i]){
						return p2;
					}
				}
				//player have same hands.
			}
			return p2;
		}*/
		//return tie;
	//}
	
	//finds the best hands of the player
	public int[] findBestHand(Card[] cards){
		
		//combine cards
		
		//find the best hand
		int highHand[] = null;
		if(isRoyalStraightFlush(cards)!=null){
			System.out.println("RoyalStraightFlush");
			highHand = isRoyalStraightFlush(cards);
			final_rank = 9;
		}
		else if(isStraightFlush(cards)!=null){
			System.out.println("StraightFlush");
			highHand = isStraightFlush(cards);
			final_rank = 8;
		}
		else if(isFourCard(cards)!=null){
			System.out.println("FourCard");
			highHand = isFourCard(cards);
			final_rank = 7;
		}
		else if(isFullHouse(cards)!=null){
			System.out.println("FullHouse");
			highHand = isFullHouse(cards);
			final_rank = 6;
		}
		else if(isFlush(cards)!=null){
			System.out.println("Flush");
			highHand = isFlush(cards);
			final_rank = 5;
		}
		else if(isStraight(cards)!=null){
			System.out.println("Straight");
			highHand = isStraight(cards);
			final_rank = 4;
		}
		else if(isThreeOfKind(cards)!=null){
			System.out.println("ThreeOfKind");
			highHand = isThreeOfKind(cards);
			final_rank = 3;
		}
		else if(isTwoPair(cards)!=null){
			System.out.println("TwoPair");
			highHand = isTwoPair(cards);
			final_rank = 2;
		}
		else if(isOnePair(cards)!=null){
			System.out.println("OnePair");
			highHand = isOnePair(cards);
			final_rank = 1;
		}
		else{	//No Pair
			System.out.println("noPair");
			highHand=noPair(cards);
			final_rank = 0;
		}
		System.out.println(Arrays.toString(highHand));
		
		return highHand;
	}

	
	public int[] isRoyalStraightFlush(Card cards[]){
		int royal_flush_helper=0;
		int[]best_set=new int[5];
		int kind=isSameKind(cards);
		if(kind!=0){
			for(int i=0;i<7;i++){			//checks for royal_flush
				if(cards[i].getKind()==kind&&(cards[i].getNumber()==1
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
			int temp[]=new int[7];
			int temp_1[]=new int[7];
			int count;
			int best_set[]=new int[5];
			int kind=isSameKind(cards);
			if(kind!=0){
				for(int i=0; i<7; i++){
					if(cards[i].getKind()==kind){
						temp[i]=cards[i].getNumber();
					}
				}
				Arrays.sort(temp);
				for(int j=0,k=6;j<7;j++,k--){
					temp_1[k]=temp[j];
				}

				for(int i=2; i>=0;i--){
			        count=0;
			        int k=i;
			        best_set[count++] = temp[i+4];
			        for(int j=i+4;j>k;j--){
			            if (temp[j-1] + 1 == temp[j]) {
			                best_set[count++] = temp[j-1];
			            }else if (temp[j-1] == temp[j]) {
			            	k--;
			            }
			            if (k < 0) break;
			        }
			        if(count==5){
			        	return best_set;
			        }
			    }
		    }
		
		return null;
	}
	public int[] isFourCard(Card cards[]){
		 int temp[];
		 int best_set[]=new int[5];
		 int high_num;
		 int pop_num;
		 temp=sort_toIntArrayAce(cards);
		if(findPair(cards)>=4){
			pop_num=getMostPopularElement(temp);
			if(pop_num==14){
				Ace_Pair=true;
			}
			else{
				Ace_Pair=false;
			}
			for(int i=6;i>=0;i--){
				 if(temp[i]==1&&Ace_Pair!=true){
					 Ace = true;
				 }
			 }
			int i;
			for(i=6;temp[i]==pop_num;i--){}
				
			high_num=temp[i];
			
			if(high_num>pop_num && pop_num!=14){
				for(int j=0;j<4;j++){
					best_set[j]=pop_num;
				}
				if(Ace==false){
					best_set[4]=high_num;
				}
				else if(Ace==true){
					best_set[4]=1;
				}
			}else if(pop_num==14){
				for(int j=0;j<4;j++){
					best_set[j]=pop_num;
				}
				best_set[4]=high_num;
			}
			else{
				best_set[4]=high_num;
				for(int j=0;j<4;j++){
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
		int pop_num2=0,pop_num3=0,pop_num4=0;
		temp=sort_toIntArrayAce(cards);
		if(findPair(cards)==3){
			pop_num1=getMostPopularElement(temp);
			for(int i=6;i>0;i--){
				if((temp[i]==temp[i-1])&&(temp[i]!=pop_num1)){
					pop_num2=temp[i];
					break;
				}
			}
			for(int i=0;i<7;i++){
				if(temp[i]==1){
					pop_num3++;
				}
			}
			if(pop_num3==3&&pop_num2!=1){
				pop_num1=1;
			}
			if(pop_num2==1){
				Ace_Pair = true;
			}
			if(pop_num2!=0){
				if(pop_num1>pop_num2&&Ace_Pair==false){
					for(int j=0;j<3;j++){
						best_set[j]=pop_num1;
					}
					for(int j=3;j<5;j++){
						best_set[j]=pop_num2;
					}
				}else if(pop_num1<pop_num2&&Ace_Pair==false) {
					for(int j=0;j<3;j++){
						best_set[j]=pop_num1;
					}
					for(int j=3;j<5;j++){
						best_set[j]=pop_num2;
					}
				}
				else if(Ace_Pair==true){
					for(int j=0;j<3;j++){
						best_set[j]=1;
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
	public int[] isFlush(Card[]card){

			int kind = isSameKind(card);
			int valid;
			int best_set[]=new int[7];
			int temp[]= new int[5];
			if(kind>0&&kind<5){
				for(int i=0; i<7; i++){
					if(card[i].getKind()==kind){
						valid =card[i].getNumber();
						best_set[i]=valid;
					}
				}
				for(int i=0;i<5;i++){		//returns 14 to 1.
					if(best_set[i]==1){
						best_set[i]=14;
					}
				}
				Arrays.sort(best_set);
				for(int j=2,k=4;j<7;j++,k--){
					temp[k]=best_set[j];
				}

				return temp;
			}
			else{
				return null;
			}
		}
	
	
	public int isSameKind(Card[]card){
		
		int isSameKind=0;					
		int clover_num=0;				//CLOVER = 1;
		int heart_num=0;				//HEART = 2;
		int diamond_num=0;				//DIAMOND = 3;
		int spade_num=0;				//SPADE = 4;
		

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
		if(clover_num>=5){
			isSameKind=1;
		}else if(heart_num>=5){
			isSameKind=2;
		}else if(diamond_num>=5){
			isSameKind=3;
		}else if(spade_num>=5){
			isSameKind=4;
		}
		return isSameKind; 
	}
	
	public int[] isStraight(Card[]cards){		
	    int temp[];			//temporary array that will hold 1 set of 7 cards
	    int count=0;
	     int best_set[]=new int[5];
	   temp=sort_toIntArray(cards);
	   
	   int helper=0;
	   for(int i=0;i<7;i++){
		   if(temp[i]==1){
					helper = 1;		
			}
		   if(temp[i]==10 && helper==1){
					helper = 2;
			}
		   if(temp[i]==11 && helper==2){
				helper = 3;
		   }
		   if(temp[i]==12 && helper==3){
				helper = 4;
		   }
		   if(temp[i]==13 && helper==4){
			   helper = 5;
		   }
	   }
	   if(helper==5){
		   best_set[0]=14;
		   best_set[1]=13;
		   best_set[2]=12;
		   best_set[3]=11;
		   best_set[4]=10;
		   return best_set;
	   }
	   
	   
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
	        	return straight;
	        }
	    }
	    
	    return null;
}

	public int[] isThreeOfKind(Card cards[]){
		 int temp[];
		 int pop_num;
		 int cardcount = 0;
		 int best_set[]=new int[5];
		 temp=sort_toIntArrayAce(cards);
		 
		 Arrays.sort(temp);
		 pop_num=getMostPopularElement(temp);
		 
		if(findPair(cards)==3&&isFullHouse(cards)==null){
			for(int j=0;j<3;j++){
				best_set[j]=pop_num;
			}
			for(int i=6;i>=0;i--){			//
			    if(temp[i]!=pop_num){
			    	best_set[3]=temp[i];
			    	break;
			    }
			}
			for(int i=6;i>=0;i--){
				if(temp[i]!=pop_num&&temp[i]!=best_set[3]){
			    	best_set[4]=temp[i];
			    	break;
			    }
			}
			
				return best_set;
		}
		return null;
	}
	//debugged and fixed errors
	public int[] isTwoPair(Card cards[]){	

		int temp[];
		 int pop_num1;
		 int pop_num2=0;
		 int high_num=0;
		 int best_set1[]=new int[5];
		
		 temp=sort_toIntArrayAce(cards);
		
		 Arrays.sort(temp);
		 pop_num1=getMostPopularElement(temp);
	
		for(int i=5;i>=0;i--){
			if((temp[i]==temp[i+1])&&temp[i]!=pop_num1){
				pop_num2=temp[i];
				break;
			}
		}
		
		if(findPair(cards)==2&&pop_num2!=0){
			//setting higher pair out of two pairs
			//higher pair will be store as first two; then lower pair gets stored
			if(pop_num1>pop_num2){
				for(int j=0;j<2;j++){
					best_set1[j]=pop_num1;
				}
				for(int j=2;j<4;j++){
					best_set1[j]=pop_num2;
				}
			}
			else{
				for(int j=0;j<2;j++){
					best_set1[j]=pop_num2;
				}
				for(int j=2;j<4;j++){
					best_set1[j]=pop_num1;
				}		
			}
			for(int i=6;i>=0;i--){
			    if(temp[i]!=pop_num1 && temp[i]!=pop_num2){
			    	best_set1[4]=temp[i];
			    	break;
			    }
			}
			return best_set1;
		}
		else{
			return null;
		}
	}
	
	public int[] isOnePair(Card cards[]){

		int best_set[] = new int[5];
		int card[] = sort_toIntArrayAce(cards);
				
		boolean ch=false;
		for(int i=0; i<6; i++)
		{
			for(int j=i+1; j<7; j++)
			{
				if(card[i] == card[j])
				{
					best_set[0] = card[i];
					best_set[1] = best_set[0];
					card[i] = 0;
					card[j] = 0;
					ch = true;
					break;
				}
			}
			if(ch)	break;
		}
		if(!ch)	return null;

		int index = 2;
		for(int i=6; i>=0; i--)
		{
			int num = card[i];
			if(num!=0)	best_set[index++] = num;
			
			if(index == 5)	break;
		}
		return best_set;
	}

	public int[] noPair(Card cards[]){
		 if(findPair(cards)==0){
			 int temp[];
			 boolean test=false;
			 int best_set[]=new int[5];
			 temp=sort_toIntArrayAce(cards);
			 for(int i=6;i>=0;i--){
				 if(temp[i]==1){
					 test=true;
					 best_set[0]=temp[i];
					 break;
				 }
			 }
			 if(test == false){
				 for(int i=6,j=0;j<5;i--,j++){
					 best_set[j]=temp[i];
				 }
			 }
			 else{
				 for(int i=6,j=1;j<5;i--,j++){
					 best_set[j]=temp[i];
				 }
			 }
				 
			 return best_set;
		 }
		 else{
			 return null;
		 }
	}

	
	private static int findPair(Card[]cards){
		int temp[];
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
    
	private static int[] sort_toIntArrayAce(Card[]cards){
		int temp[]=new int[7];
		for(int i=0;i<7;i++){
	        temp[i]=cards[i].getNumber();
	        if(temp[i] == 1) temp[i] = 14;
	    }
	    Arrays.sort(temp);
	    return temp;
	}
    
	public static final int ROYAL_STRAIGHT_FLUSH = 9;
	public static final int STRAIGHT_FLUSH = 8;
	public static final int FOURCARD = 7;
	public static final int FULLHOUSE = 6;
	public static final int FLUSH = 5;
	public static final int STRAIGHT = 4;
	public static final int THREEPAIR = 3;
	public static final int TWOPAIR = 2;
	public static final int ONEPAIR = 1;
	public static final int NOPAIR = 0;
    
}
