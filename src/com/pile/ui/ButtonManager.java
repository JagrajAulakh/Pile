package com.pile.ui;

import com.pile.image.Resources;

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
		g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		for (Button b:buttons) {
			g.setFont(Resources.font1);
			g.setColor(b.getColor());
			g.drawString(b.getText(), b.getX(), b.getY());
		}
	}
}
