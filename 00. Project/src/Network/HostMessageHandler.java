package Network;
import java.io.*;
import java.net.*;
import java.util.*;

import Poker.Poker.PlayerAction;

public class HostMessageHandler {
	
	private class ClientConnection {
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private ReceivingThread rt;
		private ClientConnection(ObjectOutputStream oos, ObjectInputStream ois,
				ReceivingThread rt) {
			this.oos = oos;
			this.ois = ois;
			this.rt = rt;
		}
		private void close() {
			rt.enable = false;
			try {
				ois.close();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String lastJoinedPlayer;
	
	private Map<String, ClientConnection> clientConnections;
	
	private Host.Host host;
	private ServerSocket server=null;
	private int port;
	protected String allowedPlayer=null;
	protected Timer nowTimer;
	private Listening listeningThread = null;
	private boolean blocking;
	
	/*
	 * Constructor 
	 * Used to create HostMessageHandler
	 * Implement socket server
	 * listen on specific port
	 * create arrays to store multiple streams.
	 * */
	public HostMessageHandler(int port, Host.Host host, Thread hostThread){
		this.port=port;
		this.host=host;
		allowedPlayer=null;
		blocking=false;
		
		try{
			server=new ServerSocket(port);
			System.out.println("Host is Listening on port ["+port+"] Waiting for client to connect...");
		}catch(IOException e){
			System.out.println("Cannot listen on port");
			e.printStackTrace();
			System.exit(0);
		}
		
		clientConnections = new HashMap<String, ClientConnection>();
		listeningThread = new Listening();
		listeningThread.start();
	}
	
	
	// stop all threads
	public void close() {
		if (listeningThread != null) {
			listeningThread.enable = false;
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (ClientConnection cc : clientConnections.values()) {
			cc.close();
		}
		clientConnections.clear();
	}
	
	
	// remove dead connections, returns true if a dead connection was found
	public boolean removeDeadConnections() {
		boolean clientDisconnected = false;
		Iterator<Map.Entry<String, ClientConnection>> iter = clientConnections.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, ClientConnection> entry = iter.next();
			if (!entry.getValue().rt.isAlive()) {
				iter.remove();
				clientDisconnected = true;
			}
		}
		return clientDisconnected;
	}
	
	
	
	// check if a player's connection is still alive
	public boolean isConnected(String playerName) {
		return clientConnections.containsKey(playerName);
	}
	
	public Set<String> getConnectedPlayerNames() {
		return clientConnections.keySet();
	}
	
	public void gameStart(){
		blocking=true;
	}
	
	public void gameEnd(){
		blocking=false;
	}
	
	/*
	 * Inner class
	 * when new client is connected to server.
	 * return socket and create new streams in arrays
	 * start receiving thread
	 * */
	public class Listening extends Thread{
		
		boolean enable = true;
		
		public void run(){
			Socket socket;
			while(enable){
				try{
					socket=server.accept();
					
					// wait for client to send player name, reply whether or not
					// the name is ok
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
					String playerName = dis.readUTF();
					boolean playerNameOk = !clientConnections.containsKey(playerName);
					dos.writeBoolean(playerNameOk);
					dos.flush();
					
					if (!playerNameOk) {
						socket.close();
						continue;
					}
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					ReceivingThread rt = new ReceivingThread(playerName, ois);
					
					lastJoinedPlayer = playerName;
					
					clientConnections.put(playerName, new ClientConnection(oos, ois, rt));
					
					rt.start();
					
					System.out.println(socket.getInetAddress().getHostAddress()+" is connected to the port ["
							+port+"] as client "+playerName);
					
					host.players[host.playerCount++] = playerName;
					
					sendAll(host.players.clone());
					
				}catch(NullPointerException e){
					System.out.println("Cannot listen on port listening()");
					e.printStackTrace();
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
		String playerName;
		ObjectInputStream myois;
		boolean enable;
		public ReceivingThread(String playerName, ObjectInputStream ois) {
			this.playerName = playerName;
			this.myois = ois;
			enable = true;
		}
		public void run(){
			
			while(enable){
				try{
					Object ac;
					ac=myois.readObject();
					if (blocking==true){
						if (playerName!=allowedPlayer) continue;
						nowTimer.cancel();
						allowedPlayer=null;
					}
					host.objReceived=ac;
					if (host.isWaiting) {
						synchronized(host){
							host.isWaiting = false;
							host.objSender = playerName;
							host.notify();
						}
					}
					System.out.println("Host receives an object from Client "+playerName);
				}catch(IOException e){
					System.out.println("Session end for client "+playerName);
					
					break;
				}catch(ClassNotFoundException e){
					System.out.println("ClassNotFound");
					e.printStackTrace();
				}
			}
		}
	}
	
	
	class autoResponse extends TimerTask{
		public void run() {
			nowTimer.cancel();
			UserAction ac=new UserAction(UserAction.Action.FOLD,0);
			host.objReceived=ac;
			if (host.isWaiting) {
				synchronized(host){
					host.isWaiting = false;
					host.objSender = allowedPlayer;
					host.notify();
				}
			}	
			allowedPlayer=null;
		}
		
	}
	
	
	
	// call this function will send game state to specific client,
	// which are arguments
	public synchronized void send(String playerName, Object ob){
		System.out.println("a2");
		if (blocking==true){
			int turn=((GameState.Gamestate)ob).whoseTurn;
			if (turn!=-1){ 
				System.out.println("turn:"+turn);
				allowedPlayer=host.players[turn];
				System.out.println("allow:"+allowedPlayer);
				nowTimer=new Timer();
				nowTimer.schedule(new autoResponse(), 5000);
			}
		}
		ObjectOutputStream oos= clientConnections.get(playerName).oos;
		try{
			oos.writeObject(ob);
			oos.flush();
		}catch(IOException e){
			System.out.println("Cannot send object");
			e.printStackTrace();
		}
	}
	
	// call this function will send game state to all clients
	public synchronized void sendAll(Object ob){
		for (String playerName : clientConnections.keySet())
			send(playerName, ob);
	}
	
}
