package Host;

public class Card {
	private int kind;
	private int number;
	
	static final int KIND_MAX = 4;
	static final int NUM_MAX = 13;
	
	static final int SPADE = 4;
	static final int DIAMOND = 3;
	static final int HEART = 2;
	static final int CLOVER = 1;

	public Card(int kind, int number)
	{
		this.kind = kind;
		this.number = number;
	}
		
	public int getKind()
	{
		return kind;
	}
	
	public int getNumber()
	{
		return number;
	}
	public String toString()
	{
		String kind = "";
		String number = "";
		
		switch(this.kind) {
		case 1:
			kind = "CLOVER";
			break;
		case 2:
			kind = "HEART";
		case 3:
			kind = "DIAMOND";
			break;
		case 4:
			kind = "SPADE";
			break;
	}
		switch(this.number) {
		case 11:
			number = "J";
			break;
		case 12:
			number = "Q";
			break;
		case 13:
			number = "K";
			break;
		default:
			number = this.number + "";
		}
		return "kind:" +kind+ ", number:" +number;
	}
	
	
}
