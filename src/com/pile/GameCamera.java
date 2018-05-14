package com.pile;

import com.pile.entity.Entity;

public class GameCamera {
	private double offsetX, offsetY;
	private World world;
	public GameCamera(World world) {
		this.world = world;
		offsetX = 0;
		offsetY = 0;
	}
	public double getOffsetX() { return offsetX; }
	public double getOffsetY() { return offsetY; }

	public void centerOn(Entity e) {
		offsetX = e.getX() + e.getWidth()/2 - (double)Game.WIDTH/2;
		offsetY = e.getY() + e.getHeight()/2 - (double)Game.HEIGHT/2;
		if (offsetX < 0) {
			offsetX = 0;
		}
		if (offsetX + Game.WIDTH > world.getWidth()) {
			offsetX = world.getWidth()- Game.WIDTH;
		}

		if (offsetY < 0) {
			offsetY = 0;
		}
		if (offsetY + Game.HEIGHT > world.getHeight()) {
			offsetY = world.getHeight() - Game.HEIGHT;
		}
	}
}
