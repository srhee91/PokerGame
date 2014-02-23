package GameState;

//sends Gamestate from server to host while in the game
public class Gamestate {
	public TableInfo tableInfo;
	public PlayerInfo playerInfo[];
	
	public Gamestate()
	{
		tableInfo = new TableInfo();
		playerInfo = new PlayerInfo[8];
		
	}
}
