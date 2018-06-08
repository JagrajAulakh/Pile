package com.pile.image;

import com.pile.block.Block;
import com.pile.crafting.Recipe;
import com.pile.io.Reader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Resources {
	public static double SCALE = 0.25;
	private static Font mainFont;
	public static HashMap<String, BufferedImage> partsMale, partsFemale, partsZombie;
	public static BufferedImage[] blockStages;
	public static BufferedImage heart0, heart1, heart2;
	public static SingleImage[] itemImages;
	public static int[] blockSpeeds, blockStack, blockDrop, toolSpeeds;
	public static LinkedList[] toolBlocks;
	public static boolean[] blockPlaceable;
	public static Recipe[] recipes;

	public static void load() throws IOException,FontFormatException {
		mainFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Andy Bold.ttf"));

		readFiles();
		blockStages = new BufferedImage[Block.STAGES];
		for (int i = 0; i < Block.STAGES; i++) {
			String path = "assets/images/destroy/" + i + ".png";
			BufferedImage img = ImageIO.read(new File(path));
			blockStages[i] = scale(img, Block.WIDTH, Block.HEIGHT);
		}
		partsMale = getParts("male");
		partsFemale = getParts("female");
		partsZombie = getParts("zombie");

		heart0 = ImageIO.read(new File("assets/images/REAL/heart0.png"));
		heart1 = ImageIO.read(new File("assets/images/REAL/heart1.png"));
		heart2 = ImageIO.read(new File("assets/images/REAL/heart2.png"));

	}

	private static void readFiles() throws IOException {
		String[] bFile = Reader.readDataFile("assets/data/blocks.txt").split("\n");
		final int TOTAL_BLOCKS = 50;
		// All blocks, sorted by ID numbers
		itemImages = new SingleImage[TOTAL_BLOCKS];
		blockSpeeds = new int[TOTAL_BLOCKS];
		blockStack = new int[TOTAL_BLOCKS];
		blockDrop = new int[TOTAL_BLOCKS];
		blockPlaceable = new boolean[TOTAL_BLOCKS];

		String[] tFile = Reader.readDataFile("assets/data/tools.txt").split("\n");
		toolSpeeds = new int[TOTAL_BLOCKS];
		toolBlocks = new LinkedList[TOTAL_BLOCKS];

		String[] rFile = Reader.readDataFile("assets/data/crafting.txt").split("\n");
		recipes = new Recipe[TOTAL_BLOCKS];

		for (String line:bFile) {
			String[] parts = line.split(" ");
			int id = Integer.parseInt(parts[0]);
			String path = "assets/images/REAL/" + parts[1] + ".png";
			itemImages[id] = new SingleImage(scale(ImageIO.read(new File(path)), SCALE));
			blockDrop[id] = Integer.parseInt(parts[2]);
			blockSpeeds[id] = Integer.parseInt(parts[3]);
			blockStack[id] = Integer.parseInt(parts[4]);
		}


		for (String line:tFile) {
			String[] parts = line.split(" ");
			int id = Integer.parseInt(parts[0]);
			String path = "assets/images/REAL/" + parts[1] + ".png";
			itemImages[id] = new SingleImage(scale(ImageIO.read(new File(path)), 0.4));
			toolSpeeds[id] = Integer.parseInt(parts[2]);
			for (int i = 3; i < parts.length; i++) {
				if (toolBlocks[id] == null) {
					toolBlocks[id] = new LinkedList<Integer>();
				}
				toolBlocks[id].add(Integer.parseInt(parts[i]));
			}
		}

		for (String line:rFile) {
			String[] parts = line.split(" ");
			int id = Integer.parseInt(parts[0]);
			Recipe r = new Recipe(id);
			for (int i = 1; i < parts.length; i++) {
				String[] ingredients = parts[i].split(":");
				r.addItem(Integer.parseInt(ingredients[0]), Integer.parseInt(ingredients[1]));
			}
			recipes[id] = r;
		}
	}
	private static HashMap<String, BufferedImage> getParts(String ch) throws IOException {
		ch = ch.toLowerCase();
		HashMap<String, BufferedImage> map = new HashMap<String, BufferedImage>();
		String[] p = new String[] {"head", "arm", "leg", "body"};
		for (String part:p) {
			String folder = ch.equals("male")||ch.equals("female")?"Player "+ch:ch.substring(0, 1).toUpperCase()+ch.substring(1);
			String path = String.format("assets/images/PNG/Characters/%s/%s_%s.png", folder, ch, part);
			map.put(part, scale(ImageIO.read(new File(path)), Resources.SCALE*2));
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
