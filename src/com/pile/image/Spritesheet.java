package com.pile.image;

import java.awt.image.BufferedImage;

public class Spritesheet {
	private BufferedImage sheet;
	private int margin, spacing, tileSizeX, tileSizeY;

	public Spritesheet(BufferedImage sheet) {
		this.sheet = sheet;
	}
	public Spritesheet(BufferedImage sheet, int margin, int spacing, int tileSizeX, int tileSizeY) {
		this.sheet = sheet;
		this.margin = margin;
		this.spacing = spacing;
		this.tileSizeX = tileSizeX;
		this.tileSizeY = tileSizeY;
	}

	public BufferedImage getImage(int[] co) {
		return getImage(co[0], co[1], co[2], co[3]);
	}
	public BufferedImage getImage(int x, int y, int width, int height) {
		return sheet.getSubimage(x, y, width, height);
	}

	public BufferedImage getTile(int x, int y) {
		return getImage(margin+x*spacing+x*tileSizeX, margin+y*spacing+y*tileSizeY, tileSizeX, tileSizeY);
	}
}
