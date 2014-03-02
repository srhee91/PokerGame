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

	private TrueTypeFont portNumberFont;
	private TrueTypeFont mainStatusFont;
	private TrueTypeFont labelFont;
	private TrueTypeFont startButtonFont;

	private final int[] playerPanelCenterOffset = {85, 70};
	private final int[] playerPanelLabelOffset = {85, 75};
	private final int[] mainTextOffset = {250, 75};
	private final int[] portNumberPosition = {500, 290};
	private final int[] mainStartButtonOffset = {150, 50};
	
	private Animation[] waitingAnimations;
	
	private Button startButton;
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		super.init(container, game);
		
		mainStatusFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 32), true);
		portNumberFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 48), true);
		labelFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 19), true);
		startButtonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 28), true);
		
		startButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_orange.png", 
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_orange_down.png",
				mainPanelPosition[0]+mainStartButtonOffset[0],
				mainPanelPosition[1]+mainStartButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("started game!");
						
					}
				});
		startButton.setEnable(false);
		
		
		waitingAnimations = new Animation[3];
		
		/*
		SpriteSheet sheet = new SpriteSheet(GUI.RESOURCES_PATH+"waiting.png", 60, 60);
		for (int A=0; A<3; ++A) {
			waitingAnimations[A] = new Animation();
			for (int i=0; i<60; ++i) {
				waitingAnimations[A].addFrame(sheet.getSprite(i%8, i/8), 16);
			}
			waitingAnimations[A].setCurrentFrame(A*(waitingAnimations[A].getFrameCount()/3));
		}	
		*/
		
		SpriteSheet sheet = new SpriteSheet(GUI.RESOURCES_PATH+"waiting2.png", 60, 60);
		for (int A=0; A<3; ++A) {
			waitingAnimations[A] = new Animation();
			for (int i=0; i<30; ++i) {
				waitingAnimations[A].addFrame(sheet.getSprite(i%5, i/5), 32);
			}
			waitingAnimations[A].setCurrentFrame(A*(waitingAnimations[A].getFrameCount()/3));
		}	
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		super.update(container, game, delta);
		
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
		
		super.render(container, game, g);
		
		drawPortNumber(g);
		drawPlayerNamesAndStatuses(container, g);
	}

	private void drawPortNumber(Graphics g) {
		g.setColor(Color.white);
		GUI.drawStringCentered(g, portNumberFont, Color.white, "Port: 80", portNumberPosition[0], portNumberPosition[1]);
	}
	
	
	
	
	private void drawPlayerNamesAndStatuses(GUIContext container, Graphics g) {
		
		// for testing!!!!!!!
		boolean playerJoined[] = {false, false, false, true, true, true, true, true};
		int hostIndex = 4;
		boolean isHost = true;
		boolean enoughPlayers = true;
		
		g.setColor(Color.white);
		GUI.drawStringCentered(g, infoFont, Color.white, "Player0", mainPanelPosition[0]+mainNameOffset[0],
				mainPanelPosition[1]+mainNameOffset[1]);
		
		for (int i=1; i<8; ++i) {
			
			if (i==hostIndex) {
				
				g.setColor(Color.white);
				GUI.drawStringCentered(g, infoFont, Color.white, "Player"+i, playerPanelPositions[i][0]+playerNameOffset[0],
						playerPanelPositions[i][1]+playerNameOffset[1]);
				
				
				drawLabelCentered(g, labelFont, "HOST", Color.white, new Color(142, 68, 173, 255),
						playerPanelPositions[i][0]+playerPanelLabelOffset[0], 
						playerPanelPositions[i][1]+playerPanelLabelOffset[1]);
				
			}
			else if (playerJoined[i]) {
				
				g.setColor(Color.white);
				GUI.drawStringCentered(g, infoFont, Color.white, "Player"+i, playerPanelPositions[i][0]+playerNameOffset[0],
						playerPanelPositions[i][1]+playerNameOffset[1]);
						
				/*
				drawLabelCentered(g, labelFont, "JOINED", Color.white, new Color(52, 152, 219, 255),
						playerPanelPositions[i][0]+playerPanelLabelOffset[0], 
						playerPanelPositions[i][1]+playerPanelLabelOffset[1]);
				*/
				GUI.drawStringCentered(g, labelFont, Color.white, "JOINED",
						playerPanelPositions[i][0]+playerPanelLabelOffset[0], 
						playerPanelPositions[i][1]+playerPanelLabelOffset[1]);
			}
			else {
				Animation waitingAnimation = waitingAnimations[i%3];
				waitingAnimation.draw(playerPanelPositions[i][0]+playerPanelCenterOffset[0]-waitingAnimation.getWidth()/2,
						playerPanelPositions[i][1]+playerPanelCenterOffset[1]-waitingAnimation.getHeight()/2);
			}
		}
		
		g.setColor(Color.white);
		if (!enoughPlayers) {
			GUI.drawStringCentered(g, mainStatusFont, Color.white, "Waiting for more players ...", 
					mainPanelPosition[0]+mainTextOffset[0],
					mainPanelPosition[1]+mainTextOffset[1]);
		}
		else {
			if (!isHost) {
				GUI.drawStringCentered(g, mainStatusFont, Color.white, "Waiting for host to start game ...", 
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

	private void drawLabelCentered(Graphics g, TrueTypeFont font, String s, Color textColor, Color labelColor, int x, int y) {
		
		int textWidth = font.getWidth(s);
		int textHeight = font.getHeight(s);
		int labelWidth = textWidth+20;
		int labelHeight = textHeight+2;
		
		g.setColor(labelColor);
		g.fillRoundRect(x-labelWidth/2, y-labelHeight/2, labelWidth, labelHeight, 4);
		
		g.setColor(textColor);
		font.drawString(x-textWidth/2, y-textHeight/2, s);
	}
	

	
	@Override
	public int getID() {
		
		return 2;
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