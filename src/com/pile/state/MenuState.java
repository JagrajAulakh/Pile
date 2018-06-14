package com.pile.state;

import com.pile.Game;
import com.pile.ui.Button;
import com.pile.ui.ButtonManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuState implements GameState {
	private ButtonManager buttons;
	BufferedImage background, piles;

	public MenuState() {
		this.buttons = new ButtonManager();
		Button playButton = new Button("Play", Game.WIDTH/2, Game.HEIGHT/2, 70f, new Color(0, 0, 0), new Color(0,255,0), new Color(0,0,255));
		Button controlButton = new Button("Controls", Game.WIDTH/2, Game.HEIGHT/2 + 200, 70f, new Color(0,0,0), new Color(0,0,0), new Color(0,0,0));
		buttons.add(playButton);
		buttons.add(controlButton);
		try{
			background = ImageIO.read(new File("assets/images/REAL/MenuBG.png"));
			piles = ImageIO.read(new File("assets/images/REAL/PilesFontLogo.png"));
		}catch (IOException e) {}
	}

	@Override
	public void update() {
		buttons.update();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(background,Game.WIDTH/2 - background.getWidth()/2, Game.HEIGHT/2 - background.getHeight()/2,null);
		g.drawImage(piles,Game.WIDTH/2 - piles.getWidth()/2,25,null);
		buttons.render(g);
	}
}
