package com.pile.crafting;

import com.pile.entity.player.inv.Item;

import java.util.ArrayList;

public class Recipe {
    private Item crafting;
    private ArrayList<Item> items;
    public Recipe(Item crafting) {
        this.crafting = crafting;
        items = new ArrayList<Item>();
    }

    public void addItem(int amount, int id) { addItem(new Item(id, amount)); }
	public void addItem(Item item) { items.add(item); }
	public ArrayList<Item> getItems() { return items; }
	public Item getCrafting() { return crafting; }

	@Override
	public String toString() {
		String tot = "{";

		for (Item i:items) {
			tot += i.getAmount() + ":" + i.getId() + ", ";
		}

		return tot.substring(0, tot.length() - 2) + "}";
	}
}
