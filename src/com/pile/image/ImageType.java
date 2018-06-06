package com.pile.image;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class ImageType implements Serializable {
	public abstract void tick();
	public abstract BufferedImage getImage();
	public abstract void reset();
}
