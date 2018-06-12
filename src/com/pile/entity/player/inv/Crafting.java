package com.pile.entity.player.inv;

import com.pile.crafting.Recipe;
import com.pile.entity.player.Player;
import com.pile.image.Resources;

import java.util.ArrayList;
import java.util.LinkedList;

public class Crafting {

	public static ArrayList<Recipe> getRecipes(Inventory inventory) {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		for (Recipe r:Resources.recipes) {
			boolean canCraft = true;
			if (r != null) {
				for (Item i:r.getItems()) {
					if (!inventory.has(i.getId(), i.getAmount())) {
						canCraft = false;
						break;
					}
				}
				if (canCraft) recipes.add(r);
			}
		}
		return recipes;
	}

	public static void giveItems(Player player, Recipe recipe) {
		LinkedList<Item> items = recipe.getItems();
		for (Item item:items) {
			player.removeItem(item);
		}
		player.addItem(recipe.getCrafting());
	}
}
