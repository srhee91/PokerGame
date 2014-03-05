package Network;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

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
			socket = new Socket(IP, port);
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
	
	
	
	
/*	public static void main(String args[]){
		InetAddress serverIP=null;
		try {
			serverIP=InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("connect to "+serverIP.getHostAddress());
		ClientMessageHandler client=new ClientMessageHandler(serverIP,4321);
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.disconnect();
	}*/
	
	/*call this function will start SendThread*/
	public void sending(){
		new SendThread().start();
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
				//if(str.equalsIgnoreCase("0")){
				//	disconnect();
				//}else{
				ua = new UserAction(str);
				send(ua);
				//}
			}
		}
	}
}