package GameState;

//sends Gamestate from server to host while in the game
//*** One gamestate holds through the entire game ***//
public class Gamestate {
	
	public TableInfo tableInfo;
	public PlayerInfo playerInfo[];
	
	public int me;		//player index
	
	public Gamestate()
	{
		tableInfo = new TableInfo();
		playerInfo = new PlayerInfo[8];
		
	}
}
