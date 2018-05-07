package com.pile;

import com.pile.state.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
	GameStateManager gsm;
	public GamePanel() {
		gsm = new GameStateManager();
		gsm.push(new PlayState());
//		gsm.push(new MenuState());
	}

	public void refresh() {
		gsm.currentState().update();
	}
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		gsm.currentState().render(g);
	}
}
