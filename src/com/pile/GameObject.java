package com.pile;

import java.awt.geom.Rectangle2D;

public abstract class GameObject {
	protected double x, y, width, height;
	protected Rectangle2D hitBox;

	public GameObject(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public Rectangle2D getHitBox() { return hitBox; }
	public boolean collides(GameObject other) {
		Rectangle2D enemyHit = other.getHitBox();
		return hitBox.intersects(enemyHit);
	}
}
