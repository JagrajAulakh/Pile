package com.pile.entity.player.inv;

import com.pile.image.Resources;


import java.util.Arrays;

public class Inventory {

	public static final int P_INV = 0;
	public static final int INV = 1;

	private int spot; //Holds selection in hotBar
	private Item[] inventory;
	private int width, height;
	private double x,y;

	public Inventory(double x, double y) {
		this(x, y, 10, 5);
	}
	public Inventory(double x, double y, int w, int h){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		inventory = new Item[w*h];
		spot = 0;
	}

	public int getSpot() { return spot; }
	public void setSpot(int spot) { this.spot = spot; }
	public Item getCurrentItem() { return inventory[spot]; }
	public Item[] getItems() { return inventory; }
	public void moveSpotLeft() { spot = (spot+1) % width; }
	public void moveSpotRight() { spot = spot-1 < 0 ? width-1 : spot-1; }
	public double getX() { return x; }
	public double getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public boolean has(int id, int amount) {
		// todo find multiple items
		int count = 0;
		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];
			if (item != null && item.getId() == id) {
				count += item.getAmount();
			}
			if (count >= amount) {
				return true;
			}
		}
		return false;
	}

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
	public boolean add(int id) { return add(id, 1); }
	public boolean add(int id, int amount) {
		Item find = findItem(id);
		if (find == null) {
			int s = firstEmpty();
			if (s == -1) return false;

			if (Resources.itemTypes[id] == Resources.ItemType.BLOCK) { // Check if it's a block
				inventory[s] = new Block(id, amount);
				return true;
			} else if (Resources.itemTypes[id] == Resources.ItemType.TOOL) { // Check if it's a tool
				inventory[s] = new Tool(id);
				return true;
			} else if (Resources.itemTypes[id] == Resources.ItemType.ITEM) {
				inventory[s] = new Item(id, amount);
				return true;
			}
			return false;
		} else {
			for (int i = 1; i < amount+1; i++) {
				find.add();
				find = findItem(id);
				if (find == null) {
					int leftOver = amount - i;
					if (leftOver >= 1) {
						add(id, leftOver);
						return true;
					}
				}
			}
			return true;
		}
	}

	public boolean remove(int id) {
		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i];
			if (item != null) {
				if (item.getId() == id) {
					decrease(i);
				}
			}
		}
		return false;
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

	//THIS UPDATES THE ITEMS, NO ITEMS <0 ALLOWED - SETS TO NULL
	public void updateItem(Item i){
		int spot = Arrays.asList(inventory).indexOf(i);
		if(inventory[spot].getAmount() <= 0){
			inventory[spot] = null;
		}
	}

}
