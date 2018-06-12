package com.pile.crafting;

import com.pile.entity.player.inv.Item;

import java.util.LinkedList;

public class Recipe {
    private Item crafting;
    private LinkedList<Item> items;
    public Recipe(Item crafting) {
        this.crafting = crafting;
        items = new LinkedList<Item>();
    }

    public void addItem(int amount, int id) { addItem(new Item(id, amount)); }
	public void addItem(Item item) { items.add(item); }
	public LinkedList<Item> getItems() { return  items; }
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
