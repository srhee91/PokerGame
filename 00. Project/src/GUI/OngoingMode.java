package GUI;


import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

import Poker.Poker;

public class OngoingMode extends TableMode {
	
	private final int[][] mainCardOffsets = {{172, 35}, {254, 35}};
	private final int[] mainCheckButtonOffset = {10, 35};
	private final int[] mainFoldButtonOffset = {10, 90};
	private final int[] mainRaiseButtonOffset = {340, 90};
	private final int[] mainAllInButtonOffset = {440, 54};
	private final int[] mainRaiseTextFieldOffset = {340, 35};
	private final int[] mainChipAmountOffset = {37, -30};
	private final int[] mainDealerChipOffset = {7, -30};
	
	private final int[][] playerCardOffsets = {{7, 32}, {89, 32}};
	private final int[] playerChipAmountOffset = {72, 145};
	private final int[] playerDealerChipOffset = {42, 145};
	
	//private final Color foldLabelColor = new Color(231, 76, 60, 230);
	private final Color foldLabelColor = new Color(206, 0, 0, 242);
	private final Color raiseLabelColor = new Color(92, 184, 17, 24);
	//private final Color checkLabelColor = new Color();
	//...
	
	private Cards cards;
	private ChipAmounts chipAmounts;
	private DealerChip dealerChip;

	private TrueTypeFont infoFontBig;
	
	private TrueTypeFont buttonFont;
	private TrueTypeFont allInButtonFont;
	
	private TrueTypeFont labelFont;
	
	Button foldButton;
	Button checkButton;
	Button raiseButton;
	Button allInButton;
	
