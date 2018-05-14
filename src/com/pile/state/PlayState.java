package com.pile.state;

import com.pile.World;

import java.awt.*;

public class PlayState implements GameState {

	public static World world;

	public PlayState() {
		world = new World();

	}

	@Override
	public void update() {
		world.update();
	}

	@Override
	public void render(Graphics g) {
		world.render(g);
	}
}
