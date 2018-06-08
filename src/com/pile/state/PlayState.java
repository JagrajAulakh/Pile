package com.pile.state;

import com.pile.Game;
import com.pile.HUD;
import com.pile.World;
import com.pile.image.Resources;

import java.awt.*;

public class PlayState implements GameState {

	public static World world;
	private boolean loading;
	private Thread lw;
	private int counter;

	public PlayState() {
		world = new World();
		loadWorld();
	}

	private void loadWorld() {
		lw = new Thread(new Runnable() {
			@Override
			public void run() {
				world.generateWorld();
			}
		});
		lw.start();
		loading = true;
		counter = 0;
	}

	@Override
	public void update() {
		loading = lw.isAlive();
		if (loading) counter += 10;
		else world.update();
	}

	@Override
	public void render(Graphics g) {
		if (loading) {
			Font f = Resources.getFont(50f);
			g.setFont(f);
			g.setColor(Color.GREEN);
			String loadingText = "LOADING WORLD";
			int x = Game.WIDTH/2 - Resources.getWidth(loadingText, f)/2;
			int y = Game.HEIGHT/2 + Resources.getHeight(loadingText, f)/2;
			g.drawString(loadingText, x, y);
			Graphics2D g2 = (Graphics2D)(g);
			g2.setStroke(new BasicStroke(10));
			g.drawArc(Game.WIDTH/2 - 50, Game.HEIGHT - 150, 100, 100, 90, -counter%360);
		} else {
			world.render(g);
		}
	}
}
