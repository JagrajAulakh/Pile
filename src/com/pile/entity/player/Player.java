package com.pile.entity.player;

import com.pile.GameObject;
import com.pile.Input;
import com.pile.World;
import com.pile.block.Block;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.image.ImageType;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends Entity {
	public static final double SPEED = 4;
	public static final double JUMP_HEIGHT = 12;
	private BodyPart arm, leg, body, head;
	private double counter;

	public Player(double x, double y) {
		super(x, y);
		head = new Head(Resources.partsMale.get("head"));
		body = new Body(Resources.partsMale.get("body"));
		arm = new Arm(Resources.partsMale.get("arm"));
		leg = new Leg(Resources.partsMale.get("leg"));
		image = drawImage(0, 0, 0, 0);
		width = 64;
		height = image.getImage().getHeight();
		updateHitBox();
		onGround = false;
		flipped = true;
	}

	private SingleImage drawImage(double armBack, double armFront, double legBack, double legFront) {
		BufferedImage img = new BufferedImage(200, 180, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		int headHeight = 64;
		BufferedImage h = head.getImage();
		BufferedImage b = body.getImage();
		BufferedImage a = arm.getImage();
		BufferedImage l = leg.getImage();

		g.rotate(Math.toRadians(armFront), img.getWidth()/2, headHeight + 4);
		g.drawImage(a, img.getWidth()/2 - a.getWidth()/2, headHeight + 4, null);
		g.rotate(Math.toRadians(-armFront), img.getWidth()/2, headHeight + 4);

		g.rotate(Math.toRadians(legFront), img.getWidth()/2, headHeight + b.getHeight() - 8);
		g.drawImage(l, img.getWidth()/2 - l.getWidth()/2, headHeight + b.getHeight(), null);
		g.rotate(Math.toRadians(-legFront), img.getWidth()/2, headHeight + b.getHeight() - 8);
		g.rotate(Math.toRadians(legBack), img.getWidth()/2, headHeight + b.getHeight() - 8);
		g.drawImage(l, img.getWidth()/2 - l.getWidth()/2, headHeight + b.getHeight(), null);
		g.rotate(Math.toRadians(-legBack), img.getWidth()/2, headHeight + b.getHeight() - 8);

		int hpos = (int)(img.getWidth()/2 - headHeight/2);
		g.drawImage(h, hpos, 0, null);
		g.drawImage(b, img.getWidth()/2 - b.getWidth()/2, headHeight, null);

		g.rotate(Math.toRadians(armBack), img.getWidth()/2, headHeight + 4);
		g.drawImage(a, img.getWidth()/2 - a.getWidth()/2, headHeight + 4, null);
		g.rotate(Math.toRadians(-armBack), img.getWidth()/2, headHeight + 4);

		g.dispose();
		return new SingleImage(img);
	}

	// Offscreen drawing for player's image
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
			// TODO Temporary player jumping. Change it maybe.
			counter = (counter + 10) % 360;
			double angle = (10)*Math.sin(Math.toRadians(counter));
			if (velY < 0) {
				image = drawImage(150, 150, -10, 10);
			}
			else if (velY > 0) {
				image = drawImage(angle+150, -angle+150, -angle, angle);
			}
		}
	}

	private void collisionY() {
		if (y + height > PlayState.world.getHeight()) {
			y = PlayState.world.getHeight() - height;
			accY = velY = 0;
			onGround = true;
		} else if (y < 0) {
			y = 0;
			accY = velY = 0;
		}
		LinkedList<GameObject> l = PlayState.world.getObjectsAround(this);
		if (l != null) {
			for (GameObject e:l) {
				if (e instanceof Block) {
					if (collides(e)) {
						System.out.println("HITTING BLOCK");
					}
				}
			}
		}
	}
	private void collisionX() {
		LinkedList<GameObject> l = PlayState.world.getObjectsAround(this);
		if (l != null) {
			for (GameObject e:l) {
				if (e instanceof Enemy) {
					if (collides(e)) {
//						System.out.println("HITTING DA ENEMY");
					}
				}
			}
		}
		if (x < 0) {
			x = 0;
		} else if (x + width > PlayState.world.getWidth()) {
			x = PlayState.world.getWidth() - width;
		}
	}

	@Override
	public void update() {
		accX = 0;
		accY = World.GRAVITY;

		if (Input.keys[KeyEvent.VK_D]) {
			accX = SPEED;
			velX = Math.min(velX, 3);
			flipped = true;
		} if (Input.keys[KeyEvent.VK_A]) {
			accX = -SPEED;
			velX = Math.max(velX, -3);
			flipped = false;
		} if (Input.keys[KeyEvent.VK_SPACE]) {
			if (onGround) {
				velY = -JUMP_HEIGHT;
			}
		}

		// Starting here is the Y movement
		onGround = false;
		velY += accY;
		y += velY;
		collisionY();


		// Starting here is the X movement
		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;
		collisionX();

		updateHitBox();
//		prevImage = image;
		determineImage();
//		if (prevImage != image) {
//			image.reset();
//		}
		image.tick();
	}
}
