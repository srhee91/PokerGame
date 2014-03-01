package GUI;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class RaiseTextField {
	
	private int x;
	private int y;
	
	TrueTypeFont raiseByFont;
	TrueTypeFont textFieldFont;
	TrueTypeFont dollarSignFont;
	
	private MyTextField textField;
	
	private float alphaWhileDisabled;
	
	
	

	public RaiseTextField(GameContainer container, int x, int y) {
		this.x = x;
		this.y = y;
		
		raiseByFont = new TrueTypeFont(new java.awt.Font("Segoe UI", Font.ITALIC, 12), true);
		textFieldFont = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 16), true);
		dollarSignFont = new TrueTypeFont(new java.awt.Font("Segoe UI Semibold", Font.PLAIN, 18), true);
		
		textField = new MyTextField(container, textFieldFont, x+15, y+19, 75, 26);
		textField.setBackgroundColor(new Color(1.0f, 1.0f, 1.0f, 0.2f));
		textField.setBorderColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		textField.setTextColor(Color.white);
		textField.setMaxLength(6);
		textField.setNumeralsOnly(true);
		textField.setAcceptingInput(true);
		
		alphaWhileDisabled = 0.5f;
	}
	
	public void setEnable(boolean enable) {
		textField.setText("");
		
		if (enable && !textField.isAcceptingInput()) {
			textField.setAcceptingInput(true);
			textField.setBackgroundColor(new Color(1.0f, 1.0f, 1.0f, 0.2f));
			textField.setTextColor(Color.white);
		}
		else if (!enable && textField.isAcceptingInput()) {
			textField.setBackgroundColor(new Color(1.0f, 1.0f, 1.0f, 0.2f*alphaWhileDisabled));
			textField.setTextColor(new Color(1.0f, 1.0f, 1.0f, alphaWhileDisabled));
			textField.setFocus(false);
			textField.setAcceptingInput(false);
		}
	}
	
	public boolean getEnable() {
		return textField.isAcceptingInput();
	}
	
	public float getAlphaWhileDisabled() {
		return alphaWhileDisabled;
	}

	public void setAlphaWhileDisabled(float alpha) {
		alphaWhileDisabled = alpha;
		if (!textField.isAcceptingInput()) {
			textField.setBackgroundColor(new Color(1.0f, 1.0f, 1.0f, 0.2f*alphaWhileDisabled));
			textField.setTextColor(new Color(1.0f, 1.0f, 1.0f, alphaWhileDisabled));
		}
	}
	
	public void render(GameContainer container, Graphics g) {
		textField.render(container, g);
		Color c;
		if (textField.isAcceptingInput())
			c = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		else
			c = new Color(1.0f, 1.0f, 1.0f, alphaWhileDisabled);
		
		dollarSignFont.drawString(x, y+32-dollarSignFont.getHeight()/2, "$", c);
		raiseByFont.drawString(x+10, y, "Raise by:", c);
	}
	
	
}
