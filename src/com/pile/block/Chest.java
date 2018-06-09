package com.pile.block;


import com.pile.entity.player.inv.Inventory;

public class Chest extends Block {
	private Inventory storage;
	public Chest(double x, double y) {
		super(x, y, false, 10);
		storage = new Inventory(250,300,10,1);
		storage.add(10);
		storage.add(12);
		//Todo take care of inventory graphics here
	}

	public Inventory getStorage() { return storage; }

}
