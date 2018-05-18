package com.pile.state;

import com.pile.World;

import java.awt.*;

public class PlayState implements GameState {

	public static World world;

	public PlayState() {
		world = new World();
		loadWorld();
	}

	private void loadWorld() {
		Thread lw = new Thread(new Runnable() {
			@Override
			public void run() {
				world.generateWorld();
			}
		});
		lw.start();
		while (lw.isAlive()) {
			System.out.println("LOADING WORLD");
		}
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
