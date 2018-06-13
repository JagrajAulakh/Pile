package com.pile.block;

import com.pile.entity.player.inv.Item;

public class Furnace extends Block {

	private Item cooking, fuel;
	private int cookingCounter;

	public Furnace(double x, double y) {
		super(x, y, 3);
		cooking = null;
		fuel = null;
	}

	public Item getCooking() { return cooking; }
	public void setCooking(Item cooking) { this.cooking = cooking; }
	public Item getFuel() { return fuel; }
	public void setFuel(Item fuel) { this.fuel = fuel; }
	public int getCookingCounter() { return cookingCounter; }
}
