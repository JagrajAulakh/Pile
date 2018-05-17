package com.pile;

import com.pile.block.*;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.entity.EntityManager;
import com.pile.entity.player.Player;

import java.awt.*;
import java.util.LinkedList;

public class World {
	public static final double GRAVITY = 0.4;
	public static final double FRICTION = 0.2;
	// Used for detecting collisions within 1 Grid Space of Entities
	public static final int GRID_SIZE = 200;
	// Todo Change W & H
	private int width = 2000; // World Width
	private int height = 1000; // World Height

	private int frame;

	// Grid of the World, used to determine where every GameObjects are
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
		blockGrid = new LinkedList[width][height];
		generateWorld();
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getGridX(double wx) { return (int)(wx/GRID_SIZE); }
	public int getGridY(double wy) { return (int)(wy/GRID_SIZE); }
	public int getGridX(GameObject e) { return getGridX(e.getX()); }
	public int getGridY(GameObject e) { return getGridY(e.getY()); }
	private void clearGrid() { grid = new LinkedList[width][height]; }
	private void clearBlockGrid() { blockGrid = new LinkedList[width][height]; }

	public LinkedList<GameObject> getGameObjectsAtGridSpot(int gx, int gy) { return grid[gx][gy]; }
	public LinkedList<GameObject> getGameObjectsAtSpot(double x, double y) {
		return getGameObjectsAtGridSpot(getGridX(x), getGridX(y));
	}
	public LinkedList<GameObject> getGameObjectsAtSpot(GameObject e) {
		return getGameObjectsAtSpot(e.getX(), e.getY());
	}
	public LinkedList<GameObject> getObjectsAround(GameObject e) {
		return getObjectsAround(e, 1);
	}
	public LinkedList<GameObject> getObjectsAround(GameObject e, int rad) {
		LinkedList<GameObject> list = new LinkedList<GameObject>();
		int gx = getGridX(e);
		int gy = getGridY(e);
		for (int x = gx - rad; x <= gx + rad; x++) {
			for (int y = gy - rad; y <= gy + rad; y++) {
				if (0 <= x && x <= getGridX(width) && 0 <= y && y <= getGridY(height)) {
					LinkedList<GameObject> l = grid[x][y];
					if (l != null) list.addAll(l);
				}
			}
		}
		return list;
	}

	// returns a block if there is a block at position (x,y) on screen
	public Block getBlockAtSpot(double x, double y) {
		LinkedList<GameObject> l = getGameObjectsAtSpot(x, y);
		if (l != null) {
			for (GameObject o:l) {
				if (o instanceof Block) {
					System.out.println("o");
					double screenX, screenY;
					screenX = o.getX() - camera.getOffsetX();
					screenY = o.getY() - camera.getOffsetY();
					if (screenX < x && x < screenX + Block.WIDTH) {
						if (screenY < y && y < screenY + Block.WIDTH) {
							return (Block)o;
						}
					}
				}
			}
		}
		return null;
	}

	private void sortBlocks() {
		for (Block b:blocks.getBlocks()) {
			int posX = getGridX(b);
			int posY = getGridY(b);
			if (grid[posX][posY] == null) {
				grid[posX][posY] = new LinkedList<GameObject>();
			}
			grid[posX][posY].add(b);
		}
	}

	private void sortEntities() {
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
	private void sortGrid() {
		clearGrid();
		sortBlocks();
		sortEntities();
	}

	public void generateWorld() {
		entities.add(new Player(width/2, 0));
		int tmp = 2;
		for (int i = -tmp; i <= tmp; i++) {
			entities.add(new Enemy(width/2 + i*150, 0));
		}

		for (int x = 0; x < width; x += Block.WIDTH) {
			blocks.add(new Dirt(x, height-Block.HEIGHT*7));
			blocks.add(new Grass(x, height-Block.HEIGHT*8));
			for (int y = 0; y <= 6; y += 1) {
				blocks.add(new Stone(x, height - Block.HEIGHT*y));
			}
		}

	}

	public void update() {
//		if (genThread == null) {
//			genThread = new Thread(() -> { generateWorld(); });
//			genThread.start();
//			while (genThread.isAlive()) { System.out.println("LOADING"); }
//		}
		blocks.update();
		entities.update();
		if (frame % 10 == 1) {
			sortGrid();
		}
		frame = (frame + 1) % 60;
	}
	public void render(Graphics g) {
		blocks.render(g);
		entities.render(g);
	}
}
