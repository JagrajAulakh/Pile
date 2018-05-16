package com.pile.entity;

import com.pile.GameObject;
import com.pile.image.ImageType;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Entity extends GameObject {
	protected double width, height, velX, velY, accX, accY;
	protected boolean onGround, flipped;

	public Entity(double x, double y) {
		super(x, y);
		hitBox = new Rectangle2D.Double(x, y, width, height);
	}

	public double getWidth() { return width; }
	public double getHeight() { return height; }
	public boolean isFlipped() { return flipped; }

	@Override
	public void updateHitBox() {
		hitBox.setRect(x, y, width, height);
	}
	public BufferedImage getImage() { return image.getImage(); }

	public abstract void update();

	@Override
	public String toString() {
		return "PLAYER";
	}
}
