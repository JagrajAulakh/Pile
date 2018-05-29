package com.pile.block;

import com.pile.Game;
import com.pile.GameCamera;
import com.pile.Input;
import com.pile.state.PlayState;

import java.awt.*;
import java.util.ArrayList;

public class BlockManager {
	private GameCamera camera;
	private ArrayList<Block> blocks;
	public BlockManager(GameCamera camera) {
		this.camera = camera;
		this.blocks = new ArrayList<Block>();
	}

	public void add(Block b2) {
		blocks.add(b2);
	}
	public void remove(Block b) { blocks.remove(b); }

	public ArrayList<Block> getBlocks() { return blocks; }

	public void update() {
		for (Block b:blocks) {
//			b.updateHitBox();
			b.update();
		}
	}

	public void draw(Graphics g, Block b) {
		int xPos = (int)(b.getX() - camera.getOffsetX());
		int yPos = (int)(b.getY() - camera.getOffsetY());
		if (xPos < Game.WIDTH && xPos + b.WIDTH > 0) {
			if (yPos < Game.HEIGHT && yPos + b.HEIGHT > 0) {
				g.drawImage(b.getImage(), xPos, yPos, null);
				g.setColor(new Color(0,0,0, (int) (51 * b.getDestroyAmount())));
				g.fillRect(xPos, yPos, Block.WIDTH, Block.HEIGHT);
			}
		}
	}
}
