package com.pile.state;

import com.pile.Game;
import com.pile.image.Resources;
import com.pile.ui.Button;
import com.pile.ui.ButtonManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Pause screen
public class PauseState implements GameState {
	private ButtonManager buttons;
	BufferedImage background, pause0, pause1, exit0, exit1, resume0, resume1;

	public PauseState() {
		buttons = new ButtonManager();
		try {
			background = ImageIO.read(new File("src/assets/images/REAL/MenuBG.png"));
			pause0 = ImageIO.read(new File("src/assets/images/REAL/pause0.png"));
			pause1 = ImageIO.read(new File("src/assets/images/REAL/pause1.png"));
			exit0 = Resources.scale(ImageIO.read(new File("src/assets/images/REAL/exit0.png")), 0.7);
			exit1 = Resources.scale(ImageIO.read(new File("src/assets/images/REAL/exit1.png")), 0.7);
			resume0 = Resources.scale(ImageIO.read(new File("src/assets/images/REAL/resume0.png")), 0.7);
			resume1 = Resources.scale(ImageIO.read(new File("src/assets/images/REAL/resume1.png")), 0.7);
		} catch (IOException e) {}

		buttons.add(new Button("Pause", pause0,Game.WIDTH/2, Game.HEIGHT/2 - 250, pause0));
		buttons.add(new Button("Resume", resume0,Game.WIDTH/2, Game.HEIGHT/2, resume1));
		buttons.add(new Button("Exit", exit0,Game.WIDTH/2, Game.HEIGHT/2 + 250, exit1));
	}
	@Override
	public void update() {
		buttons.update();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(background,Game.WIDTH/2 - background.getWidth()/2, Game.HEIGHT/2 - background.getHeight()/2,null);
		buttons.render(g);
	}
}
