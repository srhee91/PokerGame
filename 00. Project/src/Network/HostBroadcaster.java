package Network;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.corba.se.pept.transport.ListenerThread;

import GameState.LobbyState;
import sun.net.ConnectionResetException;


public class HostBroadcaster {
	ObjectOutputStream oos=null;
	DatagramSocket socket=null;
	//ServerSocket server=null;
	int port;
	//public LobbyState lobbyState;
	Listening listeningThread;
	String hostname;
	
	public HostBroadcaster(int port, String hostname){
		this.port=port;
		this.hostname=hostname;
		//lobbyState=new LobbyState(hostname);
		/*try{
			server=new ServerSocket(port);
			listeningThread = new Listening();
			listeningThread.start();
			System.out.println("Host Broadcaster is Listening on port ["+port+"] Waiting for client to connect...");
		}catch(IOException e){
			System.out.println("Cannot listen on port");
			System.exit(0);
		}*/
		try {
			socket=new DatagramSocket(4320);
		} catch (SocketException e) {
			e.printStackTrace();
		}
        listeningThread=new Listening(socket);
        listeningThread.start();
	}
	
	public void close() {
		if (listeningThread!=null) {
			listeningThread.enable = false;
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class Listening extends Thread{
		DatagramSocket socket;
		public Listening(DatagramSocket socket) {
			this.socket=socket;
		}
		boolean enable = true;
		public void run(){
			while(enable){
				byte[] recvBuf = new byte[100];
		        DatagramPacket recvPacket = new DatagramPacket(recvBuf , recvBuf.length);
				try {
					socket.receive(recvPacket);
					InetAddress IP=recvPacket.getAddress();
					System.out.println("Receive from IP "+IP.getHostAddress());
					recvPacket.setData(hostname.getBytes());
					socket.send(recvPacket);
				} catch (IOException e) {
				}
				
			}
		}
	}
	
	public static void main(String args[]){
		HostBroadcaster hb=new HostBroadcaster(4320,"hostname");
	}
}
