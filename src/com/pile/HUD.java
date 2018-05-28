package com.pile;

import com.pile.entity.player.Player;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;

import java.awt.*;

//Keep track of drawing player's information
public class HUD {
	public static void update() {}

	public static void render(Graphics g, Player player) {
		Inventory inventory = player.getInventory();
		Item[] items = inventory.getInventory();
		g.setColor(Color.BLUE);
		int y = 0;
		for (int i = 0; i < items.length; i++) {
			if (i % Inventory.WIDTH == 0) y++;
			g.fillRect(i*50, 50*y, 40, 40);
		}
	}
}
