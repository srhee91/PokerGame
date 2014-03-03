/*package Network;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;



public class HostMessageHandler {
	final static int port=4321;
	ObjectInputStream ois=null;
	ObjectOutputStream oos=null;
	public static void main(String args[]){
		ServerSocket server=null;
		try{
			server=new ServerSocket(port);
			System.out.println("Listening on port for client");
		}catch(IOException e){
			System.out.println("Cannot listen on port");
			e.printStackTrace();
		}
		
		ArrayList<Socket> serverSockets=new ArrayList<Socket>();
		int index=0;
		
		while(true){
			try{
				Socket socket=server.accept();
				System.out.println(socket.getInetAddress().getHostAddress()+" is connected\n");
				try{
					ois=new ObjectInputStream(socket.getInputStream());
					oos=new ObjectOutputStream(socket.getOutputStream());
				}catch(IOException e){
					System.out.println("Cannot read from Stream");
					e.printStackTrace();
				}
				break;
			}catch(Exception e){
				e.printStackTrace();
			}
			index++;
			
		}
	}
	
	public void send(String str){
		Message message=new Message("qwe","asd",str);
		try{
			oos.writeObject(message);
			oos.flush();
		}catch(IOException e){
			System.out.println("Cannot send object");
			e.printStackTrace();
		}
	}
	
	class ReceiveThread extends Thread{
		public void run(){
			Message message;
			
			while(true){
				try{
					message=(Message)ois.readObject();
					//System.out.println("receive: \n"+message.toString());
					System.out.println(message);
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



class ServerThread{
	Socket socket;
	class ReceiveThread extends Thread{
		public void run(){
			Message message;
			try{
				ois=new ObjectInputStream(socket.getInputStream());
				oos=new ObjectOutputStream(socket.getOutputStream());
			}catch(IOException e){
				System.out.println("Cannot read from Stream");
				e.printStackTrace();
			}
			while(true){
				try{
					message=(Message)ois.readObject();
					//System.out.println("receive: \n"+message.toString());
					System.out.println(message);
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
	class SendThread extends Thread{
		public void run(){
			Scanner input=new Scanner(System.in);
			System.out.println("Input your message:");
			while(true){
				String str=input.nextLine();
				send(str);
			}
		}
	}
	public ServerThread(Socket socket){
		this.socket=socket;
		Thread receive=new ReceiveThread();
		receive.start();
	}
}*/














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
import java.util.Scanner;

public class HostMessageHandler {
	final static int port=4321;
	ObjectOutputStream oos=null;
	ObjectInputStream ois=null;
	public HostMessageHandler(){
		Socket socket=null;
		ServerSocket server=null;
		try{
			server=new ServerSocket(port);
			System.out.println("Listening on port for client");
		}catch(IOException e){
			System.out.println("Cannot listen on port");
			e.printStackTrace();
		}
		
		while(true){
			try{
				socket=server.accept();
				System.out.println(socket.getInetAddress().getHostAddress()+" is connected\n");
				oos=new ObjectOutputStream(socket.getOutputStream());
				ois=new ObjectInputStream(socket.getInputStream());
				break;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void startReceiving(){
		Message message;
		while(true){
			try{
				message=(Message)ois.readObject();
				System.out.println(message);
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
	
	public void send(String str){
		Message message=new Message("qwe","asd",str);
		try{
			oos.writeObject(message);
			oos.flush();
		}catch(IOException e){
			System.out.println("Cannot send object");
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		HostMessageHandler host=new HostMessageHandler();
		Thread th=host.new SendThread();
		th.start();
		host.startReceiving();
	}
	
	public class SendThread extends Thread{
		public void run(){
			Scanner input=new Scanner(System.in);
			System.out.println("Input your message:");
			while(true){
				String str=input.nextLine();
				send(str);
			}
		}
	}
}