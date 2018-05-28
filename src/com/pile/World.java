package com.pile;

import com.pile.block.*;
import com.pile.entity.Drop;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.entity.EntityManager;
import com.pile.entity.player.Player;
import com.pile.image.Resources;

import java.awt.*;
import java.util.LinkedList;

public class World {
	public static final double GRAVITY = 0.4;
	public static final double FRICTION = 0.2;
	// Used for detecting collisions within 1 Grid Space of Entities
	public static final int GRID_SIZE = (int)(200 * Resources.SCALE*2);
	// Todo Change W & H
	private int width = 2048*10; // World Width
	private int height = 1024*2; // World Height

	// Counter that resets every second
	private int frame;

	// Grid of the World, used to determine where every GameObjects are
	private LinkedList<Entity>[][] entityGrid;
	private Block[][] blockGrid;

	public GameCamera camera;
	private EntityManager entities;
	private BlockManager blocks;
	private Player player;

	public World() {
		camera = new GameCamera(this);
		entities = new EntityManager(camera);
		blocks = new BlockManager(camera);
		entityGrid = new LinkedList[width][height];
		blockGrid = new Block[width/Block.WIDTH + 1][height/Block.HEIGHT + 1];
	}
	private void addPlayer(Player p) {
		addEntity(p);
		this.player = p;
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public void addEntity(Entity e) {
		if (entityGrid[e.getGridX()][e.getGridY()] == null) {
			entityGrid[e.getGridX()][e.getGridY()] = new LinkedList<Entity>();
		}
		entityGrid[e.getGridX()][e.getGridY()].add(e);
	}

	public void addBlock(Block b) {
		int bx = (int)(b.getX() / Block.WIDTH);
		int by = (int)(b.getY() / Block.HEIGHT);
		if (blockGrid[bx][by] == null) {
			blockGrid[bx][by] = b;
		} else {
			blockGrid[bx][by].reset();
		}
	}

	public void removeBlock(Block b) {
		b.destroy();
		if (b.destroyed()) {
			blockGrid[b.getGridX()][b.getGridY()] = null;
			addEntity(new Drop(b.getX(), b.getY(), b.getId(), Math.random()*16-8, -5));
		}
	}
	public void removeEntity(Entity e) {
		entityGrid[e.getGridX()][e.getGridY()].remove(e);
	}

	public void sortEntities(int range) {
		int px = player.getGridX();
		int py = player.getGridY();
		for (int x = px - range; x <= px + range; x++) {
			for (int y = py - range; y <= py + range; y++) {
				if (0 <= x && x < entityGrid.length && 0 <= y && y < entityGrid[0].length) {
					LinkedList<Entity> l = entityGrid[x][y];
					if (l != null) {
						LinkedList<Entity> toMove = new LinkedList<Entity>();
						for (Entity e:l) {
							if (e.getGridX() != x || e.getGridY() != y) {
								toMove.add(e);
							}
						}
						for (Entity e:toMove) {
							entityGrid[x][y].remove(e);
							if (entityGrid[e.getGridX()][e.getGridY()] == null) {
								entityGrid[e.getGridX()][e.getGridY()] = new LinkedList<Entity>();
							}
							entityGrid[e.getGridX()][e.getGridY()].add(e);
						}
					}
				}
			}
		}
	}

	public Block getBlockAtSpot(double wx, double wy) {
//		Block b = ;
		return blockGrid[(int)(wx / Block.WIDTH)][(int)(wy / Block.HEIGHT)];
	}

	public LinkedList<Block> getBlocksAround(Entity e, int rad) {
		LinkedList<Block> l = new LinkedList<Block>();
		int px = (int)(e.getX() / Block.WIDTH);
		int py = (int) (e.getY() / Block.HEIGHT);
		for (int x = px - rad; x <= px + rad; x++) {
			for (int y = py - rad; y <= py + rad; y++) {
				if (0 <= x && x < blockGrid.length && 0 <= y && y < blockGrid[0].length) {
					if (blockGrid[x][y] != null) {
						l.add(blockGrid[x][y]);
					}
				}
			}
		}
		return l;
	}

	public LinkedList<Entity> getEntitiesAround(Entity e, int rad) {
		LinkedList<Entity> l = new LinkedList<Entity>();
		int px = e.getGridX();
		int py = e.getGridY();
		for (int x = px - rad; x <= px + rad; x++) {
			for (int y = py - rad; y <= py + rad; y++) {
				if (0 <= x && x < entityGrid.length && 0 <= y && y < entityGrid[0].length) {
					if (entityGrid[x][y] != null) {
						l.addAll(entityGrid[x][y]);
					}
				}
			}
		}
		return l;
	}
	public void generateWorld() {
		addPlayer(new Player(width/2, 0, this));

		int dir = (int)(Math.random()*2) == 0?-1:1;
		int y = height/Block.HEIGHT/2;
		for (int x = 0; x < width; x += Block.WIDTH) {
			if ((int)(Math.random()*100) < 20) dir *= -1;
			y = Math.max(3, Math.min(y + (int)(Math.random()*3)*dir, height/Block.HEIGHT-15));
			addBlock(new Block(x, height - y*Block.HEIGHT, Block.GRASS));
			addBlock(new Block(x, height - y*Block.HEIGHT + Block.HEIGHT, Block.DIRT));
			addBlock(new Block(x, height - y*Block.HEIGHT + Block.HEIGHT*2, Block.DIRT));
			for (int i = 0; i <= y-3; i++) {
				addBlock(new Block(x, height - i*Block.HEIGHT, Block.STONE));
			}
		}

		final int rad = 100;
		for (int i = 0; i < (int)(Math.random()*10); i++) {
			int randX = (int)(Math.random()*width);
			int randY = (int)(Math.random()*height);
			for (int bx = randX - rad; bx < randX + rad; bx += Block.WIDTH) {
				for (int by = randY - rad; by < randY + rad; by += Block.HEIGHT) {

				}
			}
		}
	}

	public void update() {
		// TODO FIX THIS!
		LinkedList<GameObject> l = new LinkedList<GameObject>();
		l.addAll(getBlocksAround(player, 10));
		l.addAll(getEntitiesAround(player, 10));
		for (GameObject g:l) {
			g.update();
		}
		camera.centerOn(player);
//		if (Input.mousePressed(2)) {
//			int wx = (int)((Input.mx + camera.getOffsetX())/Block.WIDTH) * Block.WIDTH;
//			int wy = (int)((Input.my + camera.getOffsetY())/Block.HEIGHT) * Block.HEIGHT;
//			Block b = new Block(wx, wy, 0);
//			if (!b.collides(player)) {
//				addBlock(b);
//			}
//		}
		if (frame % 10 == 0) {
			sortEntities(5);
		}
		frame = (frame + 1) % 600;
		HUD.update();
	}
	public void render(Graphics g) {
		for (Block e:getBlocksAround(player, Game.WIDTH/2)) {
			blocks.draw(g, e);
		}
		for (Entity e:getEntitiesAround(player, 10)) {
			if (!(e instanceof Player)) {
				entities.draw(g, e);
			}
		}
		// FOR DRAWING BLOCK SELECTION
		Block b = getBlockAtSpot(Input.mx + camera.getOffsetX(), Input.my + camera.getOffsetY());
//		System.out.println(b);
		if (b != null) {
			g.setColor(Color.GREEN);
			//Todo Fix block selection
			int xPos = (int)(b.getX() - camera.getOffsetX());
			int yPos = (int)(b.getY() - camera.getOffsetY());
			g.drawRect(xPos, yPos, Block.WIDTH, Block.HEIGHT);
		}

		if (player != null) {
			entities.draw(g, player);
		}
		HUD.render(g, player);
	}
}
