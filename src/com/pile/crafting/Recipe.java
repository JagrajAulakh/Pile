package com.pile.crafting;

import com.pile.entity.player.inv.Item;

import java.util.ArrayList;

// Stores the required materials for crafting an item
public class Recipe {
	private Item crafting;
	private ArrayList<Item> items;
	private boolean requiresBench;
	public Recipe(Item crafting, boolean requiresBench) {
		this.crafting = crafting;
		items = new ArrayList<Item>();
		this.requiresBench = requiresBench;
	}

	public void addItem(int amount, int id) { addItem(new Item(id, amount)); }
	public void addItem(Item item) { items.add(item); }
	public ArrayList<Item> getItems() { return items; }
	public Item getCrafting() { return crafting; }
	public boolean requiresBench() { return requiresBench; }

	@Override
	public String toString() {
		String tot = "{";

		for (Item i:items) {
			tot += i.getAmount() + ":" + i.getId() + ", ";
		}

		return tot.substring(0, tot.length() - 2) + "}";
	}
}
