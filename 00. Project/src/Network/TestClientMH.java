package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestClientMH {
	
	static HostMessageHandler host;
	static int  NumClient=20;
	static ClientMessageHandler[] client=new ClientMessageHandler[NumClient];
	
	
	public static void main(String args[]){
		
		
		System.out.println("Creating host...");
		host=new HostMessageHandler(4321);
		System.out.println("Finished.\n");
		
		System.out.println("Creating "+NumClient+" clients...");	
		InetAddress serverIP=null;
		try {
			serverIP=InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}	
		for (int i=0;i<NumClient;i++){
		client[i]=new ClientMessageHandler(serverIP,4321);
		System.out.println(" Client"+(i+1)+"is connectting to "+serverIP.getHostAddress());		
		}
		System.out.println("Finished.\n");
		
		System.out.println("Testing: make one client send user action(sending message) for host to receive continuously...");
		System.out.println("Input client number(1 to 20) to log in:");
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
