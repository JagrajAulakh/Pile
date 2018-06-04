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
	private Player player;

	public Drop(double x, double y, int id, Player player, double velX, double velY) {
		this(x, y, id, player);
		this.velX = velX;
		this.velY = velY;
	}
	public Drop(double x, double y, int id, Player player) {
		super(x, y);
		this.id = id;
		this.player = player;
		double sc = 0.7;
		this.image = new SingleImage(Resources.scale(Resources.blocks[id].getImage(), sc));
		this.width = Block.WIDTH * sc;
		this.height = Block.HEIGHT * sc;
	}
	public int getId() { return id; }
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
