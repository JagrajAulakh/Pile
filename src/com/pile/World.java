package com.pile;

import com.pile.block.*;
import com.pile.entity.*;
import com.pile.entity.player.Player;
import com.pile.image.Resources;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

// Takes care of storing world info: world size, entity locations, blocks, player...
public class World {
	public static final double GRAVITY = 0.4;
	public static final double FRICTION = 0.2;
	// Used for detecting collisions within x Grid Space of Entities
	public static final int GRID_SIZE = (int)(256 * Resources.SCALE*2);
	private int width = Block.WIDTH*500; // World Width
	private int height = Block.HEIGHT*100; // World Height

	// Counter that resets every second
	private int frame;

	// Grid of the World, used to determine where every GameObjects is
	private ArrayList<Entity>[][] entityGrid;
	private Block[][] blockGrid;

	// Particles when breaking blocks
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private ArrayList<Particle> particlesToRemove = new ArrayList<Particle>();

	public GameCamera camera;
	private EntityManager entities;
	private BlockManager blocks;
	private Player player;

	public World() {
		camera = new GameCamera(this);
		entities = new EntityManager(camera);
		blocks = new BlockManager(camera);
		entityGrid = new ArrayList[width][height];
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

	public void removeParticle(Particle p) { particlesToRemove.add(p); }

	public void addEntity(Entity e) {
		if (entityGrid[e.getGridX()][e.getGridY()] == null) {
			entityGrid[e.getGridX()][e.getGridY()] = new ArrayList<Entity>();
		}
		entityGrid[e.getGridX()][e.getGridY()].add(e);
	}

	public boolean addBlock(Block b) {
		int bx = (int)(b.getX() / Block.WIDTH);
		int by = (int)(b.getY() / Block.HEIGHT);
		if (0 <= bx && bx < blockGrid.length && 0 <= by && by < blockGrid[0].length) {
			if (blockGrid[bx][by] == null) {
				blockGrid[bx][by] = b;
				return true;
			}
		}
		return false;
	}

	// Gets ride of block completely
	public void removeBlockPermanent(Block b) { blockGrid[b.getGridX()][b.getGridY()] = null; }
	public void removeBlock(Block b) { removeBlock(b, 1); }
	// Destroys block with breaking animation
	public void removeBlock(Block b, double destroyAmount) {
		b.destroy(destroyAmount);
		if (frame % 3 == 0) particles.add(new Particle(Math.random()* Block.WIDTH + b.getX(), Math.random()*Block.HEIGHT + b.getY()));
		if (b.destroyed()) {
			blockGrid[b.getGridX()][b.getGridY()] = null;
			// Throws all items in chest on the ground
			if(b instanceof Chest) {
				Chest c = (Chest)b;
				c.emptyChest();
			}
			// Makes drop of broken block
			if (Resources.blockDrop[b.getId()] != -1) addEntity(new Drop(b.getX(), b.getY(), Resources.blockDrop[b.getId()], Math.random()*16-8, -5));
		}
	}
	public void removeEntity(Entity e) {
		if (entityGrid[e.getGridX()][e.getGridY()] == null) {
			int px = (int)((e.getX() - e.getVelX()) / GRID_SIZE);
			int py = (int)((e.getY() - e.getVelY()) / GRID_SIZE);
			try {
				entityGrid[px][py].remove(e);
			} catch (NullPointerException error) {}
		} else {
			entityGrid[e.getGridX()][e.getGridY()].remove(e);
		}
	}

	// Updates entity's position in the grid "range" spaces around the player
	public void sortEntities(int range) {
		int px = player.getGridX();
		int py = player.getGridY();
		for (int x = px - range; x <= px + range; x++) {
			for (int y = py - range; y <= py + range; y++) {
				if (0 <= x && x < entityGrid.length && 0 <= y && y < entityGrid[0].length) {
					ArrayList<Entity> l = entityGrid[x][y];
					if (l != null) {
						// List that stores entities that moved spots in grid
						ArrayList<Entity> toMove = new ArrayList<Entity>();
						for (Entity e:l) {
							if (e.getGridX() != x || e.getGridY() != y) {
								toMove.add(e);
							}
						}
						// Move entities from toMove
						for (Entity e:toMove) {
							entityGrid[x][y].remove(e);
							if (entityGrid[e.getGridX()][e.getGridY()] == null) {
								entityGrid[e.getGridX()][e.getGridY()] = new ArrayList<Entity>();
							}
							entityGrid[e.getGridX()][e.getGridY()].add(e);
						}
					}
				}
			}
		}
	}

	// Get a block at spot in the world
	public Block getBlockAtGridSpot(int bx, int by) { return blockGrid[bx][by]; }
	public Block getBlockAtSpot(double wx, double wy) {
		int x = (int)(wx / Block.WIDTH);
		int y = (int)(wy / Block.HEIGHT);
		return blockGrid[x][y];
	}

	// Gets blocks around object e ("rad * rad" area around);
	public LinkedList<Block> getBlocksAround(GameObject e, int rad) {
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

	// Same thing as getBlocksAround, but with entities
	public LinkedList<Entity> getEntitiesAround(GameObject e, int rad) {
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

	// generates a tree with random size at (wx, wy)
	private synchronized void makeTree(double wx, double wy) {
		int size = (int)((Math.random() * 6) + 4);
		// storing an int[] of (x,y) in an array
		int[][] trunks = new int[size][2];
		int index = 0;
		for (double i = wy; i > wy-size*Block.HEIGHT; i-=Block.HEIGHT) {
			// removes block to make room for trunk of tree
			if (getBlockAtSpot(wx, i) != null) removeBlockPermanent(getBlockAtSpot(wx, i));
			addBlock(new Block(wx, i, 8));
			// X coordinate
			trunks[index][0] = (int)(wx/Block.WIDTH);
			// Y coordinate
			trunks[index][1] = (int)(i/Block.HEIGHT);
			index++;
		}
		// Rad of leaves is based off of trunk size
		double rad = size*Block.WIDTH/2;
		double top = (int)(wy - size*Block.HEIGHT);
		for (double x = wx - rad; x <= wx + rad; x += Block.WIDTH) {
			for (double y = top - rad; y <= top + rad; y += Block.HEIGHT) {
				double dist = Math.hypot(x - wx + Block.WIDTH/2, y - top + Block.HEIGHT/2);
				if (dist <= rad) {
					addBlock(new Leaves(x, y, trunks));
				}
			}
		}
	}
	// Generates random terrain, trees and ores
	public synchronized void generateWorld() {
		// Stores which way the terrain is going (up/down)
		int dir = (int)(Math.random()*2) == 0 ? -1 : 1;
		// Y starts at half-way height
		int y = (int)(Math.random()*height - Block.HEIGHT*20) / Block.HEIGHT + 20;
		for (int x = 0; x < width; x += Block.WIDTH) {
			// Random chance of direction changing
			if ((int)(Math.random()*100) < 20) dir *= -1;
			// y changes by random num between 0 and 2
			y = Math.max(20, Math.min(y + (int)(Math.random()*3)*dir, height/Block.HEIGHT-20));

			// GRASS
			addBlock(new Block(x, height - y*Block.HEIGHT, 0));
			// TREE
			if (Math.random()*100 < 10) makeTree(x, height - y*Block.HEIGHT - Block.HEIGHT);
			// DIRT
			int dirtUnder = 5;
			for (int rd = 0; rd < dirtUnder; rd++) {
				addBlock(new Block(x, height - y*Block.HEIGHT + Block.HEIGHT * (rd+1), 1));
			}
			// STONE
			for (int i = 0; i <= y - dirtUnder; i++) {
				addBlock(new Block(x, height - i*Block.HEIGHT, 2));
			}
			if (x == width/2) {
				addPlayer(new Player(width/2, height - y*Block.HEIGHT - 200, this));
			}
		}
		addEntity(new Enemy(width/2 - 200, y*Block.HEIGHT - 200));
		addEntity(new Enemy(width/2 - 100, y*Block.HEIGHT - 200));
		addEntity(new Enemy(width/2, y*Block.HEIGHT - 200));
		addEntity(new Enemy(width/2 + 100, y*Block.HEIGHT - 200));
		addEntity(new Enemy(width/2 + 200, y*Block.HEIGHT - 200));

		// COAL
		int times = width/Block.WIDTH/5;
		for (int i = 0; i < times; i++) {
			Point randPoint = randomSpot(4);
			if (randPoint != null) {
				makeVein((int)randPoint.getX(), (int)randPoint.getY(), 4, (int)(Math.random()*2)+4);
			}
		}

		// IRON
		times = width/Block.WIDTH/6;
		for (int i = 0; i < times; i++) {
			Point randPoint = randomSpot(5);
			if (randPoint != null) {
				makeVein((int)randPoint.getX(), (int)randPoint.getY(), 5, (int)(Math.random()*2)+4);
			}
		}

		times = width/Block.WIDTH/6;
		for (int i = 0; i < times; i++) {
			Point randPoint = randomSpot(5);
			if (randPoint != null) {
				makeVein((int)randPoint.getX(), (int)randPoint.getY(), 5, (int)(Math.random()*2)+4);
			}
		}
	}

	// Finds a random spot to place a vein of ore
	public Point randomSpot(int id) {
		int attempts = 0;
		while (true) {
			int randX = (int)(Math.random() * width/Block.WIDTH);
			int randY = (int)(Math.random() * height/Block.HEIGHT);
			if (blockGrid[randX][randY] != null && blockGrid[randX][randY].getId() == 2) {
				boolean alreadyThere = false;
				for (int x = randX - 6; x <= randX + 6; x++) {
					for (int y = randY - 6; y <= randY + 6; y++) {
						if (0 <= x && x < blockGrid.length && 0 <= y && y < blockGrid[0].length) {
							// Check if there are NO ORES AROUND to intersect the vein
							if (blockGrid[x][y] != null && blockGrid[x][y].getId() < 4 && blockGrid[x][y].getId() > 7) alreadyThere = true;
						}
					}
				}
				if (!alreadyThere) return new Point(randX, randY);
			}
			attempts++;
			if (attempts >= 50) return null;
		}
	}

	// generates a vein if size "amount" of id "id"
	public void makeVein(int bx, int by, int id, int amount) {
		final int rad = 6;
		blockGrid[bx][by].changeBlockTo(id);
		amount--;
		int stoneCount = 0;
		for (int x = bx - rad; x <= bx + rad; x++) {
			for (int y = by - rad; y <= by + rad; y++) {
				if (0 <= x && x < blockGrid.length && 0 <= y && y < blockGrid[0].length) {
					if (blockGrid[x][y] != null && blockGrid[x][y].getId() == 2) {
						stoneCount++;
					}
				}
			}
		}
		amount = Math.min(stoneCount, amount);
		for (int i = 0; i < amount-1; i++) {
			boolean placed = false;
			while (!placed) {
				int randX = (int)(Math.random()*rad) + bx;
				int randY = (int)(Math.random()*rad) + by;
				if (0 <= randX && randX < blockGrid.length && 0 <= randY && randY < blockGrid[0].length) {
					if (blockGrid[randX][randY] != null && blockGrid[randX][randY].getId() == 2 && oreAround(randX, randY, id)) {
						blockGrid[randX][randY].changeBlockTo(id);
						placed = true;
					}
				}
			}
		}
	}

	// check if there is an ore of id "id" directly around (bx, by)
	private boolean oreAround(int bx, int by, int id) {
		for (int x = bx - 1; x <= bx + 1; x++) {
			for (int y = by - 1; y <= by + 1; y++) {
				if (0 <= x && x < blockGrid.length && 0 <= y && y < blockGrid[0].length) {
					if (blockGrid[x][y] != null && blockGrid[x][y].getId() == id) return true;
				}
			}
		}
		return false;
	}

	// World update
	public void update() {
		// Sorts entities 5 grid spaces around player
		sortEntities(5);
		// Updates all blocks/entities around player
		LinkedList<GameObject> l = new LinkedList<GameObject>();
		l.addAll(getBlocksAround(player, width/2 / Block.WIDTH));
		l.addAll(getEntitiesAround(player, 10));
		for (GameObject g:l) {
			g.update();
		}
		// Particle update
		for (Particle p:particles) { p.update(); }
		// Removes particles that are "dead"
		for (Particle p:particlesToRemove) { particles.remove(p); }
		camera.centerOn(player);
		frame = (frame + 1) % 600;
		// Update HUD
		HUD.update(player);
	}
	public void render(Graphics g) {
		// Blue sky "background"
		g.setColor(new Color(176, 233, 252));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		for (Block e:getBlocksAround(player, Game.WIDTH/2)) {
			blocks.draw(g, e);
		}
		// Draws entities around player
		for (Entity e:getEntitiesAround(player, 10)) {
			if (!(e instanceof Player)) {
				entities.draw(g, e);
			}
		}
		if (!player.inventoryState()) {
			// FOR DRAWING BLOCK SELECTION
			Block b = player.getSelectedBlock();
			if (b != null) {
				g.setColor(Color.GREEN);
				int xPos = (int)(b.getX() - camera.getOffsetX());
				int yPos = (int)(b.getY() - camera.getOffsetY());
				g.drawRect(xPos, yPos, Block.WIDTH, Block.HEIGHT);
			}
		}

		// Draws player
		if (player != null) {
			entities.draw(g, player);
		}
		// Draws particles
		for (Particle p:particles) {
			g.setColor(p.getColor());
			g.fillRect((int)(p.getX() - camera.getOffsetX()), (int)(p.getY() - camera.getOffsetY()), p.getSize(), p.getSize());
		}

		// Block grid
		if (player.ruler()){
			Graphics2D g2 = (Graphics2D)g;
			// Translucent
			g2.setColor(new Color(255,255,255,50));
			g2.setStroke(new BasicStroke(1));
			for(int x = 0; x <= Game.WIDTH; x += Block.WIDTH){
				g.drawLine((int) (x - camera.getOffsetX() % Block.WIDTH),0,(int) (x - camera.getOffsetX() % Block.WIDTH),Game.HEIGHT);
			}
			for(int y = 0; y <= Game.HEIGHT; y += Block.HEIGHT){
				g.drawLine(0,(int) (y - camera.getOffsetY() % Block.HEIGHT),Game.WIDTH,(int) (y - camera.getOffsetY() % Block.HEIGHT));
			}
		}
		// Draw HUD
		HUD.render(g, player);
	}
}
