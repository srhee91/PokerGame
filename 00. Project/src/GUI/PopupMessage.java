package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
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
	
	protected boolean visible;
	
	
	
	
	protected class ExitListener implements ComponentListener {
		private ComponentListener listener;
		public ExitListener(ComponentListener listener) {this.listener=listener;}
		@Override
		public void componentActivated(AbstractComponent source) {
			setVisible(false);
			listener.componentActivated(source);
		}
	}
	
	
	public PopupMessage(GameContainer container, int[] popupPosition, int[] popupSize,
			TrueTypeFont msgFont, TrueTypeFont buttonFont,
			ComponentListener onExitListener) throws SlickException {
		
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
				new ExitListener(onExitListener));
		
		okButton.setEnable(false);
		visible = false;		
	}
	
	
	public void setVisible(boolean visible) {
		okButton.setEnable(visible);
		this.visible = visible;
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
	
	public void render(Graphics g) {
		if (visible) {
			g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.85f));
			g.fillRect(popupPosition[0], popupPosition[1], popupSize[0], popupSize[1]);
			
			GUI.drawStringCentered(g, messageFont, Color.white, messageString,
					popupPosition[0]+messageStringOffset[0], popupPosition[1]+messageStringOffset[1]);
			
			okButton.render(g, buttonFont, Color.white, "OK");
		}
	}
}
