package com.pile.entity;

import com.pile.World;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {
	public Enemy(double x, double y) {
		super(x, y);
		width = 50;
		height = 172;
		image = new SingleImage(new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_ARGB));
		Graphics g = image.getImage().createGraphics();
		g.setColor(Color.RED);
		g.fillRect(0, 0, (int)width, (int)height);
		g.dispose();
		updateHitBox();
	}

	@Override
	public void update() {
		accY = World.GRAVITY;
		accX = 0;

		if ((int)(Math.random()*100) <= 10 && Math.abs(accX) < 0.1) {
			accX = (accX > 0 ? (Math.random()*100 <= 50?-Math.random()*10:Math.random()*10) : (Math.random()*100 <= 50?Math.random()*10:-Math.random()*10));
		}

		onGround = false;
		velY += accY;
		y += velY;
		if (y + height > PlayState.world.getHeight()) {
			y = PlayState.world.getHeight() - height;
			velY = accY = 0;
			onGround = true;
		}

		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;
		updateHitBox();
	}
}
