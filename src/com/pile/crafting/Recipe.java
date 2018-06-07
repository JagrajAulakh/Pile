package com.pile.crafting;

import com.pile.entity.player.inv.Item;

import java.util.LinkedList;

public class Recipe {
    private int id;
    private LinkedList<Item> items;
    public Recipe(int id) {
        this.id = id;
        items = new LinkedList<Item>();
    }

    public void addItem(int id, int amount) {
    	items.add(new Item(id, amount));
    }

	@Override
	public String toString() {
		String tot = "{";

		for (Item i:items) {
			tot += i.getId() + ":" + i.getAmount() + ", ";
		}

		return tot.substring(0, tot.length() - 2) + "}";
	}
}
