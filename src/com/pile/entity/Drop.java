package com.pile.entity;

import com.pile.World;
import com.pile.image.Resources;

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
		this.image = Resources.blocks[id];
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
		updateHitBox();
		collisionX();
		updateHitBox();

		velY += accY;
		updateHitBox();
		collisionY();
		updateHitBox();
	}
}
