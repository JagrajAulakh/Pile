package com.pile.entity.player.inv;

import com.pile.crafting.Recipe;
import com.pile.entity.player.Player;
import com.pile.block.Block;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.util.ArrayList;
import java.util.LinkedList;

public class Crafting {

	public static ArrayList<Recipe> getRecipes(Player player) {
		Inventory inventory = player.getInventory();
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
				if (canCraft) {
					LinkedList<Block> blocks = PlayState.world.getBlocksAround(player, 10);
					if (r.requiresBench()) {
						canCraft = false;
						for (Block b:blocks) {
							if (b.getId() == 35) {
								canCraft = true;
								break;
							}
						}
					}
					if (canCraft) recipes.add(r);
				}
			}
		}
		return recipes;
	}

	public static void giveItems(Player player, Recipe recipe) {
		ArrayList<Item> items = recipe.getItems();
		for (Item item:items) {
			player.removeItem(item);
		}
		player.addItem(recipe.getCrafting());
	}
}
