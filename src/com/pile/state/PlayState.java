package com.pile.state;

import com.pile.Game;
import com.pile.World;
import com.pile.image.Resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class PlayState implements GameState {

	public static World world;
	private boolean loading;
	private Thread lw;
	private int counter;
	private BufferedImage background, loadingImg, worldImg;


	public PlayState() {
		try {
			background = ImageIO.read(new File("assets/images/REAL/MenuBG.png"));
			loadingImg = ImageIO.read(new File("assets/images/REAL/loading.png"));
			worldImg = ImageIO.read(new File("assets/images/REAL/world.png"));
		} catch (IOException e) {}
		loadWorld();
	}

	// Loads world in new thread
	private void loadWorld() {
		lw = new Thread(new Runnable() {
			@Override
			public void run() {
				world = new World();
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
		// loading screen
		if (loading) {
			g.drawImage(background,Game.WIDTH/2 - background.getWidth()/2, Game.HEIGHT/2 - background.getHeight()/2,null);
			g.drawImage(loadingImg,Game.WIDTH/2 - loadingImg.getWidth()/2, Game.HEIGHT/2 - loadingImg.getHeight()/2,null);
			Graphics2D g2 = (Graphics2D)(g);
			g2.setStroke(new BasicStroke(10));
			g.drawArc(Game.WIDTH/2 - 50, Game.HEIGHT - 150, 100, 100, 90, -counter%360);
		} else { // Does world drawing
			world.render(g);
		}
	}
}
