package Network;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import sun.awt.event.IgnorePaintEvent;

public class ClientMessageHandler {
	
	ObjectOutputStream oos=null;
	ObjectInputStream ois=null;
	Socket socket=null;
	
	/* 
	 * Constructor 
	 * Used to create ClientMessageHandler
	 * Implement socket 
	 * Connect to a specific port listened by HostMessageHandler
	 * Get a pair of streams in socket
	 **/
	public ClientMessageHandler(InetAddress IP,int port){
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(IP, port), 1000);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println("Cannot connect to server");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Cannot create object stream");
			e.printStackTrace();
			System.exit(0);
		}
		new ReceivingThread().start();
		
	}
	
	/*
	 * Call this function will
	 * send UserAction object 
	 * through oos stream to host message handler
	 * */
	public synchronized void send(UserAction ua){
		/*if (disconnect==true){
			System.out.println("Send Failed. Already disconnected!");
			return;
		}*/
		try{
			oos.writeObject(ua);
			oos.flush();
		}catch(IOException e){
			System.out.println("Cannot send object");
			e.printStackTrace();
		}
	}
	
	/*
	 * Call this function will make
	 * current socket close
	 * */
	public void disconnect(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Inner class thread
	 * continuously receive game state through socket stream
	 * show game state when there is one
	 * run when client message handler is created
	 * */
	class ReceivingThread extends Thread{
		public void run(){
			Gamestate gs;
			while(true){
				try{
					gs=(Gamestate)ois.readObject();
					System.out.println("Receive a game state from host\n\t: "+gs);
					Thread.sleep(20);
				}catch(IOException e){
					System.out.println("Session End");
					e.printStackTrace();
					break;
				}catch(ClassNotFoundException e){
					System.out.println("ClassNotFound");
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
	/*
	 * Get user input and call send(input);
	 * */
	public class SendThread extends Thread{
		UserAction ua;
		public void run(){
			Scanner input=new Scanner(System.in);
			System.out.println("Input your message continously:");
			while(true){
				String str=input.nextLine();
				ua = new UserAction(str);
				send(ua);
			}
		}
	}
	
	public static void searchHost(){
		int i;
		for (i=1;i<255;i++){
			new SearchThread(i).start();
		}
	}
	
	public static void main(String args[]){
		searchHost();
		/*InetAddress serverIP=null;
		try {
			serverIP=InetAddress.getByName("192.168.1.2");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("connect to "+serverIP.getHostAddress());
		ClientMessageHandler client=new ClientMessageHandler(serverIP,4321);
		client.sending();*/
	}
	
	/*call this function will start SendThread*/
	public void sending(){
		new SendThread().start();
	}
}

/*class SearchThread extends Thread{
	int i;
	public SearchThread(int i){
		this.i=i;
	}
	public void run(){
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(InetAddress.getByName("192.168.1."+i), 4321), 1000);
			System.out.println("Connecting successfully: 192.168.1."+i);
		} catch (IOException e) {
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
			}
		}
	}
}*/