	RaiseTextField raiseTextField;

	
	@Override
	public void init(GameContainer container, StateBasedGame game)throws SlickException {
		
		super.init(container, game);
		
		// load UI fonts
		infoFontBig = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 26), true);
		buttonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 24), true);
		allInButtonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 16), true);		
		labelFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 22), true);
		
		// initialize cards
		int[][][] playerCardPositions = new int[8][2][2];
		playerCardPositions[0][0][0] = mainPanelPosition[0]+mainCardOffsets[0][0];
		playerCardPositions[0][0][1] = mainPanelPosition[1]+mainCardOffsets[0][1];
		playerCardPositions[0][1][0] = mainPanelPosition[0]+mainCardOffsets[1][0];
		playerCardPositions[0][1][1] = mainPanelPosition[1]+mainCardOffsets[1][1];
		for (int i=1; i<8; ++i) {
			playerCardPositions[i][0][0] = playerPanelPositions[i][0]+playerCardOffsets[0][0];
			playerCardPositions[i][0][1] = playerPanelPositions[i][1]+playerCardOffsets[0][1];
			playerCardPositions[i][1][0] = playerPanelPositions[i][0]+playerCardOffsets[1][0];
			playerCardPositions[i][1][1] = playerPanelPositions[i][1]+playerCardOffsets[1][1];
		}
		cards = new Cards(playerCardPositions);
		
		
		// initialize chip amounts
		int[][] playerAmountPositions = new int[8][2];
		playerAmountPositions[0][0] = mainPanelPosition[0]+mainChipAmountOffset[0];
		playerAmountPositions[0][1] = mainPanelPosition[1]+mainChipAmountOffset[1];
		for (int i=1; i<8; ++i) {
			playerAmountPositions[i][0] = playerPanelPositions[i][0]+playerChipAmountOffset[0];
			playerAmountPositions[i][1] = playerPanelPositions[i][1]+playerChipAmountOffset[1];
		}
		chipAmounts = new ChipAmounts(infoFont, infoFontBig, 200, playerAmountPositions);
		
		// initialize dealer chip
		int[][] dealerChipPositions = new int[8][2];
		dealerChipPositions[0][0] = mainPanelPosition[0]+mainDealerChipOffset[0];
		dealerChipPositions[0][1] = mainPanelPosition[1]+mainDealerChipOffset[1];
		for (int i=1; i<8; ++i) {
			dealerChipPositions[i][0] = playerPanelPositions[i][0]+playerDealerChipOffset[0];
			dealerChipPositions[i][1] = playerPanelPositions[i][1]+playerDealerChipOffset[1];
		}
		dealerChip = new DealerChip(dealerChipPositions, 0);
		
		
		

		
		// load buttons and textfield
		
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
	
	
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		super.update(container, game, delta);
		
		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_1))
			game.enterState(1);
		else if (container.getInput().isKeyPressed(Input.KEY_2))
			game.enterState(2);
		else if (container.getInput().isKeyPressed(Input.KEY_3))
			game.enterState(3);

		
		// TEST!!!
		if (container.getInput().isKeyPressed(Input.KEY_F)) {
			
			cards.resetCards();
			
			boolean[] existPlayer = new boolean[8];
			for (int i=0; i<8; ++i) {
				existPlayer[i] = Math.random() < 0.75;
			}
			
			cards.dealCards((int)(Math.random()*8.0), existPlayer);
			cards.showPlayerCards(0);
			
			cards.dealFlop();
			cards.fold((int)(Math.random()*8.0));
			
			cards.dealTurn();
			cards.fold((int)(Math.random()*8.0));
			
			cards.dealRiver();
			cards.showPlayerCards((int)(Math.random()*8.0));
			cards.showPlayerCards((int)(Math.random()*8.0));
			cards.fold((int)(Math.random()*8.0));
			cards.fold(0);
		}
		else if (container.getInput().isKeyPressed(Input.KEY_G)) {
			checkButton.setEnable(!checkButton.getEnable());
			foldButton.setEnable(!foldButton.getEnable());
			raiseButton.setEnable(!raiseButton.getEnable());
			allInButton.setEnable(!allInButton.getEnable());
			raiseTextField.setEnable(!raiseTextField.getEnable());
		}
		/*
		else if (container.getInput().isKeyPressed(Input.KEY_H)) {
			checkButton.setAlphaWhileDisabled(1.5f-checkButton.getAlphaWhileDisabled());
			foldButton.setAlphaWhileDisabled(1.5f-foldButton.getAlphaWhileDisabled());
			raiseButton.setAlphaWhileDisabled(1.5f-raiseButton.getAlphaWhileDisabled());
			allInButton.setAlphaWhileDisabled(1.5f-allInButton.getAlphaWhileDisabled());
			raiseTextField.setAlphaWhileDisabled(1.5f-raiseTextField.getAlphaWhileDisabled());
		}*/
		else if (container.getInput().isKeyPressed(Input.KEY_H)) {
			boolean srcIsPlayer = Math.random()<0.5;
			boolean destIsPlayer = Math.random()<0.5;
			int srcIndex = (int)Math.floor(Math.random()*8.0);
			int destIndex = (int)Math.floor(Math.random()*8.0);
			int amount = (int)Math.floor(Math.random()*11.0);
			chipAmounts.addSendToQueue(amount, srcIsPlayer, srcIndex,
					destIsPlayer, destIndex, 0.0, true);
		}
		else if (container.getInput().isKeyPressed(Input.KEY_J)) {
			int dealer = (int)Math.floor(Math.random()*8.0);
			dealerChip.moveTo(dealer);
		}
		
		
		
		// update all cards
		cards.update(delta);
		
		// update all chip amounts
		chipAmounts.update(delta);
		
		// update dealer chip
		dealerChip.update(delta);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		super.render(container, game, g);
		drawPlayerNames(g);
		
		
		// draw cards, chip amounts, and dealer chip
		// try to draw moving elements on top
		if (dealerChip.isMoving()) {
			chipAmounts.draw(g);
			cards.draw();
			dealerChip.draw();
		} else if (chipAmounts.sendOngoing()) {
			dealerChip.draw();
			cards.draw();
			chipAmounts.draw(g);
		} else {
			chipAmounts.draw(g);
			dealerChip.draw();
			cards.draw();
		}
		
		drawLabels(g);
		drawInteractiveElements(container, g);
	}

	private void drawLabels(Graphics g) {
		for (int i=0; i<8; ++i) {
			if (i%2==0)
				drawPlayerLabel(g, i, labelFont, "Folded", Color.white, foldLabelColor);
			else
				drawPlayerLabel(g, i, labelFont, "Folded", Color.white, raiseLabelColor);
		}
	}
	
	
	private void drawPlayerLabel(Graphics g, int player, TrueTypeFont font, String s,
			Color textColor, Color labelColor) {
		if (player==0) {
			
		} else {
			g.setColor(labelColor);
			g.fillRoundRect(playerPanelPositions[player][0],
					playerPanelPositions[player][1]+62, 170, 40, 0);
			GUI.drawStringCenter(g, labelFont, textColor, s,
					playerPanelPositions[player][0]+82,
					playerPanelPositions[player][1]+82);
		}
	}
	
	
	private void drawInteractiveElements(GUIContext container, Graphics g) {
		g.setColor(Color.white);
		checkButton.render(container, g, buttonFont, Color.white, "Check");
		foldButton.render(container, g,  buttonFont, Color.white, "Fold");
		raiseButton.render(container, g, buttonFont, Color.white, "Raise");
		allInButton.render(container, g, allInButtonFont, Color.white, "All In");
		
		raiseTextField.render(container, g);
	}


	
	private void drawPlayerNames(Graphics g) {
		g.setColor(Color.white);

		GUI.drawStringCenter(g, infoFont, Color.white, "Player0", mainPanelPosition[0]+mainNameOffset[0],
				mainPanelPosition[1]+mainNameOffset[1]);
		
		for (int i=1; i<8; ++i) {
			GUI.drawStringCenter(g, infoFont, Color.white, "Player"+i, playerPanelPositions[i][0]+playerNameOffset[0],
					playerPanelPositions[i][1]+playerNameOffset[1]);
		}
	}
		
	
	public int getID() {
		return 4;
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