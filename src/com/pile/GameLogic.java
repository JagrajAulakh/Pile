package com.pile;

import com.pile.state.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameLogic {
	GameStateManager gsm; // Used to manage different screens
	public GameLogic() {
		gsm = new GameStateManager();
		gsm.push(new MenuState());
		gsm.push(new PlayState());
	}

	public void refresh() {
		Input.update();
		if (gsm.currentState() instanceof MenuState) {
			if (Input.keyUp && Input.keys[KeyEvent.VK_ESCAPE]) {
				System.exit(0);
			}
		} else if (gsm.currentState() instanceof PlayState) {
			if (Input.keyUp && Input.keys[KeyEvent.VK_ESCAPE]) {
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
