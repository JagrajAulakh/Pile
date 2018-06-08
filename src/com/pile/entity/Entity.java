package com.pile.entity;

import com.pile.GameObject;
import com.pile.block.Block;
import com.pile.image.ImageType;
import com.pile.state.PlayState;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public abstract class Entity extends GameObject {
	protected double width, height, velX, velY, accX, accY;
	protected boolean onGround, flipped;

	public Entity(double x, double y) {
		super(x, y);
		hitBox = new Rectangle2D.Double(x, y, width, height);
	}

	public double getWidth() { return width; }
	public double getHeight() { return height; }
	public double getVelX() { return velX; }
	public double getVelY() { return velY; }
	public boolean isFlipped() { return flipped; }

	@Override
	public void updateHitBox() {
		hitBox.setRect(x, y, width, height);
	}
	public BufferedImage getImage() { return image.getImage(); }

	protected void blockCollisionX(LinkedList<Block> blocks) {
		if (x < 0) {
			x = 0;
		} else if (x + width > PlayState.world.getWidth()) {
			x = PlayState.world.getWidth() - width;
		}
		for (Block e:blocks) {
			if (e.canCollide() && collides(e)) {
				if (velX > 0) {
					x = e.getX() - width;
					velX = accX = 0;
				} else if (velX < 0) {
					x = e.getX() + Block.WIDTH;
					velX = accX = 0;
				}
			}
			updateHitBox();
		}
	}
	protected void blockCollisionY(LinkedList<Block> blocks) {
		if (y + height > PlayState.world.getHeight()) {
			y = PlayState.world.getHeight() - height;
			accY = velY = 0;
			onGround = true;
		} else if (y < 0) {
			y = 0;
			accY = velY = 0;
		}

		boolean hit = false;
		for (Block e:blocks) {
			if (e.canCollide() && collides(e)) {
				hit = true;
				if (velY >= 0) {
					y = e.getY() - height;
					velY = accY = 0;
					onGround = true;
				} else {
					y = e.getY() + Block.HEIGHT;
					velY = accY = 0;
				}
			}
			updateHitBox();
		}
		if (!hit) onGround = false;
	}
}
