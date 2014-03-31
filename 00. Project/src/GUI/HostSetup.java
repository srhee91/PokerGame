package GUI;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Host.Host;
import Host.Invoker;
import Network.ClientMessageHandler;

public class HostSetup extends Thread {
	
	private String hostName;
	
	public HostSetup(String hostName) {
		this.hostName = hostName;
	}
	
	public void run() {
		
		// start host process by creating an invoker thread
		Invoker hostInvoker = new Invoker(Host.class);
		hostInvoker.start();
		
		
		// wait some time to allow host process to start
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		// connect to the newly-created host process
		InetAddress myIP = null;
		try {
			myIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		GUI.cmh = new ClientMessageHandler(myIP, 4321);
		
		
		// send the hostName to the newly-created host process
		try {
			GUI.cmh.send(hostName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		StartMode.hostSetupComplete_flag = true;
	}
}
