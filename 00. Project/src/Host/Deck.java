package Host;

public class Deck {
	 public final static int kinds = 13;
	 public final static int SPADES = 0;
	 public final static int HEARTS = 1; 
	 public final static int CLOVER = 2;
	 public final static int DIAMOND = 3; 
	 private int suit=0;
	 private int value=0;

	 public Deck(int theValue, int theSuit) {
	      if (theSuit != SPADES && theSuit != HEARTS && theSuit != DIAMOND && theSuit != CLOVER)
	         throw new IllegalArgumentException("Illegal playing card suit");
	      if ((theValue < 1 || theValue > 13))
	         throw new IllegalArgumentException("Illegal playing card value");
	      value = theValue;
	      suit = theSuit;
	   }
}
