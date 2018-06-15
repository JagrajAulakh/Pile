package com.pile.entity.player;

import java.awt.image.BufferedImage;

// Parent class to all body parts
public abstract class BodyPart {
	private BufferedImage image;
	public BodyPart(BufferedImage image) {
		this.image = image;
	}
	public BufferedImage getImage() { return image; }
}
