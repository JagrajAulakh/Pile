package com.pile;

import com.pile.entity.player.Player;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.image.Resources;
import com.pile.image.SingleImage;

import java.awt.*;
//Todo think of drawling health, idea: Row of hearts drawn, opacity changes & length to shown health
//Basically a standard health bar except with heart graphics replacing a solid bar
//Todo add selected item box? Pretty pointless --> Add selection indicator on hotBar <-- better idea
//Keep track of drawing player's information
public class HUD {
	//Todo look at these 2 variables
	static int invBox_Width = 40;
	static int invBox_Height = 40;
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
			g.fillRect(bx, by, invBox_Width, invBox_Height);
			if (items[i] != null) {
				g.setColor(Color.BLACK);
				g.setFont(Resources.getFont(32));
				//Todo also draw images for block
				g.drawImage(items[i].getImage().getImage(), bx + invBox_Width/2 - (int)(items[i].getWidth()/2), by + invBox_Height/2 - (int)(items[i].getHeight()/2), null);
				g.drawString(""+items[i].getAmount(), bx, by+invBox_Height);
			}
		}
	}
}
