package com.pile.entity;

import com.pile.Game;
import com.pile.GameLogic;
import com.pile.GameObject;
import com.pile.World;
import com.pile.block.Block;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;
import com.pile.entity.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

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

	public void collisionY() {
		if (y + height > PlayState.world.getHeight()) {
			y = PlayState.world.getHeight() - height;
			velY = accY = 0;
			onGround = true;
		}
		LinkedList<GameObject> l = PlayState.world.getObjectsAround(this);
		for (GameObject e:l) {
			if (e instanceof Block) {
				if (collides(e)) {
					if (velY > 0) {
						y = e.getY() - height;
						velY = accY = 0;
						onGround = true;
					}
					else if (velY < 0) {
						y = e.getY() + Block.HEIGHT;
						velY = 0;
					}
				}
			}
		}
	}
	public void collisionX() {

	}

	@Override
	public void update() {
		accY = World.GRAVITY;
		accX = 0;

//		if ((int)(Math.random()*100) <= 10 && Math.abs(accX) < 0.1) {
//			accX = (accX > 0 ? (Math.random()*100 <= 50?-Math.random()*10:Math.random()*10) : (Math.random()*100 <= 50?Math.random()*10:-Math.random()*10));
//		}

		onGround = false;
		velY += accY;
		y += velY;
		collisionY();

		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;
		collisionX();

		updateHitBox();
	}
}
