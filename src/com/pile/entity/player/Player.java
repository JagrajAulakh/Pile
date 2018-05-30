package com.pile.entity.player;

import com.pile.Input;
import com.pile.World;
import com.pile.block.Block;
import com.pile.entity.Drop;
import com.pile.entity.Entity;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends Entity {
	public static final double SPEED = 4 * Resources.SCALE*2;
	public static final double JUMP_HEIGHT = Block.HEIGHT * Resources.SCALE;
	private BodyPart arm, leg, body, head;
	private double counter;
	private World world;
	private boolean inventoryState, mining;
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
		inventoryState = false;
	}

	public boolean inventoryState() { return inventoryState; }
	public Inventory getInventory() { return inventory; }

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
				image = drawImage(angle, mining ? counter : -angle, -angle, angle);
			} else {
				counter = (counter + 10) % 360;
				image = drawImage(mining ? -counter : 0, 0, 0, 0);
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

	private void collisionY(LinkedList<Block> blocks) {
		blockCollisionY(blocks);
	}
	private void collisionX(LinkedList<Block> blocks) {
		blockCollisionX(blocks);
		for (Entity e:PlayState.world.getEntitiesAround(this, 1)) {
			if (e instanceof Drop) {
				if (collides(e)) {
					Drop d = (Drop)e;
					if (inventory.add(d.getId())) PlayState.world.removeEntity(e);
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
			//Flipping between 2 states of the inventory. HotBar only & Full Inventory
			inventoryState = !inventoryState;
		}

		if (!inventoryState) {
			int wx = (int)((Input.mx + world.camera.getOffsetX())/Block.WIDTH) * Block.WIDTH;
			int wy = (int)((Input.my + world.camera.getOffsetY())/Block.HEIGHT) * Block.HEIGHT;
			if (Input.mousePressed(2)) {
				Item item = inventory.getCurrentItem();
				if (item != null) {
					Block b = new Block(wx, wy, item.getId());
					if (!collides(b)) {
						if (world.addBlock(b)) inventory.decrease();
					}
				}
			}
			mining = false;
			if (Input.mousePressed(0)) {
				Block b = world.getBlockAtSpot(wx, wy);
				if (b != null) {
					mining = true;
					world.removeBlock(b);
				}
			}
		}
		if (Input.wheelUp()) {
			inventory.moveSpotLeft();
		} else if (Input.wheelDown()) {
			inventory.moveSpotRight();
		}
		for (int i = 0; i <= 8; i++) {
			if (Input.keyDown('1'+i)) {
				inventory.setSpot(i);
			}
		} if (Input.keyDown('0')) inventory.setSpot(9);

		LinkedList<Block> blocks = PlayState.world.getBlocksAround(this, 3);

		// Starting here is the Y movement
		velY += accY;
		y += velY;
		updateHitBox();
		collisionY(blocks);
		updateHitBox();

		// Starting here is the X movement
		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;
		updateHitBox();
		collisionX(blocks);
		updateHitBox();

		determineImage();
	}
}
