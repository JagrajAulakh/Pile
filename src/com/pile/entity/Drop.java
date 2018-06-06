package com.pile.entity;

import com.pile.World;
import com.pile.block.Block;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;
import com.pile.entity.player.Player;

import java.util.LinkedList;

public class Drop extends Entity {
	public static final int DESPAWN_TIME = 60 * 60 * 5;
	private int id, despawnCounter;
	private boolean canPick;
	private Player player;
	private int pickDelayMax;

	public Drop(double x, double y, int id, Player player, double velX, double velY) {
		this(x, y, id, player, velX, velY, 10);
	}
	public Drop(double x, double y, int id, Player player, double velX, double velY, int pickDelay) {
		super(x, y);
		this.id = id;
		this.player = player;
		double sc = 0.7;
		this.image = new SingleImage(Resources.scale(Resources.blocks[id].getImage(), sc));
		this.width = Block.WIDTH * sc;
		this.height = Block.HEIGHT * sc;
		this.velX = velX;
		this.velY = velY;
		this.pickDelayMax = pickDelay;
		canPick = false;
	}
	public int getId() { return id; }
	public boolean canPick() { return canPick; }
	public void collisionX(LinkedList<Block> blocks) {
		blockCollisionX(blocks);
	}
	public void collisionY(LinkedList<Block> blocks) {
		blockCollisionY(blocks);
	}

	@Override
	public void update() {
		despawnCounter++;
		if (despawnCounter > DESPAWN_TIME) PlayState.world.removeEntity(this);
		if (despawnCounter > pickDelayMax) canPick = true;
		accX = 0;
		accY = World.GRAVITY;

//		double dx = (player.getX() + player.getWidth()/2) - (x + width/2);
//		double dy = (player.getY() + player.getHeight()/2) - (y + height/2);
//		double r = Math.hypot(dx, dy);
//
//		if (r < Block.WIDTH * 5) {
//
//			double angle = Math.atan2(dy, dx);
//			velX = 100/(r * Math.cos(angle));
//			velY = (r * Math.sin(angle)) / 50;
//		}

		LinkedList<Block> blocks = PlayState.world.getBlocksAround(this, 3);

		accX -= velX * World.FRICTION/2;
		velX += accX;
		x += velX;
		updateHitBox();
		collisionX(blocks);
		updateHitBox();

		velY += accY;
		y += velY;
		updateHitBox();
		collisionY(blocks);
		updateHitBox();
	}
}
