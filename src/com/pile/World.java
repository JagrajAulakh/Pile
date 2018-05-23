package com.pile;

import com.pile.block.*;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.entity.EntityManager;
import com.pile.entity.player.Player;
import com.pile.state.PlayState;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

public class World {
	public static final double GRAVITY = 0.4;
	public static final double FRICTION = 0.2;
	// Used for detecting collisions within 1 Grid Space of Entities
	public static final int GRID_SIZE = 200;
	// Todo Change W & H
	private int width = 2048*10; // World Width
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
		grid = new LinkedList[width][height];
		blockGrid = new LinkedList[width][height];
	}
	private void addPlayer(Player p) {
		this.player = p;
		entities.add(player);
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
	public LinkedList<GameObject> getBlocksAround(GameObject e) { return getThingsAround(e, blockGrid, 1); }
	public LinkedList<GameObject> getObjectsAround(GameObject e, int rad) { return getThingsAround(e, grid, rad); }
	public LinkedList<GameObject> getThingsAround(GameObject e, LinkedList[][] g, int rad) {
		LinkedList<GameObject> list = new LinkedList<GameObject>();
		int gx = getGridX(e);
		int gy = getGridY(e);
		for (int x = gx - rad; x <= gx + rad; x++) {
			for (int y = gy - rad; y <= gy + rad; y++) {
				if (0 <= x && x <= getGridX(width) && 0 <= y && y <= getGridY(height)) {
					LinkedList<GameObject> l = g[x][y];
					if (l != null) list.addAll(l);
				}
			}
		}
		return list;
	}

	// returns a block if there is a block at position (x,y) in world
	public Block getBlockAtSpot(double x, double y) {
//		LinkedList<GameObject> l = getGameObjectsAtSpot(x, y);
		LinkedList<GameObject> l = blockGrid[getGridX(x)][getGridX(y)];
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

	public void addBlock(Block b) {
		blocks.add(b);
		if (blockGrid[b.getGridX()][b.getGridY()] == null) {
			blockGrid[b.getGridX()][b.getGridY()] = new LinkedList<GameObject>();
		}
		blockGrid[b.getGridX()][b.getGridY()].add(b);
	}

	public void removeBlock(Block b) {
		blocks.remove(b);
		blockGrid[b.getGridX()][b.getGridY()].remove(b);
	}

	public int countEntities() {
		int tot = 0;
		for (LinkedList[] i:grid) {
			for (LinkedList<GameObject> j:i) {
				if (j != null) {
					for (GameObject e:j) {
						if (e instanceof Entity) {
							tot++;
						}
					}
				}
			}
		}
		return tot;
	}

	public void sortEntities(int range) {
		int px = player.getGridX();
		for (int x = px - range; x <= px + range; x++) {
			for (int y = 0; y <= height/Block.HEIGHT; y++) {
				if (0 <= x && x <= width/Block.WIDTH) {
//					if (blockGrid[x][y] != null) {
//						grid[x][y] = new LinkedList<GameObject>();
//						grid[x][y].addAll(blockGrid[x][y]);
//					}
					grid[x][y] = null;
				}
			}
		}
		for (Entity e:entities.getEntities()) {
			int ex = e.getGridX();
			int ey = e.getGridY();
			if (0 <= ex && ex <= getGridX(width)) {
				if (px - range < ex && ex < px + range) {
					if (grid[ex][ey] == null) {
						grid[ex][ey] = new LinkedList<GameObject>();
					}
					grid[ex][ey].add(e);
				}
			}
		}
	}
	public void generateWorld() {
		addPlayer(new Player(width/2, 0));
		int tmp = 80;
		for (int i = -tmp; i <= tmp; i++) {
			entities.add(new Enemy(width/2 + i*150, 0));
		}

		int dir = (int)(Math.random()*2) == 0?-1:1;
		int y = height/Block.HEIGHT/2;
		for (int x = 0; x < width; x += Block.WIDTH) {
			if ((int)(Math.random()*100) < 20) dir *= -1;
			y = Math.max(3, Math.min(y + (int)(Math.random()*3)*dir, height/Block.HEIGHT-15));
			System.out.println(y);
			addBlock(new Block(x, height - y*Block.HEIGHT, 1));
			addBlock(new Block(x, height - y*Block.HEIGHT + Block.HEIGHT, 0));
			for (int i = 0; i <= y-2; i++) {
				addBlock(new Block(x, height - i*Block.HEIGHT, 2));
			}
		}

	}

	public void update() {
		for (GameObject e:getThingsAround(player, blockGrid, 10)) {
			e.update();
		}
		for (GameObject e:getThingsAround(player, grid, 10)) {
			e.update();
		}
		camera.centerOn(player);
		if (Game.input.mouseUp(2)) {
			int wx = (int)((Game.input.mx + camera.getOffsetX())/Block.WIDTH) * Block.WIDTH;
			int wy = (int)((Game.input.my + camera.getOffsetY())/Block.HEIGHT) * Block.HEIGHT;
//			blocks.add(new Block(wx, wy, 0));
			addBlock(new Block(wx, wy, 0));
		}
		if (frame % 10 == 0) {
			sortEntities(5);
		}
		frame = (frame + 1) % 60;
	}
	public void render(Graphics g) {
		for (GameObject e:getThingsAround(player, blockGrid,10)) {
			if (e instanceof Block) {
				blocks.draw(g, (Block)e);
			}
		}
		for (GameObject e:getObjectsAround(player, 10)) {
			if (e instanceof Entity) {
				if (!(e instanceof Player)) {
					entities.draw(g, (Entity)e);
				}
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
				removeBlock(b);
			}
		}

		if (player != null) {
			entities.draw(g, player);
		}
	}
}
