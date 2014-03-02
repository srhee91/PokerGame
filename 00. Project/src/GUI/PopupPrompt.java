package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

public class PopupPrompt extends PopupMessage2Buttons{


	//private final int[] messageStringOffset = {500, 75};
	private final int[] textFieldOffset = {300, 110};
	private final int[] textFieldSize = {400, 50};


	private MyTextField textField;
	//GUIContext container;
	

	public PopupPrompt(GUIContext container, int[] popupPosition, int[] popupSize,
			TrueTypeFont msgFont, TrueTypeFont buttonFont, TrueTypeFont textFieldFont,
			ComponentListener onOkListener,
			ComponentListener onCancelListener) throws SlickException {
		
		super(container, popupPosition, popupSize, msgFont, buttonFont,
				onOkListener, onCancelListener);
		
		
		textField = new MyTextField(container, textFieldFont, popupPosition[0]+textFieldOffset[0],
				popupPosition[1]+textFieldOffset[1], textFieldSize[0], textFieldSize[1]);
		textField.setBackgroundColor(new Color(255, 255, 255, 32));
		textField.setBorderColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		textField.setTextColor(Color.white);
		textField.setOnTextChangeListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent source) {

				okButton.setEnable(visible && !textField.getText().isEmpty());
			}
		});
		
		textField.setAcceptingInput(false);
	}
	
	public void setMaxLength(int length) {
		textField.setMaxLength(length);
	}
	
	@Override
	public void makeVisible(AbstractComponent source) {
		super.makeVisible(source);
		textField.setAcceptingInput(true);
		okButton.setEnable(!textField.getText().isEmpty());
	}
	
	@Override
	public void makeInvisible() {
		super.makeInvisible();
		textField.setAcceptingInput(false);
	}
	
	
	public String getText() {
		return textField.getText();
	}
	
	@Override
	public void render(GUIContext container, Graphics g) {
		super.render(container, g);
		
		if (visible) {
			g.setColor(Color.white);
			textField.render(container, g);
		}
	}
}
