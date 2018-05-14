package com.pile.entity;

import com.pile.GameCamera;
import com.pile.entity.player.Player;
import com.pile.image.Resources;

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
	public void add(Player p) {
		add((Entity)p);
		player = p;
	}

	public ArrayList<Entity> getEntities() { return entities; }

	public void update() {
		for (Entity e:entities) {
			e.update();
		}
		camera.centerOn(player);
	}
	public void render(Graphics g) {
		for (Entity e:entities) {
			BufferedImage img = e.isFlipped() ? Resources.flip(e.getImage(), true, false) : e.getImage();
			double realX, realY;
			realX = (e.getX()+e.getWidth()/2) - (e.getImage().getWidth()/2) - camera.getOffsetX();
			realY = e.getY() - camera.getOffsetY();
			g.drawImage(img, (int)(realX), (int)(realY), null);
			Rectangle2D box = e.getHitBox();
			g.setColor(Color.BLUE);
			g.drawRect((int)(e.getX() - camera.getOffsetX()), (int)(e.getY() - camera.getOffsetY()), (int)e.getWidth(), (int)e.getHeight());
		}
	}
}
