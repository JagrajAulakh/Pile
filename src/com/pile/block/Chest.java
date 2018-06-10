package com.pile.block;

import com.pile.entity.player.inv.Inventory;

public class Chest extends Block {
	private Inventory storage;
	public Chest(double x, double y) {
		super(x, y, false, 10);
		storage = new Inventory(490,270,10,4);
	}

	public Inventory getStorage() { return storage; }

}
