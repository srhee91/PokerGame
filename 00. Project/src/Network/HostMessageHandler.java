package Network;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import sun.net.ConnectionResetException;


public class HostMessageHandler {
	int clientIndex;
	ArrayList<ObjectOutputStream> oos=null;
	ArrayList<ObjectInputStream> ois=null;
	Socket socket=null;
	ServerSocket server=null;
	int port;
	
	/*
	 * Constructor 
	 * Used to create HostMessageHandler
	 * Implement socket server
	 * listen on specific port
	 * create arrays to store multiple streams.
	 * */
	public HostMessageHandler(int port){
		this.port=port;
		try{
			server=new ServerSocket(port);
			System.out.println("Host is Listening on port ["+port+"] Waiting for client to connect...");
		}catch(IOException e){
			System.out.println("Cannot listen on port");
			//e.printStackTrace();
			System.exit(0);
		}
		
		clientIndex=0;
		oos=new ArrayList<ObjectOutputStream>();
		ois=new ArrayList<ObjectInputStream>();
		oos.add(0,null);
		ois.add(0,null);
		
		new Listening().start();

	}
	
	/*
	 * Inner class
	 * when new client is connected to server.
	 * return socket and create new streams in arrays
	 * start receiving thread
	 * */
	class Listening extends Thread{
		public void run(){
			while(true){
				try{
					socket=server.accept();
					clientIndex++;
					System.out.println(socket.getInetAddress().getHostAddress()+" is connected to the port ["+port+"] as client "+clientIndex);
					DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
					dos.writeInt(clientIndex);
					oos.add(clientIndex, new ObjectOutputStream(socket.getOutputStream()));
					ois.add(clientIndex, new ObjectInputStream(socket.getInputStream()));
					new ReceivingThread(clientIndex).start();
				}catch(NullPointerException e){
					System.out.println("Cannot listen on port");
					break;
				}catch(Exception e){
				}
			}
		}
	}
	
	/*
	 * Inner class 
	 * receiving thread
	 * continuously receive UserAction through socket stream
	 * show action when there is one
	 * */
	class ReceivingThread extends Thread{
		ObjectInputStream myois=null;
		int clientIndex;
		public ReceivingThread(int index) {
			myois=ois.get(index);
			clientIndex=index;
		}
		public void run(){
			
			while(true){
				try{
					UserAction ac;
					ac=(UserAction)myois.readObject();
					System.out.println("Host receives an action from Client "+clientIndex+": \n");
					System.out.println("\t"+ac+"\n");
					Thread.sleep(20);
				}catch(IOException e){
					System.out.println("Session end for client "+clientIndex);
					//e.printStackTrace();
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
	
	/*call this function will send game state to specific client,
	 * which are arguments*/
	public synchronized void send(int index,Gamestate gs){
		gs.setIndex(index);
		try{
			oos.get(index).writeObject(gs);
			oos.get(index).flush();
		}catch(IOException e){
			System.out.println("Cannot send object");
			e.printStackTrace();
		}
	}
	
	/*call this function will send game state to all clients*/
	public synchronized void sendAll(Gamestate gs){

		for(int i=1;i<=clientIndex;i++){
			send(i,gs);
		}
	}
	
	
	
	public static void main(String args[]){
		HostMessageHandler host=new HostMessageHandler(4321);
		host.sending();
	}
	
	/*call this function will start SendThread */
	public void sending(){
		new SendThread().start();
	}
	
	/*
	 * Get user input and call send(input);
	 * */
	public class SendThread extends Thread{
		Gamestate gs;
		public void run(){
			Scanner input=new Scanner(System.in);
			System.out.println("Input the client number and message(game state) continously: (0 means send to all, e.g.: 0 msg)");
			while(true){
				int index=input.nextInt();
				String str=input.nextLine();
				gs=new Gamestate(index,str);
				if (index==0) 
					sendAll(gs);
				else
					send(index,gs);
			}
		}
	}
}
