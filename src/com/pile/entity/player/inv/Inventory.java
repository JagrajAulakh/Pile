package com.pile.entity.player.inv;

import com.pile.image.Resources;

public class Inventory {

	public static final int P_INV = 0;
	public static final int INV = 1;

	private int spot; //Holds selection in hotBar
	private Item[] inventory;
	private int invX, invY, invW, invH;

	//Todo use items to search for it's crafting recipes. Remember each recipe calls for a number of items
	//Have different types of items, stackable, material,

	//Inventory should be able to check free slots, find
	//https://softwareengineering.stackexchange.com/questions/246454/structuring-a-storage-system-for-a-game

	public Inventory(int x, int y) {
		this(x, y, 10, 5);
	}
	public Inventory(int x, int y, int w, int h){
		invX = x;
		invY = y;
		invW = w;
		invH = h;
		inventory = new Item[w*h];
		spot = 0;
	}

	public int getSpot() { return spot; }
	public void setSpot(int spot) { this.spot = spot; }
	public Item getCurrentItem() { return inventory[spot]; }
	public Item[] getItems() { return inventory; }
	public void moveSpotLeft() { spot = (spot+1) % invW; }
	public void moveSpotRight() { spot = spot-1 < 0 ? invW-1 : spot-1; }
	public int getInvX() { return invX; }
	public int getInvY() { return invY; }
	public int width() { return invW; }
	public int height() { return invH; }

	public Item findItem(int id) {
		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];
			if (item != null) {
				if (item.getId() == id && item.getAmount() < item.getStackMax()) {
					return item;
				}
			}
		}
		return null;
	}

	// Returns the index of first empty spot
	public int firstEmpty() {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] == null) {
				return i;
			}
		}
		return -1;
	}

	// Adds item with id to inventory. Returns true if successful, false otherwise.
	public boolean add(int id) {
		Item find = findItem(id);
		if (find == null) {
			int s = firstEmpty();
			if (s == -1) return false;

			if (Resources.blockSpeeds[id] != 0) { // Check if it's a block
				inventory[s] = new Block(id);
				return true;
			} else if (Resources.toolSpeeds[id] != 0) { // Check if it's a tool
				inventory[s] = new Tool(id);
				return true;
			}
			return false;
		} else {
			find.add();
			return true;
		}
	}

	public void decrease() { decrease(spot); }

	public void decrease(int spot) {
		if (inventory[spot] != null) {
			inventory[spot].decrease();
			if (inventory[spot].getAmount() == 0) {
				inventory[spot] = null;
			}
		}
	}

}
