package com.pile.state;

import java.awt.*;

public interface GameState {
	void render(Graphics g);
	void update();
}
