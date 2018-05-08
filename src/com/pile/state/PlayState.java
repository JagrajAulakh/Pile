package com.pile.state;

import com.pile.GameCamera;
import com.pile.block.BlockManager;
import com.pile.entity.*;

import java.awt.*;

public class PlayState implements GameState {

	private EntityManager entities;
	private BlockManager blocks;
	private GameCamera camera;

	public PlayState() {
		camera = new GameCamera();
		entities = new EntityManager(camera);
		entities.add(new Player(0, 0));
	}

	@Override
	public void update() {
		entities.update();
	}

	@Override
	public void render(Graphics g) {
		entities.render(g);
	}
}
