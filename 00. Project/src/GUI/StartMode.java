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
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartMode extends BasicGameState
{
	private Image background;

	private int[] menuPanelPosition = {40, 250};
	private int[] menuPanelSize = {220, 230};
	
	private int[] hostGameButtonOffset = {10, 65};
	private int[] joinGameButtonOffset = {10, 120};
	private int[] spectateGameButtonOffset = {10, 175};
	private int[] exitButtonOffset = {10, 270};
	
	private int[] portTextFieldOffset = {160, 10};
	private int[] portTextFieldSize = {50, 40};
	
	private int[] portLabelOffset = {90, 10};

	
	private TrueTypeFont buttonFont;
	private TrueTypeFont textFieldFont;
	private TrueTypeFont portLabelFont;
	
	private Button hostGameButton;
	private Button joinGameButton;
	private Button spectateGameButton;
	private Button exitButton;
	
	private TextField portTextField;

	
	
	
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		background = new Image(GUI.RESOURCES_PATH+"background.png");
		
		buttonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 20), true);
		textFieldFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 28), true);
		portLabelFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 28), true);
		
		
		portTextField = new TextField(container, textFieldFont, menuPanelPosition[0]+portTextFieldOffset[0],
				menuPanelPosition[1]+portTextFieldOffset[1], portTextFieldSize[0], portTextFieldSize[1]);
		portTextField.setBackgroundColor(new Color(255, 255, 255, 32));
		portTextField.setBorderColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		portTextField.setAcceptingInput(true);
		
		
		
		hostGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+hostGameButtonOffset[0],
				menuPanelPosition[1]+hostGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("hosted game!");
						
					}
				});
		
		joinGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+joinGameButtonOffset[0],
				menuPanelPosition[1]+joinGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("joined game!");
						
					}
				});
		
		
		spectateGameButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_lightblue_down.png",
				menuPanelPosition[0]+spectateGameButtonOffset[0],
				menuPanelPosition[1]+spectateGameButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("spectated game!");
						
					}
				});
		
		exitButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_darkred.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_darkred_down.png",
				menuPanelPosition[0]+exitButtonOffset[0],
				menuPanelPosition[1]+exitButtonOffset[1],
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent source) {
						
						System.out.println("exitted game!");
						
					}
				});
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
		
		drawMenuButtons(g);
		
		portLabelFont.drawString(menuPanelPosition[0]+portLabelOffset[0],
				menuPanelPosition[1]+portLabelOffset[1], "Port:");
		portTextField.render(container, g);
	}
	
	private void drawMenuPanel(Graphics g) {
		g.setColor(new Color(64, 64, 64, 160));
		g.fillRoundRect(menuPanelPosition[0], menuPanelPosition[1],
				menuPanelSize[0], menuPanelSize[1], 4);
	}
	
	private void drawMenuButtons(Graphics g) {
		g.setColor(Color.white);
		
		hostGameButton.render(g, buttonFont, "Host Game");
		joinGameButton.render(g, buttonFont, "Join Game");
		spectateGameButton.render(g, buttonFont, "Spectate Game");
		exitButton.render(g, buttonFont, "Exit");
	}
	
	public int getID() {
		return 1;
	}
}
