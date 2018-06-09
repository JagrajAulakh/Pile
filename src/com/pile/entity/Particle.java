package com.pile.entity;

import com.pile.World;
import com.pile.state.PlayState;

import java.awt.*;

public class Particle {
	private double x, y, velX, velY, accX, accY;
	private int size, lifeCounter, lifeMax;
	private Color color;
	public Particle(double x, double y) {
		int r = (int)(Math.random()*100)+80;
		color = new Color(r, r, r);
		this.x = x;
		this.y = y;
		size = (int)(Math.random()*7 + 3);
		lifeMax = (int)(Math.random()*20) + 10;
		lifeCounter = 0;
		velX = Math.random() * 10 - 5;
		velY = Math.random() * -2 - 3;
	}

	public double getX() { return x; }
	public double getY() { return y; }
	public int getSize() { return size; }
	public Color getColor() { return color; }

	public void update() {
		lifeCounter++;
		if (lifeCounter >= lifeMax) {
			PlayState.world.removeParticle(this);
		}

		accY = World.GRAVITY/2;
		accX = 0;

		accX -= velX * World.FRICTION;
		velX += accX;
		velY += accY;
		x += velX;
		y += velY;
	}

	public void render(Graphics g) {

	}
}
