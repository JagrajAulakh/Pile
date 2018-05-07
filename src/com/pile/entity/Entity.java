package com.pile.entity;

import com.pile.image.ImageType;

import java.awt.image.BufferedImage;

public abstract class Entity {
	protected double x, y, width, height, velX, velY, accX, accY;
	protected ImageType image;

	public Entity(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public BufferedImage getImage() { return image.getImage(); }

	public abstract void update();
}
