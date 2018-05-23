package com.pile.ui;

import java.awt.*;
import java.util.ArrayList;

public class ButtonManager {
	private ArrayList<Button> buttons;

	public ButtonManager() {
		buttons = new ArrayList<Button>();
	}

	public void add(Button b) {
		buttons.add(b);
	}

	public void update() {
		for (Button b:buttons) {
			b.update();
		}
	}
	
	public void render(Graphics g) {
		for (Button b:buttons) {
			// Background
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());

			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			g.setFont(b.getFont());
			g.setColor(b.getColor());
			g.drawString(b.getText(), b.getX(), b.getY() + b.getHeight()*3/4);
		}
	}
}
