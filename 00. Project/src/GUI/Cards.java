package GUI;

import java.util.*;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cards {

	
	private class QueuedAction {
		private double waitTime;
		private boolean letFinish;
		private boolean started;
		
		private Card card;
		private boolean faceUp;
		private boolean visible;
		private int[] position;
		
		private QueuedAction(Card card, boolean faceUp, boolean visible,
				int[] position, double waitTime, boolean letFinish) {
			this.card = card;
			this.faceUp = faceUp;
			this.visible = visible;
			this.position = position;
			this.waitTime = waitTime;
			this.letFinish = letFinish;
			started = false;
		}
		private void countDown(double delta) {
			waitTime -= delta;
			if (waitTime<=0.0 && !started) {	// execute
				card.setFaceUp(faceUp);
				card.moveTo(position[0], position[1]);
				card.setVisible(visible);
				started = true;
			}
		}
		private boolean canRemoveFromQueue() {
			if (!letFinish) {
				return started;
			} else {
				return (started && !card.isMoving());
			}
		}
	}
	
	
	private final int[] deckPosition = {463, 230};
	private final int[][] centerCardPositions = {{283, 230}, {373, 230}, 
			{463, 230}, {553, 230}, {643, 230}};
	private int[][][] playerCardPositions;
	
	
	private Image[][] cardFaces;
	private Image cardBack;
	
	private Card[] centerCards;
	private Card[][] playerCards;
	private Card[] deckCards;	// used for rendering a deck
	
	
	private Queue<QueuedAction> actionQueue;
	
	
	
	public Cards(int[][][] playerCardPositions) throws SlickException {
		
		this.playerCardPositions = playerCardPositions;
		
		// load 52 card face images
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
				
		
		// load cards, no face image initially, all at deck position
		centerCards = new Card[5];
		for (int i=0; i<5; ++i) {
			centerCards[i] = new Card(cardBack, null, deckPosition[0], deckPosition[1], false);
		}
		playerCards = new Card[8][2];
		for (int i=0; i<8; ++i) {
			playerCards[i][0] = new Card(cardBack, null, deckPosition[0], deckPosition[1], false);
			playerCards[i][1] = new Card(cardBack, null, deckPosition[0], deckPosition[1], false);
		}
		deckCards = new Card[4];
		for (int i=0; i<4; ++i) {
			deckCards[i] = new Card(cardBack, null, deckPosition[0], deckPosition[1], false);
		}
		
		actionQueue = new ArrayDeque<QueuedAction>();
	}
	
	public boolean actionQueueEmpty() {
		return actionQueue.isEmpty();
	}
	
	
	public void dealCards() {
		// set all cards visible, no delay
		for (int i=0; i<8; ++i) {
			actionQueue.add(new QueuedAction(playerCards[i][0],
					false, true, deckPosition, 0.0, false));
			actionQueue.add(new QueuedAction(playerCards[i][1],
					false, true, deckPosition, 0.0, i==7));		// wait for last card to finish
		}
		
		// deal them to players
		for (int i=0; i<8; ++i) {
			actionQueue.add(new QueuedAction(playerCards[i][0],
					false, true, playerCardPositions[i][0], 100.0, false));
		}
		for (int i=0; i<8; ++i) {
			actionQueue.add(new QueuedAction(playerCards[i][1],
					false, true, playerCardPositions[i][1], 100.0, i==7));	// wait for last card to finish
		}
	}
	
	
	
	public void update(double delta) {
		// count down time for head of action queue, execute action if time expires
		// remove after starting/completing when appropriate
		QueuedAction qa = actionQueue.peek();
		if (qa != null) {
			qa.countDown(delta);
			if (qa.canRemoveFromQueue())
				actionQueue.remove(qa);
		}
		
		for (int i=0; i<5; ++i) {
			centerCards[i].update(delta);
		}
		for (int i=0; i<8; ++i) {
			playerCards[i][0].update(delta);
			playerCards[i][1].update(delta);
		}
		for (int i=0; i<4; ++i) {
			deckCards[i].update(delta);
		}
	}
	
	
	public void draw() {
		// draw cards
		for (int i=0; i<5; ++i) {
			centerCards[i].draw();
		}
		for (int i=0; i<8; ++i) {
			playerCards[i][0].draw();
			playerCards[i][1].draw();
		}
		for (int i=0; i<4; ++i) {
			deckCards[i].draw();
		}
	}
}
