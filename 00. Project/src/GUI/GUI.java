package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
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
	{
		this.addState(new StartMode());
		this.addState(new LobbyMode());
		this.addState(new OngoingMode());
		this.addState(new OverMode());
	}
	
	
	protected static void drawStringCentered(Graphics g, TrueTypeFont font, Color c, String s, int x, int y) {
		font.drawString(x-font.getWidth(s)/2, y-font.getHeight(s)/2, s, c);
	}
}