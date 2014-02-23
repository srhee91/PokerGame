import Host.Host;
import java.lang.*;

public class Poker {

	public static void main(String[] args) throws Exception {
	
		startClientProcess();
		hostProcess();
		
	}
	
	public static void startClientProcess() throws Exception
	{
		Process client = new ProcessBuilder("Client.java", "myArg").start();	
	}
	
	public static void hostProcess() throws Exception
	{
		while(true)
		{
			waitToStart();
			startHostProcess();
			waitToEnd();
		}
	}

	public static void startHostProcess() throws Exception
	{
		Process host = new ProcessBuilder("Host.java", "myArg").start();
	}
	
	public static void waitToStart()
	{
		//wait for the client.gui to start the host or client to be called to become a new host
	}
	
	public static void waitToEnd()
	{
		//wait until the game ends or the player loses.
	}
}
