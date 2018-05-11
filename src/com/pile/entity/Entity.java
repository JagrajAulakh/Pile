package com.pile.entity;

import com.pile.image.ImageType;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
	protected double x, y, width, height, velX, velY, accX, accY;
	protected ImageType image;
	protected boolean onGround, flipped;
	protected Rectangle2D hitBox;

	public Entity(double x, double y) {
		this.x = x;
		this.y = y;
		hitBox = new Rectangle2D.Double(x, y, width, height);
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public double getWidth() { return width; }
	public double getHeight() { return height; }
	public boolean isFlipped() { return flipped; }
	public Rectangle2D getHitBox() { return hitBox; }
	public void updateHitBox() {
		hitBox.setRect(x, y, width, height);
	}
	public BufferedImage getImage() { return image.getImage(); }
	public boolean collides(Entity e) {
		Rectangle2D enemyHit = e.getHitBox();
		return hitBox.intersects(enemyHit);
	}

	public abstract void update();

	@Override
	public String toString() {
		return "PLAYER";
	}
}
