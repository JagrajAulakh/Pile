package com.pile.entity;

import com.pile.World;
import com.pile.block.Block;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.util.LinkedList;

public class FallingSand extends Entity {

	public FallingSand(double x, double y) {
		super(x, y);
		image = Resources.itemImages[28];
		width = Block.WIDTH;
		height = Block.HEIGHT;
	}

	@Override
	public void update() {
		accY = World.GRAVITY;
		accX = 0;

		LinkedList<Block> blocks = PlayState.world.getBlocksAround(this, 2);
		blockCollisionY(blocks);
		if (PlayState.world.getBlockAtSpot(x, y + height + 10) != null) {
			System.out.println("GOING TO BLOCK");
			PlayState.world.addBlock(new Block(Math.round(x/Block.WIDTH)*Block.WIDTH, Math.round(y/Block.HEIGHT)*Block.HEIGHT, 28));
			PlayState.world.removeEntity(this);
		}
		velY += accY;
		y += velY;
		updateHitBox();
	}
}
