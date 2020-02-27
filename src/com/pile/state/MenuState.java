package com.pile.state;

import com.pile.Game;
import com.pile.ui.Button;
import com.pile.ui.ButtonManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Menu
public class MenuState implements GameState {
	private ButtonManager buttons;
	private BufferedImage background, piles, play0, play1;

	public MenuState() {
		this.buttons = new ButtonManager();
		try {
			background = ImageIO.read(new File("src/assets/images/REAL/MenuBG.png"));
			piles = ImageIO.read(new File("src/assets/images/REAL/PilesFontLogo.png"));
			play0 = ImageIO.read(new File("src/assets/images/REAL/play0.png"));
			play1 = ImageIO.read(new File("src/assets/images/REAL/play1.png"));
		} catch (IOException e) {}
		buttons.add(new Button("Play", play0,Game.WIDTH/2, Game.HEIGHT/2, play1));
	}

	@Override
	public void update() {
		buttons.update();
	}

	@Override
	public void render(Graphics g) {
		//Drawling the background
		g.drawImage(background,Game.WIDTH/2 - background.getWidth()/2, Game.HEIGHT/2 - background.getHeight()/2,null);
		//Drawling "Piles" Logo
		g.drawImage(piles,Game.WIDTH/2 - piles.getWidth()/2,25,null);
		buttons.render(g);
	}
}
