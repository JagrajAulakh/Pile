package com.pile;

import com.pile.block.*;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.entity.EntityManager;
import com.pile.entity.player.Player;
import com.pile.state.PlayState;

import java.awt.*;
import java.util.LinkedList;

public class World {
	public static final double GRAVITY = 0.4;
	public static final double FRICTION = 0.2;
	// Used for detecting collisions within 1 Grid Space of Entities
	public static final int GRID_SIZE = 200;
	// Todo Change W & H
	private int width = 2048; // World Width
	private int height = 1024; // World Height

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
	private void clearEntities() {
		if (grid == null) grid = new LinkedList[width][height];
		int px = getGridX(player);
		for (int x = px - 5; x <= px + 5; x++) {
			for (int y = 0; y < grid[px].length; y++) {
				if (0 <= x && x < width/GRID_SIZE) {
					LinkedList<GameObject> objs = grid[x][y];
					if (objs != null) {
						for (int i = objs.size() - 1; i >= 0; i--) {
							if (objs.get(i) instanceof Entity) {
								grid[x][y].remove(i);
							}
						}
					}
				}
			}
		}
	}
	private void clearGrid() {
		if (grid == null) {
			grid = new LinkedList[width][height];
		}
		int px = getGridX(player);
		int rad = 5;
		for (int x = px - rad; x <= px + rad; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (0 <= x && x < grid.length) {
					grid[x][y] = null;
				}
			}
		}
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
		if (frame % 30 == 0) {
			System.out.println("SORTING BLOCKS");
			clearGrid();
			sortEntities();
			sortBlocks();
		} else {
			clearEntities();
			sortEntities();
		}
	}

	public void generateWorld() {
		entities.add(new Player(width/2, 0));
		int tmp = 2;
		for (int i = -tmp; i <= tmp; i++) {
			entities.add(new Enemy(width/2 + i*150, 0));
		}

		int dir = (int)(Math.random()*2) == 0?-1:1;
//		int y = (int)(Math.random() * (height/Block.HEIGHT - 16)) + 8;
		int y = height/Block.HEIGHT/2;
		for (int x = 1; x < width; x += Block.WIDTH) {
//			if (y < 5 || y > height/Block.HEIGHT - 20) dir *= -1;
			if ((int)(Math.random()*100) < 20) dir *= -1;
			y = Math.max(3, Math.min(y + (int)(Math.random()*3)*dir, height/Block.HEIGHT-15));
			System.out.println(y);
			blocks.add(new Block(x, height - y*Block.HEIGHT, 1));
			blocks.add(new Block(x, height - y*Block.HEIGHT + Block.HEIGHT, 0));
			for (int i = 0; i <= y-2; i++) {
				blocks.add(new Block(x, height - i*Block.HEIGHT, 2));
			}
		}

	}

	public void update() {
//		blocks.update();
//		entities.update();
		for (GameObject e:getObjectsAround(player, 10)) {
			e.update();
		}
		camera.centerOn(player);
		if (frame % 10 == 0) {
			clearEntities();
			sortGrid();
		}
		frame = (frame + 1) % 60;
	}
	public void render(Graphics g) {
		for (GameObject e:getObjectsAround(player, 10)) {
			if (e instanceof Entity) {
				if (!(e instanceof Player)) {
					entities.draw(g, (Entity)e);
				}
			} else if (e instanceof Block) {
				blocks.draw(g, (Block)e);
			}
		}
		// FOR DRAWING BLOCK SELECTION
		Block b = getBlockAtSpot(Game.input.mx + camera.getOffsetX(), Game.input.my + camera.getOffsetY());
		if (b != null) {
			g.setColor(Color.GREEN);
			int xPos = (int)(b.getX() - camera.getOffsetX());
			int yPos = (int)(b.getY() - camera.getOffsetY());
			g.drawRect(xPos, yPos, Block.WIDTH, Block.HEIGHT);
			if (Game.input.mouseUp(0)) {
				blocks.remove(b);
			}
		}
		if (Game.input.mouseUp(2)) {
			System.out.println("HERE");
//			clearBlockGrid();
			blocks.add(new Block((int)(Game.input.mx/Block.HEIGHT) * Block.HEIGHT, (int)(Game.input.my/Block.HEIGHT) * Block.HEIGHT, 0));
		}

		if (player != null) {
			entities.draw(g, player);
		}
	}
}
