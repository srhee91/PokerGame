package GUI;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.GUIContext;

public class PopupMessageAnimation extends PopupMessage {

	private final int[] animationOffset = {500, 200};
	private Animation animation;
	
	public PopupMessageAnimation(GUIContext container, int[] popupPosition, int[] popupSize,
			TrueTypeFont msgFont, Animation animation) throws SlickException {
		
		super(container, popupPosition, popupSize, msgFont);
		
		this.animation = animation;
	}
	
	@Override
	public void render(GUIContext container, Graphics g) {
		super.render(container, g);
		if (visible) {
			animation.draw(popupPosition[0]+animationOffset[0]-animation.getWidth()/2,
					popupPosition[1]+animationOffset[1]-animation.getHeight()/2);
		}
	}
}
