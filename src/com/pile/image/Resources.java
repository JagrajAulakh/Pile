package com.pile.image;

import com.pile.io.Reader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

public class Resources {
	public static double SCALE = 0.25;
	public static Font mainFont;
	public static SingleImage[] blocks;
	public static HashMap<String, BufferedImage> partsMale, partsFemale, partsZombie;

	public static void load() throws IOException,FontFormatException {
		mainFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Andy Bold.ttf"));

		// All blocks, sorted by ID numbers
		blocks = new SingleImage[50]; // Random number
		String[] bFile = Reader.readFile("assets/data/blocks.txt").split("\n");
		for (int i = 0; i < bFile.length; i++) {
			String path = "assets/images/PNG/Tiles/" + bFile[i] + ".png";
			blocks[i] = new SingleImage(scale(ImageIO.read(new File(path)), SCALE));
		}

		partsMale = getParts("male");
		partsFemale = getParts("female");
		partsZombie = getParts("zombie");

	}
	private static HashMap<String, BufferedImage> getParts(String ch) throws IOException {
		ch = ch.toLowerCase();
		HashMap<String, BufferedImage> map = new HashMap<String, BufferedImage>();
		String[] p = new String[] {"head", "arm", "leg", "body"};
		for (String part:p) {
			String folder = ch.equals("male")||ch.equals("female")?"Player "+ch:ch.substring(0, 1).toUpperCase()+ch.substring(1);
			String path = String.format("assets/images/PNG/Characters/%s/%s_%s.png", folder, ch, part);
			map.put(part, ImageIO.read(new File(path)));
		}
		return map;
	}
	public static Font getFont(float size) { return mainFont.deriveFont(size); }
	public static int getWidth(String text, Font f) { return (int)f.getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getWidth(); }
	public static int getHeight(String text, Font f) { return (int)f.getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getHeight(); }
//	public static int[] getSize(String text, Font f) {
//		Rectangle2D s = f.getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true));
//		return new int[] {(int)s.getWidth(), (int)s.getHeight()};
//	}
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
