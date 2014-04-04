package Network;
import java.io.*;
import java.net.*;

import GUI.GUI;

public class ClientMessageHandler {
	
	ObjectOutputStream oos=null;
	ObjectInputStream ois=null;
	Socket socket=null;
	Object receivedObj=null;
	int ChildIndex=-1;
	ReceivingThread receivingThread;
	
	
	public static class NameTakenException extends Exception {
		public NameTakenException(String msg) {
			super(msg);
		}
	}
	
	/* 
	 * Constructor 
	 * Used to create ClientMessageHandler
	 * Implement socket 
	 * Connect to a specific port listened by HostMessageHandler
	 * Get a pair of streams in socket
	 **/
	public ClientMessageHandler(InetAddress IP, int port, String playerName)
			throws IOException, ClassNotFoundException, NameTakenException{

		socket = new Socket();
		socket.connect(new InetSocketAddress(IP, port), 10000);
		
		// send player name to host, await boolean reply to see if name
		// is ok
		DataInputStream dis=new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		dos.writeUTF(playerName);
		dos.flush();
		boolean playerNameOk = dis.readBoolean();
		if (!playerNameOk) {
			socket.close();
			throw new NameTakenException("Name "+playerName+" is being used by another client.");
		}
				
		// get object i/o streams of socket
		oos=new ObjectOutputStream(socket.getOutputStream());
		ois=new ObjectInputStream(socket.getInputStream());
		
		receivingThread = new ReceivingThread();
		receivingThread.start();
	}	
	
	
	public Object getReceivedObject() {
		if (receivedObj==null)
			return null;
		else{
			Object temp=receivedObj;
			receivedObj=null;
			return temp;
		}
	}
	
	
	/*
	 * Call this function will
	 * send UserAction object 
	 * through oos stream to host message handler
	 * */
	public synchronized void send(Object ob) throws IOException{
		oos.writeObject(ob);
		oos.flush();
	}
	
	/*
	 * Call this function will make
	 * current socket close
	 * */
	public void close(){
		receivingThread.enable = false;
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
		public boolean enable = true;
		public void run(){
			while(enable){
				try{
					receivedObj=ois.readObject();
					System.out.println("Receive a game state from host\n\t: "+receivedObj);
				}catch(IOException | ClassNotFoundException e){
					System.out.println("Lost connection to host!");
					e.printStackTrace();
					if (enable) {
						GUI.lobbyMode.hostConnectionError_flag = true;
						GUI.ongoingMode.hostConnectionError_flag = true;
					}
					break;
				}
			}
		}
	}

	/*
	//Get user input and call send(input);

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
	
	//call this function will start SendThread
	public void sending(){
		new SendThread().start();
	}
	*/
}	
