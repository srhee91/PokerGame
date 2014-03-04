package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestNet {
	
	static HostMessageHandler host;
	
	public static void main(String args[]){
		
		
		System.out.println("Creating host...");
		host=new HostMessageHandler(4321);
		System.out.println("Finished...");
		
		
		
		System.out.println("Creating 20 clients...");
		for (int i=0;i<20;i++){
		InetAddress serverIP=null;
		try {
			serverIP=InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	
		
		ClientMessageHandler client=new ClientMessageHandler(serverIP,4321);
		System.out.println(" Client"+i+"connect to "+serverIP.getHostAddress());		
		}
		System.out.println("Finished...");
		
		
		System.out.println("Testing host sending message ...");
		host.sending();
		
		
		
		
/*		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.disconnect();*/
	}
	
	
	
}
