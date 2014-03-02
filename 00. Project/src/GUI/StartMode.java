package GUI;

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
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartMode extends BasicGameState
{
	private Image background;

	private final int[] menuPanelPosition = {40, 250};
	private final int[] menuPanelSize = {220, 230};
	
	private final int[] hostGameButtonOffset = {10, 65};
	private final int[] joinGameButtonOffset = {10, 120};
	private final int[] spectateGameButtonOffset = {10, 175};
	private final int[] exitButtonOffset = {10, 270};
	
	private final int[] portTextFieldOffset = {160, 10};
	private final int[] portTextFieldSize = {50, 40};
	
	private final int[] portLabelOffset = {90, 10};

	private final String emptyPortErrorString = "Please enter the port number of the game you want to host/join/spectate.";
	private final String invalidPortErrorString = "No game exists with that port number.";
	private final String namePromptString = "Enter player name:";
	private final String areYouSureExitString = "Are you sure you want to exit to desktop?";
	
	private TrueTypeFont buttonFont;
	private TrueTypeFont portTextFieldFont;
	private TrueTypeFont portLabelFont;
	private TrueTypeFont popupMessageFont;
	
	private Button hostGameButton;
	private Button joinGameButton;
	private Button spectateGameButton;
	private Button exitButton;
	
	private MyTextField portTextField;

	// popup message stuff
	private int[] popupPosition = {0, 150};
	private int[] popupSize = {1000, 300};
	
	private TrueTypeFont popupPromptTextFieldFont;
	
	
	private PopupMessage popupOneButtonMessage;
	private PopupMessage2Buttons popupTwoButtonMessage;
	private PopupPrompt popupTwoButtonPrompt;
	
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		background = new Image(GUI.RESOURCES_PATH+"background.png");
		
		buttonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 20), true);
		portTextFieldFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 28), true);
		portLabelFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 28), true);
		popupMessageFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 28), true);
		popupPromptTextFieldFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 28), true);
		
		
		portTextField = new MyTextField(container, portTextFieldFont, menuPanelPosition[0]+portTextFieldOffset[0],
				menuPanelPosition[1]+portTextFieldOffset[1], portTextFieldSize[0], portTextFieldSize[1]);
		portTextField.setBackgroundColor(new Color(255, 255, 255, 32));
		portTextField.setBorderColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		portTextField.setTextColor(Color.white);
		portTextField.setMaxLength(2);
		portTextField.setNumeralsOnly(true);
		portTextField.setAcceptingInput(true);
		
		
		
		
		
		popupOneButtonMessage = new PopupMessage(container, popupPosition, popupSize, popupMessageFont, buttonFont,
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {	// OK (exit) action
						setMenuEnable(true);
					}
				});
		
		popupTwoButtonMessage = new PopupMessage2Buttons(container, popupPosition, popupSize,
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
		
		popupTwoButtonPrompt = new PopupPrompt(container, popupPosition, popupSize, popupMessageFont, buttonFont,
				popupPromptTextFieldFont,
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {	// OK action
						
						if (source==(AbstractComponent)hostGameButton) {
							System.out.println("Player "+popupTwoButtonPrompt.getText()+" will host");
						}
						else if (source==(AbstractComponent)joinGameButton){
							System.out.println("Player "+popupTwoButtonPrompt.getText()+" will join");
						}
						else {
							System.out.println("SOMETHING'S WRONG");
						}
						
						setMenuEnable(true);
					}
				},
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {	// cancel action
						setMenuEnable(true);
					}
				});
		popupTwoButtonPrompt.setMaxLength(20);
		
		
		
		
		
		
		hostGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+hostGameButtonOffset[0],
				menuPanelPosition[1]+hostGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("host game button pressed!");
						
						String portString = portTextField.getText();
						if (portString.isEmpty()) {
							showOneButtonMessagePopup(source, emptyPortErrorString);
						}
						else {
							int port = Integer.parseInt(portString);
							System.out.println("	creating new game on port "+port);
							showPromptPopup(source, namePromptString);
						}
					}
				});
		hostGameButton.setAlphaWhileDisabled(1.0f);
		
		joinGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+joinGameButtonOffset[0],
				menuPanelPosition[1]+joinGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("join game button pressed!");
						
						String portString = portTextField.getText();
						if (portString.isEmpty()) {
							showOneButtonMessagePopup(source, emptyPortErrorString);
						}
						else {
							int port = Integer.parseInt(portString);
							if (port>9) {
								showOneButtonMessagePopup(source, invalidPortErrorString);
							}
							else {
								System.out.println("	joining game on port "+port);
								showPromptPopup(source, "Enter player name:");
							}
						}
						
					}
				});
		joinGameButton.setAlphaWhileDisabled(1.0f);
		
		spectateGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+spectateGameButtonOffset[0],
				menuPanelPosition[1]+spectateGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("spectate game button pressed!");
						
						String portString = portTextField.getText();
						if (portString.isEmpty()) {
							showOneButtonMessagePopup(source, emptyPortErrorString);
						}
						else {
							int port = Integer.parseInt(portString);
							if (port>9) {
								showOneButtonMessagePopup(source, invalidPortErrorString);
							}
							else {
								System.out.println("	spectating game on port "+port);
							}
						}
					}
				});
		spectateGameButton.setAlphaWhileDisabled(1.0f);
		
		exitButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_darkred.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_darkred_down.png",
				menuPanelPosition[0]+exitButtonOffset[0],
				menuPanelPosition[1]+exitButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("exit button pressed!");
						showMessageTwoButtonPopup(source, areYouSureExitString);
					}
				});
		exitButton.setAlphaWhileDisabled(1.0f);

	}
	
	private void setMenuEnable(boolean enable) {
		hostGameButton.setEnable(enable);
		joinGameButton.setEnable(enable);
		spectateGameButton.setEnable(enable);
		exitButton.setEnable(enable);
		portTextField.setAcceptingInput(enable);
	}
	
	private void showOneButtonMessagePopup(AbstractComponent source, String msg) {
		setMenuEnable(false);
		popupOneButtonMessage.setMessageString(msg);
		popupOneButtonMessage.makeVisible(source);
	}
	
	private void showPromptPopup(AbstractComponent source, String msg) {
		setMenuEnable(false);
		popupTwoButtonPrompt.setMessageString(msg);
		popupTwoButtonPrompt.makeVisible(source);
	}
	
	private void showMessageTwoButtonPopup(AbstractComponent source, String msg) {
		setMenuEnable(false);
		popupTwoButtonMessage.setMessageString(msg);
		popupTwoButtonMessage.makeVisible(source);
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
		
		background.draw(0, 0, container.getWidth(), container.getHeight());
		
		drawMenuPanel(g);
		
		drawMenuButtons(container, g);
		
		portLabelFont.drawString(menuPanelPosition[0]+portLabelOffset[0],
				menuPanelPosition[1]+portLabelOffset[1], "Port:");
		portTextField.render(container, g);
		
		
		// call render on all popup msgs; only visible ones will render
		popupOneButtonMessage.render(container, g);
		popupTwoButtonMessage.render(container, g);
		popupTwoButtonPrompt.render(container, g);
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
