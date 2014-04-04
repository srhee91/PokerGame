package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
		new SearcherListening().start();
		
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
	
}



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
	public SearchThread(int i){
		this.i=i;
	}
	public void run(){
		if (HostSearcher.IP4[i]!=null) return;
		byte IP[]=new byte[4];
		IP[0]=(byte)HostSearcher.IP1;
		IP[1]=(byte)HostSearcher.IP2;
		IP[2]=(byte)HostSearcher.IP3;
		IP[3]=(byte)i;
		
		DatagramSocket socket=null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		DatagramPacket packet=null;
		try {
			packet=new DatagramPacket(IP,1,InetAddress.getByAddress(IP), HostSearcher.port);
			socket.send(packet);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

class SearcherListening extends Thread{
	DatagramSocket socket;
	boolean enable = true;
	public void run(){
		try {
			socket=new DatagramSocket(HostSearcher.port-1);
		} catch (SocketException e1) {
			e1.printStackTrace();
			return;
		}
		while(enable){
			byte[] recvBuf = new byte[15];
	        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
			try {
				socket.setSoTimeout(2000);
				socket.receive(recvPacket);
				if (enable==false) return;
				InetAddress IP=recvPacket.getAddress();
				String hostname=new String(recvPacket.getData());
				hostname=hostname.substring(0, hostname.indexOf("!|!"));
				System.out.println("Receive from IP "+IP.getHostAddress()+ " with name: "+ hostname);
				HostSearcher.IP4[IP.getAddress()[3]]=hostname;
				System.out.println(hostname.length());
			} catch (IOException e) {
			}
			
		}
	}
}
