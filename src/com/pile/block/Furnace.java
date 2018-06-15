package com.pile.block;

import com.pile.HUD;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;

public class Furnace extends Block {

	private Inventory cooking, fuel, done;
	private int cookingCounter, cookingStage;

	public Furnace(double x, double y) {
		super(x, y, 3);
		cooking = new Inventory(490,270,1,1);
		fuel = new Inventory(490,270 + HUD.INV_BOX_HEIGHT*2,1,1);
	}

	public Item getCooking() { return cooking.getItems()[0]; }
	public void setCooking(Item cooking) { this.cooking.setSpot(0, cooking); }
	public Item getFuel() { return fuel.getItems()[0]; }
	public void setFuel(Item fuel) { this.fuel.setSpot(0, fuel); }
	public int getCookingCounter() { return cookingCounter; }

	private boolean addItem(int id) {
		if (done.getItems()[0] == null) {
			done.setSpot(0, new Item(id, 1));
			return true;
		} else {
			if (done.getItems()[0].getId() == id) {
				done.add(id, 1);
				return true;
			} else {
				return false;
			}
		}
	}

	public void cook() {
		if (cookingStage >= 8 && addItem(cooking.getItems()[0].getId())) {
			cookingCounter = 0;
			cookingStage = 0;
		} else {
			cookingCounter++;
			if (cookingCounter >= 60) {
				cookingStage++;
			}
		}
	}
}
