package GUI;


import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Button {

	
	GameContainer container;
	private MyMouseOverArea area;
	Image normalImage, pressedImage;

	
		
	public Button(GameContainer container, 
				String normalImagePath, String pressedImagePath, int x, int y,
				ComponentListener listener) throws SlickException {
		
		normalImage = new Image(normalImagePath);
		pressedImage = new Image(pressedImagePath);

		area = new MyMouseOverArea(container, normalImage, x, y, listener);
		area.setMouseDownImage(pressedImage);
		area.setNormalColor(new Color(0.9f, 0.9f, 0.9f, 1.0f));
		area.setMouseOverColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		area.setMouseDownColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		area.setAcceptingInput(true);
		
		this.container = container;
	}
	

	public void setEnable(boolean enable) {
		if (enable && !area.isAcceptingInput()) {
			area.setNormalColor(new Color(0.9f, 0.9f, 0.9f, 1.0f));
			area.setAcceptingInput(true);
		}
		else if (!enable && area.isAcceptingInput()) {
			area.setAcceptingInput(false);
			area.setNormalColor(new Color(0.9f, 0.9f, 0.9f, 0.5f));
		}
	}
	
	
	public boolean getEnable() {
		return area.isAcceptingInput();
	}
	
	public void render(Graphics g, String s) {
		
		area.render(container, g);
		
		// calculate where string should be drawn
		// ASSUMING g HAS NOT BEEN TRANSFORMED
		Font font = g.getFont();
		int x = area.getX() + (area.getWidth()-font.getWidth(s))/2;
		int y = area.getY() + (area.getHeight()-font.getHeight(s))/2;
		
		if (!area.isAcceptingInput()) {	// draw string at half alpha if button inactive
			Color gColor = g.getColor();
			g.setColor(gColor.multiply(new Color(1.0f, 1.0f, 1.0f, 0.5f)));
			g.drawString(s, x, y);
			g.setColor(gColor);
		}
		else {
			g.drawString(s, x, y);
		}
	}
}
