package com.pile.image;

import java.awt.image.BufferedImage;

public abstract class ImageType {
	public abstract void tick();
	public abstract BufferedImage getImage();
}
