package Network;

import java.io.Serializable;

public class Gamestate implements Serializable{
	
	private String msg;
	private int index;
	public Gamestate(int index, String msg){
		this.msg=msg;
		this.index=index;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public int getIndex(){
		return index;
	}

	public void setMsg(String s){
		this.msg=s;
	}
	
	public void setIndex(int i){
		this.index=i;
	}
	
	public String toString(){
		StringBuffer s=new StringBuffer();
		s.append("\tYour client number is "+index+", Your game state is: ["+getMsg()+" ] !");
		return s.toString();
	}
}

