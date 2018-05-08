package com.pile.entity;

import com.pile.GameCamera;

import java.awt.*;
import java.util.ArrayList;

public class EntityManager {
	private ArrayList<Entity> entities;
	private GameCamera camera;

	public EntityManager(GameCamera camera) {
		this.camera = camera;
		entities = new ArrayList<Entity>();
	}

	public void add(Entity e) {
		entities.add(e);
	}

	public void update() {
		for (Entity e:entities) {
			e.update();
		}
	}
	public void render(Graphics g) {
		for (Entity e:entities) {
			g.drawImage(e.getImage(), (int)(e.getX() - camera.getOffsetX()), (int)(e.getY() - camera.getOffsetY()), null);
		}
	}
}
