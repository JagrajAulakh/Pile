package com.pile;

import com.pile.block.Block;
import com.pile.entity.Drop;
import com.pile.entity.player.Player;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.image.Resources;
import com.pile.image.SingleImage;
import com.pile.state.PlayState;

import java.awt.*;
//Todo think of drawling health, idea: Row of hearts drawn, opacity changes & length to shown health
//Basically a standard health bar except with heart graphics replacing a solid bar
//Todo add selected item box? Pretty pointless --> Add selection indicator on hotBar <-- better idea
//Keep track of drawing player's information
public class HUD {
	//Todo look at these 2 variables
	public static final int INV_BOX_WIDTH = 40;
	public static final int INV_BOX_HEIGHT = 40;
	public static final int SPACING = 5;

	private static Item inHand = null;

	private static String amountToString(Item item) { return "" + (item.getAmount() == 1 ? "" : item.getAmount()); }

	public static void update(Player player) {
		Inventory inventory = player.getInventory();
		Item[] items = inventory.getInventory();

		if (player.inventoryState()) {
			if (0 <= Input.mx && Input.mx <= (INV_BOX_WIDTH+SPACING)*Inventory.WIDTH && 0 <= Input.my && Input.my <= (INV_BOX_HEIGHT+SPACING)*Inventory.HEIGHT) {
				int ix = Input.mx / (INV_BOX_WIDTH+SPACING);
				int iy = Input.my / (INV_BOX_HEIGHT+SPACING);
				int spot = iy*Inventory.WIDTH + ix;
				if (Input.mouseUp(0)) {
//					if (items[spot].getId() == inHand.getId())
					Item tmp = items[spot];
					items[spot] = inHand;
					inHand = tmp;
				}
			} else {
				if (Input.mouseUp(0)) {
					if (inHand != null) {
						int a = inHand.getAmount();
						int id = inHand.getId();
						for (int i = 0; i < a; i++) {
							double x = player.getVelX() > 0 ? player.getX() + player.getWidth() : player.getX() - Block.WIDTH;
							double y = player.getY() - player.getHealth() / 2;
							PlayState.world.addEntity(new Drop(x, y, id, player, 4 * (player.getVelX()>0 ? 1 : -1), -5, 60));
							inHand = null;
						}
					} else {
						player.toggleInventory();
					}
				}
			}
		}
	}

	public static void render(Graphics g, Player player) {
		for (int i = 0; i < player.getHealth()/2; i++) {
			int hw = Resources.heart2.getWidth();
			g.drawImage(Resources.heart2, (int)(Game.WIDTH - hw*2 - i*hw*1.2), 50, null);
		}

		Inventory inventory = player.getInventory();
		Item[] items = inventory.getInventory();
		int y = -1;
		g.setFont(Resources.getFont(32));
		for (int i = 0; i < (player.inventoryState()?items.length:Inventory.WIDTH); i++) {
			if (i % Inventory.WIDTH == 0) y++;
			int bx = (i % Inventory.WIDTH) * (INV_BOX_WIDTH+SPACING);
			int by = y * (INV_BOX_HEIGHT+SPACING);
			g.setColor(y==0 ? new Color(255,0,0,100) : new Color(0,50,255,100));
			g.fillRect(bx, by, INV_BOX_WIDTH, INV_BOX_HEIGHT);
			if (i == inventory.getSpot()) {
				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(5));
				g2.drawRect(bx, by, INV_BOX_WIDTH, INV_BOX_HEIGHT);
			}
			g.setColor(Color.BLACK);
			g.setFont(Resources.getFont(32));
			if (items[i] != null) {
				//Todo also draw images for block
				g.drawImage(items[i].getImage(), bx + INV_BOX_WIDTH/2 - (int)(items[i].getWidth()/2), by + INV_BOX_HEIGHT/2 - (int)(items[i].getHeight()/2), null);
				g.drawString(amountToString(items[i]), bx, by+INV_BOX_HEIGHT);
			}
		}
		if (inHand != null) {
			g.setColor(Color.BLACK);
			g.drawImage(inHand.getImage(), Input.mx - inHand.getImage().getWidth()/2, Input.my - inHand.getImage().getHeight()/2, null);
			g.drawString(amountToString(inHand), Input.mx - INV_BOX_WIDTH/2, Input.my + INV_BOX_HEIGHT/2);
		}
	}
}
