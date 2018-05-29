package com.pile.entity.player.inv;
import com.pile.image.Resources;

import com.pile.image.SingleImage;
import com.pile.image.ImageType;

public class Item {
	public static final int STACK_MAX = 99;
	private int id, amount;
	private ImageType image;
	public Item(int id) {
		this.id = id;
		amount = 1;
		double sc = 0.7;
		this.image = new SingleImage(Resources.scale(Resources.blocks[id].getImage(), sc));
	}

	public ImageType getImage() { return image; }
	public int getId() { return id; }
	public int getAmount() { return amount; }
	public void add() { amount++; }
	public void decrease() { amount--; }
	//Todo relook this.. bad? way of doing things
	public int getWidth(){ return image.getImage().getWidth();}
	public int getHeight(){ return image.getImage().getHeight();}

	public int getStackMax() { return STACK_MAX; }

	@Override
	public String toString() {
		return "{" + id + "," + amount + "}";
	}
}
