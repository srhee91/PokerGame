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
								GUI.cmh.send(new UserAction(UserAction.Action.ALL_IN, 
										gameState.player[GUI.playerIndexInHost].totalChip));
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
									
									if (raiseAmount <= gameState.highestBet) {
										// must raise to above the highest bet
										raiseTextField.setText("");
										setButtonsEnable(false);
										popupRaiseInvalid.setMessageString(betOrRaise
												? "Must bet an amount above $"+gameState.highestBet
												: "Must raise to an amount above $"+gameState.highestBet);
										popupRaiseInvalid.setVisible(raiseButton);
									} else if (raiseAmount >= gameState.player[GUI.playerIndexInHost].totalChip) {
										// assume this means all in, show all in popup 
										raiseTextField.setText(""+gameState.player[GUI.playerIndexInHost].totalChip);
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
						raiseTextField.setText(""+gameState.player[GUI.playerIndexInHost].totalChip);
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

					
					
					// fold player's cards if needed
					for (int i=0; i<8; i++) {
						if (gameState.player[i]!=null
								&& gameState.player[i].hasFolded) {
							cards.fold(hostToLocalIndex(i));
						}
					}

					// show all player's cards if showdown occurred
					if (gameState.showdown) {
						for (int i=0; i<8; i++) {
							if (gameState.player[i]!=null) {
								int localIndex = hostToLocalIndex(i);
								cards.showPlayerCards(localIndex);
							}
						}
					}
					
					// update player chip amounts (bet amounts)
					boolean betCollected = false;
					for (int i=0; i<8; i++) {
						int localIndex = hostToLocalIndex(i);
						// if the updated chip amount is less, send the difference to the main pot
						// UNLESS flopstate is 0 (chip amount will be winnings from prev hand)
						// otherwise, just update it without animating
						if (gameState.player[i] != null) {
							int amount = gameState.player[i].betAmount;
							int oldAmount = chipAmounts.getPlayerAmount(localIndex);
							if (oldAmount>amount && gameState.flopState!=0) {
								chipAmounts.addSendToQueue(
										oldAmount - amount,
										true, localIndex,
										false, 0,	// send to main pot
										0.0, false);
								betCollected = true;
							} else {
								chipAmounts.setPlayerAmount(localIndex, amount);
							}
						} else {
							// if player does not exist, set amount to 0
							chipAmounts.setPlayerAmount(localIndex, 0);
						}
					}
					
					
					// update pot amounts
					// if bets were collected, update the pot with animations
					if (betCollected) {
						// update side pot amounts by taking difference from main pot
						Host.GameSystem.Pot pot = gameState.potTotal;
						for (int i=0; i<8; i++) {
							if (pot==null)
								break;
							int amount = pot.totalPot;
							int oldAmount = chipAmounts.getPotAmount(i);
							if (amount > oldAmount) {
								chipAmounts.addSendToQueue(
										amount-oldAmount,
										false, 0,	// take from main pot
										false, i,	// send to whichever sidepot
										0.0, true);
							}
							pot = pot.splitPot;
						}
					}
					// otherwise, update them directly with the new values
					// (should stay consistent with pot amounts from after animation)
					else {
						Host.GameSystem.Pot pot = gameState.potTotal;
						for (int i=0; i<8; i++) {
							if (pot != null) {
								if (chipAmounts.getPotAmount(i) != pot.totalPot) {
									System.out.println("Pot "+i+" inconsistent with gamestate!!!");
								}
								chipAmounts.setPotAmount(i, pot.totalPot);
								System.out.println("\tpot "+i+": "+pot.totalPot);
								pot = pot.splitPot;
							} else {
								if (chipAmounts.getPotAmount(i) != 0) {
									System.out.println("Pot "+i+" inconsistent with gamestate!!!");
								}
								chipAmounts.setPotAmount(i, 0);
							}
						}
					}
					

	
// FLOPSTATE PROCESSING --------------------------------------------------------------------------------------
					
					switch (gameState.flopState) {
					
					case 0:
						
						// when a new hand starts...
						if (lastFlopState != 0) {
							
							// update dealer chip position
							int localDealerIndex = hostToLocalIndex(gameState.dealer);
							dealerChip.moveTo(localDealerIndex);
							
							// reset and deal cards, show main player's cards
							Host.GameSystem.Card[] hand = gameState.player[GUI.playerIndexInHost].hand;
							cards.playerCards[0][0].setFaceImage(hand[0]);
							cards.playerCards[0][1].setFaceImage(hand[1]);
							cards.collectCards();
							cards.dealCards(localDealerIndex, 1000.0, playerNamesLocal);
							cards.showPlayerCards(0);
						}
						break;
						
					case 1:
						
						if (lastFlopState==0) {
							
							// flip over flop cards
							for (int i=0; i<3; ++i) {
								cards.centerCards[i].setFaceImage(gameState.flops[i]);
							}
							cards.dealFlop(1000.0);
						} 
						break;
						
					case 2:
						if (lastFlopState == 1) {
							// flip over turn card
							cards.centerCards[3].setFaceImage(gameState.flops[3]);							
							cards.dealTurn(1000.0);
							
						} else {
							// flip over flop and turn cards
							for (int i=0; i<4; ++i) {
								cards.centerCards[i].setFaceImage(gameState.flops[i]);
							}
							cards.dealFlop(1000.0);
							cards.dealTurn(0.0);							
						}
						break;
						
					case 3:
						if (lastFlopState==2) {
							// flip over river card
							cards.centerCards[4].setFaceImage(gameState.flops[4]);
							cards.dealRiver(1000.0);
							
						} else if (lastFlopState==1) {
							// flip over turn and river cards
							cards.centerCards[3].setFaceImage(gameState.flops[3]);
							cards.dealTurn(1000.0);
							cards.centerCards[4].setFaceImage(gameState.flops[4]);
							cards.dealRiver(0.0);
						} else {
							// flip over flop, turn, and river cards
							for (int i=0; i<5; ++i) {
								cards.centerCards[i].setFaceImage(gameState.flops[i]);
							}
							cards.dealFlop(1000.0);
							cards.dealTurn(0.0);
							cards.dealRiver(0.0);
						}
						break;
					
					case 4:
						// when a hand ends
						if (lastFlopState != 4) {
							
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
												500.0, true);
												//first ? 300.0 : 0.0, false);
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
									int leftOver = chipAmounts.getPotAmount(potIndex);
									if (leftOver > 0) {
										chipAmounts.addSendToQueue(
												leftOver,
												false, potIndex,
												false, 0,	 // send to main pot
												0.0, true);
									}
									pot = pot.splitPot;
								}
							}
						}
					}
					lastFlopState = gameState.flopState;	
					
					
// END GAMESTATE PROCESSING ------------------------------------------------------------------------
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
			if (highestBet >= gameState.player[GUI.playerIndexInHost].totalChip) {
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
				else {
					
					//int betAmount = gameState.player[i].betAmount;
					
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
							drawPlayerLabel(g, localIndex, "All In $"+lastAction.raiseAmount, Color.white, allInLabelColor);
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