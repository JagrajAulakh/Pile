package com.pile.entity.player;

import com.pile.Game;
import com.pile.GameObject;
import com.pile.Input;
import com.pile.World;
import com.pile.block.Block;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends Entity {
	public static final double SPEED = 4;
	public static final double JUMP_HEIGHT = 12;
	private BodyPart arm, leg, body, head;
	private double counter;
	private World world;

	public Player(double x, double y, World world) {
		super(x, y);
		this.world = world;
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

		int hpos = img.getWidth()/2 - headHeight/2;
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
		if (y + height > world.getHeight()) {
			y = world.getHeight() - height;
			accY = velY = 0;
			onGround = true;
		} else if (y < 0) {
			y = 0;
			accY = velY = 0;
		}
		LinkedList<GameObject> l = world.getBlocksAround(this);
		for (GameObject e:l) {
			if (e instanceof Block) {
				if (collides(e)) {
					if (velY >= 0) {
						y = e.getY() - height;
						velY = accY = 0;
						onGround = true;
					}
					else {
						y = e.getY() + Block.HEIGHT;
						velY = 0;
					}
				}
			}
		}
	}
	private void collisionX() {
		if (x < 0) {
			x = 0;
		} else if (x + width > world.getWidth()) {
			x = world.getWidth() - width;
		}
		LinkedList<GameObject> l = world.getBlocksAround(this);
		for (GameObject e:l) {
			if (e instanceof Enemy) {
				if (collides(e)) {
//					System.out.*println("OUCH!");
				}
			} else if (e instanceof Block) {
				if (collides(e)) {
					if (velX > 0) {
						x = e.getX() - width;
						velX = accX = 0;
					} else if (velX < 0) {
						x = e.getX() + Block.WIDTH;
						velX = accX = 0;
					}
				}
			}
		}
	}

	@Override
	public void update() {
		accX = 0;
		accY = World.GRAVITY;

		if (Input.keyDown(KeyEvent.VK_D)) {
			accX = SPEED;
			velX = Math.min(velX, 3);
			flipped = true;
		} if (Input.keyDown(KeyEvent.VK_A)) {
			accX = -SPEED;
			velX = Math.max(velX, -3);
			flipped = false;
		} if (Input.keyDown(KeyEvent.VK_SPACE)) {
			if (onGround) {
				onGround = false;
				velY = -JUMP_HEIGHT;
			}
		}

		// Starting here is the Y movement
		velY += accY;
		y += velY;
		updateHitBox();
		collisionY();
		updateHitBox();


		// Starting here is the X movement
		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;
		updateHitBox();
		collisionX();
		updateHitBox();

		determineImage();
		image.tick();
	}
}
