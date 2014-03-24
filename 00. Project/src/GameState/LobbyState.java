package GameState;

import java.io.Serializable;

public class LobbyState implements Serializable{
	public int numOfPlayer;
	public String nickNames[];
	
	public LobbyState(int numOfPlayer){
		this.numOfPlayer=numOfPlayer;
		nickNames=new String[8];
	}
	
	public LobbyState(String hostNickName){
		numOfPlayer=1;
		nickNames=new String[8];
		nickNames[0]=hostNickName;
	}
}
