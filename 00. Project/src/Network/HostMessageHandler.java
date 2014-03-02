package Network;



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
				new ServerThread(socket);
				break;
			}catch(Exception e){
				e.printStackTrace();
			}
			index++;
			
		}
	}
}

class ServerThread{
	Socket socket;
	class ReceiveThread extends Thread{
		public void run(){
			ObjectInputStream ois=null;
			Message message;
			try{
				ois=new ObjectInputStream(socket.getInputStream());
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
	
	/*send from host to client*/
	class SendThread extends Thread{
		
		public void run(){
			ObjectOutputStream oos=null;
			Scanner input=new Scanner(System.in);
			Message message;
			while(true){
				message=new Message("qwe","asd",input.nextLine());
				try{
					//System.out.println("Send: \n"+message.toString());
					oos.writeObject(message);
					oos.flush();
				}catch(IOException e){
					System.out.println("Cannot send object");
					e.printStackTrace();
					break;
				}

			}
		}
	}
	
	public ServerThread(Socket socket){
		this.socket=socket;
		Thread receive=new ReceiveThread();
		receive.start();
	}
}