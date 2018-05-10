package com.pile.entity;

import com.pile.World;
import com.pile.image.Resources;
import com.pile.image.SingleImage;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {
	public Enemy(double x, double y) {
		super(x, y);
		image = new SingleImage(new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB));
		Graphics g = image.getImage().createGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, 50, 50);
		g.dispose();
	}

	@Override
	public void update() {
		accY = World.GRAVITY;
		accX = 1;

		
		velY += accY;
		y += velY;

		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;
	}
}
