package Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import Network.ClientMessageHandler;
import Network.HostMessageHandler;

public class TestHostMH {
	
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
			System.out.println(" creating client "+(i+1)+" to connect "+serverIP.getHostAddress());		
			client[i]=new ClientMessageHandler(serverIP,4321);
		}
		System.out.println("Finished.\n");
		
		
		System.out.println("Testing: Make host send game state to one or all clients who receive it continously...");
		host.sending();
		
		
	}
}
