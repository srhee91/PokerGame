package GUI;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class GUI extends StateBasedGame
{

	public static final String RESOURCES_PATH = "./resources/";
	public static final String CARDSPRITES_FOLDER = "cardsprites2/";
	public static final String BUTTONS_FOLDER = "buttons/";
	
	public GUI(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException
	{this.addState(new LobbyMode());
		this.addState(new StartMode());
		
		this.addState(new OngoingMode());
		this.addState(new OverMode());
	}
	
	
	
	
}