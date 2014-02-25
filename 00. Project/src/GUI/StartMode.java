package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartMode extends BasicGameState
{

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}
	
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_2))
			game.enterState(2);
		else if (container.getInput().isKeyPressed(Input.KEY_3))
			game.enterState(3);
		else if (container.getInput().isKeyPressed(Input.KEY_4))
			game.enterState(4);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawString("mode 1: start mode (press 1, 2, 3, 4 to switch modes)", 100, 100);
	}

	public int getID() {
		return 1;
	}
}
