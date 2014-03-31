package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.SlickException;

public class PopupMessageOneButton extends PopupMessage {

	protected final int[] okButtonOffset = {770, 200};
	protected Button okButton;
	protected TrueTypeFont buttonFont;
	
		
	public PopupMessageOneButton(GUIContext container, int[] popupPosition, int[] popupSize,
			TrueTypeFont msgFont, TrueTypeFont buttonFont,
			ComponentListener onOkListener) throws SlickException {
		
		super(container, popupPosition, popupSize, msgFont);
		
		this.buttonFont = buttonFont;
		
		okButton = new Button(container,
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_green.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_green_down.png",
				popupPosition[0]+okButtonOffset[0],
				popupPosition[1]+okButtonOffset[1],
				new ExitListener(onOkListener));
		
		okButton.setEnable(false);	
	}
	
	@Override
	public void setVisible(AbstractComponent source) {
		super.setVisible(source);
		okButton.setEnable(true);
	}
	
	@Override
	public void setInvisible() {
		super.setInvisible();
		okButton.setEnable(false);
	}
	
	@Override
	public void render(GUIContext container, Graphics g) {
		super.render(container, g);
		if (visible) {
			okButton.render(container, g, buttonFont, Color.white, "OK");
		}
	}
}
