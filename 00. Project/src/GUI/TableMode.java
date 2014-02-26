package GUI;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class TableMode extends BasicGameState {

	protected Image background;	
	protected TrueTypeFont infoFont;
	
	protected final int mainPanelPosition[] = {250, 430};
	protected final int mainPanelSize[] = {500, 150};
	protected final int mainPlayerNameOffset[] = {250, 15};
	
	protected final int panelPositions[][] = {{-1, -1},
											{25, 290},
											{25, 100},
											{215, 10},
											{415, 10},
											{615, 10},
											{805, 100},
											{805, 290}};
	protected final int panelSize[] = {170, 140};
	protected final int playerNameOffset[] = {85, 15};
	
	
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		// load background image of table surface
		background = new Image(GUI.RESOURCES_PATH + "table_background.jpg");
		
		// load font
		infoFont = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 16), true);
	}

	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}
	
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// draw background
		background.draw(0, 0, container.getWidth(), container.getHeight());
		
		drawPanels(g);
	}

	private void drawPanels(Graphics g) {
		
		g.setColor(new Color(32, 32, 32, 128));
		
		g.fillRoundRect(mainPanelPosition[0], mainPanelPosition[1],
				mainPanelSize[0], mainPanelSize[1], 10);	// mainplayer rect
		
		for (int i=1; i<8; ++i) {
			g.fillRoundRect(panelPositions[i][0], panelPositions[i][1],
					panelSize[0], panelSize[1], 10);
		}
	}
	
	protected void drawStringCentered(Graphics g, String s, int x, int y) {
		org.newdawn.slick.Font font = g.getFont();
		g.drawString(s, x-font.getWidth(s)/2, y-font.getHeight(s)/2);
	}
	
	protected void drawStringCentered(Graphics g, TrueTypeFont font, String s, int x, int y) {
		font.drawString(x-font.getWidth(s)/2, y-font.getHeight(s)/2, s);
	}
	
	@Override
	public int getID() {
		return -1;
	}

}
