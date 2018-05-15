package com.pile.entity.player;

import com.pile.GameObject;
import com.pile.Input;
import com.pile.World;
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
	public static final double JUMP_HEIGHT = 10;
	private ImageType prevImage;
	private BodyPart arm, leg, body, head;
	private double counter;

	public Player(double x, double y) {
		super(x, y);
		image = Resources.playerMaleWalking;
		width = 64;
		height = image.getImage().getHeight();
		updateHitBox();
		onGround = false;
		flipped = true;
		prevImage = image;
		arm = new Arm();
		leg = new Leg();
		body = new Body();
		head = new Head();
	}

	private SingleImage drawImage(double armL, double armR, double legL, double legR) {
		BufferedImage img = new BufferedImage(96, 180, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		BufferedImage h = head.getImage();
		BufferedImage b = body.getImage();
		BufferedImage a = arm.getImage();
		BufferedImage l = leg.getImage();

		g.rotate(Math.toRadians(armR), img.getWidth()/2, h.getHeight()+8);
		g.drawImage(a, img.getWidth()/2 - a.getWidth()/2, h.getHeight()+8, null);
		g.rotate(Math.toRadians(-armR), img.getWidth()/2, h.getHeight()+8);

		g.rotate(Math.toRadians(legR), img.getWidth()/2, h.getHeight() + b.getHeight() - 8);
		g.drawImage(l, img.getWidth()/2 - l.getWidth()/2, h.getHeight() + b.getHeight(), null);
		g.rotate(Math.toRadians(-legR), img.getWidth()/2, h.getHeight() + b.getHeight() - 8);
		g.rotate(Math.toRadians(legL), img.getWidth()/2, h.getHeight() + b.getHeight() - 8);
		g.drawImage(l, img.getWidth()/2 - l.getWidth()/2, h.getHeight() + b.getHeight(), null);
		g.rotate(Math.toRadians(-legL), img.getWidth()/2, h.getHeight() + b.getHeight() - 8);

		g.drawImage(h, img.getWidth()/2 - h.getWidth()/2, 0, null);
		g.drawImage(b, img.getWidth()/2 - b.getWidth()/2, h.getHeight(), null);

		g.rotate(Math.toRadians(armL), img.getWidth()/2, h.getHeight()+8);
		g.drawImage(a, img.getWidth()/2 - a.getWidth()/2, h.getHeight()+8, null);
		g.rotate(Math.toRadians(-armL), img.getWidth()/2, h.getHeight()+8);

		g.dispose();
		return new SingleImage(img);
	}

	private void determineImage() {
		if (onGround) {
			if (Math.abs(velX) > 0.8) {
				counter = (counter + 10) % 360;
			} else {
				counter = 0;
			}
		} else {
			counter = 0;
		}

		// Offscreen drawing of the player
		double angle = 30*Math.sin(Math.toRadians(counter));
		image = drawImage(angle, -angle, -angle, angle);
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
	}
	private void collisionX() {
		LinkedList<GameObject> l = PlayState.world.getObjectsAround(this);
		if (l != null) {
			for (GameObject e:l) {
				if (!(e instanceof Player)) {
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
