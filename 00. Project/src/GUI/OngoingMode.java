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
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class OngoingMode extends BasicGameState {
	

	private static final String RESOURCES_PATH = "./resources/";
	private static final String CARDSPRITES_FOLDER = "cardsprites2/";
	private static final String BUTTONS_FOLDER = "buttons/";
		
	private Image background;	
	
	private Image[][] cardFaces;
	private Image cardBack;
	
	private Card[] centerCards;
	private Card[][] playerCards;
	
	private final int[][] centerCardPositions = {{283, 230}, {373, 230}, {463, 230}, {553, 230}, {643, 230}};
	private final int[][][] playerCardPositions = {
			{{422, 465}, {504, 465}},
			{{32, 322}, {114, 322}},
			{{32, 132}, {114, 132}},
			{{222, 42}, {304, 42}},
			{{422, 42}, {504, 42}},
			{{622, 42}, {704, 42}},
			{{812, 132}, {894, 132}},
			{{812, 322}, {894, 322}},
		};
	
	private Image chip;
	private Image chipBig;
	private Image dealerChip;

	private TrueTypeFont infoFont;	// UI font
	private TrueTypeFont infoFontBig;
	
	private TrueTypeFont buttonFont;
	private TrueTypeFont allInButtonFont;
	
	private TrueTypeFont raiseByFont;
	private TrueTypeFont textFieldFont;
	private TrueTypeFont dollarSignFont;	
	
	private TextField textField;
	
	
	Button foldButton;
	Button checkButton;
	Button raiseButton;
	Button allInButton;
	
	
	
	
	public void init(GameContainer container, StateBasedGame game)throws SlickException {
		
		// load background image of table surface
		background = new Image("./resources/table_background.jpg");
				
		// load images of chips
		chip = new Image(RESOURCES_PATH + "chip25.png");
		chipBig = new Image(RESOURCES_PATH + "chip30.png");
		dealerChip = new Image(RESOURCES_PATH + "dchip25.png");
		
		
		// load UI fonts
		infoFont = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 16), true);
		infoFontBig = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 26), true);
		buttonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 24), true);
		allInButtonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 16), true);		
		raiseByFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.ITALIC, 12), true);
		textFieldFont = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 16), true);
		dollarSignFont = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 18), true);
		
		
		// load 52 cards
		cardFaces = new Image[4][14];	// extra column so card values match index
		String indexStr;
		for (int i=1; i<=13; ++i) {
			indexStr = String.format("%02d", i);
			cardFaces[0][i] = new Image(RESOURCES_PATH+CARDSPRITES_FOLDER+"c"+indexStr+".png");
			cardFaces[1][i] = new Image(RESOURCES_PATH+CARDSPRITES_FOLDER+"d"+indexStr+".png");
			cardFaces[2][i] = new Image(RESOURCES_PATH+CARDSPRITES_FOLDER+"h"+indexStr+".png");
			cardFaces[3][i] = new Image(RESOURCES_PATH+CARDSPRITES_FOLDER+"s"+indexStr+".png");
		}
		// load card back
		cardBack = new Image(RESOURCES_PATH+CARDSPRITES_FOLDER+"BackBlue.png");
		
		
		textField = new org.newdawn.slick.gui.TextField(container, textFieldFont, 605, 484, 75, 26);
		textField.setBackgroundColor(new Color(1.0f, 1.0f, 1.0f, 0.2f));
		textField.setBorderColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));

				
		centerCards = new Card[5];
		for (int i=0; i<5; ++i) {
			centerCards[i] = new Card(cardBack, cardFaces[i%4][13-i], centerCardPositions[i][0], centerCardPositions[i][1], true);
		}
		playerCards = new Card[8][2];
		for (int i=0; i<8; ++i) {
			playerCards[i][0] = new Card(cardBack, cardFaces[0][1], playerCardPositions[i][0][0], playerCardPositions[i][0][1], true);
			playerCards[i][1] = new Card(cardBack, cardFaces[3][8], playerCardPositions[i][1][0], playerCardPositions[i][1][1], true);
		}

		
		checkButton = new Button(container, RESOURCES_PATH+BUTTONS_FOLDER+"button_blue.png",
				RESOURCES_PATH+BUTTONS_FOLDER+"button_blue_down.png", 260, 465, 
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						
						
						
						System.out.println("checked/called!");
						
						
						
						
					}
		});
		
		foldButton = new Button(container, RESOURCES_PATH+BUTTONS_FOLDER+"button_red.png",
				RESOURCES_PATH+BUTTONS_FOLDER+"button_red_down.png", 260, 520, 
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("folded!");
					}
		});
				
		raiseButton = new Button(container, RESOURCES_PATH+BUTTONS_FOLDER+"button_green.png",
				RESOURCES_PATH+BUTTONS_FOLDER+"button_green_down.png", 590, 520, 
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("raised!");
					}
		});
		
		allInButton = new Button(container, RESOURCES_PATH+BUTTONS_FOLDER+"button_purple.png",
				RESOURCES_PATH+BUTTONS_FOLDER+"button_purple_down.png", 690, 484, 
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("all in!");
					}
		});
	
	}
	
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		/*
		if (container.getInput().isKeyPressed(Input.KEY_1)) {
			game.enterState(1);
		}*/
		
		// update all cards
		for (int i=0; i<5; ++i) {
			centerCards[i].update(delta);
		}
		for (int i=0; i<8; ++i) {
			playerCards[i][0].update(delta);
			playerCards[i][1].update(delta);
		}
		
		
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
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		background.draw(0, 0, container.getWidth(), container.getHeight());
		
		drawPanels(g);			// draw all translucent rects
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
		g.setFont(buttonFont);
		checkButton.render(g,  "Check");
		foldButton.render(g,  "Fold");
		raiseButton.render(g, "Raise");
		g.setFont(allInButtonFont);
		allInButton.render(g, "All In");
		
		// 250, 430
		g.setFont(dollarSignFont);
		g.drawString("$", 590, 497-dollarSignFont.getHeight()/2);
		g.setFont(raiseByFont);
		g.drawString("Raise by:", 600, 465);
		
		textField.render(container, g);
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
	
	private void drawStringCentered(Graphics g, String s, int x, int y) {
		org.newdawn.slick.Font font = g.getFont();
		g.drawString(s, x-font.getWidth(s)/2, y-font.getHeight(s)/2);
	}
	private void drawChipAmount(Graphics g, int amount, int x, int y) {
		chip.draw(x, y);
		g.setColor(Color.white);
		g.setFont(infoFont);
		g.drawString("$"+amount, x+30, y+12-infoFont.getHeight()/2);
	}
	private void drawChipAmountBig(Graphics g, int amount, int x, int y) {
		chipBig.draw(x, y);
		g.setColor(Color.white);
		g.setFont(infoFontBig);
		g.drawString("$"+amount, x+40, y+15-infoFontBig.getHeight()/2);
	}
	
	
	

	public int getID() {
		return 0;
	}

	/*
	playerbox
	locations: 25,290  25,100  215,10  415,10  615,10  805,100  805,290
	offset values:
	170 x 140
	player string: 85,15 (center)
	card1: 7,32
	card2: 89,32
	chip: 72,145
	
	mainPlayerBox
	location: 250,430
	offset values:
	500 x 150
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
}
