import GUI.GUI;
import Host.Host;
import java.lang.*;

public class Poker {

	public static boolean isSpectator;
	
	public static void main(String[] args) throws Exception {
	
		GUI gui = new GUI();
		gui.run();
	}
	
	public static void startHostProcess() throws Exception
	{
		Process host = new ProcessBuilder("Host.java", "myArg").start();
	}
	
	public static void startMessageHandler()
	{
		//create a Thread of ClientMessageHandler
	}
}
