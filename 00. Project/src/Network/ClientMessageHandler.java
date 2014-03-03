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
	final static int port=4321;
	ObjectOutputStream oos=null;
	ObjectInputStream ois=null;
	public ClientMessageHandler(InetAddress IP){
		Socket socket=null;
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
		InetAddress serverIP=null;
		try {
			serverIP=InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("connect to "+serverIP.getHostAddress());
		ClientMessageHandler client=new ClientMessageHandler(serverIP);
		Thread th=client.new SendThread();
		th.start();
		client.startReceiving();
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