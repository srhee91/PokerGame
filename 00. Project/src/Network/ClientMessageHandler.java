package Network;
import java.io.DataInputStream;
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
	Gamestate gamestate=null;
	int ChildIndex=-1;
	
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
			DataInputStream dis=new DataInputStream(socket.getInputStream());
			ChildIndex=dis.readInt();
			System.out.println("I am client "+ChildIndex);
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
	
	public Gamestate getUpdatedGameState() {
		if (gamestate==null)
			return null;
		else{
			Gamestate temp=gamestate;
			gamestate=null;
			return temp;
		}
	}
	
	
	/*
	 * Call this function will
	 * send UserAction object 
	 * through oos stream to host message handler
	 * */
	public synchronized void send(UserAction ua) throws IOException{
			oos.writeObject(ua);
			oos.flush();
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
			while(true){
				try{
					gamestate=(Gamestate)ois.readObject();
					System.out.println("Receive a game state from host\n\t: "+gamestate);
				}catch(IOException e){
					System.out.println("Session End");
					e.printStackTrace();
					break;
				}catch(ClassNotFoundException e){
					System.out.println("ClassNotFound");
					e.printStackTrace();
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
				try {
					send(ua);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String args[]){
		InetAddress serverIP=null;
		try {
			serverIP=InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("connect to "+serverIP.getHostAddress());
		ClientMessageHandler client=new ClientMessageHandler(serverIP,4321);
		client.sending();
	}
	
	/*call this function will start SendThread*/
	public void sending(){
		new SendThread().start();
	}
}
