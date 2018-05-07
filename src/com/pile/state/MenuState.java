package com.pile.state;

import com.pile.ui.Button;
import com.pile.ui.ButtonManager;

import java.awt.*;

public class MenuState implements GameState {
	private ButtonManager bm;

	public MenuState() {
		this.bm = new ButtonManager();
		bm.add(new Button("test", 100, 100, 100, 50, new Color(0, 0, 0), new Color(0,255,0), new Color(0,0,255)));
	}

	@Override
	public void update() {
		bm.update();
	}

	@Override
	public void render(Graphics g) {
		bm.render(g);
	}
}
