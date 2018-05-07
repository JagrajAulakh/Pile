package com.pile.image;

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

public class Resources {
	public static Font font1 = new Font("Times New Roman", Font.PLAIN, 80);
	public static SingleImage player1;
	public static void load() throws Exception {
		NodeList playerXML = getXML("assets/images/Spritesheets/spritesheet_characters.xml").getElementsByTagName("SubTexture");

		Spritesheet player1Sheet = new Spritesheet(ImageIO.read(new File("assets/images/Spritesheets/spritesheet_characters.png")));
		player1 = new SingleImage(player1Sheet.getImage(getCo(playerXML, "male_body.png")));
//		player1 = new Animation(10);
//		player1.addScene(player1Sheet.getImage(getCo(playerXML, "male_head.png")));
	}

	public static int[] getCo(NodeList tree, String name) {
		for (int i = 0; i < tree.getLength(); i++) {
			Node node = tree.item(i);
//			if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element n = (Element)node;
			if (n.getAttribute("name").equals(name)) {
				return new int[] {parseInt(n.getAttribute("x")), parseInt(n.getAttribute("y")), parseInt(n.getAttribute("width")), parseInt(n.getAttribute("height"))};
			}
//			}
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
}
