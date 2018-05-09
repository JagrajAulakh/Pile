package com.pile;

import com.pile.state.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
	GameStateManager gsm;
	public GamePanel() {
		gsm = new GameStateManager();
		gsm.push(new MenuState());
		gsm.push(new PlayState());
	}

	public void refresh() {
		if (gsm.currentState() instanceof MenuState) {
			if (Input.keys[KeyEvent.VK_ENTER]) {
				gsm.push(new PlayState());
			}
		} else if (gsm.currentState() instanceof PlayState) {
			if (Input.keys[KeyEvent.VK_ESCAPE]) {
				gsm.pop();
			}
		}
		gsm.currentState().update();
	}
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		gsm.currentState().render(g);
	}
}
