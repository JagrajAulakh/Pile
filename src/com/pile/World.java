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
	private int height = 1024; // World Height

	// Counter that resets every second
	private int frame;

	// Grid of the World, used to determine where every GameObjects are
	private LinkedList<Entity>[][] entityGrid;
	private Block[][] blockGrid;

	private EntityManager entities;
	private BlockManager blocks;
	private GameCamera camera;
	private Player player;

	public World() {
		camera = new GameCamera(this);
//		entities = new EntityManager(camera);
//		blocks = new BlockManager(camera);
		entityGrid = new LinkedList[width][height];
		blockGrid = new Block[width/Block.WIDTH][height/Block.HEIGHT];
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

	public void addEntity(Entity e) {
		if (entityGrid[e.getGridX()][e.getGridY()] == null) {
			entityGrid[e.getGridX()][e.getGridY()] = new LinkedList<Entity>();
		}
		entityGrid[e.getGridX()][e.getGridY()].add(e);
	}

	public void addBlock(Block b) {
		if (blockGrid[b.getGridX()][b.getGridY()] == null) {
			blockGrid[b.getGridX()][b.getGridY()] = b;
		}
	}

	public void removeBlock(Block b) {
		b.destroy();
		if (b.destroyed()) {
			blockGrid[b.getGridX()][b.getGridY()] = null;
		}
	}

	public void sortEntities(int range) {
		int px = player.getGridX();
		int py = player.getGridY();
		for (int x = px - range; x <= px + range; x++) {
			for (int y = py - range; y <= py + range; y++) {
				if (0 <= x && x < entityGrid.length && 0 <= y && y < entityGrid[0].length) {
					LinkedList<Entity> l = entityGrid[x][y];
					for (Entity e:l) {
						if (e.getGridX() != x || e.getGridY() != y) {
							entityGrid[x][y].remove(e);
							entityGrid[e.getGridX()][e.getGridY()].add(e);
						}
					}
				}
			}
		}
	}
	public void generateWorld() {
		addPlayer(new Player(width/2, 0, this));
		int tmp = 5;
		for (int i = -tmp; i <= tmp; i++) {
			entities.add(new Enemy(width/2 + i*150, 0));
			entities.add(new Drop(width/2 + i*150, 0, Block.DIRT));
		}

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
			int randY = (int)(Math.random()*width);
			for (int bx = randX - rad; bx < randX + rad; bx += Block.WIDTH) {
				for (int by = randY - rad; by < randY + rad; by += Block.HEIGHT) {

				}
			}
		}
	}

	public void update() {
		// TODO FIX THIS!
		for (GameObject e:getThingsAround(player, blockGrid, 10)) {
			e.update();
		}
		for (GameObject e:getThingsAround(player, grid, 10)) {
			e.update();
		}
		camera.centerOn(player);
		if (Input.mousePressed(2)) {
			int wx = (int)((Input.mx + camera.getOffsetX())/Block.WIDTH) * Block.WIDTH;
			int wy = (int)((Input.my + camera.getOffsetY())/Block.HEIGHT) * Block.HEIGHT;
			addBlock(new Block(wx, wy, 0));
		}
		if (frame % 10 == 0) {
			sortEntities(5);
		}
//		inventory.update();
		frame = (frame + 1) % 60;
	}
	public void render(Graphics g) {
		for (GameObject e:getBlocksAround(player, 10)) {
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
		Block b = getBlockAtSpot(Input.mx + camera.getOffsetX(), Input.my + camera.getOffsetY());
//		System.out.println(b);
		if (b != null) {
			g.setColor(Color.GREEN);
			//Todo Fix block selection
			int xPos = (int)(b.getX() - camera.getOffsetX());
			int yPos = (int)(b.getY() - camera.getOffsetY());
			g.drawRect(xPos, yPos, Block.WIDTH, Block.HEIGHT);
			if (Input.mousePressed(0)) {
				removeBlock(b);
			}
		}

		if (player != null) {
			entities.draw(g, player);
		}
//		inventory.render(g);
	}
}
