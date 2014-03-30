package GUI;

import java.awt.Font;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartMode extends BasicGameState
{
	private Image background;

	private final int[] menuPanelPosition = {40, 300};
	private final int[] menuPanelSize = {220, 180};
	
	private final int[] hostGameButtonOffset = {10, 15};
	private final int[] joinGameButtonOffset = {10, 70};
	private final int[] spectateGameButtonOffset = {10, 125};
	private final int[] exitButtonOffset = {10, 220};
	

	private final String namePromptString = "Enter player name:";
	private final String areYouSureExitString = "Are you sure you want to exit to desktop?";
	private final String loadingString = "Loading... (TEST! press 'f' to finish)";
	
	private TrueTypeFont buttonFont;
	private TrueTypeFont popupMessageFont;
	
	private Button hostGameButton;
	private Button joinGameButton;
	private Button spectateGameButton;
	private Button exitButton;
	

	// popup message stuff
	private int[] popupPosition = {0, 150};
	private int[] popupSize = {1000, 300};
	
	private TrueTypeFont popupPromptTextFieldFont;
	
	
	private PopupMessageAnimation popupMessageAnimation;
	private PopupMessageTwoButtons popupMessageTwoButtons;
	private PopupPromptTwoButtons popupPromptTwoButtons;
	
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		background = new Image(GUI.RESOURCES_PATH+"background.png");
		
		buttonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 20), true);
		popupMessageFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 28), true);
		popupPromptTextFieldFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 28), true);
		
		/*
		portTextField = new MyTextField(container, portTextFieldFont, menuPanelPosition[0]+portTextFieldOffset[0],
				menuPanelPosition[1]+portTextFieldOffset[1], portTextFieldSize[0], portTextFieldSize[1]);
		portTextField.setBackgroundColor(new Color(255, 255, 255, 32));
		portTextField.setBorderColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		portTextField.setTextColor(Color.white);
		portTextField.setMaxLength(2);
		portTextField.setNumeralsOnly(true);
		portTextField.setAcceptingInput(true);
		*/
		
		
		Animation waitingAnimation = new Animation();
		SpriteSheet sheet = new SpriteSheet(GUI.RESOURCES_PATH+"loading2.png", 32, 32);
		for (int i=0; i<8; ++i) {
			waitingAnimation.addFrame(sheet.getSprite(i, 0), 64);
		}
		popupMessageAnimation = new PopupMessageAnimation(container, popupPosition, popupSize, popupMessageFont,
				waitingAnimation);
		
		
		popupMessageTwoButtons = new PopupMessageTwoButtons(container, popupPosition, popupSize,
				popupMessageFont, buttonFont,
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {	// OK action
						setMenuEnable(true);
						
						if (source==(AbstractComponent)exitButton) {
							System.exit(0);
						}
						else {
							System.out.println("SOMETHING'S WRONG");
						}
					}
				},
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {	// cancel action
						setMenuEnable(true);
					}
				});
		
		popupPromptTwoButtons = new PopupPromptTwoButtons(container, popupPosition, popupSize, popupMessageFont, buttonFont,
				popupPromptTextFieldFont,
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {	// OK action
						setMenuEnable(true);
						
						if (source==(AbstractComponent)hostGameButton) {
							System.out.println("Player "+popupPromptTwoButtons.getText()+" will host");
							
							
							showPopupMessageAnimation(source, loadingString);
							
							// start host process
							
							
						}
						else if (source==(AbstractComponent)joinGameButton){
							System.out.println("Player "+popupPromptTwoButtons.getText()+" will join");
							showPopupMessageAnimation(source, loadingString);
						}
						else {
							System.out.println("SOMETHING'S WRONG");
						}
					}
				},
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {	// cancel action
						setMenuEnable(true);
					}
				});
		popupPromptTwoButtons.setMaxLength(10);
		
		
		
		
		
		
		hostGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+hostGameButtonOffset[0],
				menuPanelPosition[1]+hostGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("host game button pressed!");
						showPopupPromptTwoButtons(source, namePromptString);
					}
				});
		//hostGameButton.setAlphaWhileDisabled(1.0f);
		
		joinGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+joinGameButtonOffset[0],
				menuPanelPosition[1]+joinGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("join game button pressed!");
						showPopupPromptTwoButtons(source, namePromptString);
					}
				});
		//joinGameButton.setAlphaWhileDisabled(1.0f);
		
		spectateGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+spectateGameButtonOffset[0],
				menuPanelPosition[1]+spectateGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("spectate game button pressed!");
						showPopupMessageAnimation(source, loadingString);
					}
				});
		//spectateGameButton.setAlphaWhileDisabled(1.0f);
		
		exitButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_darkred.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_darkred_down.png",
				menuPanelPosition[0]+exitButtonOffset[0],
				menuPanelPosition[1]+exitButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("exit button pressed!");
						showPopupMessageTwoButtons(source, areYouSureExitString);
					}
				});
		//exitButton.setAlphaWhileDisabled(1.0f);

	}
	
	private void setMenuEnable(boolean enable) {
		hostGameButton.setEnable(enable);
		joinGameButton.setEnable(enable);
		spectateGameButton.setEnable(enable);
		exitButton.setEnable(enable);
	}
	
	private void showPopupMessageAnimation(AbstractComponent source, String msg) {
		setMenuEnable(false);
		popupMessageAnimation.setMessageString(msg);
		popupMessageAnimation.makeVisible(source);
	}
	private void showPopupPromptTwoButtons(AbstractComponent source, String msg) {
		setMenuEnable(false);
		popupPromptTwoButtons.setMessageString(msg);
		popupPromptTwoButtons.makeVisible(source);
	}
	private void showPopupMessageTwoButtons(AbstractComponent source, String msg) {
		setMenuEnable(false);
		popupMessageTwoButtons.setMessageString(msg);
		popupMessageTwoButtons.makeVisible(source);
	}
	
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_2))
			game.enterState(2);
		else if (container.getInput().isKeyPressed(Input.KEY_3))
			game.enterState(3);
		else if (container.getInput().isKeyPressed(Input.KEY_4))
			game.enterState(4);

		
		// temporary method for stopping the loading screen
		if (container.getInput().isKeyPressed(Input.KEY_F)) {
			if (popupMessageAnimation.isVisible()) {
				popupMessageAnimation.makeInvisible();
				setMenuEnable(true);
			}
		}
	}
	

	
	
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		background.draw(0, 0, container.getWidth(), container.getHeight());
		
		drawMenuPanel(g);
		
		drawMenuButtons(container, g);

		// call render on all popup msgs; only visible ones will render
		popupMessageAnimation.render(container, g);
		popupMessageTwoButtons.render(container, g);
		popupPromptTwoButtons.render(container, g);
	}
	
	private void drawMenuPanel(Graphics g) {
		g.setColor(new Color(64, 64, 64, 160));
		g.fillRoundRect(menuPanelPosition[0], menuPanelPosition[1],
				menuPanelSize[0], menuPanelSize[1], 4);
	}
	
	private void drawMenuButtons(GUIContext container, Graphics g) {
		g.setColor(Color.white);
		
		hostGameButton.render(container, g, buttonFont, Color.white, "Host Game");
		joinGameButton.render(container, g, buttonFont, Color.white, "Join Game");
		spectateGameButton.render(container, g, buttonFont, Color.white, "Spectate Game");
		exitButton.render(container, g, buttonFont, Color.white, "Exit");
	}
	
	
	public int getID() {
		return 1;
	}
}
