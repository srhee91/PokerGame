package GUI;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class OverMode extends BasicGameState
{

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
