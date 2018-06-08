package com.pile.block;

import com.pile.GameObject;
import com.pile.image.ImageType;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Block extends GameObject {
	public static final int WIDTH = (int)(128 * Resources.SCALE);
	public static final int HEIGHT = (int)(128 * Resources.SCALE);

	public static final int STAGES = 9;

	protected int id;
	protected ImageType image;
	protected boolean canCollide;
	protected double destroyDelayMax, resetDelay, destroyDelay, destroyAmount;

	public Block(double x, double y, int id) { this(x, y, true, id); }

	public Block(double x, double y, boolean canCollide, int id) {
		super(x, y);
		this.id = id;
		this.image = Resources.itemImages[id];
		this.canCollide = true;
		updateHitBox();
		destroyDelayMax = Resources.blockSpeeds[id];
		destroyDelay = destroyAmount = 0;
	}

	public BufferedImage getImage() { return image.getImage(); }
	public int getId() { return id; }
	public boolean canCollide() { return canCollide; }

	private void changeBlockTo(int id) {
		this.id = id;
		image = Resources.itemImages[id];
	}

	@Override
	public void updateHitBox() {
		hitBox.setRect(x, y, WIDTH, HEIGHT);
	}

	@Override
	public void update() {
		if (resetDelay < 3) {
			resetDelay++;
		} else {
			reset();
		}
		updateHitBox();
		// Grass regrow
		if (id == 1) {
			if (PlayState.world.getBlockAtSpot(x, y - 10) == null) {
				LinkedList<Block> around = PlayState.world.getBlocksAround(this, 3);
				boolean grassFound = false;
				for (Block b:around) {
					if (b.getId() == 0) {
						grassFound = true;
						break;
					}
				}
				if (grassFound) {
					if (Math.random()*1000 <= 1) changeBlockTo(0);
				}
			}
		} else if (id == 0) {
			if (PlayState.world.getBlockAtSpot(x, y - 10) != null) changeBlockTo(1);
		}
	}

	public void destroy(double amount) {
		resetDelay = 0;
		destroyDelay += amount;
		while (destroyDelay >= destroyDelayMax) {
			destroyDelay -= destroyDelayMax;
			destroyAmount++;
		}
	}
	public boolean destroyed() { return destroyAmount >= STAGES+1; }
	public double getDestroyAmount() { return destroyAmount; }
	public void reset() { destroyAmount = destroyDelay = 0; }
	public int getGridX() { return (int)(x / WIDTH); }
	public int getGridY() { return (int)(y / HEIGHT); }
}
