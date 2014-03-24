package Network_test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import Network.*;

public class test {

	private static final int timeout = 1000;
	private static Socket[] sockets = new Socket[255];
	private static boolean[] connected = new boolean[255];
	private static SearchThread[] threads = new SearchThread[255];
	private static boolean foundAHost;
	
	public static void main(String[] args) {
		searchHost();
	}
	
	public static void searchHost(){
		
		for (int i=1; i<255; ++i) {
			sockets[i] =  new Socket();
			connected[i] = false;
		}
		
		do {
			foundAHost = false;
			
			for (int i=1;i<255;i++){
				threads[i] = new SearchThread(i);
				threads[i].start();
			}
			try {
				for (int i=1; i<255; ++i) {
					threads[i].join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} while (foundAHost);
	}

	
	
	private static class SearchThread extends Thread{
		int i;
		public SearchThread(int i){
			this.i=i;
		}
		public void run(){
			try {
				if (!connected[i]) {
					sockets[i].connect(new InetSocketAddress(
							InetAddress.getByName("192.168.1."+i), 4321), timeout);
					connected[i] = true;
					foundAHost = true;
					System.out.println("Connecting successfully: 192.168.1."+i);
				}
			} catch (IOException e) {
			}
		}
	}
}


