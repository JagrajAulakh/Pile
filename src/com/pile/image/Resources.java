package com.pile.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

public class Resources {
	public static double SCALE = 0.5;
	public static Font font1 = new Font("Times New Roman", Font.PLAIN, 80);
	public static SingleImage dirt;
	public static Animation playerMaleWalking, playerFemaleWalking;
	public static void load() throws Exception {

		playerMaleWalking = new Animation(2);
		playerFemaleWalking = new Animation(2);
		for (int i = 0; i <= 19; i++) {
			playerMaleWalking.addScene(ImageIO.read(new File(String.format("assets/images/char/male/%d.png", i))));
			playerFemaleWalking.addScene(ImageIO.read(new File(String.format("assets/images/char/female/%d.png", i))));
		}
		dirt = new SingleImage(scale(ImageIO.read(new File("assets/images/PNG/Tiles/dirt.png")), SCALE));
	}

	public static int[] getSize(String text, Font f) {
		Rectangle2D s = Resources.font1.deriveFont(Font.BOLD).getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true));
		return new int[] {(int)s.getWidth(), (int)s.getHeight()};
	}
	public static BufferedImage scale(BufferedImage original, int newWidth, int newHeight) {
		BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
		g.dispose();
		return resized;
	}
	public static BufferedImage scale(BufferedImage image, double factor) {
		return scale(image, (int)(image.getWidth()*factor), (int)(image.getHeight()*factor));
	}
	public static BufferedImage flip(BufferedImage original, boolean hz, boolean vt) {
		if (!hz && !vt) { return original; }
		int w = original.getWidth();
		int h = original.getHeight();
		BufferedImage flipped =new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = flipped.createGraphics();
		if (hz && vt) {
			g.drawImage(original, 0, 0, w, h, w, h, 0, 0, null);
		} else if (hz) {
			g.drawImage(original, 0, 0, w, h, w, 0, 0, h, null);
		} else if (vt) {
			g.drawImage(original, 0,0,w,h, 0,h,w,0, null);
		}
		g.dispose();
		return flipped;
	}
}
