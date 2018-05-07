package com.pile.image;

import java.awt.image.BufferedImage;

public class SingleImage extends ImageType {
	private BufferedImage image;
	public SingleImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public void tick() {}

	@Override
	public BufferedImage getImage() {
		return image;
	}
}
