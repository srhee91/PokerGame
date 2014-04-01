package GuiActionThreads;

import java.io.IOException;
import java.net.InetAddress;

import GUI.GUI;
import Network.ClientMessageHandler;

public class JoinHostThread extends Thread {
	
	String hostIp;
	String playerName;
	
	public JoinHostThread(String hostIp, String playerName) {
		this.hostIp = hostIp;
		this.playerName = playerName;
	}
	
	@Override
	public void run() {
		
		GUI.joinMode.joinHostSuccess_flag = false;
		GUI.joinMode.joinHostError_flag = false;
		
		try {
			// connect to host, send playername
			GUI.cmh = new ClientMessageHandler(
					InetAddress.getByName(hostIp), 4321);
			GUI.cmh.send(playerName);
			GUI.joinMode.joinHostSuccess_flag = true;	
		} catch (IOException e) {
			// failed to join
			//e.printStackTrace();
			GUI.joinMode.joinHostError_flag = true;
		}	
	}
}
