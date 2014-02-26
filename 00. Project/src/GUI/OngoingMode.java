package GUI;


import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.StateBasedGame;

import Poker.Poker;

public class OngoingMode extends TableMode {
	
	
	private Image[][] cardFaces;
	private Image cardBack;
	
	private Card[] centerCards;
	private Card[][] playerCards;
	
	private final int[][] centerCardPositions = {{283, 230}, {373, 230}, {463, 230}, {553, 230}, {643, 230}};
	
	private final int[][] mainPlayerCardOffsets = {{172, 35}, {254, 35}};
	
	private final int[] mainCheckButtonOffset = {10, 35};
	private final int[] mainFoldButtonOffset = {10, 90};
	private final int[] mainRaiseButtonOffset = {340, 90};
	private final int[] mainAllInButtonOffset = {440, 54};
	private final int[] mainRaiseTextFieldOffset = {340, 35};
	
	private final int[][] playerCardOffsets = {{7, 32}, {89, 32}};
	
	
	
	private Image chip;
	private Image chipBig;
	private Image dealerChip;

	private TrueTypeFont infoFontBig;
	
	private TrueTypeFont buttonFont;
	private TrueTypeFont allInButtonFont;
	

	
	Button foldButton;
	Button checkButton;
	Button raiseButton;
	Button allInButton;
	
	RaiseTextField raiseTextField;

	
	
