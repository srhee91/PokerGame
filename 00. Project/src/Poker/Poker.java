package Poker;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import GUI.GUI;



public class Poker {

	public static boolean isSpectator;
	
	public static void main(String[] args) throws Exception {
	
		try {
			AppGameContainer app = new AppGameContainer(new GUI("Poker"));
			app.setDisplayMode(1000, 600, false);
			app.start();
		}
		catch (SlickException e) {
			System.out.println("ERROR: GUI could not be started");
		}
	}
	
	public static void startHostProcess() throws Exception
	{
		Process host = new ProcessBuilder("Host.java", "myArg").start();
	}
	
	public static void startMessageHandler()
	{
		//create a Thread of ClientMessageHandler
	}
	
	
	public static enum PlayerAction{CHECK, RAISE, FOLD}
	
	
}
