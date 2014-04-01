package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import GameState.LobbyState;

public abstract class HostSearcher {
	
	static int IP1,IP2,IP3;
	static String IP4[]=new String[255];
	static volatile boolean stop = true;
	static int port;
	
	
	public static void start(int port){
		String myIP;
		HostSearcher.port=port;
		try {
			myIP=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Cannot get local IP");
			return;
		}
		IP1=Integer.parseInt(myIP.substring(0,myIP.indexOf('.')));
		myIP=myIP.substring(myIP.indexOf('.')+1);
		IP2=Integer.parseInt(myIP.substring(0,myIP.indexOf('.')));
		myIP=myIP.substring(myIP.indexOf('.')+1);
		IP3=Integer.parseInt(myIP.substring(0,myIP.indexOf('.')));
		
		for(int i=0;i<255;i++) IP4[i]=null;
		stop=false;
		
		new SearchSuperThread().start();
	}
	
	
	public static ArrayList<String[]> getValidNamesAndIps(){
		ArrayList<String[]> ret = new ArrayList<String[]>();
		String IPprefix=IP1+"."+IP2+"."+IP3+".";
		for(int i=0;i<255;i++){
			if(IP4[i]!=null) {
				String[] nameAndIp = new String[2];
				nameAndIp[0] = IP4[i];
				nameAndIp[1] = IPprefix+i;
				ret.add(nameAndIp);
			}
		}
		return ret;
	}
		
	public static boolean isRunning() {
		return !stop;
	}
	
	public static void stop(){
		stop=true;
	}
	
	public static void main(String args[]){
		start(4320);
	}
	
	static class SearchSuperThread extends Thread{
		public void run(){
			while(!stop){
				for (int i=1;i<255;i++){
					try {
						Socket socket = new Socket();
						socket.connect(new InetSocketAddress(InetAddress.getByName(""+IP1+"."+IP2+"."+IP3+"."+i), port),
								30);
						ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
						LobbyState lobbyState=(LobbyState)ois.readObject();
						System.out.println("HostNickName: "+lobbyState.hostName);
						IP4[i]=lobbyState.hostName;
						ois.close();
						socket.close();
						System.out.println("Connecting successfully: 192.168.1."+i);
					
					} catch (IOException e) {
						//e.printStackTrace();
						HostSearcher.IP4[i] = null;
					
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}




/*
class SearchSuperThread extends Thread{
	public void run(){
		while(!HostSearcher.stop){
			for (int i=1;i<255;i++){
				new SearchThread(i).start();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class SearchThread extends Thread{
	int i;
	static Object mutex;
	public SearchThread(int i){
		this.i=i;
	}
	public void run(){
		if (HostSearcher.IP4[i]!=null) return;
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(InetAddress.getByName(""+HostSearcher.IP1+"."+HostSearcher.IP2+"."+HostSearcher.IP3+"."+i), HostSearcher.port), 500);
			synchronized(mutex){
				System.out.println("Connecting successfully: 192.168.1."+i);
				ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
				LobbyState lobbyState=(LobbyState)ois.readObject();
				System.out.println("HostNickName: "+lobbyState.nickNames[0]);
				HostSearcher.IP4[i]=lobbyState.nickNames[0];
			}
		} catch( ClassNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
		}
	}
}*/

