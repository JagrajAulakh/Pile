package com.pile;

import com.pile.block.*;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.entity.EntityManager;
import com.pile.entity.player.Player;

import java.awt.*;
import java.util.LinkedList;

public class World {
	public static final double GRAVITY = 0.6;
	public static final double FRICTION = 0.2;
	public static final int GRID_SIZE = 200;
	private int width = 10000;
	private int height = 1000;

	private LinkedList<GameObject>[][] grid;
	private LinkedList<GameObject>[][] blockGrid;

	private EntityManager entities;
	private BlockManager blocks;
	private GameCamera camera;

	private Thread genThread;

	public World() {
		camera = new GameCamera(this);
		entities = new EntityManager(camera);
		blocks = new BlockManager(camera);
		clearGrid();
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	private void clearGrid() {
		grid = new LinkedList[width][height];
	}

	public int getGridX(double wx) { return (int)(wx/GRID_SIZE); }
	public int getGridY(double wy) { return (int)(wy/GRID_SIZE); }
	public int getGridX(GameObject e) { return getGridX(e.getX()); }
	public int getGridY(GameObject e) { return getGridY(e.getY()); }

	public LinkedList<GameObject> getGameObjectsAtGridSpot(int gx, int gy) { return grid[gx][gy]; }
	public LinkedList<GameObject> getGameObjectsAtSpot(GameObject e) {
		return getGameObjectsAtGridSpot(getGridX(e.getX()), getGridY(e.getY()));
	}
	public LinkedList<GameObject> getGameObjectsAtSpot(double x, double y) {
		return getGameObjectsAtGridSpot(getGridX(x), getGridX(y));
	}
	public LinkedList<GameObject> getObjectsAround(GameObject e) {
		LinkedList<GameObject> list = new LinkedList<GameObject>();
		int gx = getGridX(e);
		int gy = getGridY(e);
		for (int x = gx-1; x <= gx + 1; x++) {
			for (int y = gy-1; y <= gy + 1; y++) {
				if (0 <= x && x <= width/GRID_SIZE && 0 <= y && y <= height/GRID_SIZE) {
					LinkedList<GameObject> l = grid[x][y];
					if (l != null) {
						list.addAll(l);
					}
				}
			}
		}
		return list;
	}

	private void sortBlocks() {
		for (Block b:blocks.getBlocks()) {
			int posX = getGridX(b);
			int posY = getGridY(b);
		}
	}

	private void sortGrid() {
		grid = blockGrid;
		for (Entity e:entities.getEntities()) {
			int posX = getGridX(e);
			int posY = getGridY(e);
			if (grid[posX][posY] == null) {
				grid[posX][posY] = new LinkedList<GameObject>();
			}
			grid[posX][posY].add(e);
		}
	}

	public void generateWorld() {
		entities.add(new Player(width/2, 0));
		int tmp = 2;
		for (int i = -tmp; i <= tmp; i++) {
			entities.add(new Enemy(width/2 + i*150, 0));
		}

		for (int x = 0; x < width; x += Block.WIDTH) {
			blocks.add(new Dirt(x, height-Block.HEIGHT*11));
			blocks.add(new Grass(x, height-Block.HEIGHT*12));
			for (int y = 0; y <= 10; y += 1) {
				blocks.add(new Stone(x, height - Block.HEIGHT*y));
			}
		}

	}

	public void update() {
		if (genThread == null) {
			genThread = new Thread(() -> { generateWorld(); });
			genThread.start();
			while (genThread.isAlive()) { System.out.println("LOADING"); }
		}
		blocks.update();
		entities.update();
		clearGrid();
		sortGrid();
	}
	public void render(Graphics g) {
		blocks.render(g);
		entities.render(g);
	}
}
