package com.pile.entity;

import com.pile.GameCamera;
import com.pile.World;
import com.pile.image.Resources;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class EntityManager {
	public static final int GRID_SIZE = 100;
	private ArrayList<Entity> entities;
	private GameCamera camera;
	private Player player;
	private LinkedList<Entity>[][] grid;

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

	private void clearGrid() {
		grid = new LinkedList[World.WIDTH][World.HEIGHT];
//		for (int x = 0; x < grid.length; x++) {
//			for (int y = 0; y < grid[x].length; y++) {
//				grid[x][y] = null;
//			}
//		}
	}

	private void sortGrid() {
		for (Entity e:entities) {
			int posX = (int)(e.getX()/World.WIDTH*GRID_SIZE);
			int posY = (int)(e.getY()/World.HEIGHT*GRID_SIZE);
			if (grid[posX][posY] == null) {
				grid[posX][posY] = new LinkedList<Entity>();
			}
			grid[posX][posY].add(e);
			System.out.println(posX + " " + posY);
		}
	}

	public void update() {
		clearGrid();
		sortGrid();
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
