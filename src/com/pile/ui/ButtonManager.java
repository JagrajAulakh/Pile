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

	//Getting the buttons to update themselvess
	public void update() {
		for (Button b:buttons) {
			b.update();
		}
	}
	//Drawling all the images
	public void render(Graphics g) {
		for (Button b:buttons) {
			g.drawImage(b.getImage(),b.getX() , b.getY() , null);
		}
	}
}
