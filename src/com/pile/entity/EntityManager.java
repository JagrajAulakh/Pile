package com.pile.entity;

import java.awt.*;
import java.util.ArrayList;

public class EntityManager {
	private ArrayList<Entity> entities;

	public EntityManager() {
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
			g.drawImage(e.getImage(), (int)e.getX(), (int)e.getY(), null);
		}
	}
}
