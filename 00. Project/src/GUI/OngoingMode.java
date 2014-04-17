package GUI;


import java.awt.Font;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

import GameState.Gamestate;
import Host.GameSystem.Player;
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
	private final int[] mainTotalAmountOffset = {10, 15};		// added (center-left align)
	
	private final int[][] playerCardOffsets = {{7, 32}, {89, 32}};
	private final int[] playerChipAmountOffset = {72, 145};
	private final int[] playerDealerChipOffset = {42, 145};
	protected final int playerNameOffset[] = {8, 15};			// added, overloads. (center-left align)
	private final int[] playerTotalAmountOffset = {162, 15};		// added (center-right align)
	
	
	//private final Color winnerLabelColor = new Color(212, 65, 238, 242);
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
	private TrueTypeFont totalAmountFont;		// added
	
	Button foldButton;
	Button checkButton;
	Button raiseButton;
	Button allInButton;
	RaiseTextField raiseTextField;

	//private String checkButtonString;	// changes between check or call
	//private String raiseButtonString;	// changes between bet and raise

	private boolean checkOrCall;	// true=check
	private boolean betOrRaise;		// true = bet;
	
	private String[] playerNamesLocal;
	
	//private Gamestate prevGameState;
	private Gamestate gameState;
	
	private int lastFlopState;
	private boolean lostGame;
	
	
	@Override
	public void init(GameContainer container, final StateBasedGame game)throws SlickException {
		
		super.init(container, game);
		
		// load UI fonts
		infoFontBig = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 26), true);
		buttonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 24), true);
		allInButtonFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 16), true);		
		totalAmountFont = new TrueTypeFont(new java.awt.Font("Segoe UI Light", Font.PLAIN, 16), true);
		
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
		cards.collectCards();
		
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
						raiseTextField.setText("");
						setButtonsEnable(true);
					}
				});
		
		
		popupAllInConfirm = new PopupMessageTwoButtons(container, allInConfirmString,
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent arg0) {	// ok action
						//if (GUI.cmh!=null) {
							try {
								Player player = gameState.player[GUI.playerIndexInHost];
								GUI.cmh.send(new UserAction(UserAction.Action.ALL_IN, 
										player.totalChip + player.betAmount));
							} catch (IOException e) {
								System.out.println("failed to send raise (all in)!");
							}
						//}
						// do not re-enable buttons
					}
				},
				new ComponentListener() {
					
					@Override
					public void componentActivated(AbstractComponent arg0) {	// cancel action
						// close popup, enable buttons
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
						try {
							//if (GUI.cmh!=null) {
								GUI.cmh.send(new UserAction(
										checkOrCall ? UserAction.Action.CHECK : UserAction.Action.CALL,
										gameState.highestBet));
								setButtonsEnable(false);
							//}
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
						try {
							//if (GUI.cmh!=null) {
								GUI.cmh.send(new UserAction(UserAction.Action.FOLD, 0));
								setButtonsEnable(false);
							//}
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
						try {
							//if (GUI.cmh!=null) {
								
								String raiseAmtString = raiseTextField.getText();
								int raiseAmount = 0;
								if (!raiseAmtString.isEmpty()) {
									raiseAmount = Integer.parseInt(raiseAmtString);
									
									Player player = gameState.player[GUI.playerIndexInHost];
									int playerTotalPlusBet = player.totalChip + player.betAmount;
									
									if (raiseAmount <= gameState.highestBet) {
										// must raise to above the highest bet
										raiseTextField.setText("");
										setButtonsEnable(false);
										popupRaiseInvalid.setMessageString(betOrRaise
												? "Must bet an amount above $"+gameState.highestBet
												: "Must raise to an amount above $"+gameState.highestBet);
										popupRaiseInvalid.setVisible(raiseButton);
									} else if (raiseAmount >= playerTotalPlusBet) {
										// assume this means all in, show all in popup 
										raiseTextField.setText(""+playerTotalPlusBet);
										setButtonsEnable(false);
										popupAllInConfirm.setVisible(raiseButton);
									} else {
										GUI.cmh.send(new UserAction(
												betOrRaise ? UserAction.Action.BET : UserAction.Action.RAISE,
												raiseAmount));
										setButtonsEnable(false);
									}
									
								} else {
									// no amount entered
									setButtonsEnable(false);
									popupRaiseInvalid.setMessageString("No amount entered!");
									popupRaiseInvalid.setVisible(raiseButton);
								}
								
							//}
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
						// show all in popup
						Player player = gameState.player[GUI.playerIndexInHost];
						int playerTotalPlusBet = player.totalChip + player.betAmount;
						raiseTextField.setText(""+playerTotalPlusBet);
						setButtonsEnable(false);
						popupAllInConfirm.setVisible(allInButton);
					}
		});
		
		playerNamesLocal = new String[8];
		for (int i=0; i<8; ++i) {
			playerNamesLocal[i] = null;
		}
	}
	
	protected void setPlayerNamesLocal(String[] names) {
		this.playerNamesLocal = names;
	}
	
	
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		super.update(container, game, delta);
		
		// on-enter-mode actions
		if (GUI.currentMode != 4) {
			
			//prevGameState = null;
			lostGame = false;
			
			lastFlopState = 3;
			checkOrCall = true;
			betOrRaise = true;
			
			// reset cards
			cards.resetCards();
			
			// clear all chip amounts
			for (int i=0; i<8; i++) {
				chipAmounts.setPlayerAmount(i, 0);
				chipAmounts.setPotAmount(i, 0);
			}
			
			// disable buttons
			setButtonsEnable(false);
			
			GUI.currentMode = 4;
		}
		

		/*
		// temporary method for transitioning between modes
		if (container.getInput().isKeyPressed(Input.KEY_1))
			game.enterState(1);
		else if (container.getInput().isKeyPressed(Input.KEY_2))
			game.enterState(2);
		else if (container.getInput().isKeyPressed(Input.KEY_3))
			game.enterState(3);
		*/
		
		
		// check if we're still connected to host
		if (GUI.hostConnectionError_flag) {
			GUI.hostConnectionError_flag = false;
			setButtonsEnable(false);
			popupHostConnectionLost.setVisible(null);
		}
		
		
		
		// check for received gamestates from host
		if (GUI.cmh != null) {
			Object receivedObject = GUI.cmh.getReceivedObject();
			if (receivedObject!=null) {
				
				if (receivedObject instanceof Gamestate) {
				
					//prevGameState = gameState;
					gameState = (Gamestate)receivedObject;
					
					
					// DEBUG: print game state
					System.out.println("\n\n\n\nFlops :");
					if(gameState.flopState == 0)	System.out.println("-");
					else if(gameState.flopState == 1)	for(int k=0; k<3; k++)	System.out.println(gameState.flops[k]);
					else if(gameState.flopState == 2)	for(int k=0; k<4; k++)	System.out.println(gameState.flops[k]);
					else if(gameState.flopState == 3)	for(int k=0; k<5; k++)	System.out.println(gameState.flops[k]);
					else if(gameState.flopState == 4)	System.out.print("FLOPSTATE_4: round over.");
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
					
					
					// check if we lost the game during the last gameState
					if (lostGame) {
						popupLostGame.setVisible(null);
					}
					
					
					// check if we've lost the game
					if (gameState.player[GUI.playerIndexInHost]==null) {
						setButtonsEnable(false);
						lostGame = true;
					}
					

					// enable/disable buttons based on if it's our turn
					setButtonsEnable(gameState.whoseTurn==GUI.playerIndexInHost);

					
					// update faces of all centercards and player cards
					for (int i=0; i<5; i++) {
						cards.centerCards[i].setFaceImage(gameState.flops[i]);
					}
					for (int i=0; i<8; i++) {
						if (gameState.player[i] !=null) {
							int localIndex = hostToLocalIndex(i);
							Host.GameSystem.Card[] hand = gameState.player[i].hand;
							cards.playerCards[localIndex][0].setFaceImage(hand[0]);
							cards.playerCards[localIndex][1].setFaceImage(hand[1]);
						}
					}
					
					
					// fold player's cards if needed
					for (int i=0; i<8; i++) {
						if (gameState.player[i]!=null
								&& gameState.player[i].hasFolded) {
							cards.fold(hostToLocalIndex(i));
						}
					}

				
					
					// update chip amounts (depends on whoseTurn)
					
					if (gameState.whoseTurn==-2) {
						
						// show animation to collect bets
						for (int i=0; i<8; i++) {
							
							if (gameState.player[i] != null) {
								
								int localIndex = hostToLocalIndex(i);
								int amount = chipAmounts.getPlayerAmount(localIndex);
								if (amount > 0) {
									chipAmounts.addSendToQueue(
											amount,
											true, localIndex,
											false, 0,	// send to main pot
											0.0, false);
								}
							}
						}
						
						// show animation for split pots
						Host.GameSystem.Pot pot = gameState.potTotal;
						for (int i=0; i<8; i++) {
							if (pot==null)
								break;
							int amount = pot.totalPot;
							int oldAmount = chipAmounts.getPotAmount(i);
							if (oldAmount!=amount) {
								chipAmounts.addSendToQueue(
										amount-oldAmount,
										false, 0,	// take from main pot
										false, i,	// send to whichever sidepot
										0.0, true);
							}
							pot = pot.splitPot;
						}
					} else if (gameState.whoseTurn==-3) {
												
						
						int leftOvers[] = new int[8];
						
						// distribute each pot to its winners
						Host.GameSystem.Pot pot = gameState.potTotal;
						for (int potIndex=0; potIndex<8; potIndex++) {
							if (pot==null)
								break;
							
							System.out.println("distributing pot "+potIndex+"...");
							
							// calculate winnings per winner
							int numWinners = 0;
							for (int i=0; i<8; i++) {
								if (pot.winner[i])
									numWinners++;
							}
							int amountPerWinner = pot.totalPot / numWinners;
							leftOvers[potIndex] = amountPerWinner % numWinners;
							
							System.out.println("amt = $"+pot.totalPot);
							System.out.println("amt per winner = $"+amountPerWinner);
							
							// send that amount to each winner of this pot
							boolean first = true;
							for (int i=0; i<8; i++) {
								if (gameState.player[i]!=null && pot.winner[i]) {
									chipAmounts.addSendToQueue(
											amountPerWinner,
											false, potIndex,
											true, hostToLocalIndex(i),
											//500.0, true);
											first ? 500.0 : 0.0, false);
									first = false;
								}
							}
							pot = pot.splitPot;
						}
						
						// collect each pot's leftover into the main pot
						if (gameState.potTotal != null) {
							pot = gameState.potTotal.splitPot;
							for (int potIndex=1; potIndex<8; potIndex++) {
								if (pot==null)
									break;
								if (leftOvers[potIndex] > 0) {
									chipAmounts.addSendToQueue(
											leftOvers[potIndex],
											false, potIndex,
											false, 0,	 // send to main pot
											0.0, true);
								}
								pot = pot.splitPot;
							}
						}
												
						
					} else if (gameState.whoseTurn >= -1) {
						
						// update bets without animation if this gamestate comes
						// before/after a player's turn
						for (int i=0; i<8; i++) {
						
							if (gameState.player[i] != null) {
								int localIndex = hostToLocalIndex(i);
								int amount = gameState.player[i].betAmount;
								chipAmounts.setPlayerAmount(localIndex, amount);
							}
						}
						
						// update pot amounts (should be redundant)
						Host.GameSystem.Pot pot = gameState.potTotal;
						for (int i=0; i<8; i++) {
							if (pot==null)
								break;
							if (chipAmounts.getPotAmount(i) != pot.totalPot) {
								System.out.println("pot "+i+" inconsistent with gamestate!");
								chipAmounts.setPotAmount(i, pot.totalPot);
							}
							pot = pot.splitPot;
						}
					}
					
					
					
					// update cards and dealerchip (depends on flopState)
					
					switch (gameState.flopState) {
					
					case 0:
						
						// when a new hand starts...
						if (lastFlopState != 0) {
							
							// update dealer chip position
							int localDealerIndex = hostToLocalIndex(gameState.dealer);
							dealerChip.moveTo(localDealerIndex);
							
							// reset and deal cards, show main player's cards					
							cards.collectCards();
							cards.dealCards(localDealerIndex, 1000.0, playerNamesLocal);
							cards.showPlayerCards(0);
						}
						break;
						
					case 1:
						
						if (lastFlopState==0) {
							
							cards.dealFlop(1000.0);
						} 
						break;
						
					case 2:
						if (lastFlopState == 1) {
							// flip over turn card						
							cards.dealTurn(1000.0);
							
						} else {
							// flip over flop and turn cards
							cards.dealFlop(1000.0);
							cards.dealTurn(0.0);							
						}
						break;
						
					case 3:
						if (lastFlopState==2) {
							cards.dealRiver(1000.0);
							
						} else if (lastFlopState==1) {
							// flip over turn and river cards
							cards.dealTurn(1000.0);
							cards.dealRiver(0.0);
						} else {
							// flip over flop, turn, and river cards
							cards.dealFlop(1000.0);
							cards.dealTurn(0.0);
							cards.dealRiver(0.0);
						}
						break;
						
					case 4:

						// if showdown occurred, need to check which flopstate
						// we were in when everyone went all in
						if (gameState.showdown) {
							
							// reveal everyone's cards who haven't folded
							for (int i=0; i<8; i++) {
								if (gameState.player[i]!=null && !gameState.player[i].hasFolded) {
									cards.showPlayerCards(hostToLocalIndex(i));
								}
							}
							
							// reveal any flop cards that haven't been revealed yet
							switch (lastFlopState) {
							case 0:
								cards.dealFlop(0.0);
								cards.dealTurn(0.0);
								cards.dealRiver(0.0);
								break;
							case 1:
								cards.dealTurn(0.0);
								cards.dealRiver(0.0);
								break;
							case 2:
								cards.dealRiver(0.0);
								break;
							}	
						}
						
						break;
					}
					lastFlopState = gameState.flopState;	
					
				}	
				
				else {
					System.out.println("unexpected object type received in OngoingMode");
				}
			}
		}
		
		
		// TEST!!!
		/*
		if (container.getInput().isKeyPressed(Input.KEY_F)) {
			
			cards.resetCards();

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
		*/
		
				
		
		// update all cards
		cards.update(delta);
		
		// update all chip amounts
		chipAmounts.update(delta);
		
		// update dealer chip
		dealerChip.update(delta);
	}
	


	
	protected void setButtonsEnable(boolean enable) {
		
		checkButton.setEnable(enable);
		foldButton.setEnable(enable);
		raiseButton.setEnable(enable);
		allInButton.setEnable(enable);
		raiseTextField.setEnable(enable);
		
		if (enable && gameState!=null) {
			
			// update call/check label
			int highestBet = gameState.highestBet;
			int currentBet = gameState.player[GUI.playerIndexInHost].betAmount;
			checkOrCall = (highestBet==currentBet);
			// update bet/raise label
			if (highestBet==0 || 
					(gameState.flopState==0 && gameState.bigBlinder==GUI.playerIndexInHost
					&& gameState.highestBet==gameState.blind)) {
				betOrRaise = true;
				raiseTextField.setRaiseByString("Bet:");
			} else {
				betOrRaise = false;
				raiseTextField.setRaiseByString("Raise to:");
			}
		
			// if current bet is more than what I have, the only options are all in and fold
			Player player = gameState.player[GUI.playerIndexInHost];
			int playerTotalPlusBet = player.totalChip + player.betAmount;
			if (highestBet >= playerTotalPlusBet) {
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
		drawTotalAmounts(g);
		drawInteractiveElements(container, g);
		
		popupHostConnectionLost.render(container, g);
		popupRaiseInvalid.render(container, g);
		popupAllInConfirm.render(container, g);
		popupLostGame.render(container, g);
	}

	
	
	private void drawTotalAmounts(Graphics g) {
		if (gameState==null)
			return;
		
		for (int i=0; i<8; i++) {
			Player player = gameState.player[localToHostIndex(i)];
			if (player != null) {
				if (i==0) {
					GUI.drawStringLeftCenter(g, totalAmountFont, Color.white,
							"$"+player.totalChip,
							mainPanelPosition[0]+mainTotalAmountOffset[0],
							mainPanelPosition[1]+mainTotalAmountOffset[1]);
				} else {
					GUI.drawStringRightCenter(g, totalAmountFont, Color.white,
							"$"+player.totalChip,
							playerPanelPositions[i][0]+playerTotalAmountOffset[0],
							playerPanelPositions[i][1]+playerTotalAmountOffset[1]);
				}
			}
		}
	}
	
	private void drawLabels(Graphics g) {
		
		if (gameState==null || gameState.showdown)	// no labels drawn during showdown
			return;
		
		for (int i=0; i<8; ++i) {
			if (gameState.player[i] != null) {
				
				int localIndex = hostToLocalIndex(i);
				
				if (i==gameState.whoseTurn) {
					drawPlayerLabel(g, localIndex, "Thinking...", Color.white, thinkingLabelColor);
					continue;
				}
				
				// check isAllIn() instead of lastAction for the All In label
				if (gameState.player[i].isAllIn()) {
					drawPlayerLabel(g, localIndex, "All In $"+gameState.player[i].betAmount,
							Color.white, allInLabelColor);
				}
				else {

					UserAction lastAction = gameState.player[i].latestAction;
					if (lastAction!=null) {
						switch (lastAction.action) {
						case CHECK:
							drawPlayerLabel(g, localIndex, "Check", Color.white, checkLabelColor);
							break;
						case CALL:
							drawPlayerLabel(g, localIndex, "Call $"+lastAction.raiseAmount, Color.white, checkLabelColor);
							break;
						case FOLD:
							drawPlayerLabel(g, localIndex, "Fold", Color.white, foldLabelColor);
							break;
						case BET:
							drawPlayerLabel(g, localIndex, "Bet $"+lastAction.raiseAmount, Color.white, raiseLabelColor);
							break;
						case RAISE:
							drawPlayerLabel(g, localIndex, "Raise to $"+lastAction.raiseAmount, Color.white, raiseLabelColor);
							break;
						default:
							break;
						}
					}
				
				}
			}
		}
	}
	
		
	
	private void drawInteractiveElements(GUIContext container, Graphics g) {
		g.setColor(Color.white);
		checkButton.render(container, g, buttonFont, Color.white,
				checkOrCall ? "Check" : "Call $"+gameState.highestBet);
		foldButton.render(container, g,  buttonFont, Color.white, "Fold");
		raiseButton.render(container, g, buttonFont, Color.white,
				betOrRaise ? "Bet" : "Raise");
		allInButton.render(container, g, allInButtonFont, Color.white, "All In");
		
		raiseTextField.render(container, g);
	}


	
	private void drawPlayerNames(Graphics g) {

		GUI.drawStringCenter(g, infoFont, Color.white, playerNamesLocal[0],
				mainPanelPosition[0]+mainNameOffset[0],
				mainPanelPosition[1]+mainNameOffset[1]);
		
		for (int i=1; i<8; ++i) {
			if (playerNamesLocal[i] != null) {
				GUI.drawStringLeftCenter(g, infoFont, Color.white, playerNamesLocal[i],
						playerPanelPositions[i][0]+playerNameOffset[0],
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