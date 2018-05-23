package com.pile.entity;

import com.pile.Game;
import com.pile.GameCamera;
import com.pile.Input;
import com.pile.block.Block;
import com.pile.entity.player.Player;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EntityManager {

	private ArrayList<Entity> entities;
	private GameCamera camera;
	private Player player;

	public EntityManager(GameCamera camera) {
		this.camera = camera;
		entities = new ArrayList<Entity>();
	}

	public void add(Entity e) {
		entities.add(e);
	}

	public ArrayList<Entity> getEntities() { return entities; }

	public void update() {
		for (Entity e:entities) {
			e.update();
		}
		camera.centerOn(player);
	}

	public void draw(Graphics g, Entity e) {
		BufferedImage img = e.isFlipped() ? Resources.flip(e.getImage(), true, false) : e.getImage();
		double realX, realY, screenX, screenY;
		realX = (e.getX()+e.getWidth()/2) - (e.getImage().getWidth()/2) - camera.getOffsetX();
		realY = e.getY() - camera.getOffsetY();
		screenX = e.getX() - camera.getOffsetX();
		screenY = e.getY() - camera.getOffsetY();
		if (screenX <= Game.WIDTH && screenX + e.getWidth() >= 0) {
			if (screenY <= Game.HEIGHT && screenY + e.getHeight() >= 0) {
				g.drawImage(img, (int)(realX), (int)(realY), null);
				Rectangle2D box = e.getHitBox();
				g.setColor(Color.BLUE);
				g.drawRect((int)(box.getX() - camera.getOffsetX()), (int)(box.getY() - camera.getOffsetY()), (int)box.getWidth(), (int)box.getHeight());
			}
		}
	}
	public void render(Graphics g) {
		for (Entity e:entities) {
			if (!(e instanceof Player)) {
				draw(g, e);
			}
		}
		if (player != null) {
			draw(g, player);
		}
	}
}
