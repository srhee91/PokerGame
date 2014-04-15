package Network;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class HostBroadcaster {
	int port;
	Listening listeningThread;
	BroadcastSuperThread broadcasting;
	String hostname;
	static volatile boolean stop = true;
	
	public boolean isRunning() {
		return !stop;
	}
	
	public HostBroadcaster(int port, String hostname){
		this.port=port;
		this.hostname=hostname;
		stop=false;
        listeningThread=new Listening();
        listeningThread.start();
        broadcasting=new BroadcastSuperThread(port-1);
        broadcasting.start();
	}
	
	public void close() {
		if (listeningThread!=null) {
			stop=true;
			try {
				listeningThread.socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class Listening extends Thread{
		DatagramSocket socket;
		boolean enable = true;
		public void run(){
			try {
				socket=new DatagramSocket(port);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			while(!HostBroadcaster.stop){
				byte[] recvBuf = new byte[5];
		        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
				try {
					socket.setSoTimeout(1000);
					socket.receive(recvPacket);
					if (HostBroadcaster.stop==true) return;
					InetAddress IP=recvPacket.getAddress();
					System.out.println("Receive from IP "+IP.getHostAddress());
					recvPacket.setPort(port-1);
					recvPacket.setData(hostname.getBytes());
					recvPacket.setLength(hostname.length());
					socket.send(recvPacket);
				} catch (IOException e) {
				}
			}
		}
	}
	
	class BroadcastSuperThread extends Thread{
		int port;
		public BroadcastSuperThread(int port){
			this.port=port;
		}
		public void run(){
			while(!HostBroadcaster.stop){
				for (byte i=1;i<255;i++){
					new BroadcastThread(i,port).start();
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class BroadcastThread extends Thread{
		byte IP[];
		int port;
		public BroadcastThread(byte i,int port){
			this.port=port;
			IP=new byte[4];
			InetAddress myIP;
			try {
				myIP=InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				System.out.println("Cannot get local IP");
				return;
			}
			IP[0]=myIP.getAddress()[0];
			IP[1]=myIP.getAddress()[1];
			IP[2]=myIP.getAddress()[2];
			IP[3]=i;
		}
		public void run(){
			DatagramSocket socket=null;
			try {
				socket = new DatagramSocket();
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			DatagramPacket packet=null;
			try {
				packet=new DatagramPacket(hostname.getBytes(),hostname.length(),InetAddress.getByAddress(IP), port);
				socket.send(packet);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]){
		HostBroadcaster hb=new HostBroadcaster(4320,"LHC");
	}
}
