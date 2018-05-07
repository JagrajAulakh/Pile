package com.pile.entity.player;

import com.pile.Game;
import com.pile.Input;
import com.pile.World;
import com.pile.image.Resources;
import com.pile.entity.Entity;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Entity {
	public static final double SPEED = 5;
	public static final double JUMP_HEIGHT = 10;

	private boolean onGround, flipped;

	public Player(double x, double y) {
		super(x, y);
		image = Resources.player1;
		width = image.getImage().getWidth();
		height = image.getImage().getHeight();
		onGround = false;
	}

	@Override
	public void update() {
		accX = 0;
		accY = World.GRAVITY;

		if (Input.keys[KeyEvent.VK_D]) {
			accX = onGround==true?SPEED:SPEED/2;
			velX = Math.min(velX, 3);
		} if (Input.keys[KeyEvent.VK_A]) {
			accX = onGround==true?-SPEED:-SPEED/2;
			velX = Math.max(velX, -3);
		} if (Input.keys[KeyEvent.VK_SPACE]) {
			if (onGround) {
				velY = -JUMP_HEIGHT;
			}
		}
		onGround = false;

		if (y + height > Game.HEIGHT) {
			y = Game.HEIGHT - height;
			accY = 0;
			velY = 0;
			onGround = true;
		}
		accX -= velX * World.FRICTION;
		velX += accX;
		velY += accY;
		x += velX;
		y += velY;

		image.tick();
	}
}
