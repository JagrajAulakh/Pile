package com.pile.entity.player;

import com.pile.HUD;
import com.pile.Input;
import com.pile.World;
import com.pile.block.Block;
import com.pile.block.Chest;
import com.pile.entity.Drop;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.entity.player.inv.Tool;
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
	public static final int REACH = 5;
	private BodyPart arm, leg, body, head;
	private int health, maxHealth;
	private double counter;
	private World world;
	private boolean inventoryState, mining, godMode, ruler, invincible;
	private Inventory inventory;

	private Chest currentChest;

	public Player(double x, double y, World world) {
		super(x, y);
		this.world = world;
		inventory = new Inventory(0, 0);
		inventory.add(15);
		inventory.add(20);
		inventory.add(25);
		inventory.add(30);

		head = new Head(Resources.partsMale.get("head"));
		body = new Body(Resources.partsMale.get("body"));
		arm = new Arm(Resources.partsMale.get("arm"));
		leg = new Leg(Resources.partsMale.get("leg"));
		image = drawImage(0, 0, 0, 0);
		width = 56 * Resources.SCALE*2;
		height = 180 * Resources.SCALE * 2;
		maxHealth = health = 10;
		onGround = inventoryState = godMode = ruler = invincible = false;
		flipped = true;
		currentChest = null;
		updateHitBox();
	}

	public boolean ruler() { return ruler; }
	public boolean isInvincible() { return invincible; }

	public Inventory getInventory() { return inventory; }
	public boolean inventoryState() { return inventoryState; }
	public void toggleInventory() { inventoryState = !inventoryState; }

	public void addItem(Item item) {
		inventory.add(item.getId(), item.getAmount());
	}
	public void removeItem(Item item) {
		for (int i = 0; i < item.getAmount(); i++) inventory.remove(item.getId());
	}
	public Chest getChest() { return currentChest; }
	public void setChest(Chest c) { currentChest = c; }

	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }

	private void drawItem(Graphics2D g, int hi) {
		Item item = inventory.getCurrentItem();
		if (item != null) {
			if (item instanceof Tool) {
				g.rotate(Math.toRadians(-45), item.getImage().getWidth()/2 - item.getImage().getWidth()/2, hi - leg.getImage().getHeight()*0.2 - item.getHeight()/2);
				g.drawImage(Resources.flip(item.getImage(), true, false), item.getImage().getWidth()/2 - item.getImage().getWidth()/2, (int)(hi - leg.getImage().getHeight()*0.2 - item.getHeight()/2), null);
				g.rotate(Math.toRadians(45), item.getImage().getWidth()/2 - item.getImage().getWidth()/2, hi - leg.getImage().getHeight()*0.2 - item.getHeight()/2);
			} else if (item instanceof com.pile.entity.player.inv.Block) {
				g.drawImage(Resources.flip(item.getImage(), true, false), (int)(item.getImage().getWidth()/2 + item.getImage().getWidth()*0.8), hi - leg.getImage().getHeight() - item.getImage().getHeight()/2, null);
			}
		}
	}

	private SingleImage drawImage(double armBack, double armFront, double legBack, double legFront) {
		int w = (int)(200*Resources.SCALE * 2);
		int hi = (int)(220*Resources.SCALE * 2);
		BufferedImage img = new BufferedImage(w, hi, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		int headHeight = (int)(64 * Resources.SCALE * 2);
		BufferedImage h = head.getImage();
		BufferedImage b = body.getImage();
		BufferedImage a = arm.getImage();
		BufferedImage l = leg.getImage();

		// ARM
		g.rotate(Math.toRadians(armFront), img.getWidth()/2, hi - l.getHeight() - b.getHeight() + 4);
		g.drawImage(a, img.getWidth()/2 - a.getWidth()/2, hi - l.getHeight() - b.getHeight() + 4, null);
		if (!flipped) {
			drawItem(g, hi);
		}
		g.rotate(Math.toRadians(-armFront), img.getWidth()/2, hi - l.getHeight() - b.getHeight() + 4);

		// LEGS
		g.rotate(Math.toRadians(legFront), img.getWidth()/2, hi - l.getHeight() - 8);
		g.drawImage(l, img.getWidth()/2 - l.getWidth()/2, hi - l.getHeight(), null);
		g.rotate(Math.toRadians(-legFront), img.getWidth()/2, hi - l.getHeight() - 8);
		g.rotate(Math.toRadians(legBack), img.getWidth()/2, hi - l.getHeight() - 8);
		g.drawImage(l, img.getWidth()/2 - l.getWidth()/2, hi - l.getHeight(), null);
		g.rotate(Math.toRadians(-legBack), img.getWidth()/2, hi - l.getHeight() - 8);

		// HEAD
		g.drawImage(h, img.getWidth()/2 - headHeight/2, hi - l.getHeight() - b.getHeight() - headHeight, null);
		// BODY
		g.drawImage(b, img.getWidth()/2 - b.getWidth()/2, hi - l.getHeight() - b.getHeight(), null);

		// ARM
		g.rotate(Math.toRadians(armBack), img.getWidth()/2, hi - l.getHeight() - b.getHeight() + 4);
		if (flipped) {
			drawItem(g, hi);
		}
		g.drawImage(a, img.getWidth()/2 - a.getWidth()/2, hi - l.getHeight() - b.getHeight() + 4, null);
		g.rotate(Math.toRadians(-armBack), img.getWidth()/2, hi - l.getHeight() - b.getHeight() + 4);

		g.dispose();
		return new SingleImage(img);
	}

	private boolean withinReach(Block b) { return withinReach(b.getX(), b.getY()); }
	private boolean withinReach(double wx, double wy) {
		return Math.hypot(wx - x - width/2, wy - y - height/2) / Block.WIDTH <= REACH;
	}

	public Block getSelectedBlock() {
		double wx = Input.mx + world.camera.getOffsetX();
		double wy = Input.my + world.camera.getOffsetY();
		Block b = world.getBlockAtSpot(wx, wy);
		if (b != null) {
			if (withinReach(b)) return b;
		}
		return null;
	}

	// Offscreen drawing for player's image
	private void determineImage() {
		if (onGround) {
			if (Math.abs(velX) > 0.8) {
				counter = (counter + 10) % 360;
				double angle = 30*Math.sin(Math.toRadians(counter));
				if (flipped) image = drawImage(mining ? -counter%180 + 180: -angle, angle, -angle, angle);
				else image = drawImage(angle, mining ? -counter%180 + 180: -angle, -angle, angle);
			} else {
				counter = (counter + 10) % 360;
				if (flipped) image = drawImage(mining ? -counter%180 + 180 : 0, 0, 0, 0);
				else image = drawImage(0, mining ? -counter%180 + 180 : 0, 0, 0);
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
		if (!godMode) blockCollisionY(blocks);
	}
	private void collisionX(LinkedList<Block> blocks) {
		if (!godMode) blockCollisionX(blocks);
		LinkedList<Entity> l = PlayState.world.getEntitiesAround(this, 1);
		for (Entity e:l) {
			if (e instanceof Drop) {
				Drop d = (Drop)e;
				if (collides(d) && d.canPick()) {
					boolean added = inventory.add(d.getId());
					if (added) PlayState.world.removeEntity(d);
				}
			}
		}
	}

	@Override
	public void update() {
		if (!godMode) {
			accY = World.GRAVITY;
		} else {
			accY = 0;
		}
		accX = 0;

		playerInput();

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
	private void playerInput(){
		//Movement
		if (godMode) {
			if (Input.keyDown(KeyEvent.VK_W)) {
				velY = -5;
			} if (Input.keyDown(KeyEvent.VK_S)) {
				velY = 5;
			}
		}
		if (Input.keyDown(KeyEvent.VK_D)) {
			accX = SPEED;
			velX = Math.min(velX, 3);
			flipped = true;
		} if (Input.keyDown(KeyEvent.VK_A)) {
			accX = -SPEED;
			velX = Math.max(velX, -3);
			flipped = false;
		} if (Input.keyDown(KeyEvent.VK_SPACE) || Input.keyDown(KeyEvent.VK_W)) {
			if (onGround) {
				onGround = false;
				velY = -JUMP_HEIGHT;
			}
		}
		//Inventory
		if (Input.keyUpOnce(KeyEvent.VK_E)) {
			//Closing inventory with an item in hand will cause it to drop
			if(HUD.getInHand() != null){
				HUD.dropInHand(this);
			}
			//Flipping between 2 states of the inventory. HotBar only & Full Inventory
			inventoryState = !inventoryState;
			//Only have a chest open when player is in full inventory
			currentChest = inventoryState == false ? null : currentChest;
		}
		//godMode
		if (Input.keyUpOnce(KeyEvent.VK_G)) {
			godMode = !godMode;
		}
		//ruler
		if(Input.keyDownOnce(KeyEvent.VK_R)){
			ruler = !ruler;
		}

		if (Input.keyUpOnce(KeyEvent.VK_C)) {
			inventory.add(10);
		}

		if (Input.keyUpOnce(KeyEvent.VK_Q)) {
			if (inventory.getCurrentItem() != null) {
				double x = getVelX() > 0 ? getX() + getWidth() : getX() - Block.WIDTH;
				double y = getY() - getHealth() / 2;
				Item spot = inventory.getCurrentItem();
				world.addEntity(new Drop(x, y, spot.getId(), this, (Math.random()*2+2) * (getVelX()>0 ? 1 : -1), -5, 60));
				inventory.decrease();
			}
		}

		if (!inventoryState) {
			int wx = (int)((Input.mx + world.camera.getOffsetX())/Block.WIDTH) * Block.WIDTH;
			int wy = (int)((Input.my + world.camera.getOffsetY())/Block.HEIGHT) * Block.HEIGHT;
			if (Input.mouseDown(2)) {
				Block b = getSelectedBlock();
				if (b != null && b instanceof Chest) {
					inventoryState = !inventoryState;
					currentChest = (Chest)b;
					System.out.println("OPEN SESAME!");
				}
			}
			if (Input.mousePressed(2)) {
				//Placing blocks
				Item item = inventory.getCurrentItem();
				if (item != null && item instanceof com.pile.entity.player.inv.Block) {
					Block b;
					if (item.getId() == 10) {
						b = new Chest(wx, wy);
					} else {
						b = new Block(wx, wy, item.getId());
					}
					boolean hitting = false;
					for (Entity e:world.getEntitiesAround(this, 4)) {
						if ((e instanceof Player || e instanceof Enemy) && e.collides(b)) {
							hitting = true;
							break;
						}
					}
					if (withinReach(b) && !hitting && !collides(b)) {
						// Checks surrounding blocks to place next to (can't place floating blocks)
						if (world.getBlockAtSpot(b.getX() - Block.WIDTH, b.getY()) != null || world.getBlockAtSpot(b.getX() + Block.WIDTH, b.getY()) != null || world.getBlockAtSpot(b.getX(), b.getY() - Block.HEIGHT) != null || world.getBlockAtSpot(b.getX(), b.getY() + Block.HEIGHT) != null) {
							if (world.addBlock(b)) inventory.decrease();
						}
					}
				}
			}
			//Mining
			mining = false;
			if (Input.mousePressed(0)) {
				Block b = getSelectedBlock();
				if (b != null) {
					mining = true;
					if(godMode) world.removeBlock(b, 100);
					else {
						Item item = inventory.getCurrentItem();
						if (item != null && item instanceof Tool && Resources.toolBlocks[item.getId()].contains(b.getId())) {
							world.removeBlock(b, Resources.toolSpeeds[item.getId()]);
						} else {
							world.removeBlock(b);
						}
					}
				}
			}
		}

		// Hotbar Inventory Selection
		if (!inventoryState) {
			if (Input.wheelUp()) {
				inventory.moveSpotLeft();
			} else if (Input.wheelDown()) {
				inventory.moveSpotRight();
			}
		}
		for (int i = 0; i <= 8; i++) {
			if (Input.keyDown('1'+i)) {
				inventory.setSpot(i);
			}
		} if (Input.keyDown('0')) inventory.setSpot(9);
	}
}
