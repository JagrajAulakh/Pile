package com.pile.image;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;

public class Resources {
	public static double SCALE = 0.5;
	public static Font font1 = new Font("Times New Roman", Font.PLAIN, 80);
	public static SingleImage player1, dirt;
	public static Animation playerMaleWalking;
	public static void load() throws Exception {
		NodeList playerXML = getXML("assets/images/Spritesheets/spritesheet_characters.xml").getElementsByTagName("SubTexture");

		Spritesheet playerSheet = new Spritesheet(ImageIO.read(new File("assets/images/Spritesheets/spritesheet_characters.png")));
//		player1 = new SingleImage(scale(playerSheet.getImage(getCo(playerXML, "male_body.png")), SCALE));
		player1 = new SingleImage(scale(ImageIO.read(new File("assets/images/char/male/maleWalk1.png")), SCALE*2));
		playerMaleWalking = new Animation(50);
		for (int i = 1; i <= 5; i++) {
			playerMaleWalking.addScene(ImageIO.read(new File(String.format("assets/images/char/male/maleWalk%d.png", i))));
		}
		dirt = new SingleImage(scale(ImageIO.read(new File("assets/images/PNG/Tiles/dirt.png")), SCALE));
	}

	public static int[] getCo(NodeList tree, String name) {
		for (int i = 0; i < tree.getLength(); i++) {
			Node node = tree.item(i);
			Element n = (Element)node;
			if (n.getAttribute("name").equals(name)) {
				return new int[] {parseInt(n.getAttribute("x")), parseInt(n.getAttribute("y")), parseInt(n.getAttribute("width")), parseInt(n.getAttribute("height"))};
			}
		}
		return null;
	}

	private static Document getXML(String path) {
		File file = new File(path);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.parse(file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int parseInt(String x) { return Integer.parseInt(x); }

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
		} else {
			g.drawImage(original, 0, 0, null);
		}
		g.dispose();
		return flipped;
	}
}
