package Network;

import java.io.Serializable;

public class UserAction implements Serializable{

	private String msg;
	public UserAction( String msg){
		this.msg=msg;
	}
	
	public String getMsg(){
		return msg;
	}

	public void setMsg(String s){
		this.msg=s;
	}
	
	public String toString(){
		StringBuffer s=new StringBuffer();
		s.append("User Action: send [ "+getMsg()+" ] !");
		return s.toString();
	}
}

