package com.pile.block;

import com.pile.Game;
import com.pile.GameCamera;
import com.pile.Input;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.awt.*;
import java.util.ArrayList;

// Helps draw the blocks
public class BlockManager {
	private GameCamera camera;
	public BlockManager(GameCamera camera) {
		this.camera = camera;
	}

	// Draws all the blocks in respect to camera
	public void draw(Graphics g, Block b) {
		int xPos = (int)(b.getX() - camera.getOffsetX());
		int yPos = (int)(b.getY() - camera.getOffsetY());
		if (xPos < Game.WIDTH && xPos + Block.WIDTH > 0) {
			if (yPos < Game.HEIGHT && yPos + Block.HEIGHT > 0) {
				g.drawImage(b.getImage(), xPos, yPos, null);
				double da = b.getDestroyAmount();
				if (da >= 1) {
					g.drawImage(Resources.blockStages[(int)da-1], xPos, yPos, null);
				}
			}
		}
	}
}
