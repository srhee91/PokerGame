package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.SlickException;

public class PopupMessage {

	protected int[] popupPosition;
	protected int[] popupSize;
	
	protected final int[] messageStringOffset = {500, 75};
	protected final int[] okButtonOffset = {770, 200};
	
	protected Button okButton;
	protected String messageString;
	
	protected TrueTypeFont messageFont;
	protected TrueTypeFont buttonFont;
	
	protected AbstractComponent popupSource;	// the most recent source popped up this msg
	
	protected boolean visible;
	
	
	
	
	protected class ExitListener implements ComponentListener {
		private ComponentListener listener;
		public ExitListener(ComponentListener listener) {this.listener=listener;}
		@Override
		public void componentActivated(AbstractComponent source) {
			makeInvisible();
			// provided on-exit listener will be give source that caused the popup,
			// not the source that caused the exit (which is just okButton)
			listener.componentActivated(popupSource);
		}
	}
	
	
	public PopupMessage(GUIContext container, int[] popupPosition, int[] popupSize,
			TrueTypeFont msgFont, TrueTypeFont buttonFont,
			ComponentListener onOkListener) throws SlickException {
		
		this.popupPosition = new int[2];
		this.popupPosition[0] = popupPosition[0];
		this.popupPosition[1] = popupPosition[1];
		this.popupSize = new int[2];
		this.popupSize[0] = popupSize[0];
		this.popupSize[1] = popupSize[1];
		
		this.messageFont = msgFont;
		this.buttonFont = buttonFont;
		
		okButton = new Button(container,
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_green.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_green_down.png",
				popupPosition[0]+okButtonOffset[0],
				popupPosition[1]+okButtonOffset[1],
				new ExitListener(onOkListener));
		
		okButton.setEnable(false);
		visible = false;		
	}
	
	
	public void makeVisible(AbstractComponent source) {
		popupSource = source;
		okButton.setEnable(true);
		visible = true;
	}
	
	public void makeInvisible() {
		okButton.setEnable(false);
		visible = false;
	}
	
	
	public boolean isVisible() {
		return visible;
	}
	
	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public void render(GUIContext container, Graphics g) {
		if (visible) {
			g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.85f));
			g.fillRect(popupPosition[0], popupPosition[1], popupSize[0], popupSize[1]);
			
			GUI.drawStringCentered(g, messageFont, Color.white, messageString,
					popupPosition[0]+messageStringOffset[0], popupPosition[1]+messageStringOffset[1]);
			
			okButton.render(container, g, buttonFont, Color.white, "OK");
		}
	}
}
