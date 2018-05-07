package com.pile.state;

import com.pile.entity.*;
import com.pile.entity.player.*;

import java.awt.*;

public class PlayState implements GameState {

	private EntityManager em;

	public PlayState() {
		em = new EntityManager();
		em.add(new Player(0, 0));
	}

	@Override
	public void update() {
		em.update();
	}

	@Override
	public void render(Graphics g) {
		em.render(g);
	}
}
