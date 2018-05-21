package com.pile;

import com.pile.image.ImageType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
// Takes care of all Entities & Blocks
public abstract class GameObject {
	protected double x, y, width, height; // Location & Size of Object
	protected Rectangle2D hitBox; // Hitbox of GameObject
	protected ImageType image; // Visual display of GameObject

	public GameObject(double x, double y) {
		this.x = x;
		this.y = y;
		hitBox = new Rectangle2D.Double(x, y, width, height);
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public Rectangle2D getHitBox() { return hitBox; }
	public boolean collides(GameObject other) {
		Rectangle2D enemyHit = other.getHitBox();
		return hitBox.intersects(enemyHit);
	}
//	public abstract void update();
	public abstract void updateHitBox();

	public abstract void update();
}
