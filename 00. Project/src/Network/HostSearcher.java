package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
	
	/*
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
	}*/
}



class SearchSuperThread extends Thread{
	public void run(){
		while(!HostSearcher.stop){
			for (int i=4;i<5;i++){
				new SearchThread(i).start();
			}
			break;
			/*try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
	}
}

class SearchThread extends Thread{
	int i;
	static Object mutex = new Object();
	public SearchThread(int i){
		this.i=i;
	}
	public void run(){
		if (HostSearcher.IP4[i]!=null) return;
		DatagramSocket socket=null;
		try {
			socket = new DatagramSocket(HostSearcher.port-1);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		DatagramPacket packet=null;
		try {
			packet=new DatagramPacket("0".getBytes(),1,InetAddress.getByName(""+HostSearcher.IP1+"."+HostSearcher.IP2+"."+HostSearcher.IP3+"."+i), HostSearcher.port);
			socket.send(packet);
			socket.setSoTimeout(5000);
			socket.receive(packet);
			System.out.println(new String(packet.getData()));
			/*socket.connect(new InetSocketAddress(InetAddress.getByName(""+HostSearcher.IP1+"."
					+HostSearcher.IP2+"."+HostSearcher.IP3+"."+i), HostSearcher.port), 10000);*/
			/*synchronized(mutex){
				System.out.println("Connecting successfully: 192.168.1."+i);
				ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
				LobbyState lobbyState=(LobbyState)ois.readObject();
				System.out.println("HostNickName: "+lobbyState.hostName);
				HostSearcher.IP4[i]=lobbyState.hostName;
			}*/
		} catch(SocketTimeoutException e){
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

