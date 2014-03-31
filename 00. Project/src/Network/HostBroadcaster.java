package Network;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import GameState.LobbyState;
import sun.net.ConnectionResetException;


public class HostBroadcaster {
	ObjectOutputStream oos=null;
	Socket socket=null;
	ServerSocket server=null;
	int port;
	public LobbyState lobbyState;
	
	
	public HostBroadcaster(int port, String hostname){
		this.port=port;
		lobbyState=new LobbyState(hostname);
		try{
			server=new ServerSocket(port);
			System.out.println("Host Broadcaster is Listening on port ["+port+"] Waiting for client to connect...");
		}catch(IOException e){
			System.out.println("Cannot listen on port");
			System.exit(0);
		}
	}
	
	public class Listening extends Thread{
		public void run(){
			while(true){
				try{
					socket=server.accept();
					oos=new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(lobbyState);
					oos.flush();
					oos.close();
					socket.close();
				}catch(NullPointerException e){
					System.out.println("Cannot listen on port");
					break;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String args[]){
		HostBroadcaster hb=new HostBroadcaster(4320,"hostname");

		hb.new Listening().start();
	}
}
