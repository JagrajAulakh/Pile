package com.pile;

import com.pile.block.*;
import com.pile.entity.*;
import com.pile.entity.player.Player;
import com.pile.image.Resources;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class World {
	public static final double GRAVITY = 0.4;
	public static final double FRICTION = 0.2;
	// Used for detecting collisions within x Grid Space of Entities
	public static final int GRID_SIZE = (int)(256 * Resources.SCALE*2);
	// Todo Change W & H
	private int width = Block.WIDTH*500; // World Width
	private int height = Block.HEIGHT*100; // World Height

	// Counter that resets every second
	private int frame;

	// Grid of the World, used to determine where every GameObjects is
	private ArrayList<Entity>[][] entityGrid;
	private Block[][] blockGrid;

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

	public void removeBlockPermanent(Block b) { blockGrid[b.getGridX()][b.getGridY()] = null; }
	public void removeBlock(Block b) { removeBlock(b, 1); }
	public void removeBlock(Block b, double destroyAmount) {
		b.destroy(destroyAmount);
		if (frame % 3 == 0) particles.add(new Particle(Math.random()* Block.WIDTH + b.getX(), Math.random()*Block.HEIGHT + b.getY()));
		if (b.destroyed()) {
			blockGrid[b.getGridX()][b.getGridY()] = null;
			if(b instanceof Chest) {
				Chest c = (Chest)b;
				c.emptyChest();
			}
			if (Resources.blockDrop[b.getId()] != -1) addEntity(new Drop(b.getX(), b.getY(), Resources.blockDrop[b.getId()], player, Math.random()*16-8, -5));
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

	public void sortEntities(int range) {
		int px = player.getGridX();
		int py = player.getGridY();
		for (int x = px - range; x <= px + range; x++) {
			for (int y = py - range; y <= py + range; y++) {
				if (0 <= x && x < entityGrid.length && 0 <= y && y < entityGrid[0].length) {
					ArrayList<Entity> l = entityGrid[x][y];
					if (l != null) {
						ArrayList<Entity> toMove = new ArrayList<Entity>();
						for (Entity e:l) {
							if (e.getGridX() != x || e.getGridY() != y) {
								toMove.add(e);
							}
						}
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

	public Block getBlockAtGridSpot(int bx, int by) { return blockGrid[bx][by]; }
	public Block getBlockAtSpot(double wx, double wy) {
		int x = (int)(wx / Block.WIDTH);
		int y = (int)(wy / Block.HEIGHT);
		return blockGrid[x][y];
	}

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

	private synchronized void makeTree(double wx, double wy) {
		int size = (int)((Math.random() * 6) + 4);
		int[][] trunks = new int[size][2];
		int index = 0;
		for (double i = wy; i > wy-size*Block.HEIGHT; i-=Block.HEIGHT) {
			if (getBlockAtSpot(wx, i) != null) removeBlockPermanent(getBlockAtSpot(wx, i));
			addBlock(new Block(wx, i, 8));
			trunks[index][0] = (int)(wx/Block.WIDTH);
			trunks[index][1] = (int)(i/Block.HEIGHT);
			index++;
		}
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
	public synchronized void generateWorld() {
		int dir = (int)(Math.random()*2) == 0 ? -1 : 1;
		int y = (int)(Math.random()*height - Block.HEIGHT*20) / Block.HEIGHT + 20;
		for (int x = 0; x < width; x += Block.WIDTH) {
			if ((int)(Math.random()*100) < 20) dir *= -1;
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
		addBlock(new Block(width / 2 - Block.WIDTH*10, Block.HEIGHT*10, 28));
		addEntity(new Enemy(width/2 - 200, y*Block.HEIGHT - 200));
		addEntity(new Enemy(width/2 - 100, y*Block.HEIGHT - 200));
		addEntity(new Enemy(width/2, y*Block.HEIGHT - 200));
		addEntity(new Enemy(width/2 + 100, y*Block.HEIGHT - 200));
		addEntity(new Enemy(width/2 + 200, y*Block.HEIGHT - 200));


		int times = width/Block.WIDTH/5;
		for (int i = 0; i < times; i++) {
			Point randPoint = randomSpot(4);
			if (randPoint != null) {
				makeVein((int)randPoint.getX(), (int)randPoint.getY(), 4, (int)(Math.random()*2)+4);
			}
		}

		times = width/Block.WIDTH/6;
		for (int i = 0; i < times; i++) {
			Point randPoint = randomSpot(5);
			if (randPoint != null) {
				makeVein((int)randPoint.getX(), (int)randPoint.getY(), 4, (int)(Math.random()*2)+4);
			}
		}
	}

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
							if (blockGrid[x][y] != null && blockGrid[x][y].getId() == id) alreadyThere = true;
						}
					}
				}
				if (!alreadyThere) return new Point(randX, randY);
			}
			attempts++;
			if (attempts >= 50) return null;
		}
	}

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
	public void update() {
		sortEntities(5);
		LinkedList<GameObject> l = new LinkedList<GameObject>();
		l.addAll(getBlocksAround(player, width/2 / Block.WIDTH));
		l.addAll(getEntitiesAround(player, 10));
		for (GameObject g:l) {
			g.update();
		}
		for (Particle p:particles) { p.update(); }
		for (Particle p:particlesToRemove) { particles.remove(p); }
		camera.centerOn(player);
		frame = (frame + 1) % 600;
		HUD.update(player);
	}
	public void render(Graphics g) {
		// Todo draw background
		g.setColor(new Color(176, 233, 252));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		for (Block e:getBlocksAround(player, Game.WIDTH/2)) {
			blocks.draw(g, e);
		}
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

		if (player != null) {
			entities.draw(g, player);
		}
		for (Particle p:particles) {
			g.setColor(p.getColor());
			g.fillRect((int)(p.getX() - camera.getOffsetX()), (int)(p.getY() - camera.getOffsetY()), p.getSize(), p.getSize());
		}
		if (player.ruler()){
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(new Color(255,255,255,50));
			g2.setStroke(new BasicStroke(1));
			for(int x = 0; x <= Game.WIDTH; x += Block.WIDTH){
				g.drawLine((int) (x - camera.getOffsetX() % Block.WIDTH),0,(int) (x - camera.getOffsetX() % Block.WIDTH),Game.HEIGHT);
			}
			for(int y = 0; y <= Game.HEIGHT; y += Block.HEIGHT){
				g.drawLine(0,(int) (y - camera.getOffsetY() % Block.HEIGHT),Game.WIDTH,(int) (y - camera.getOffsetY() % Block.HEIGHT));
			}
		}

		HUD.render(g, player);
	}
}
