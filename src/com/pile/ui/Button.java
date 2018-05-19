package com.pile.ui;

import com.pile.Input;
import com.pile.image.Resources;
import com.pile.state.MenuState;

import java.awt.*;
import java.util.Arrays;

public class Button {
	public static final int WIDTH = 150;
	public static final int HEIGHT = 50;
	private String text;
	private int x, y, width, height;
	private float fontSize;
	private Color curColor, normColor, clickColor, hoverColor;

	public Button(String text, int x, int y, Color col) { this(text, x, y, Resources.getWidth(text, Resources.getFont(96f)), Resources.getHeight(text, Resources.getFont(96f)), col, col, col, 96f); }

	public Button(String text, int x, int y, Color normColor, Color clickColor, Color hoverColor) {
		this(text, x, y, Resources.getWidth(text, Resources.getFont(96f)), Resources.getHeight(text, Resources.getFont(96f)), normColor, clickColor, hoverColor, 96f);
	}

	public Button(String text, int x, int y, int width, int height, Color col) {
		this(text, x, y, width, height, col, col, col, 96f);
	}
	public Button(String text, int x, int y, int width, int height, Color normColor, Color clickColor, Color hoverColor, float fontSize) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.normColor = normColor;
		this.clickColor = clickColor;
		this.hoverColor = hoverColor;
		this.width = width;
		this.height = height;
		this.height -= this.height/4;
		this.fontSize = fontSize;
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public String getText() { return text; }
	public int getX() { return x - width/2; }
	public int getY() { return y - height/2; }
	public Color getColor() { return curColor; }
	public Font getFont() { return Resources.getFont(fontSize); }

	public void update() {
		if (getX() < Input.mx && Input.mx < getX() + width && getY() < Input.my && Input.my < getY() + height) {
			curColor = hoverColor;
			if (Input.mb[0]) {
				curColor = clickColor;
			}
			if (Input.mouseUp) {
				// TODO button actions
			}
		} else {
			curColor = normColor;
		}
	}
}
