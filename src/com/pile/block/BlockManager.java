package com.pile.block;

import com.pile.Game;
import com.pile.GameCamera;

import java.awt.*;
import java.util.ArrayList;

public class BlockManager {
	private GameCamera camera;
	private ArrayList<Block> blocks;
	public BlockManager(GameCamera camera) {
		this.camera = camera;
		this.blocks = new ArrayList<Block>();
	}

	public void add(Block b) {
		blocks.add(b);
	}

	public ArrayList<Block> getBlocks() { return blocks; }

	public void update() {
		for (Block b:blocks) {
			b.update();
		}
	}

	public void render(Graphics g) {
		for (Block b:blocks) {
			int xPos = (int)(b.getX() - camera.getOffsetX());
			int yPos = (int)(b.getY() - camera.getOffsetY());
			if (xPos < Game.WIDTH && xPos + b.WIDTH > 0) {
				if (yPos < Game.HEIGHT && yPos + b.HEIGHT > 0) {
					g.drawImage(b.getImage(), xPos, yPos, null);
				}
			}
		}
	}
}
