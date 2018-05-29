package com.pile.entity.player.inv;
import com.pile.image.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.pile.image.SingleImage;
import com.pile.image.ImageType;




public class Item {
	private int id, amount;
	private ImageType image;
	public Item(int id) {
		this.id = id;
		amount = 1;
		//this.image = Resources.blocks[id];
		double sc = 0.7;
		this.image = new SingleImage(Resources.scale(Resources.blocks[id].getImage(), sc));
	}

	public ImageType getImage() { return image; }

	public int getId() { return id; }
	public int getAmount() { return amount; }
	public void add() { amount++; }
	//Todo relook this.. bad? way of doing things
	public int getWidth(){ return image.getImage().getWidth();}
	public int getHeight(){ return image.getImage().getHeight();}

	@Override
	public String toString() {
		return "{" + id + "," + amount + "}";
	}
}
