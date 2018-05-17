package com.pile.block;

import com.pile.GameObject;
import com.pile.image.ImageType;
import com.pile.image.Resources;

import java.awt.image.BufferedImage;
//Todo Think about how we want Images of Physically in the world, Dropped Blocks and Inventory
// ^^^ Could use the same Image
public abstract class Block extends GameObject {
	public static int WIDTH = (int)(128 * Resources.SCALE);
	public static int HEIGHT = (int)(128 * Resources.SCALE);
	protected double x, y;
	protected ImageType image;
	protected boolean canCollide;

	public Block(double x, double y, ImageType image) {
		super(x, y);
		this.image = image;
		this.canCollide = true;
	}

	@Override
	public void updateHitBox() {
		hitBox.setRect(x, y, WIDTH, HEIGHT);
	}

	public BufferedImage getImage() { return image.getImage(); }
	public boolean canCollide() { return canCollide; }
	public abstract void update();

	@Override
	public String toString() {
		return "BLOCK";
	}
}
