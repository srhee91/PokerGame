package GUI;

import java.awt.Font;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

public class LobbyMode extends TableMode {

	private TrueTypeFont mainStatusFont;
	private TrueTypeFont labelFont;
	private TrueTypeFont startButtonFont;

	private final int[] playerPanelAnimationOffset = {69, 54};
	private final int[] mainTextOffset = {250, 75};
	private final int[] mainStartButtonOffset = {150, 50};
	
	private final Color hostLabelColor = new Color(85, 163, 217, 242);
	private final Color joinedLabelColor = new Color(128, 128, 128, 242);

	
	
	private Animation[] waitingAnimations;
	private final int NUM_ANIMATIONS = 4;
	
	private Button startButton;
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		super.init(container, game);
		
		mainStatusFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 32), true);
		labelFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 20), true);
		startButtonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 28), true);
		
		startButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightbluebig.png", 
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightbluebig_down.png",
				mainPanelPosition[0]+mainStartButtonOffset[0],
				mainPanelPosition[1]+mainStartButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("started game!");
						
					}
				});
		startButton.setEnable(false);
		
		
		waitingAnimations = new Animation[NUM_ANIMATIONS];
		
		SpriteSheet sheet = new SpriteSheet(GUI.RESOURCES_PATH+"loading2.png", 32, 32);
		for (int A=0; A<NUM_ANIMATIONS; ++A) {
			waitingAnimations[A] = new Animation();
			for (int i=0; i<8; ++i) {
				waitingAnimations[A].addFrame(sheet.getSprite(i, 0), 64);
			}
			waitingAnimations[A].setCurrentFrame(A*(waitingAnimations[A].getFrameCount()/NUM_ANIMATIONS));
		}
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		super.update(container, game, delta);
		
		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_1))
			game.enterState(1);
		else if (container.getInput().isKeyPressed(Input.KEY_2))
			game.enterState(2);
		else if (container.getInput().isKeyPressed(Input.KEY_4))
			game.enterState(4);
	}
	
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		super.render(container, game, g);
		
		drawPlayerNamesAndStatuses(container, g);
	}
	
	
	
	
	private void drawPlayerNamesAndStatuses(GUIContext container, Graphics g) {
		
		// for testing!!!!!!!
		boolean playerJoined[] = {false, false, false, true, true, true, true, true};
		int hostIndex = 4;
		boolean isHost = true;
		boolean enoughPlayers = true;
		
		g.setColor(Color.white);
		GUI.drawStringCenter(g, infoFont, Color.white, "Player0", mainPanelPosition[0]+mainNameOffset[0],
				mainPanelPosition[1]+mainNameOffset[1]);
		
		for (int i=1; i<8; ++i) {
			
			if (i==hostIndex) {
				
				g.setColor(Color.white);
				GUI.drawStringCenter(g, infoFont, Color.white, "Player"+i, playerPanelPositions[i][0]+playerNameOffset[0],
						playerPanelPositions[i][1]+playerNameOffset[1]);
				
				drawPlayerLabel(g, i, "Host", Color.white, hostLabelColor);
				
			}
			else if (playerJoined[i]) {
				
				g.setColor(Color.white);
				GUI.drawStringCenter(g, infoFont, Color.white, "Player"+i, playerPanelPositions[i][0]+playerNameOffset[0],
						playerPanelPositions[i][1]+playerNameOffset[1]);
						
				drawPlayerLabel(g, i, "Joined", Color.white, joinedLabelColor);
			}
			else {
				Animation waitingAnimation = waitingAnimations[i%NUM_ANIMATIONS];
				waitingAnimation.draw(playerPanelPositions[i][0]+playerPanelAnimationOffset[0],
						playerPanelPositions[i][1]+playerPanelAnimationOffset[1]);
			}
		}
		
		g.setColor(Color.white);
		if (!enoughPlayers) {
			GUI.drawStringCenter(g, mainStatusFont, Color.white, "Waiting for more players ...", 
					mainPanelPosition[0]+mainTextOffset[0],
					mainPanelPosition[1]+mainTextOffset[1]);
		}
		else {
			if (!isHost) {
				GUI.drawStringCenter(g, mainStatusFont, Color.white, "Waiting for host to start game ...", 
						mainPanelPosition[0]+mainTextOffset[0],
						mainPanelPosition[1]+mainTextOffset[1]);
			}
			else {
				
				// draw start button
				startButton.setEnable(true);
				startButton.render(container, g, startButtonFont, Color.white, "Start Game");
			}
		}
	}

	
	
	@Override
	public int getID() {
		
		return 3;
	}
}


/*
playerbox
170 x 140
locations: 25,290  25,100  215,10  415,10  615,10  805,100  805,290
offset values:
player string: 85,15 (center)


mainPlayerBox
500 x 150
location: 250,430
offset values:
player string: 250,15 (center)
*/