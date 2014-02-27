package Host;

import GameState.PlayerInfo;

public class test {

	public static void main(String[] arg)
	{
		test t = new test();
		
		t.testPot();
	}
	
	public void testPot(){
		//initialization
		PlayerInfo player[] = new PlayerInfo[8];
		Card cards[] = new Card[2];
		Pot pot = new Pot();

		for(int i=0; i<PokerInfo.MAXPLAYER; i++)
		{
			player[i] = new PlayerInfo(cards);
		}
		
		//Test 1
		for(int i=0; i<PokerInfo.MAXPLAYER; i++)
		{
			player[i] = new PlayerInfo(cards);
			player[i].bet(1000);
			System.out.println("Player "+ i + " : " + player[i].totalChip + "chips and " + player[i].betAmount + "bet");
		}
		pot = new Pot();
		pot.gatherPots(player, 300);
		pot.printPot();
		
		//Test 2
		for(int i=0; i<PokerInfo.MAXPLAYER; i++)
		{
			player[i] = new PlayerInfo(cards);
			player[i].totalChip = 200;
			player[i].bet(200);
		}
		player[3].totalChip += 200;
		player[4].totalChip += 300;
		player[3].bet(200);
		player[4].bet(250);

		for(int i=0; i<PokerInfo.MAXPLAYER; i++)
		{
			System.out.println("Player "+ i + " : " + player[i].totalChip + "chips and " + player[i].betAmount + "bet");
		}
		pot = new Pot();
		pot.gatherPots(player, 500);
		pot.printPot();
	}
}
