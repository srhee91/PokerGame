package GUI;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import client.Client;


public class GUI extends StateBasedGame
{
	// use this to call methods that notify Client to GUI events
	private Client clientContext;
	
	public GUI(Client clientContext, String title)
	{
		super(title);
		this.clientContext = clientContext;
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException
	{
		this.addState(new OngoingMode(this));
		this.addState(new OverMode(this));
	}
	
	
	
	
}