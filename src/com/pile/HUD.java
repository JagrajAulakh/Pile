package com.pile;

import com.pile.entity.player.Player;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.image.Resources;

import java.awt.*;

//Keep track of drawing player's information
public class HUD {
	public static void update() {}

	public static void render(Graphics g, Player player) {
		Inventory inventory = player.getInventory();
		Item[] items = inventory.getInventory();
		int y = -1;
		for (int i = 0; i < (player.inventoryState()?items.length:Inventory.WIDTH); i++) {
			if (i % Inventory.WIDTH == 0) y++;
			int bx = (i % Inventory.WIDTH) * 50;
			int by = y * 50;
			g.setColor(new Color(0,50,255,100));
			g.fillRect(bx, by, 40, 40);
			if (items[i] != null) {
				g.setColor(Color.BLACK);
				g.setFont(Resources.getFont(32));
				g.drawString(""+items[i].getAmount(), bx, by+40);
			}
		}
	}
}
