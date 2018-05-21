package com.pile;

import com.pile.state.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameLogic {
	public static GameStateManager gsm; // Used to manage different screens
	public GameLogic() {
		gsm = new GameStateManager();
		gsm.push(new MenuState());
	}

	public void refresh() {
		Game.input.update();
		if (gsm.currentState() instanceof MenuState) {
			if (Game.input.keyDownOnce(KeyEvent.VK_ESCAPE)) {
				System.exit(0);
			}
		} else if (gsm.currentState() instanceof PlayState) {
			if (Game.input.keyDownOnce(KeyEvent.VK_ESCAPE)) {
				gsm.push(new PauseState());
			}
		} else if (gsm.currentState() instanceof PauseState) {
			if (Game.input.keyDownOnce(KeyEvent.VK_ESCAPE)) {
				System.exit(0);
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
