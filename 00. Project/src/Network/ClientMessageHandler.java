package Network;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMessageHandler {
	final static int port=4321;
	public ClientMessageHandler(InetAddress IP){
		Socket client=null;
		ObjectOutputStream oos=null;
		Message message=null;
		try {
			client = new Socket(IP, port);
			oos=new ObjectOutputStream(client.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("Cannot connect to server");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Cannot create object stream");
			e.printStackTrace();
			System.exit(0);
		}
		Scanner input=new Scanner(System.in);
		System.out.println("Input your message:");
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
	
	
	
	
	public static void main(String args[]){
		InetAddress serverIP=null;
		try {
			serverIP=InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("connect to "+serverIP.getHostAddress());
		ClientMessageHandler client=new ClientMessageHandler(serverIP);
	}
}