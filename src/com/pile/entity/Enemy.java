package com.pile.entity;

import com.pile.Game;
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

		onGround = false;
		velY += accY;
		y += velY;
		if (y + height > Game.HEIGHT) {
			y = Game.HEIGHT - height;
			accY = velY = 0;
			onGround = true;
		}
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
