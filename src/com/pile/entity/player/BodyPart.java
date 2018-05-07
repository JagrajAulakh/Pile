package com.pile.entity.player;

import java.awt.image.BufferedImage;

public abstract class BodyPart {
	protected BufferedImage image;
	public BodyPart(BufferedImage image) {
		this.image = image;
	}
}
