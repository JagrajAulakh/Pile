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

	// Counter that resets every second
	private int frame;

	// Grid of the World, used to determine where every GameObjects are
	private LinkedList<GameObject>[][] grid;
	private LinkedList<GameObject>[][] blockGrid;

	private EntityManager entities;
	private BlockManager blocks;
	private GameCamera camera;
	private Player player;

	public World() {
		player = new Player(0,0);
		camera = new GameCamera(this);
		entities = new EntityManager(camera);
		blocks = new BlockManager(camera);
		clearGrid();
		blockGrid = new LinkedList[width][height];
	}
	public void setPlayer(Player player) { this.player = player; }

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
	private void clearGrid() {
		if (grid == null) {
			grid = new LinkedList[width][height];
		}
		int px = getGridX(player);
		int py = getGridY(player);
		for (int x = px - 5; x <= px + 5; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (0 <= x && x < grid.length) {
					grid[x][y] = null;
				}
			}
		}
	}
	private void clearBlockGrid() {
//		blockGrid = new LinkedList[width][height];
		if (blockGrid == null) {
			blockGrid = new LinkedList[width][height];
		}
		int px = getGridX(player);
		int py = getGridY(player);
		for (int x = px - 5; x <= px + 5; x++) {
			for (int y = 0; y < blockGrid[0].length; y++) {
				if (0 <= x && x < blockGrid.length) {
					blockGrid[x][y] = null;
				}
			}
		}
	}

	public LinkedList<GameObject> getGameObjectsAtGridSpot(int gx, int gy) { return grid[gx][gy]; }
	public LinkedList<GameObject> getGameObjectsAtSpot(double x, double y) {
		return getGameObjectsAtGridSpot(getGridX(x), getGridY(y));
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
					double screenX, screenY;
					screenX = o.getX();
					screenY = o.getY();
					if (screenX <= x && x <= screenX + Block.WIDTH) {
						if (screenY <= y && y <= screenY + Block.HEIGHT) {
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
		String dir = "down";
		int y = (int)(Math.random()*(height/Block.HEIGHT/4)) * Block.HEIGHT;
		for (int x = 0; x < width; x += Block.WIDTH) {
			// Random chance that direction will change
			if (Math.random()*100 < 10) {
				if (dir.equals("down")) {
					dir = "up";
				} else {
					dir = "down";
				}
			}

			int diff = (int)(Math.random()*3) * Block.HEIGHT;
			y += (dir.equals("down")? diff: -diff);
			for (int i = y+Block.HEIGHT*2; i > 0; i+=Block.HEIGHT) {
				blocks.add(new Stone(x, i));
			}
		}
//		int tmp = 2;
//		for (int i = -tmp; i <= tmp; i++) {
//			entities.add(new Enemy(width/2 + i*150, 0));
//		}
//
//		for (int x = 1; x < width; x += Block.WIDTH) {
//			blocks.add(new Grass(x, height-Block.HEIGHT*8));
//			blocks.add(new Dirt(x, height-Block.HEIGHT*7));
//			for (int y = 1; y <= 6; y += 1) {
//				blocks.add(new Stone(x, height - Block.HEIGHT*y));
//			}
//		}

	}

	public void update() {
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
