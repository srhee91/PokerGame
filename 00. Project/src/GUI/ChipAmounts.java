package GUI;

import java.util.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class ChipAmounts {
		
	// represents a stack of chips belonging to a player or to a pot
	private class StaticAmount {
		private boolean big;
		private int x, y;
		private int amount;
	
		private StaticAmount(int x, int y, int initialAmount, boolean big) {
			this.x = x;
			this.y = y;
			this.amount = initialAmount;
			this.big = big;
		}
		
		private void setAmount(int amount) {
			this.amount = amount;
		}
		
		private void draw(Graphics g) {
			if (amount != 0) {
				if (!big) {
					chip.draw(x, y);
					font.drawString(x+30, y+12-font.getHeight()/2,
							"$"+amount, Color.white);
				} else  {
					chipBig.draw(x, y);
					fontBig.drawString(x+36, y+15-fontBig.getHeight()/2,
							"$"+amount, Color.white);
				}
			}
		}
	}
	
	private class SendAmount {
		private int amount;
		private StaticAmount source;
		private StaticAmount destination;
		
		private double currX, currY;
		private boolean started, completed;
		private final double moveSpeed = 1.0;	// pixels per ms
		
		private SendAmount(StaticAmount source, StaticAmount destination, int amount) {
			this.source = source;
			this.destination = destination;
			this.amount = amount;
			currX = source.x;
			currY = source.y;
			started = false;
			completed = false;
		}
		private void start() {
			started = true;
			source.amount -= amount;
		}
		private void update(double delta) {
			// update screen position
			if (started && !completed) {
				double dX = destination.x - currX;
				double dY = destination.y - currY;
				double distToDest = java.lang.Math.sqrt(dX*dX+dY*dY);
				double dDist = delta * moveSpeed;
				if (dDist >= distToDest) {
					destination.amount += amount;
					completed = true;
				}
				else {
					currX += dX / distToDest * dDist;
					currY += dY / distToDest * dDist;
				}
			}
		}
		private void draw(Graphics g) {
			int x = (int)currX;
			int y = (int)currY;
			chip.draw(x, y);
			//font.drawString(x+30, y+12-font.getHeight()/2,
					//"$"+amount, Color.white);
		}
	}
	
	
	private class QueuedSend {
		
		private double waitTime;
		private boolean letFinish;
		private SendAmount send;
		
		private QueuedSend(int amount, boolean srcIsPlayer, int srcIndex,
				boolean destIsPlayer, int destIndex,
				double waitTime, boolean leftFinish) {
			this.waitTime = waitTime;
			this.letFinish = leftFinish;
			this.send = new SendAmount(
					srcIsPlayer ? playerAmounts[srcIndex] : potAmounts[srcIndex],
					destIsPlayer ? playerAmounts[destIndex] : potAmounts[destIndex],
					amount);
		}
		private void countDown(double delta) {
			waitTime -= delta;
			if (waitTime<=0.0 && !send.started) {	// execute
				send.start();
				amountsInTransit.add(send);
			}
		}
		private boolean canRemoveFromQueue() {
			if (!letFinish) {
				return send.started;
			} else {
				return send.completed;
			}
		}
	}
	
	
	
	private final int[][] potAmountPositions = {{485, 340},
			{600, 343}, {680, 343}, {640, 373}, {560, 373}, {405, 343}, {325, 343}, {245, 343}};
	
	private Image chip;
	private Image chipBig;
	private TrueTypeFont font;
	private TrueTypeFont fontBig;
	
	private StaticAmount[] playerAmounts;
	private StaticAmount[] potAmounts;
	
	private Queue<QueuedSend> sendQueue;
	private List<SendAmount> amountsInTransit;
	
	
	public ChipAmounts(TrueTypeFont font, TrueTypeFont fontBig,
			int initialPlayerAmount, int[][] playerAmountPositions)
					throws SlickException {
		
		chip = new Image(GUI.RESOURCES_PATH + "chip25.png");
		chipBig = new Image(GUI.RESOURCES_PATH + "chip30.png");
		this.font = font;
		this.fontBig = fontBig;

		playerAmounts = new StaticAmount[8];
		for (int i=0; i<8; ++i) {
			playerAmounts[i] = new StaticAmount(playerAmountPositions[i][0],
					playerAmountPositions[i][1], initialPlayerAmount, false);
		}
		potAmounts = new StaticAmount[8];
		for (int i=0; i<8; ++i) {
			potAmounts[i] = new StaticAmount(potAmountPositions[i][0],
					potAmountPositions[i][1], 0, i==0);
		}
		amountsInTransit = new ArrayList<SendAmount>();
		sendQueue = new ArrayDeque<QueuedSend>();
	}
	
	public void setPlayerAmount(int player, int amount) {
		playerAmounts[player].setAmount(amount);
	}
	
	public void setPotAmount(int pot, int amount) {
		potAmounts[pot].setAmount(amount);
	}
	
	public boolean sendOngoing() {
		return !amountsInTransit.isEmpty();
	}
	
	public void addSendToQueue(int amount, boolean srcIsPlayer, int srcIndex,
			boolean destIsPlayer, int destIndex,
			double waitTime, boolean removeFromQueueWhenComplete) {
		sendQueue.add(new QueuedSend(amount, srcIsPlayer, srcIndex,
				destIsPlayer, destIndex, waitTime, removeFromQueueWhenComplete));
	}
	
	
	public void update(double delta) {
		// count down time for the head of sendqueue, execute send if time expired
		// remove after starting/completing when appropriate
		QueuedSend qs = sendQueue.peek();
		if (qs != null) {
			qs.countDown(delta);
			if (qs.canRemoveFromQueue())
				sendQueue.remove();
		}
		
		// update SendAmount positions, remove ones that have completed
		for (int i=amountsInTransit.size()-1; i>=0; --i) {
			SendAmount sa = amountsInTransit.get(i);
			sa.update(delta);
			if (sa.completed)
				amountsInTransit.remove(i);
		}
	}
	
	public void draw(Graphics g) {
		for (int i=0; i<8; ++i) {
			playerAmounts[i].draw(g);
		}
		for (int i=0; i<8; ++i) {
			potAmounts[i].draw(g);
		}
		// render moving amounts on top
		for (SendAmount sa : amountsInTransit) {
			sa.draw(g);
		}
	}
}
