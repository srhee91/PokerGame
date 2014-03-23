package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

public class PopupMessage {
	
	protected int[] popupPosition;
	protected int[] popupSize;
	
	protected final int[] messageStringOffset = {500, 75};
	protected String messageString;
	protected TrueTypeFont messageFont;
	
	protected AbstractComponent popupSource;
	
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
			TrueTypeFont msgFont) throws SlickException {
		
		this.popupPosition = new int[2];
		this.popupPosition[0] = popupPosition[0];
		this.popupPosition[1] = popupPosition[1];
		this.popupSize = new int[2];
		this.popupSize[0] = popupSize[0];
		this.popupSize[1] = popupSize[1];
		
		this.messageFont = msgFont;
		
		visible = false;
	}
	
	
	public void makeVisible(AbstractComponent source) {
		popupSource = source;
		visible = true;
	}
	
	public void makeInvisible() {
		visible = false;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	public void render(GUIContext container, Graphics g) {
		if (visible) {
			g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.85f));
			g.fillRect(popupPosition[0], popupPosition[1], popupSize[0], popupSize[1]);
			
			GUI.drawStringCenter(g, messageFont, Color.white, messageString,
					popupPosition[0]+messageStringOffset[0], popupPosition[1]+messageStringOffset[1]);
		}
	}
}
