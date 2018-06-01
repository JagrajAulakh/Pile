package com.pile.state;

import com.pile.Game;
import com.pile.Input;
import com.pile.ui.Button;
import com.pile.ui.ButtonManager;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState implements GameState {
	private ButtonManager buttons;

	public MenuState() {
		this.buttons = new ButtonManager();
		Button playButton = new Button("Play", Game.WIDTH/2, Game.HEIGHT/2, 70f, new Color(0, 0, 0), new Color(0,255,0), new Color(0,0,255));
		Button controlButton = new Button("Controls", Game.WIDTH/2, Game.HEIGHT/2 + 200, 70f, new Color(0,0,0), new Color(0,0,0), new Color(0,0,0));
		buttons.add(playButton);
		buttons.add(controlButton);
	}

	@Override
	public void update() {
		buttons.update();
	}

	@Override
	public void render(Graphics g) {
		buttons.render(g);
	}
}
