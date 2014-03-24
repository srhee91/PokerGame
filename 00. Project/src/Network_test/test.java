package Network_test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import Network.*;

public class test {

	private static final int timeout = 15;
	private static boolean[] connected = new boolean[255];
	private static SearchThread[] threads = new SearchThread[255];
	private static boolean foundAHost;
	
	public static void main(String[] args) throws Exception {
		
		int numWorked = 0;
		
		for (int T=0; T<100; ++T) {
			searchHost();
			boolean worked = true;
			for (int i=2; i<4; ++i) {
				if (!connected[i]) {
					worked = false;
					break;
				}
			}
			
			if (worked) {
				numWorked++;
			}
			
		}
		
		System.out.println("worked: "+numWorked);
	}
	
	public static void searchHost() throws Exception {
		
		for (int i=0; i<255; ++i) {
			connected[i] = false;
		}
		
		foundAHost = false;
		
		for (int i=2; i<5; ++i) {
			for (int t=0; t<2; ++t) {
				threads[i] = new SearchThread(i);
				threads[i].start();
				threads[i].join();
			}
		}
		
		/*
		for (int i=0; i<255; ++i) {
			if (connected[i])
				System.out.println(i+" connected.");
		}*/
	}

	
	private static void connect(int i) {
		if (!connected[i]) {
			//System.out.println("trying "+i+"...");
			Socket socket = new Socket();
			try{
				socket.connect(new InetSocketAddress(
					InetAddress.getByName("192.168.1."+i), 4321), timeout);
				socket.close();
				connected[i] = true;
				foundAHost = true;
				//System.out.println("Connecting successfully: 192.168.1."+i);
			}catch(Exception e) {
				//System.out.println(e.getMessage());
			};
		}
	}
	
	
	private static class SearchThread extends Thread{
		int i;
		public SearchThread(int i){
			this.i=i;
		}
		public void run(){
			connect(i);
		}
	}
}


