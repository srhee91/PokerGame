package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestClientMH {
	
	static HostMessageHandler host;
	static ClientMessageHandler[] client=new ClientMessageHandler[20];
	
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
		
		client[i]=new ClientMessageHandler(serverIP,4321);
		System.out.println(" Client"+(i+1)+"connect to "+serverIP.getHostAddress());		
		}
		System.out.println("Finished...");
		
		
		System.out.println("Testing a client sending message ...");
		System.out.println("Input client number first:");
		Scanner input=new Scanner(System.in);
		int ic=input.nextInt();
		client[ic-1].sending();   
		
/*		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client[ic-1].disconnect();*/
		

	}
	
	
	
	
	
}
