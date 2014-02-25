package GUI;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LobbyMode extends BasicGameState {

	
	private Image background;
	
	private TrueTypeFont infoFont;
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		// load background image of table surface
		background = new Image("./resources/table_background.jpg");
		
		// load UI fonts
		infoFont = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 16), true);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_1))
			game.enterState(1);
		else if (container.getInput().isKeyPressed(Input.KEY_3))
			game.enterState(3);
		else if (container.getInput().isKeyPressed(Input.KEY_4))
			game.enterState(4);
	}
	
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		background.draw(0, 0, container.getWidth(), container.getHeight());
		
		drawPanels(g);
		drawPlayerNames(g);
	}

		
	private void drawPanels(Graphics g) {
		g.setColor(new Color(32, 32, 32, 128));
		
		g.fillRoundRect(250, 430, 500, 150, 10);	// mainplayer rect
		
		g.fillRoundRect(215, 10, 170, 140, 10);
		g.fillRoundRect(415, 10, 170, 140, 10);
		g.fillRoundRect(615, 10, 170, 140, 10);
		
		g.fillRoundRect(25, 100, 170, 140, 10);
		g.fillRoundRect(25, 290, 170, 140, 10);
		
		g.fillRoundRect(805, 100, 170, 140, 10);
		g.fillRoundRect(805, 290, 170, 140, 10);
	}
	
	private void drawPlayerNames(Graphics g) {
		g.setColor(new Color(255, 255, 255, 255));
		g.setFont(infoFont);
		
		drawStringCentered(g, "Player0", 500, 445);
		
		drawStringCentered(g, "Player3", 300, 25);
		drawStringCentered(g, "Player4", 500, 25);
		drawStringCentered(g, "Player5", 700, 25);
		
		drawStringCentered(g, "Player2", 110, 115);
		drawStringCentered(g, "Player1", 110, 305);
		
		drawStringCentered(g, "Player6", 890, 115);
		drawStringCentered(g, "Player7", 890, 305);
	}
	
	private void drawStringCentered(Graphics g, String s, int x, int y) {
		org.newdawn.slick.Font font = g.getFont();
		g.drawString(s, x-font.getWidth(s)/2, y-font.getHeight(s)/2);
	}
	
	@Override
	public int getID() {
		
		return 2;
	}

}
