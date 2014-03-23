package GUI;

import java.util.*;
import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class JoinMode extends BasicGameState {

	private final int[] joinListPosition = {0, 50};
	private final int[] joinListSize = {1000, 500};
	private final String[] joinListColumnNames = {"HOST", "IP ADDRESS", "STATUS", "PLAYERS"};
	private final int[] joinListColumnWidths = {220, 180, 130, 120};
	
	private TrueTypeFont joinListHeaderFont;
	private TrueTypeFont joinListListFont;
	private TrueTypeFont joinListJoinButtonsFont;
	private TrueTypeFont joinListPageLabelFont;
	private TrueTypeFont joinListNavButtonsFont;
	
	
	private Image background;
	private JoinList joinList;
	
	List<String[]> gamesInfo;
	List<Boolean>  gamesJoinable;
	
	@Override
	public void init(GameContainer container, final StateBasedGame game)
			throws SlickException {
		
		background = new Image(GUI.RESOURCES_PATH+"background.png");
		
		joinListHeaderFont = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 20), true);
		joinListListFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 20), true);
		joinListJoinButtonsFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 12), true);
		joinListPageLabelFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 16), true);
		joinListNavButtonsFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 20), true);
		
		joinList = new JoinList(container, joinListPosition, joinListSize, joinListColumnNames,
				joinListColumnWidths, joinListHeaderFont, joinListListFont, joinListJoinButtonsFont,
				joinListPageLabelFont, joinListNavButtonsFont,
				new JoinList.IndexedComponentListener() {
					@Override
					public void componentActivated(AbstractComponent arg0, int index){
						System.out.println("Joined game "+index);
					}
				},
				new ComponentListener() {	
					@Override
					public void componentActivated(AbstractComponent arg0) {
						System.out.println("refresh games list!");
					}
				},
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent arg0) {
						game.enterState(1);
					}
				});
		
		
		gamesInfo = new ArrayList<String[]>();
		gamesJoinable = new ArrayList<Boolean>();
		String[] data = {"host_name", "192.128.234.122", "Ongoing", "3/8"};
		for (int i=0; i<20; ++i) {
			gamesInfo.add(data);
			gamesJoinable.add(7854%(i+1)==0);
		}
		joinList.setRowsData(gamesInfo);
		joinList.setRowsJoinable(gamesJoinable);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_1))
			game.enterState(1);
		else if (container.getInput().isKeyPressed(Input.KEY_3))
			game.enterState(3);
		else if (container.getInput().isKeyPressed(Input.KEY_4))
			game.enterState(4);
		
		
		if (container.getInput().isKeyPressed(Input.KEY_F)) {
			if (!gamesInfo.isEmpty()) {
				gamesInfo.remove(0);
				gamesJoinable.remove(0);
			}
			joinList.setRowsData(gamesInfo);
			joinList.setRowsJoinable(gamesJoinable);
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		background.draw(0, 0, container.getWidth(), container.getHeight());
		
		joinList.render(container, g);
	}

	

	@Override
	public int getID() {
		return 2;
	}
	
	
}
