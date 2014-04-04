package Network;

import java.io.Serializable;

public class UserAction implements Serializable{

	public enum Action{FOLD, CHECK_CALL, RAISE_BET, START_GAME };
	public Action action;
	public int raiseAmount;

	//private String msg;
	public UserAction(Action action, int raiseAmount){
		this.action = action;
		this.raiseAmount = raiseAmount;
	}
	
	
	public String toString(){
		StringBuffer s=new StringBuffer();
		s.append("User Action: send [ "+action.name()+" raise "+raiseAmount+" ] !");
		return s.toString();
	}
}

