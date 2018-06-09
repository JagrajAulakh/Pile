package com.pile.entity;

import com.pile.GameObject;
import com.pile.World;
import com.pile.block.Block;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;
import com.pile.entity.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Enemy extends Entity {
	private enum MovingState {
		WALKING,
		STILL
	}

	private MovingState state;
	private int dir;
	private int counter;

	public Enemy(double x, double y) {
		super(x, y);

		image = drawImage(0, 0, 0, 0);
		width = 60 * Resources.SCALE*2;
		height = image.getImage().getHeight();
		updateHitBox();

		state = MovingState.STILL;
		dir = (int)(Math.random()*2) == 0?-1:1;
	}

	private SingleImage drawImage(double armBack, double armFront, double legBack, double legFront) {
		int w = (int)(200*Resources.SCALE * 2);
		int hi = (int)(180*Resources.SCALE * 2);
		BufferedImage img = new BufferedImage(w, hi, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		int headHeight = (int)(64 * Resources.SCALE*2);
		BufferedImage h = Resources.partsZombie.get("head");
		BufferedImage b = Resources.partsZombie.get("body");
		BufferedImage a = Resources.partsZombie.get("arm");
		BufferedImage l = Resources.partsZombie.get("leg");

		g.rotate(Math.toRadians(armFront), img.getWidth()/2, headHeight + 4);
		g.drawImage(a, img.getWidth()/2 - a.getWidth()/2, headHeight + 4, null);
		g.rotate(Math.toRadians(-armFront), img.getWidth()/2, headHeight + 4);

		g.rotate(Math.toRadians(legFront), img.getWidth()/2, headHeight + b.getHeight() - 8);
		g.drawImage(l, img.getWidth()/2 - l.getWidth()/2, headHeight + b.getHeight(), null);
		g.rotate(Math.toRadians(-legFront), img.getWidth()/2, headHeight + b.getHeight() - 8);
		g.rotate(Math.toRadians(legBack), img.getWidth()/2, headHeight + b.getHeight() - 8);
		g.drawImage(l, img.getWidth()/2 - l.getWidth()/2, headHeight + b.getHeight(), null);
		g.rotate(Math.toRadians(-legBack), img.getWidth()/2, headHeight + b.getHeight() - 8);

		int hpos = img.getWidth()/2 - headHeight/2;
		g.drawImage(h, hpos, 0, null);
		g.drawImage(b, img.getWidth()/2 - b.getWidth()/2, headHeight, null);

		g.rotate(Math.toRadians(armBack), img.getWidth()/2, headHeight + 4);
		g.drawImage(a, img.getWidth()/2 - a.getWidth()/2, headHeight + 4, null);
		g.rotate(Math.toRadians(-armBack), img.getWidth()/2, headHeight + 4);

		g.dispose();
		return new SingleImage(img);
	}

	private void determineImage() {
		if (onGround) {
			if (Math.abs(velX) > 0.8) {
				counter = (counter + 10) % 360;
				double angle = 30*Math.sin(Math.toRadians(counter));
				image = drawImage(angle, -angle, -angle, angle);
			} else {
				counter = 0;
				image = drawImage(0, 0, 0, 0);
			}
		} else {
			counter = (counter + 10) % 360;
			double angle = (10)*Math.sin(Math.toRadians(counter));
			if (velY < 0) {
				image = drawImage(150, 150, -10, 10);
			}
			else if (velY > 0) {
				image = drawImage(angle+150, -angle+150, -angle, angle);
			}
		}
		if (flipped) {
			image = new SingleImage(Resources.flip(image.getImage(), true, false));
		}
	}

	private void collisionY(LinkedList<Block> blocks) {
		if (y + height > PlayState.world.getHeight()) {
			y = PlayState.world.getHeight() - height;
			accY = velY = 0;
			onGround = true;
		} else if (y < 0) {
			y = 0;
			accY = velY = 0;
		}
		blockCollisionY(blocks);
		//		LinkedList<GameObject> l = PlayState.world.getBlocksAround(this, 1);
//		for (GameObject e:l) {
//			if (e instanceof Block) {
//				if (collides(e)) {
//					if (velY >= 0) {
//						y = e.getY() - height;
//						velY = accY = 0;
//						onGround = true;
//					}
//					else {
//						y = e.getY() + Block.HEIGHT;
//						velY = 0;
//					}
//				}
//			}
//		}
	}
	private void collisionX(LinkedList<Block> blocks) {
		if (x < 0) {
			x = 0;
		} else if (x + width > PlayState.world.getWidth()) {
			x = PlayState.world.getWidth() - width;
		}
		blockCollisionX(blocks);
	}

	@Override
	public void update() {
		accY = World.GRAVITY;
		accX = 0;

		if ((int)(Math.random()*100) <= 2) {
			if (state == MovingState.WALKING) state = MovingState.STILL;
			else state = MovingState.WALKING;
		}
		if (state == MovingState.WALKING) {
			if ((int)(Math.random()*100) < 5) dir *= -1;
			velX = 5*dir;
		} else {
			velX = accX = 0;
		}
		flipped = dir==1;
		if ((int)(Math.random()*100) <= 1) {
			if (onGround) {
				onGround = false;
				velY = -Player.JUMP_HEIGHT;
			}
		}

		LinkedList<Block> blocks = PlayState.world.getBlocksAround(this, 3);
		velY += accY;
		y += velY;
		updateHitBox();
		collisionY(blocks);
		updateHitBox();

		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;
		updateHitBox();
		collisionX(blocks);
		updateHitBox();

		determineImage();
	}
}
