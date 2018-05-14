package com.pile.entity;

import com.pile.Input;
import com.pile.World;
import com.pile.image.ImageType;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Player extends Entity {
	public static final double SPEED = 4;
	public static final double JUMP_HEIGHT = 10;
	private ImageType prevImage;

	public Player(double x, double y) {
		super(x, y);
		image = Resources.playerMaleWalking;
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
				image = Resources.playerMaleWalking.getFrame(0);
			}
		} else {
			image = Resources.playerMaleWalking.getFrame(0);
		}
	}

	private void collisionY() {
		if (y + height > PlayState.world.getWidth()) {
			y = PlayState.world.getHeight() - height;
			accY = velY = 0;
			onGround = true;
		} else if (y < 0) {
			y = 0;
			accY = velY = 0;
		}
	}
	private void collisionX() {
		LinkedList<Entity> l = PlayState.world.getEntitiesAround(this);
		if (l != null) {
			for (Entity e:l) {
				if (!(e instanceof Player)) {
					if (collides(e)) {
						System.out.println("HITTING DA ENEMY");
					}
				}
			}
		}
		if (x < 0) {
			x = 0;
		} else if (x + width > PlayState.world.getWidth()) {
			x = PlayState.world.getWidth() - width;
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
		collisionY();


		// Starting here is the X movement
		accX -= velX * World.FRICTION;
		velX += accX;
		x += velX;
		collisionX();

		updateHitBox();
		prevImage = image;
		determineImage();
		if (prevImage != image) {
			image.reset();
		}
		image.tick();
	}
}
