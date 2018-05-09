package com.pile.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation extends ImageType {
	private ArrayList<BufferedImage> scenes;
	private int counter, counterMax, frame;
	public Animation(int speed) {
		scenes = new ArrayList<BufferedImage>();
		counter = frame = 0;
		counterMax = speed;
	}

	public void addScene(BufferedImage img) { scenes.add(img); }
	public BufferedImage get(int index) { return scenes.get(index); }

	@Override
	public void tick() {
		counter++;
		if (counter > counterMax) {
			System.out.println(counter);
			counter = 0;
			frame++;
			if (frame >= scenes.size()) {
				frame = 0;
			}
		}
	}

	@Override
	public BufferedImage getImage() {
		return scenes.get(frame);
	}
}
