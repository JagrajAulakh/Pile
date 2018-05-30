package com.pile;

import com.pile.image.Resources;
import com.pile.state.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GameLogic {
	public static GameStateManager gsm; // Used to manage different screens
	public GameLogic() {
		gsm = new GameStateManager();
		gsm.push(new MenuState());
	}

	public void refresh() {
		Input.update();
		if (Input.keyUpOnce(KeyEvent.VK_R)) {
			try {
				Resources.readFiles();
				System.out.println("RELOAD");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (gsm.currentState() instanceof MenuState) {
			if (Input.keyDownOnce(KeyEvent.VK_ESCAPE)) {
				System.exit(0);
			}
		} else if (gsm.currentState() instanceof PlayState) {
			if (Input.keyUpOnce(KeyEvent.VK_ESCAPE)) {
				gsm.push(new PauseState());
			}
		} else if (gsm.currentState() instanceof PauseState) {
			if (Input.keyDownOnce(KeyEvent.VK_ESCAPE)) {
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