	public void init(GameContainer container, StateBasedGame game)throws SlickException {
		
		super.init(container, game);


		// load images of chips
		chip = new Image(GUI.RESOURCES_PATH + "chip25.png");
		chipBig = new Image(GUI.RESOURCES_PATH + "chip30.png");
		dealerChip = new Image(GUI.RESOURCES_PATH + "dchip25.png");
		
		// load UI fonts
		infoFontBig = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 26), true);
		buttonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 24), true);
		allInButtonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 16), true);		
		
		
		
		// load 52 cards
		cardFaces = new Image[4][14];	// extra column so card values match index
		String indexStr;
		for (int i=1; i<=13; ++i) {
			indexStr = String.format("%02d", i);
			cardFaces[0][i] = new Image(GUI.RESOURCES_PATH+GUI.CARDSPRITES_FOLDER+"c"+indexStr+".png");
			cardFaces[1][i] = new Image(GUI.RESOURCES_PATH+GUI.CARDSPRITES_FOLDER+"d"+indexStr+".png");
			cardFaces[2][i] = new Image(GUI.RESOURCES_PATH+GUI.CARDSPRITES_FOLDER+"h"+indexStr+".png");
			cardFaces[3][i] = new Image(GUI.RESOURCES_PATH+GUI.CARDSPRITES_FOLDER+"s"+indexStr+".png");
		}
		// load card back
		cardBack = new Image(GUI.RESOURCES_PATH+GUI.CARDSPRITES_FOLDER+"BackBlue.png");
		
		
				
		centerCards = new Card[5];
		for (int i=0; i<5; ++i) {
			centerCards[i] = new Card(cardBack, cardFaces[i%4][13-i], centerCardPositions[i][0], centerCardPositions[i][1], true);
		}
		playerCards = new Card[8][2];
		for (int i=0; i<8; ++i) {
			int[] cardPosition = getPlayerCardPosition(i, 0);
			playerCards[i][0] = new Card(cardBack, cardFaces[3][1], cardPosition[0], cardPosition[1], true);
			cardPosition = getPlayerCardPosition(i, 1);
			playerCards[i][1] = new Card(cardBack, cardFaces[3][1], cardPosition[0], cardPosition[1], true);
		}

		
		raiseTextField = new RaiseTextField(container,
				mainPanelPosition[0] + mainRaiseTextFieldOffset[0],
				mainPanelPosition[1] + mainRaiseTextFieldOffset[1]);
		
		
		
		checkButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_blue.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_blue_down.png",
				mainPanelPosition[0] + mainCheckButtonOffset[0], mainPanelPosition[1] + mainCheckButtonOffset[1],
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("checked/called!");					
					}
		});
		
		foldButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_red.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_red_down.png",
				mainPanelPosition[0] + mainFoldButtonOffset[0], mainPanelPosition[1] + mainFoldButtonOffset[1],
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("folded!");
					}
		});
				
		raiseButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_green.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_green_down.png",
				mainPanelPosition[0] + mainRaiseButtonOffset[0], mainPanelPosition[1] + mainRaiseButtonOffset[1],
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("raised!");
					}
		});
		
		allInButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_purple.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_purple_down.png",
				mainPanelPosition[0] + mainAllInButtonOffset[0], mainPanelPosition[1] + mainAllInButtonOffset[1],
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("all in!");
					}
		});
	
	}
	private int[] getPlayerCardPosition(int playerIndex, int cardIndex) {
		int[] ret = new int[2];
		if (playerIndex==0) {
			ret[0] = mainPanelPosition[0] + mainPlayerCardOffsets[cardIndex][0];
			ret[1] = mainPanelPosition[1] + mainPlayerCardOffsets[cardIndex][1];
		}
		else {
			ret[0] = panelPositions[playerIndex][0] + playerCardOffsets[cardIndex][0];
			ret[1] = panelPositions[playerIndex][1] + playerCardOffsets[cardIndex][1];
		}
		return ret;
	}
	
	
	
	
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		super.update(container, game, delta);

		// update all cards
		for (int i=0; i<5; ++i) {
			centerCards[i].update(delta);
		}
		for (int i=0; i<8; ++i) {
			playerCards[i][0].update(delta);
			playerCards[i][1].update(delta);
		}
		
		
		
		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_1))
			game.enterState(1);
		else if (container.getInput().isKeyPressed(Input.KEY_2))
			game.enterState(2);
		else if (container.getInput().isKeyPressed(Input.KEY_4))
			game.enterState(4);
		
		// TEST!!!
		if (container.getInput().isKeyPressed(Input.KEY_F)) {
			for (int i=0; i<5; ++i) {
				centerCards[i].flip();
			}
		}
		// TEST!!
		if (container.getInput().isKeyPressed(Input.KEY_G)) {
			checkButton.setEnable(!checkButton.getEnable());
			foldButton.setEnable(!foldButton.getEnable());
			raiseButton.setEnable(!raiseButton.getEnable());
			allInButton.setEnable(!allInButton.getEnable());
			raiseTextField.setEnable(!raiseTextField.getEnable());
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		super.render(container, game, g);
		
		drawPlayerNames(g);		// draw player names
		drawChipAmounts(g);
		drawInteractiveElements(container, g);
		
		drawDealerChip(g, 0);
		
		// draw cards
		for (int i=0; i<5; ++i) {
			centerCards[i].draw();
		}
		for (int i=0; i<8; ++i) {
			playerCards[i][0].draw();
			playerCards[i][1].draw();
		}
	}

	
	
	private void drawInteractiveElements(GameContainer container, Graphics g) {
		g.setColor(Color.white);
		checkButton.render(g, buttonFont, "Check");
		foldButton.render(g,  buttonFont, "Fold");
		raiseButton.render(g, buttonFont, "Raise");
		allInButton.render(g, allInButtonFont, "All In");
		
		raiseTextField.render(container, g);
	}


	
	private void drawPlayerNames(Graphics g) {
		g.setColor(Color.white);

		drawStringCentered(g, infoFont, "Player0", mainPanelPosition[0]+mainPlayerNameOffset[0],
				mainPanelPosition[1]+mainPlayerNameOffset[1]);
		
		for (int i=1; i<8; ++i) {
			drawStringCentered(g, infoFont, "Player"+i, panelPositions[i][0]+playerNameOffset[0],
					panelPositions[i][1]+playerNameOffset[1]);
		}
	}
	
	private void drawChipAmounts(Graphics g) {
		drawChipAmountBig(g, 200, 485, 340);
		
		drawChipAmount(g, 100, 287, 400);	// mainplayer
		
		drawChipAmount(g, 25, 287, 155);
		drawChipAmount(g, 25, 487, 155);
		drawChipAmount(g, 25, 687, 155);
		
		drawChipAmount(g, 25, 97, 245);
		drawChipAmount(g, 25, 97, 435);
		
		drawChipAmount(g, 25, 877, 245);
		drawChipAmount(g, 25, 877, 435);
	}
	
	private void drawDealerChip(Graphics g, int player) {
		final int[][] dealerChipPositions = {
			{252, 400}, {62, 435}, {62, 245}, {252, 155}, {452, 155}, {652, 155}, {842, 245}, {842, 435}
		};
		dealerChip.draw(dealerChipPositions[player][0]+5, dealerChipPositions[player][1]);
	}
	private void drawChipAmount(Graphics g, int amount, int x, int y) {
		chip.draw(x, y);
		g.setColor(Color.white);
		infoFont.drawString(x+30, y+12-infoFont.getHeight()/2, "$"+amount);
	}
	private void drawChipAmountBig(Graphics g, int amount, int x, int y) {
		chipBig.draw(x, y);
		g.setColor(Color.white);
		infoFontBig.drawString(x+40, y+15-infoFontBig.getHeight()/2, "$"+amount);
	}
	
	
	

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
card1: 7,32
card2: 89,32
chip: 72,145

mainPlayerBox
500 x 150
location: 250,430
offset values:
player string: 250,15 (center)
card1: 172,35
card2: 254,35
checkButton: 10,35 150x45
foldButton: 10,90 150x45
raiseButton: 340,90 150x45
allInButton: 440,43 50x26
dollarSign: 340,54 (left-center)
raiseBy: 350,35
*/