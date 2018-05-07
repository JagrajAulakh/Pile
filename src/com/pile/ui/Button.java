package com.pile.ui;

import com.pile.Input;
import com.pile.image.Resources;

import java.awt.*;

public class Button {
	public static final int WIDTH = 150;
	public static final int HEIGHT = 50;
	private String text;
	private int x, y, width, height;
	private Color curColor, normColor, clickColor, hoverColor;

	public Button(String text, int x, int y, Color col) {
		this(text, x, y, WIDTH, HEIGHT, col, col, col);
	}
	public Button(String text, int x, int y, int width, int height, Color col) {
		this(text, x, y, width, height, col, col, col);
	}
	public Button(String text, int x, int y, int width, int height, Color normColor, Color clickColor, Color hoverColor) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.normColor = normColor;
		this.clickColor = clickColor;
		this.hoverColor = hoverColor;
		int[] s = Resources.getSize(text, Resources.font1);
		this.width = s[0];
		this.height = s[1];
	}

	public void update() {
		if (getX() < Input.mx && Input.mx < getX() + width) {
			curColor = hoverColor;
			if (Input.mb[0]) {
				curColor = clickColor;
			}
		} else {
			curColor = normColor;
		}
	}

	public String getText() { return text; }
	public int getX() { return x - width/2; }
	public int getY() { return y - height/2; }
	public Color getColor() { return curColor; }
}
