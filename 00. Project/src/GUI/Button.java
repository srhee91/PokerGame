package GUI;


import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class Button {

	
	GameContainer container;
	private MyMouseOverArea area;
	Image normalImage, pressedImage;

	private float alphaWhileDisabled;
		
	public Button(GameContainer container, 
				String normalImagePath, String pressedImagePath, int x, int y,
				ComponentListener listener) throws SlickException {
		
		this.container = container;
		
		normalImage = new Image(normalImagePath);
		pressedImage = new Image(pressedImagePath);

		area = new MyMouseOverArea(container, normalImage, x, y, listener);
		area.setMouseDownImage(pressedImage);
		area.setNormalColor(new Color(0.9f, 0.9f, 0.9f, 1.0f));
		area.setMouseOverColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		area.setMouseDownColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		area.setAcceptingInput(true);

		alphaWhileDisabled = 0.5f;
	}
	
	
	public void setOnlyListener(ComponentListener listener) {
		area.removeAllListeners();
		area.addListener(listener);
	}
	
	
	public void setEnable(boolean enable) {
		if (enable && !area.isAcceptingInput()) {
			area.setNormalColor(new Color(0.9f, 0.9f, 0.9f, 1.0f));
			area.setAcceptingInput(true);
		}
		else if (!enable && area.isAcceptingInput()) {
			area.setAcceptingInput(false);
			area.setNormalColor(new Color(0.9f, 0.9f, 0.9f, alphaWhileDisabled));
		}
	}
		
	public boolean getEnable() {
		return area.isAcceptingInput();
	}
	
	public void setAlphaWhileDisabled(float alpha) {
		alphaWhileDisabled = alpha;
		if (!area.isAcceptingInput()) {
			area.setNormalColor(new Color(0.9f, 0.9f, 0.9f, alphaWhileDisabled));
		}
	}
	
	public float getAlphaWhileDisabled() {
		return alphaWhileDisabled;
	}
	
	public void render(Graphics g,  TrueTypeFont font, Color c, String s) {
		
		area.render(container, g);
		
		// calculate where string should be drawn
		int x = area.getX() + (area.getWidth()-font.getWidth(s))/2;
		int y = area.getY() + (area.getHeight()-font.getHeight(s))/2;
		
		if (!area.isAcceptingInput()) {	// draw string at half alpha if button inactive
			font.drawString(x, y, s, c.multiply(new Color(1.0f, 1.0f, 1.0f, alphaWhileDisabled)));
		}
		else {
			font.drawString(x, y, s);
		}
	}
}
