package com.pile.block;

import com.pile.GameObject;
import com.pile.image.ImageType;

import java.awt.image.BufferedImage;

public abstract class Block extends GameObject {
	public static int WIDTH = 64;
	public static int HEIGHT = 64;
	protected double x, y;
	protected ImageType image;
	protected boolean canCollide;

	public Block(double x, double y, ImageType image) {
		super(x, y);
		this.image = image;
	}
	public BufferedImage getImage() { return image.getImage(); }
	public abstract void update();
}
