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

			g.setFont(b.getFont());
			g.setColor(b.getColor());
			g.drawString(b.getText(), b.getX(), b.getY() + b.getHeight()*3/4);
		}
	}
}
