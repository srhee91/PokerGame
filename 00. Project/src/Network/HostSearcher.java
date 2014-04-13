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
		InetAddress myIP;
		HostSearcher.port=port;
		try {
			myIP=InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("Cannot get local IP");
			return;
		}
		IP1=myIP.getAddress()[0];
		IP2=myIP.getAddress()[1];
		IP3=myIP.getAddress()[2];
		if(IP1<0) IP1+=256;
		if(IP2<0) IP2+=256;
		if(IP3<0) IP3+=256;
		
		for(int i=0;i<255;i++) IP4[i]=null;
		stop=false;
		checkAvailable();
		
		new SearcherListening().start();
	}
	
	public static void checkAvailable(){
		for (int i=0;i<255;i++){
			//if (IP4[i]!=null)
				new CheckThread(i).start();
		}
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

class CheckThread extends Thread{	// 255 of these; sends out IP address
	int i;
	public CheckThread(int i){
		this.i=i;
	}
	public void run(){
		byte IP[]=new byte[4];
		IP[0]=(byte)HostSearcher.IP1;
		IP[1]=(byte)HostSearcher.IP2;
		IP[2]=(byte)HostSearcher.IP3;
		IP[3]=(byte)i;
		
		DatagramSocket socket=null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		HostSearcher.IP4[i]=null;
		DatagramPacket packet=null;
		try {
			packet=new DatagramPacket(IP,1,InetAddress.getByAddress(IP), HostSearcher.port);
			socket.send(packet);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

class SearcherListening extends Thread{		// 1 of these; listens for replies from hostbroadcasters
	DatagramSocket socket;
	public void run(){
		try {
			socket=new DatagramSocket(HostSearcher.port-1);
		} catch (SocketException e1) {
			e1.printStackTrace();
			return;
		}
		while(!HostSearcher.stop){
			byte[] recvBuf = new byte[15];
	        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
			try {
				socket.setSoTimeout(1000);
				socket.receive(recvPacket);
				if (HostSearcher.stop==true) return;
				InetAddress IP=recvPacket.getAddress();
				String hostname=new String(recvPacket.getData()).substring(0, recvPacket.getLength());
				System.out.println("Receive from IP "+IP.getHostAddress()+ " with name: "+ hostname);
				HostSearcher.IP4[(int)(IP.getAddress()[3])]=hostname;
			} catch (IOException e) {
			}
			
		}
	}
}