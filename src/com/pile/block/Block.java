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

	public static final int DIRT = 0;
	public static final int GRASS = 1;
	public static final int STONE = 2;
	public static final int OVEN = 3;
	public static final int COAL_ORE = 4;
	public static final int SILVER_ORE = 5;
	public static final int IRON_ORE = 6;
	public static final int IRON_ORE_BROWN = 7;
	public static final int GOLD_ORE = 8;
	public static final int DIAMOND_ORE = 9;

	protected int id;
	protected ImageType image;
	protected boolean canCollide;
	protected int destroyDelayMax;
	protected int destroyDelay;
	protected int destroyAmount;

	public Block(double x, double y, int id) {
		super(x, y);
		this.id = id;
		this.image = Resources.blocks[id];
		this.canCollide = true;
		updateHitBox();
		destroyDelayMax = 50;
		destroyDelay = 0;
		destroyAmount = 0;
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

	public void destroy() {
		destroyDelay++;
		if (destroyDelay >= destroyDelayMax) {
			destroyDelay = 0;
			destroyAmount++;
		}
	}
	public boolean destroyed() { return destroyAmount >= 5; }
	public int getDestroyAmount() { return destroyAmount; }
	public int getGridX() { return (int)(x / WIDTH); }
	public int getGridY() { return (int)(y / HEIGHT); }
}
