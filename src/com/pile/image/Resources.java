package com.pile.image;

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
	public static Font font1 = new Font("Times New Roman", Font.PLAIN, 80); //Todo Terraria Font? ;)
	//Todo make this into a data file, it'll allow easier modifying
	public static SingleImage dirt, grass, stone;
	public static SingleImage[] blocks;

	public static HashMap<String, BufferedImage> partsMale, partsFemale;
	public static void load() throws IOException {
		// TODO add all blocks into array sorted by id
//		blocks = new SingleImage[];
		dirt = new SingleImage(scale(ImageIO.read(new File("assets/images/PNG/Tiles/dirt.png")), SCALE));
		grass = new SingleImage(scale(ImageIO.read(new File("assets/images/PNG/Tiles/dirt_grass.png")), SCALE));
		stone = new SingleImage(scale(ImageIO.read(new File("assets/images/PNG/Tiles/stone.png")), SCALE));

		//Todo create a method
		partsMale = new HashMap<>();
		partsMale.put("head", ImageIO.read(new File("assets/images/PNG/Characters/Player male/male_head.png")));
		partsMale.put("arm", ImageIO.read(new File("assets/images/PNG/Characters/Player male/male_arm.png")));
		partsMale.put("leg", ImageIO.read(new File("assets/images/PNG/Characters/Player male/male_leg.png")));
		partsMale.put("body", ImageIO.read(new File("assets/images/PNG/Characters/Player male/male_body.png")));

		partsFemale = new HashMap<>();
		partsFemale.put("head", ImageIO.read(new File("assets/images/PNG/Characters/Player female/female_head.png")));
		partsFemale.put("arm", ImageIO.read(new File("assets/images/PNG/Characters/Player female/female_arm.png")));
		partsFemale.put("leg", ImageIO.read(new File("assets/images/PNG/Characters/Player female/female_leg.png")));
		partsFemale.put("body", ImageIO.read(new File("assets/images/PNG/Characters/Player female/female_body.png")));
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
