package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

public class PopupMessageTwoButtons extends PopupMessageOneButton {

	protected final int[] cancelButtonOffset = {600, 200};
	private Button cancelButton;

	
	public PopupMessageTwoButtons(GUIContext container, int[] popupPosition, int[] popupSize,
			TrueTypeFont msgFont, TrueTypeFont buttonFont,
			ComponentListener onOkListener,
			ComponentListener onCancelListener) throws SlickException {
		
		super(container, popupPosition, popupSize, msgFont, buttonFont, onOkListener);
		
		cancelButton = new Button(container,
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_red.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_red_down.png",
				popupPosition[0]+cancelButtonOffset[0],
				popupPosition[1]+cancelButtonOffset[1],
				new ExitListener(onCancelListener));
		
		cancelButton.setEnable(false);
	}
	
	@Override
	public void makeVisible(AbstractComponent source) {
		super.makeVisible(source);
		cancelButton.setEnable(true);
	}
	
	@Override
	public void makeInvisible() {
		super.makeInvisible();
		cancelButton.setEnable(false);
	}
	
	@Override
	public void render(GUIContext container, Graphics g) {
		super.render(container, g);
		
		if (visible) {
			cancelButton.render(container, g, buttonFont, Color.white, "Cancel");
		}
	}
}
