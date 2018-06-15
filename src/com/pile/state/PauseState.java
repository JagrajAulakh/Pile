package com.pile.state;

import com.pile.Game;
import com.pile.image.Resources;
import com.pile.ui.Button;
import com.pile.ui.ButtonManager;

import java.awt.*;

// Pause screen
public class PauseState implements GameState {
	private ButtonManager buttons;

	public PauseState() {
		buttons = new ButtonManager();
		buttons.add(new Button("Exit", Game.WIDTH/2, Game.HEIGHT/2+50, 70f, Color.BLACK, Color.GREEN, Color.BLUE));
		buttons.add(new Button("Resume", Game.WIDTH/2, Game.HEIGHT/2-50, 70f, Color.BLACK, Color.GREEN, Color.BLUE));
		buttons.add(new Button("Paused", Game.WIDTH/2, 80, 96f, Color.BLACK));
	}
	@Override
	public void update() {
		buttons.update();
	}

	@Override
	public void render(Graphics g) {
//		g.setFont(Resources.getFont(96f));
//		g.setColor(Color.BLACK);
//		g.drawString("Paused", Game.WIDTH/2, 100);
		buttons.render(g);
	}
}
