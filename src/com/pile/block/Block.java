package com.pile.block;

import com.pile.image.ImageType;

public abstract class Block {
	public static int WIDTH = 64;
	public static int HEIGHT = 64;
	protected double x, y;
	protected ImageType image;

	public Block(double x, double y, ImageType image) {
		this.x = x;
		this.y = y;
		this.image = image;
	}

	public abstract void update();
}
