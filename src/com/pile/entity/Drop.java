package com.pile.entity;

import com.pile.World;
import com.pile.block.Block;
import com.pile.image.Resources;
import com.pile.image.SingleImage;

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

	public void collisionX() {
		blockCollisionX();
	}

	public void collisionY() {
		blockCollisionY();
	}

	@Override
	public void update() {
		accX = 0;
		accY = World.GRAVITY;
		// TODO maybe do some animation for items on ground

		accX *= velX * World.FRICTION;
		velX += accX;
		x += velX;
		updateHitBox();
		collisionX();
		updateHitBox();

		velY += accY;
		y += velY;
		updateHitBox();
		collisionY();
		updateHitBox();
	}
}
