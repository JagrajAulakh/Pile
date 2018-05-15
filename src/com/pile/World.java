package com.pile;

import com.pile.block.Block;
import com.pile.block.BlockManager;
import com.pile.block.Dirt;
import com.pile.block.Grass;
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
	private int width = 2000;
	private int height = 1000;

	private LinkedList<GameObject>[][] grid;

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
	public LinkedList<GameObject> getGameObjectsAtGridSpot(GameObject e) {
		return getGameObjectsAtGridSpot(getGridX(e.getX()), getGridY(e.getY()));
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
						for (GameObject s:l) {
							list.add(s);
						}
					}
				}
			}
		}
		return list;
	}

	private void sortGrid() {
		for (Block b:blocks.getBlocks()) {
			int posX = getGridX(b);
			int posY = getGridY(b);
		}
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
		int x = 2;
		for (int i = -x; i <= x; i++) {
			entities.add(new Enemy(width/2 + i*150, 0));
		}
		for (int i = 0; i < width; i+= Block.WIDTH) {
			blocks.add(new Dirt(i, height-Block.HEIGHT));
			blocks.add(new Grass(i, height-Block.HEIGHT*2));
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
