package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Card {
	
	// screen position
	private double currX;
	private double currY;
	private double destX;
	private double destY;
	private final double moveSpeed = 1.0;	// pixels per ms
	
	// face up or down
	private boolean currFaceUp;
	private boolean destFaceUp;
	private double theta;	// iterates 0 to 180
	private final double thetaSpeed = 0.5;
	
	// visibility
	private double currAlpha;
	private double destAlpha;
	private final double alphaSpeed = 0.005;	// alpha per ms
	
	private Image backImage;
	private Image faceImage;

	
	public Card(Image backImage, Image faceImage, int initialX, int initialY, boolean initialVisible) {
		
		this.faceImage = faceImage;
		this.backImage = backImage;
		
		currX = initialX;
		currY = initialY;
		destX = currX;
		destY = currY;
		
		currFaceUp = false;
		destFaceUp = currFaceUp;
		theta = 0.0;
		
		currAlpha = initialVisible ? 1.0 : 0.0;	// card initially not visible
		destAlpha = currAlpha;
	}
	
	public void setFaceImage(Image faceImage) {
		this.faceImage = faceImage;
	}
	
	
	// to move card
	public void moveTo(double destX, double destY) {
		this.destX = destX;
		this.destY = destY;
	}
	
	public void setVisible(boolean visible) {	// won't do anything if called during a flip
		if (theta==0.0) {
			destAlpha = visible ? 1.0 : 0.0;
		}
	}
	
	public void setFaceUp(boolean faceUp) {		// won't do anything if called during a flip or if not fully visible
		if (theta==0.0 && (currAlpha==1.0 && destAlpha==1.0)) {
			destFaceUp = faceUp;
		}
	}
	
	public void flip() {
		setFaceUp(!currFaceUp);
	}
	
	
	// poll card state
	public boolean isMoving() {
		return ((currX!=destX || currY!=destY) || (currAlpha!=destAlpha || currFaceUp!=destFaceUp));
	}
	public boolean isVisible() {
		return currAlpha >= 0.5;
	}
	public boolean isFaceUp() {
		return currFaceUp;
	}
	
	
	// updates current screen position of card if it's not yet at its destination
	public void update(double delta) throws SlickException {
		
		// update screen position
		if (currX != destX || currY != destY) {
			double dX = destX - currX;
			double dY = destY - currY;
			double distToDest = java.lang.Math.sqrt(dX*dX+dY*dY);
			double dDist = delta * moveSpeed;
			
			if (dDist >= distToDest) {
				currX = destX;
				currY = destY;
			}
			else {
				currX += dX / distToDest * dDist;
				currY += dY / distToDest * dDist;
			}
		}
		
		// update card visibility
		if (currAlpha < destAlpha) {
			double dAlpha = delta * alphaSpeed;
			if (dAlpha >= destAlpha-currAlpha) {
				currAlpha = destAlpha;
			}
			else {
				currAlpha += dAlpha;
			}
		}
		else if (currAlpha > destAlpha) {
			double dAlpha = delta * alphaSpeed;
			if (dAlpha >= currAlpha-destAlpha) {
				currAlpha = destAlpha;
			}
			else {
				currAlpha -= dAlpha;
			}
		}
		
		// update card theta
		if (currFaceUp != destFaceUp) {
			double dTheta = delta * thetaSpeed;
			if (dTheta >= 180.0-theta) {
				theta = 0.0;
				currFaceUp = destFaceUp;
			}
			else {
				theta += dTheta;
			}
		}
	}
	
	
	// draws the card
	public void draw() {
		
		if (currAlpha==0.0) {
			return;
		}
		
		// determine which side of card is visible right now
		Image visible = (currFaceUp==(theta<=90.0)) ? faceImage : backImage;
				
		if (theta == 0.0) {
			visible.draw((int)currX, (int)currY, new Color(1.0f, 1.0f, 1.0f,(float)currAlpha));
		}
		else {
			double s = java.lang.Math.sin(java.lang.Math.toRadians(theta));
			double perspectiveScale = 1.0 + 0.2*s;
			double c = java.lang.Math.abs(java.lang.Math.cos(java.lang.Math.toRadians(theta)));
			
			// calculate card center and image half-dimensions
			double leftNdcX, leftNdcY, rightNdcX, rightNdcY;
			/*
			if (theta <= 90.0) {
				// left edge up, right edge sliding leftward
				leftNdcX = perspectiveScale;
				leftNdcY = perspectiveScale;
				rightNdcX = 2.0*c-1.0;	// transform [0,1] to [-1,1]
				rightNdcY = 1.0;
			}
			else {
				// right edge up, right edge sliding rightward
				leftNdcX = 1.0;
				leftNdcY = 1.0;
				rightNdcX = (2.0*c-1.0)*perspectiveScale;
				rightNdcY = perspectiveScale;
			}*/
			if (theta <= 90.0) {
				// left edge up, both edges slide inward
				leftNdcX = c*perspectiveScale;
				leftNdcY = perspectiveScale;
				rightNdcX = c;
				rightNdcY = 1.0;
			}
			else {
				// right edge up, right edge sliding rightward
				leftNdcX = c;
				leftNdcY = 1.0;
				rightNdcX = c*perspectiveScale;
				rightNdcY = perspectiveScale;
			}
			double halfWidth = (double)visible.getWidth() / 2.0;
			double halfHeight = (double)visible.getHeight() / 2.0;
			double centerX = currX + halfWidth;
			double centerY = currY + halfHeight;
			
			visible.drawWarped(
					(float)(centerX - leftNdcX*halfWidth), (float)(centerY - leftNdcY*halfHeight),
					(float)(centerX - leftNdcX*halfWidth), (float)(centerY + leftNdcY*halfHeight),
					(float)(centerX + rightNdcX*halfWidth), (float)(centerY + rightNdcY*halfHeight),
					(float)(centerX + rightNdcX*halfWidth), (float)(centerY - rightNdcY*halfHeight)
			);
		}
	}
}
