package GUI;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class GUI extends StateBasedGame
{

	public GUI(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException
	{
		this.addState(new StartMode());
		this.addState(new LobbyMode());
		this.addState(new OngoingMode());
		this.addState(new OverMode());
	}
	
	
	
	
}