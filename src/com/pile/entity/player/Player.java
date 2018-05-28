package com.pile.entity.player;
//Todo should have inventory toggle
import com.pile.GameObject;
import com.pile.Input;
import com.pile.World;
import com.pile.block.Block;
import com.pile.entity.Drop;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.entity.player.inv.Inventory;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends Entity {
	public static final double SPEED = 4 * Resources.SCALE*2;
	public static final double JUMP_HEIGHT = Block.HEIGHT * Resources.SCALE;
	private BodyPart arm, leg, body, head;
	private double counter;
	private World world;
	private boolean inventoryState;
	private Inventory inventory;

	public Player(double x, double y, World world) {
		super(x, y);
		this.world = world;
		head = new Head(Resources.partsMale.get("head"));
		body = new Body(Resources.partsMale.get("body"));
		arm = new Arm(Resources.partsMale.get("arm"));
		leg = new Leg(Resources.partsMale.get("leg"));
		image = drawImage(0, 0, 0, 0);
		width = 60 * Resources.SCALE*2;
		height = image.getImage().getHeight();
		updateHitBox();
		onGround = false;
		flipped = true;
		inventory = new Inventory();
	}

	public boolean inventoryState() { return inventoryState; }

	private SingleImage drawImage(double armBack, double armFront, double legBack, double legFront) {
		int w = (int)(200*Resources.SCALE * 2);
		int hi = (int)(180*Resources.SCALE * 2);
		BufferedImage img = new BufferedImage(w, hi, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		int headHeight = (int)(64 * Resources.SCALE * 2);
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
		blockCollisionY();
	}
	private void collisionX() {
		blockCollisionX();
		for (Entity e:PlayState.world.getEntitiesAround(this, 1)) {
			if (e instanceof Drop) {
				if (collides(e)) {
					PlayState.world.removeEntity(e);
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
		} if (Input.keyUpOnce(KeyEvent.VK_E)) {
			inventoryState = !inventoryState;
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
