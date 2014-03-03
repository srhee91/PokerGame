package Network;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class HostMessageHandler {
	int clientIndex;
	ArrayList<ObjectOutputStream> oos=null;
	ArrayList<ObjectInputStream> ois=null;
	Socket socket=null;
	ServerSocket server=null;
	
	public HostMessageHandler(int port){

		try{
			server=new ServerSocket(port);
			System.out.println("Listening on port for client");
		}catch(IOException e){
			System.out.println("Cannot listen on port");
			e.printStackTrace();
		}
		
		clientIndex=0;
		oos=new ArrayList<ObjectOutputStream>(20);
		ois=new ArrayList<ObjectInputStream>(20);
		oos.add(0,null);
		ois.add(0,null);
		
		new SendThread().start();
<<<<<<< HEAD
		new Listening().start();

	}
	
	class Listening extends Thread{
		public void run(){
			while(true){
				try{
					socket=server.accept();
					clientIndex++;
					System.out.println(socket.getInetAddress().getHostAddress()+" is connected as client "+clientIndex);
					oos.add(clientIndex, new ObjectOutputStream(socket.getOutputStream()));
					ois.add(clientIndex, new ObjectInputStream(socket.getInputStream()));
					new ReceivingThread(clientIndex).start();
				}catch(Exception e){
					e.printStackTrace();
					break;
				}
=======
		
		while(clientIndex<=20){
			try{
				socket=server.accept();
				clientIndex++;
				System.out.println(socket.getInetAddress().getHostAddress()+" is connected as client "+clientIndex);
				oos.add(clientIndex, new ObjectOutputStream(socket.getOutputStream()));
				ois.add(clientIndex, new ObjectInputStream(socket.getInputStream()));
				new ReceivingThread(clientIndex).start();
			}catch(Exception e){
				e.printStackTrace();
				break;
>>>>>>> refs/remotes/GitHub/master
			}
		}
	}
	
	public synchronized void send(int index,String str){
		Message message=new Message("qwe","asd",str);
		try{
			oos.get(index).writeObject(message);
			oos.get(index).flush();
		}catch(IOException e){
			System.out.println("Cannot send object");
			e.printStackTrace();
		}
	}
	
	
	public synchronized void sendAll(String str){
		Message message=new Message("from ","to",str);
		for(int i=1;i<=clientIndex;i++){
			try{
				oos.get(i).writeObject(message);
				oos.get(i).flush();
			}catch(IOException e){
				System.out.println("Cannot send object to client "+i);
				e.printStackTrace();
			}
		}
	}
	
	class ReceivingThread extends Thread{
		ObjectInputStream myois=null;
		int clientIndex;
		public ReceivingThread(int index) {
			myois=ois.get(index);
			clientIndex=index;
		}
		public void run(){
			Message message;
			while(true){
				try{
					message=(Message)myois.readObject();
					System.out.println("Received Message from client "+clientIndex+"\n");
					System.out.println(message+"\n");
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
	
	public static void main(String args[]){
		HostMessageHandler host=new HostMessageHandler(4321);
		System.out.println("finish main");
	}
	
	
	public class SendThread extends Thread{
		public void run(){
			Scanner input=new Scanner(System.in);
			System.out.println("Input your message:");
			while(true){
				int index=input.nextInt();
				String str=input.nextLine();
				if (index==0) 
					sendAll(str);
				else
					send(index,str);
			}
		}
	}
}
