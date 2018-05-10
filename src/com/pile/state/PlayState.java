package com.pile.state;

import com.pile.Game;
import com.pile.GameCamera;
import com.pile.World;
import com.pile.block.Block;
import com.pile.block.BlockManager;
import com.pile.block.Dirt;
import com.pile.entity.*;

import java.awt.*;

public class PlayState implements GameState {

	private EntityManager entities;
	private BlockManager blocks;
	private GameCamera camera;

	public PlayState() {
		camera = new GameCamera();
		entities = new EntityManager(camera);
		blocks = new BlockManager(camera);
		entities.add(new Player(World.WIDTH/2, 0));
		blocks.add(new Dirt(500, 500));
	}

	@Override
	public void update() {
		blocks.update();
		entities.update();
	}

	@Override
	public void render(Graphics g) {
		blocks.render(g);
		entities.render(g);
	}
}
