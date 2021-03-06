package com.pile.block;

import com.pile.GameObject;
import com.pile.entity.Drop;
import com.pile.entity.FallingSand;
import com.pile.image.ImageType;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

// Blocks that make up the world
public class Block extends GameObject {
	public static final int WIDTH = (int)(128 * Resources.SCALE);
	public static final int HEIGHT = (int)(128 * Resources.SCALE);

	//Stages of block breaking
	public static final int STAGES = 9;

	protected int id;
	protected ImageType image;
	protected boolean canCollide;
	protected double destroyDelayMax, resetDelay, destroyDelay, destroyAmount;

	public Block(double x, double y, int id) { this(x, y, true, id); }

	public Block(double x, double y, boolean canCollide, int id) {
		super(Math.round(x/WIDTH)*WIDTH, Math.round(y/HEIGHT)*HEIGHT);
		this.id = id;
		this.image = Resources.itemImages[id];
		this.canCollide = Resources.blockCanCollide[id];
		updateHitBox();
		destroyDelayMax = Resources.blockSpeeds[id];
		destroyDelay = destroyAmount = 0;
	}

	public BufferedImage getImage() { return image.getImage(); }
	public int getId() { return id; }
	public boolean canCollide() { return canCollide; }

	public void changeBlockTo(int id) {
		this.id = id;
		image = Resources.itemImages[id];
	}

	@Override
	public void updateHitBox() { hitBox.setRect(x, y, WIDTH, HEIGHT); }

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
			// If there is a block above a grass, change it to dirt
			if (PlayState.world.getBlockAtSpot(x, y - 10) != null) changeBlockTo(1);
		} else if (id == 28) { // SAND
			if (PlayState.world.getBlockAtSpot(x, y + HEIGHT + 10) == null) {
				PlayState.world.addEntity(new FallingSand(x, y));
				PlayState.world.removeBlockPermanent(this);
			}
		}
		else if (id == 26) { // LEAVES
			Leaves b = (Leaves)this;
			if (!b.isDecaying()) {
				for (int[] t:b.getTrunk()) {
					if (PlayState.world.getBlockAtGridSpot(t[0], t[1]) == null) {
						b.setDecaying(true);
					}
				}
			} else {
				b.setDecayCounter(b.getDecayCounter() + 1);
				if (b.getDecayCounter() >= b.getDecayCounterMax()) {
					// Random change of dropping apple
					if ((int)(Math.random()*100) < 10) {
						PlayState.world.addEntity(new Drop(b.getX(), b.getY(), 36, Math.random()*8 - 4, -5));
					}
					PlayState.world.removeBlockPermanent(b);
				}
			}
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
