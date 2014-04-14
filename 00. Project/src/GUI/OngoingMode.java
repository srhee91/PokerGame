package GUI;


import java.awt.Font;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

import GameState.Gamestate;
import Host.GameSystem.GameSystem;
import Host.GameSystem.Player;
import Network.HostSearcher;
import Network.UserAction;

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
	
	private final Color winnerLabelColor = new Color(212, 65, 238, 242);
	private final Color thinkingLabelColor = new Color(128, 128, 128, 242);
	private final Color foldLabelColor = new Color(206, 0, 0, 242);
	private final Color raiseLabelColor = new Color(92, 184, 17, 242);
	private final Color checkLabelColor = new Color(30, 98, 208, 242);
	private final Color allInLabelColor = new Color(156, 51, 237, 242);
	//...
	
	private PopupMessageOneButton popupRaiseInvalid;
	
	private PopupMessageTwoButtons popupAllInConfirm;
	private final String allInConfirmString = "Are you sure you want to go all in?";
	
	private PopupMessageOneButton popupLostGame;
	private final String lostGameString = "You lost. Press OK to return to main menu.";
	
	private Cards cards;
	private ChipAmounts chipAmounts;
	private DealerChip dealerChip;

	private TrueTypeFont infoFontBig;
	
	private TrueTypeFont buttonFont;
	private TrueTypeFont allInButtonFont;
	
	
	private String checkButtonString;	// changes between check or call
	private String raiseButtonString;	// changes between bet and raise

	
	Button foldButton;
	Button checkButton;
	Button raiseButton;
	Button allInButton;
	
	RaiseTextField raiseTextField;

	
	private String[] playerNamesLocal;
	
	private Gamestate gameState;
	
	
	// game state flags
	/*
	private boolean playerCardsDealt = false;
	private boolean flopDealt = false;
	private boolean turnDealt = false;
	private boolean riverDealt = false;
	*/
	private int lastFlopState;
	
	@Override
	public void init(GameContainer container, final StateBasedGame game)throws SlickException {
		
		super.init(container, game);
		
		// load UI fonts
		infoFontBig = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 26), true);
		buttonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 24), true);
		allInButtonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 16), true);		

		
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
		cards.resetCards();
		
		// initialize chip amounts
		int[][] playerAmountPositions = new int[8][2];
		playerAmountPositions[0][0] = mainPanelPosition[0]+mainChipAmountOffset[0];
		playerAmountPositions[0][1] = mainPanelPosition[1]+mainChipAmountOffset[1];
		for (int i=1; i<8; ++i) {
			playerAmountPositions[i][0] = playerPanelPositions[i][0]+playerChipAmountOffset[0];
			playerAmountPositions[i][1] = playerPanelPositions[i][1]+playerChipAmountOffset[1];
		}
		chipAmounts = new ChipAmounts(infoFont, infoFontBig, 0, playerAmountPositions);
		
		// initialize dealer chip
		int[][] dealerChipPositions = new int[8][2];
		dealerChipPositions[0][0] = mainPanelPosition[0]+mainDealerChipOffset[0];
		dealerChipPositions[0][1] = mainPanelPosition[1]+mainDealerChipOffset[1];
		for (int i=1; i<8; ++i) {
			dealerChipPositions[i][0] = playerPanelPositions[i][0]+playerDealerChipOffset[0];
			dealerChipPositions[i][1] = playerPanelPositions[i][1]+playerDealerChipOffset[1];
		}
		dealerChip = new DealerChip(dealerChipPositions, 0);
		
		
		popupLostGame = new PopupMessageOneButton(container, lostGameString,
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent arg0) {	// ok action
						// disconnect from host, return to main menu
						GUI.cmh.close();
						game.enterState(1);
					}
				});

		
		popupRaiseInvalid = new PopupMessageOneButton(container, "",	// raise error msg will be set manually
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent arg0) {	// ok action
						// close popup, re-enable gui, erase raise amount
						popupRaiseInvalid.setInvisible();
						raiseTextField.setText("");
						setButtonsEnable(true);
					}
				});
		
		
		popupAllInConfirm = new PopupMessageTwoButtons(container, allInConfirmString,
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent arg0) {	// ok action
						popupAllInConfirm.setInvisible();
						if (GUI.cmh!=null) {
							try {
								GUI.cmh.send(new UserAction(UserAction.Action.RAISE_BET, 
										gameState.player[GUI.playerIndexInHost].totalChip));
							} catch (IOException e) {
								System.out.println("failed to send raise (all in)!");
							}
						}
					}
				},
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent arg0) {	// cancel action
						// close popup, enable buttons
						popupAllInConfirm.setInvisible();
						setButtonsEnable(true);
					}
				});
		
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
						
						try {
							if (GUI.cmh!=null)
								GUI.cmh.send(new UserAction(UserAction.Action.CHECK_CALL,
										gameState.highestBet));
						} catch (IOException e) {
							System.out.println("Failed to send user action");
						}
					}
		});
		
		foldButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_red.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_red_down.png",
				mainPanelPosition[0] + mainFoldButtonOffset[0], mainPanelPosition[1] + mainFoldButtonOffset[1],
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("folded!");
						
						try {
							if (GUI.cmh!=null)
								GUI.cmh.send(new UserAction(UserAction.Action.FOLD, 0));
						} catch (IOException e) {
							System.out.println("Failed to send user action");
						}
					}
		});
				
		raiseButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_green.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_green_down.png",
				mainPanelPosition[0] + mainRaiseButtonOffset[0], mainPanelPosition[1] + mainRaiseButtonOffset[1],
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("raised!");
						
						try {
							if (GUI.cmh!=null) {
								
								String raiseAmtString = raiseTextField.getText();
								int raiseAmount = 0;
								if (!raiseAmtString.isEmpty()) {
									raiseAmount = Integer.parseInt(raiseAmtString);
									
									if (raiseAmount <= gameState.highestBet) {
										// must raise to at least the highest bet
										setButtonsEnable(false);
										popupRaiseInvalid.setMessageString("Must raise to an amount above $"+gameState.highestBet);
										popupRaiseInvalid.setVisible(raiseButton);
									} else if (raiseAmount >= gameState.player[GUI.playerIndexInHost].totalChip) {
										// assume this means all in, show all in popup 
										raiseTextField.setText(""+gameState.player[GUI.playerIndexInHost].totalChip);
										setButtonsEnable(false);
										popupAllInConfirm.setVisible(raiseButton);
									}
									
								} else {
									// no amount entered
									setButtonsEnable(false);
									popupRaiseInvalid.setMessageString("No raise amount entered!");
									popupRaiseInvalid.setVisible(raiseButton);
								}
								GUI.cmh.send(new UserAction(UserAction.Action.RAISE_BET, raiseAmount));
							}
						} catch (IOException e) {
							System.out.println("Failed to send user action");
						}
					}
		});
		
		allInButton = new Button(container, GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_purple.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_purple_down.png",
				mainPanelPosition[0] + mainAllInButtonOffset[0], mainPanelPosition[1] + mainAllInButtonOffset[1],
				new ComponentListener() {
					@Override
					public void componentActivated(AbstractComponent source) {
						System.out.println("all in!");
						// show all in popup
						setButtonsEnable(false);
						popupAllInConfirm.setVisible(allInButton);
					}
		});
		
		playerNamesLocal = new String[8];
		for (int i=0; i<8; ++i) {
			playerNamesLocal[i] = null;
		}
		
		lastFlopState = 3;
		
		
		checkButtonString = "Check";
		raiseButtonString = "Bet";
	}
	
	protected void setPlayerNamesLocal(String[] names) {
		this.playerNamesLocal = names;
	}
	
	
	private int hostToLocalIndex(int hostIndex) {
		return (hostIndex + 8 - GUI.playerIndexInHost) % 8;
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		super.update(container, game, delta);
		
		// if HostSearcher is running, stop it
		if (HostSearcher.isRunning())
				HostSearcher.stop();
		/*
		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_1))
			game.enterState(1);
		else if (container.getInput().isKeyPressed(Input.KEY_2))
			game.enterState(2);
		else if (container.getInput().isKeyPressed(Input.KEY_3))
			game.enterState(3);
		*/
		
		// check for received gamestates from host
		if (GUI.cmh != null) {
			Object receivedObject = GUI.cmh.getReceivedObject();
			if (receivedObject!=null) {
				
				if (receivedObject instanceof Gamestate) {
				
					gameState = (Gamestate)receivedObject;
					
					
					// DEBUG: print game state
					System.out.println("\n\n\n\nFlops :");
					if(gameState.flopState == 0)	System.out.println("-");
					else if(gameState.flopState == 1)	for(int k=0; k<3; k++)	System.out.println(gameState.flops[k]);
					else if(gameState.flopState == 2)	for(int k=0; k<4; k++)	System.out.println(gameState.flops[k]);
					else if(gameState.flopState == 3)	for(int k=0; k<5; k++)	System.out.println(gameState.flops[k]);
					System.out.println();
					for(int k=0; k<8; k++){
						if(gameState.player[k] != null){
							if(gameState.dealer == k)	System.out.println("***Dealer***");
							if(gameState.whoseTurn == k)	System.out.println("---Your Turn---");
							System.out.println("Player "+k+":\n" + gameState.player[k]);
						}
					}
					System.out.println("It's player " + gameState.whoseTurn +"'s turn!");
					// DONE printing game state
					
					
					// check if we've lost the game
					if (gameState.player[GUI.playerIndexInHost]==null) {
						setButtonsEnable(false);
						popupLostGame.setVisible(null);
					}
					

					// enable/disable buttons based on if it's our turn
					if (gameState.whoseTurn==GUI.playerIndexInHost) {
						setButtonsEnable(true);
					} else {
						setButtonsEnable(false); 
					}

					// update player total chips
					for (int i=0; i<8; i++) {
							int localIndex = hostToLocalIndex(i);
						if (gameState.player[i] != null) {
							int amount = gameState.player[i].totalChip;
							chipAmounts.setPlayerAmount(localIndex, amount);
							System.out.println("\tplayer "+i+" current bet: "+amount);
						} else {
							chipAmounts.setPlayerAmount(localIndex, 0);
						}
					}
						
					// update pot amounts
					System.out.println("");
					Host.GameSystem.Pot pot = gameState.potTotal;
					for (int i=0; i<8; i++) {
						if (pot != null) {
							chipAmounts.setPotAmount(i, pot.totalPot);
							System.out.println("\tpot "+i+": "+pot.totalPot);
							pot = pot.splitPot;
						} else {
							chipAmounts.setPotAmount(i, 0);
						}
					}
					
					
					// fold player's cards 
					for (int i=0; i<8; i++) {
						if (gameState.player[i]!=null
								&& gameState.player[i].hasFolded) {
							cards.fold(hostToLocalIndex(i));
						}
					}

	
// FLOPSTATE PROCESSING --------------------------------------------------------------------------------------
					
					switch (gameState.flopState) {
					
					case 0:
						
						// deal if cards haven't been dealt
						if (lastFlopState == 3) {
							
							// update dealer chip position
							int localDealerIndex = hostToLocalIndex(gameState.dealer);
							dealerChip.moveTo(localDealerIndex);
							
							
							Host.GameSystem.Card[] hand = gameState.player[GUI.playerIndexInHost].hand;
							
							for(int i=0; i<8; i++) {
								if(gameState.player[i]!=null)
									System.out.println("player " + i + "cards"+gameState.player[i].hand);
							}
							
							cards.playerCards[0][0].setFaceImage(hand[0]);
							cards.playerCards[0][1].setFaceImage(hand[1]);
							
							cards.resetCards();
							cards.dealCards(localDealerIndex, playerNamesLocal);
							cards.showPlayerCards(0);
						}
						break;
						
					case 1:
						
						if (lastFlopState==0) {
							
							collectBets();
							
							// flip over flop cards
							for (int i=0; i<3; ++i) {
								cards.centerCards[i].setFaceImage(gameState.flops[i]);
							}
							cards.dealFlop();
						}
						break;
					case 2:
						
						if (lastFlopState==1) {
							
							collectBets();
							
							// flip over turn card
							cards.centerCards[3].setFaceImage(gameState.flops[3]);							
							cards.dealTurn();
						}
						break;
					case 3:
						if (lastFlopState==2) {
							
							collectBets();
							
							// flip over river card
							cards.centerCards[4].setFaceImage(gameState.flops[4]);
							cards.dealRiver();
						}
						break;
					}
					

					lastFlopState = gameState.flopState;
					
					
				}	// END GAMESTATE PROCESSING
				
				else {
					System.out.println("unexpected object type received in OngoingMode");
				}
			}
		}
		
		
		// TEST!!!
		/*
		if (container.getInput().isKeyPressed(Input.KEY_F)) {
			
			cards.resetCards();
			
			boolean[] existPlayer = new boolean[8];
			for (int i=0; i<8; ++i) {
				existPlayer[i] = playerNamesLocal[i]!=null;
			}
			
			//cards.dealCards((int)(Math.random()*8.0), playerNamesLocal);
			String[] t = new String[8];
			for (int i=0; i<8; ++i) {
				if (Math.random()<0.5)
				t[i] = "e";
			}
			cards.dealCards((int)(Math.random()*8.0), t);
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
		else if (container.getInput().isKeyPressed(Input.KEY_H)) {
			checkButton.setAlphaWhileDisabled(1.5f-checkButton.getAlphaWhileDisabled());
			foldButton.setAlphaWhileDisabled(1.5f-foldButton.getAlphaWhileDisabled());
			raiseButton.setAlphaWhileDisabled(1.5f-raiseButton.getAlphaWhileDisabled());
			allInButton.setAlphaWhileDisabled(1.5f-allInButton.getAlphaWhileDisabled());
			raiseTextField.setAlphaWhileDisabled(1.5f-raiseTextField.getAlphaWhileDisabled());
		}
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
		*/
		
		
		
		// update all cards
		cards.update(delta);
		
		// update all chip amounts
		chipAmounts.update(delta);
		
		// update dealer chip
		dealerChip.update(delta);
	}
	
	
	private void collectBets() {
		
		// put player bets into main pot
		for (int i=0; i<8; ++i) {
			Player player = gameState.player[i];
			if (player!=null && player.betAmount>0) {
				int localIndex = hostToLocalIndex(i);
				chipAmounts.addSendToQueue(
						player.betAmount,
						true, localIndex,
						false, 0,	// send to main pot
						0.1, false);
			}
		}
		// split main pot into side pots if necessary
		Host.GameSystem.Pot pot = gameState.potTotal;
		for (int i=0; i<8; i++) {
			if (pot==null)
				break;
			chipAmounts.addSendToQueue(
					pot.totalPot,
					false, 0,	// take from main pot
					false, i,	// send to whichever sidepot
					0.0, true);
			pot = pot.splitPot;
		}
	}
	

	
	protected void setButtonsEnable(boolean enable) {
		
		checkButton.setEnable(enable);
		foldButton.setEnable(enable);
		raiseButton.setEnable(enable);
		allInButton.setEnable(enable);
		raiseTextField.setEnable(enable);
		
		if (enable) {
			
			// update call/check label
			int highestBet = gameState.highestBet;
			int currentBet = gameState.player[GUI.playerIndexInHost].betAmount;
			checkButtonString = (highestBet==currentBet) ? "Check" : "Call $"+highestBet;
			// update bet/raise label
			if (highestBet==0 || 
					(gameState.flopState==0 && gameState.bigBlinder==GUI.playerIndexInHost
					&& gameState.highestBet==gameState.blind)) {
				raiseButtonString = "Bet";
				raiseTextField.setRaiseByString("     Bet:");
			} else {
				raiseButtonString = "Raise";
				raiseTextField.setRaiseByString("Raise to:");
			}
		
			// if current bet is more than what I have, the only options are all in and fold
			if (currentBet >= gameState.player[GUI.playerIndexInHost].totalChip) {
				checkButton.setEnable(false);
				raiseButton.setEnable(false);
			}
		} 
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
		if (gameState==null)
			return;
		
		for (int i=0; i<8; ++i) {
			if (gameState.player[i] != null) {
				
				int localIndex = hostToLocalIndex(i);
				
				if (i==gameState.whoseTurn) {
					drawPlayerLabel(g, localIndex, "Thinking...", Color.white, thinkingLabelColor);
					continue;
				}
				else {
					
					int betAmount = gameState.player[i].betAmount;
					
					UserAction lastAction = gameState.player[i].latestAction;
					if (lastAction!=null) {
						switch (lastAction.action) {
						case CHECK_CALL:
							drawPlayerLabel(g, localIndex, "Call $"+betAmount, Color.white, checkLabelColor);
							break;
						case FOLD:
							drawPlayerLabel(g, localIndex, "Fold", Color.white, foldLabelColor);
							break;
						case RAISE_BET:
							drawPlayerLabel(g, localIndex, "Raise $"+betAmount, Color.white, raiseLabelColor);
							break;
						default:
							drawPlayerLabel(g, localIndex, "All in $"+betAmount, Color.white, allInLabelColor);
							break;
						}
					}
				
				}
			}
		}
	}
	
		
	
	private void drawInteractiveElements(GUIContext container, Graphics g) {
		g.setColor(Color.white);
		checkButton.render(container, g, buttonFont, Color.white, checkButtonString);
		foldButton.render(container, g,  buttonFont, Color.white, "Fold");
		raiseButton.render(container, g, buttonFont, Color.white, raiseButtonString);
		allInButton.render(container, g, allInButtonFont, Color.white, "All In");
		
		raiseTextField.render(container, g);
	}


	
	private void drawPlayerNames(Graphics g) {

		GUI.drawStringCenter(g, infoFont, Color.white, playerNamesLocal[0], mainPanelPosition[0]+mainNameOffset[0],
				mainPanelPosition[1]+mainNameOffset[1]);
		
		for (int i=1; i<8; ++i) {
			if (playerNamesLocal[i] != null) {
				GUI.drawStringCenter(g, infoFont, Color.white, playerNamesLocal[i], playerPanelPositions[i][0]+playerNameOffset[0],
						playerPanelPositions[i][1]+playerNameOffset[1]);
			}
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