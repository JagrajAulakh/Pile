package com.pile.block;

import com.pile.GameObject;
import com.pile.image.ImageType;
import com.pile.image.Resources;

import java.awt.image.BufferedImage;
//Todo Think about how we want Images of Physically in the world, Dropped Blocks and Inventory
// ^^^ Could use the same Image
public class Block extends GameObject {
	public static final int WIDTH = (int)(128 * Resources.SCALE);
	public static final int HEIGHT = (int)(128 * Resources.SCALE);
	protected int id;
	protected ImageType image;
	protected boolean canCollide;

	public Block(double x, double y, int id) {
		super(x, y);
		this.id = id;
		this.image = Resources.blocks[id];
		this.canCollide = true;
		updateHitBox();
	}

	public BufferedImage getImage() { return image.getImage(); }
	public boolean canCollide() { return canCollide; }

	@Override
	public void updateHitBox() {
		hitBox.setRect(x, y, WIDTH, HEIGHT);
	}

	@Override
	public void update() {
		updateHitBox();
	}
}
