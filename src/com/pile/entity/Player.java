package com.pile.entity;

import com.pile.Game;
import com.pile.Input;
import com.pile.World;
import com.pile.image.ImageType;
import com.pile.image.Resources;

import java.awt.event.KeyEvent;

public class Player extends Entity {
	public static final double SPEED = 4;
	public static final double JUMP_HEIGHT = 10;
	private ImageType prevImage;

	public Player(double x, double y) {
		super(x, y);
		image = Resources.playerMaleWalking;
//		width = image.getImage().getWidth();
		width = 64;
		height = image.getImage().getHeight();
		updateHitBox();
		onGround = false;
		flipped = true;
		prevImage = image;
	}

	private void determineImage() {
		if (onGround) {
			if (Math.abs(velX) > 0.8) {
				image = Resources.playerMaleWalking;
			} else {
				image = Resources.player1;
			}
		} else {
			image = Resources.player1;
		}
	}

	@Override
	public void update() {
		accX = 0;
		accY = World.GRAVITY;

		if (Input.keys[KeyEvent.VK_D]) {
			accX = SPEED;
			velX = Math.min(velX, 3);
			flipped = true;
		} if (Input.keys[KeyEvent.VK_A]) {
			accX = -SPEED;
			velX = Math.max(velX, -3);
			flipped = false;
		} if (Input.keys[KeyEvent.VK_SPACE]) {
			if (onGround) {
				velY = -JUMP_HEIGHT;
			}
		}

		// Starting here is the Y movement
		onGround = false;

		velY += accY;
		y += velY;

		if (y + height > Game.HEIGHT) {
			y = Game.HEIGHT - height;
			accY = 0;
			velY = 0;
			onGround = true;
		}
		// Starting here is the X movement
		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;

		if (x < 0) {
			x = 0;
		} else if (x + width > World.WIDTH) {
			x = World.WIDTH - width;
		}

		updateHitBox();
		prevImage = image;
		determineImage();
		if (prevImage != image) {
			image.reset();
		}
		image.tick();
//		System.out.println(velX);
	}
}
