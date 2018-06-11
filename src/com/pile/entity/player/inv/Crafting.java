package com.pile.entity.player.inv;

import com.pile.crafting.Recipe;
import com.pile.image.Resources;

import java.util.LinkedList;

public class Crafting {

	public static LinkedList<Recipe> getRecipes(Inventory inventory) {
		LinkedList<Recipe> recipes = new LinkedList<Recipe>();
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
}
