package GUI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

public class PopupPrompt extends PopupMessage{


	//private final int[] messageStringOffset = {500, 75};
	private final int[] textFieldOffset = {300, 110};
	private final int[] textFieldSize = {400, 50};
	private final int[] cancelButtonOffset = {600, 200};
	
	private Button cancelButton;
	
	private MyTextField textField;
	GameContainer container;
	

	public PopupPrompt(GameContainer container, int[] popupPosition, int[] popupSize,
			TrueTypeFont msgFont, TrueTypeFont buttonFont, TrueTypeFont textFieldFont,
			ComponentListener onOKListener,
			ComponentListener onExitListener) throws SlickException {
		
		super(container, popupPosition, popupSize, msgFont, buttonFont, onOKListener);
		
		this.container = container;
		
		cancelButton = new Button(container,
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_red.png",
				GUI.RESOURCES_PATH+GUI.BUTTONS_FOLDER+"button_red_down.png",
				popupPosition[0]+cancelButtonOffset[0],
				popupPosition[1]+cancelButtonOffset[1],
				new ExitListener(onExitListener));
		
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
		cancelButton.setEnable(false);
	}
	
	public void setMaxLength(int length) {
		textField.setMaxLength(length);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		textField.setAcceptingInput(visible);
		cancelButton.setEnable(visible);
		okButton.setEnable(visible && !textField.getText().isEmpty());
	}
	
	public String getText() {
		return textField.getText();
	}
	
	@Override
	public void render(Graphics g) {
		super.render(g);
		
		if (visible) {
			g.setColor(Color.white);
			textField.render(container, g);
			cancelButton.render(g, buttonFont, Color.white, "Cancel");
		}
	}
}
