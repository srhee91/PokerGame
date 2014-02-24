package GUI;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class OverMode extends BasicGameState
{
	// use this to call methods that notify GUI to mode events
	private GUI guiContext;

	
	public OverMode(GUI guiContext) {
		this.guiContext = guiContext;
	}
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

		
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawString("state 2", 400, 300);
	}

	public int getID() {
		return 1;
	}
}
