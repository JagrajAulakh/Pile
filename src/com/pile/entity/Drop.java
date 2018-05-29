package com.pile.entity;

import com.pile.World;
import com.pile.block.Block;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;

import java.util.LinkedList;

public class Drop extends Entity {
	private int id;

	public Drop(double x, double y, int id, double velX, double velY) {
		this(x, y, id);
		this.velX = velX;
		this.velY = velY;
	}
	public Drop(double x, double y, int id) {
		super(x, y);
		this.id = id;
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
		accX = 0;
		accY = World.GRAVITY;

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
