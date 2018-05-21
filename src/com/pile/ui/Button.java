package com.pile.ui;

import com.pile.Game;
import com.pile.GameLogic;
import com.pile.image.Resources;
import com.pile.io.Writer;
import com.pile.state.MenuState;
import com.pile.state.PlayState;

import java.awt.*;

public class Button {
	public static final int WIDTH = 150;
	public static final int HEIGHT = 50;
	private String text;
	private float size;
	private int x, y, width, height;
	private Color curColor, normColor, clickColor, hoverColor;

	public Button(String text, int x, int y, float size, Color col) { this(text, x, y, 96f, col, col, col); }

	public Button(String text, int x, int y, float size, Color normColor, Color clickColor, Color hoverColor) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.normColor = normColor;
		this.clickColor = clickColor;
		this.hoverColor = hoverColor;
		this.width = Resources.getWidth(text, Resources.getFont(size));
		this.height = Resources.getHeight(text, Resources.getFont(size));
		this.height -= this.height/4;
		this.size = size;
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public String getText() { return text; }
	public int getX() { return x - width/2; }
	public int getY() { return y - height/2; }
	public Color getColor() { return curColor; }
	public Font getFont() { return Resources.getFont(size); }

	public void update() {
		if (getX() < Game.input.mx && Game.input.mx < getX() + width && getY() < Game.input.my && Game.input.my < getY() + height) {
			curColor = hoverColor;
			if (Game.input.buttonPressed(0)) {
				curColor = clickColor;
			}
			if (Game.input.mouseUp(0)) {
				if (text.toLowerCase().equals("play")) {
					GameLogic.gsm.set(new PlayState());
				} else if (text.toLowerCase().equals("exit")) {
					Writer.writeDataFile();
					GameLogic.gsm.pop();
					GameLogic.gsm.set(new MenuState());
				} else if (text.toLowerCase().equals("resume")) {
					GameLogic.gsm.pop();
				}

			}
		} else {
			curColor = normColor;
		}
	}
}
