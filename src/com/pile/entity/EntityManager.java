package com.pile.entity;

import com.pile.GameCamera;
import com.pile.image.Resources;
import javafx.beans.property.ReadOnlySetProperty;

import java.awt.*;
import java.awt.image.BufferedImage;
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
			BufferedImage img = e.isFlipped() ? Resources.flip(e.getImage(), true, false):e.getImage();
			g.drawImage(img, (int)(e.getX() - camera.getOffsetX()), (int)(e.getY() - camera.getOffsetY()), null);
		}
	}
}
