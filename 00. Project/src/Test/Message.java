package Test;
import java.io.Serializable;

public class Message implements Serializable{
	private String from,to,message;
	public Message(String from, String to, String message){
		this.from=from;
		this.to=to;
		this.message=message;
	}
	public String getFrom(){
		return from;
	}
	public String getTo(){
		return to;
	}
	public String getMessage(){
		return message;
	}
	public void setFrom(String s){
		this.from=s;
	}
	public void setTo(String s){
		this.to=s;
	}
	public void setMessage(String s){
		this.message=s;
	}
	public String toString(){
		StringBuffer s=new StringBuffer();
		s.append("From: "+getFrom());
		s.append("\tTo: "+getTo());
		s.append("\n");
		s.append(getMessage()+"\n\n");
		return s.toString();
	}
}
