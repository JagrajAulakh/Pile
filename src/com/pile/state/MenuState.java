package com.pile.state;

import com.pile.Input;
import com.pile.ui.Button;
import com.pile.ui.ButtonManager;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState implements GameState {
	private ButtonManager buttons;

	public MenuState() {
		this.buttons = new ButtonManager();
		buttons.add(new Button("Test", 200, 200, new Color(0, 0, 0), new Color(0,255,0), new Color(0,0,255)));
	}

	@Override
	public void update() {
//		if (Input.keys[KeyEvent.VK_ESCAPE]) {
//			System.exit(0);
//		}
		buttons.update();
	}

	@Override
	public void render(Graphics g) {
		buttons.render(g);
	}
}
