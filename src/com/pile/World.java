package com.pile;

import com.pile.block.Block;
import com.pile.block.BlockManager;
import com.pile.block.Dirt;
import com.pile.entity.Enemy;
import com.pile.entity.Entity;
import com.pile.entity.EntityManager;
import com.pile.entity.Player;
import com.pile.state.PlayState;

import java.awt.*;
import java.util.LinkedList;

public class World {
	public static final double GRAVITY = 0.6;
	public static final double FRICTION = 0.2;
	public static final int GRID_SIZE = 200;
	private int width = 1000;
	private int height = 1000;

	private LinkedList<Entity>[][] grid;

	private EntityManager entities;
	private BlockManager blocks;
	private GameCamera camera;

	public World() {
		camera = new GameCamera(this);
		entities = new EntityManager(camera);
		blocks = new BlockManager(camera);
		clearGrid();
		generateWorld();
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

	public int getGridX(Entity e) { return (int)(e.getX()/(double)GRID_SIZE); }
	public int getGridY(Entity e) { return (int)(e.getY()/(double)GRID_SIZE); }
	public LinkedList<Entity> getEntitiesAtGridSpot(Entity e) {
		return grid[getGridX(e)][getGridY(e)];
	}

	private void sortGrid() {
		for (Entity e:entities.getEntities()) {
			int posX = getGridX(e);
			int posY = getGridY(e);
			if (grid[posX][posY] == null) {
				grid[posX][posY] = new LinkedList<Entity>();
			}
			grid[posX][posY].add(e);
		}
	}

	public void generateWorld() {
		entities.add(new Player(width/2, 0));
		entities.add(new Enemy(height/2, 0));
		for (int i = 0; i < width; i+= Block.WIDTH) {
			blocks.add(new Dirt(i, height-Block.HEIGHT));
		}
	}

	public void update() {
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
