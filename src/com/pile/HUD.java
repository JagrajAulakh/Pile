package com.pile;

import com.pile.block.Block;
import com.pile.entity.Drop;
import com.pile.entity.player.Player;
import com.pile.entity.player.inv.Crafting;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.awt.*;

public class HUD {
	//Constants used to size inventories
	public static final int INV_BOX_WIDTH = 40;
	public static final int INV_BOX_HEIGHT = 40;
	public static final int SPACING = 5;

	//Allows player to pickup and move items from their inventory
	private static Item inHand = null;

	public static Item getInHand(){
		return inHand;
	}

	private static String amountToString(Item item) { return "" + (item.getAmount() == 1 ? "" : item.getAmount()); }

	//Checking if player's mouse is colliding with an inventory
	private static boolean inInventoryArea(Inventory inv) {
		int mx = Input.mx;
		int my = Input.my;
		boolean area;
		double posX = inv.getX();
		double posY = inv.getY();
		area = posX <= mx && mx <= (INV_BOX_WIDTH+SPACING)*inv.getWidth() + posX && posY <= my && my <= (INV_BOX_HEIGHT+SPACING)*inv.getHeight()+ posY;
		return area;
	}
	private static void swapItems(Inventory inv) {
		Item[] items = inv.getItems();
		double posX = inv.getX();
		double posY = inv.getY();
		int ix = (int) ((Input.mx - posX) / (INV_BOX_WIDTH+SPACING));
		int iy = (int) ((Input.my - posY) / (INV_BOX_HEIGHT+SPACING));
		int spot = iy*inv.getWidth()+ ix;
		if (items[spot] != null && inHand != null) {
			if (items[spot].getId() == inHand.getId()) {
				// Todo combine same items
				return;
			}
		}
		Item tmp = items[spot];
		items[spot] = inHand;
		inHand = tmp;
	}
	public static void dropInHand(Player player) {
		if (inHand != null) {
			for (int i = 0; i < inHand.getAmount(); i++) {
				double x = player.getVelX() > 0 ? player.getX() + player.getWidth() : player.getX() - Block.WIDTH;
				double y = player.getY() - player.getHealth() / 2;
				PlayState.world.addEntity(new Drop(x, y, inHand.getId(), player, (Math.random()*5 + 1) * (player.getVelX()>0 ? 1 : -1), -5, 60));
			}
			inHand = null;
		}
		else {
			player.toggleInventory();
			player.setChest(null);
		}
	}

	public static void update(Player player) {
		Inventory inventory = player.getInventory();
		System.out.println(Crafting.getRecipes(inventory));
		if (player.inventoryState()) {
			if (player.getChest() == null) {
				if (Input.mouseUp(0)) {
					if (inInventoryArea(inventory)) {
						swapItems(inventory);
					} else {
						dropInHand(player);
					}
				}
			} else {
				if (Input.mouseUp(0)) {
					if (inInventoryArea(inventory)) {
						swapItems(inventory);
					} else if (inInventoryArea(player.getChest().getStorage())) {
						swapItems(player.getChest().getStorage());
					} else {
						dropInHand(player);
					}
				}
			}
		}
	}

	public static void render(Graphics g, Player player) {
		Graphics2D g2 = (Graphics2D)g.create();
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
		g2.setRenderingHints(qualityHints);
		for (int i = 0; i < player.getHealth()/2; i++) {
			int hw = Resources.heart2.getWidth();
			g2.drawImage(Resources.heart2, (int)(Game.WIDTH - hw*2 - i*hw*1.2), 10, null);
		}

		drawInventory(g2,player,player.getInventory(),Inventory.P_INV);
		if(player.getChest() != null){
			drawInventory(g2,player,player.getChest().getStorage(),Inventory.INV);
		}

		if (inHand != null) {
			g2.setColor(Color.BLACK);
			g2.drawImage(inHand.getImage(), Input.mx - inHand.getImage().getWidth()/2, Input.my - inHand.getImage().getHeight()/2, null);
			g2.drawString(amountToString(inHand), Input.mx - INV_BOX_WIDTH/2, Input.my + INV_BOX_HEIGHT/2);
			g2.setStroke(new BasicStroke(5));
		}

		if (player.inventoryState()) {
			g2.setColor(new Color(0,0,255,100));
		}
	}
	private static void drawInventory(Graphics2D g, Player player, Inventory inv, int type){
		Item[] items = inv.getItems();
		int inventorySquares = items.length;
		double posX = inv.getX();
		double posY = inv.getY();
		int y = -1;
		g.setFont(Resources.getFont(32));
		if (type == Inventory.P_INV) {
			inventorySquares = player.inventoryState() ? items.length:inv.getWidth();
		}
		for (int i = 0; i < inventorySquares; i++) {
			if (i % inv.getWidth() == 0) y++;
			int bx = (int) (posX + (i % inv.getWidth()) * (INV_BOX_WIDTH+SPACING));
			int by = (int) (posY + y * (INV_BOX_HEIGHT+SPACING));
			if(type == Inventory.P_INV){
				int alpha = player.inventoryState() ? inInventoryArea(inv) ? 120 : 100 : 100;
				g.setColor(y==0 ? new Color(255,0,0,alpha) : new Color(0,50,255, alpha));
				g.fillRoundRect(bx, by, INV_BOX_WIDTH, INV_BOX_HEIGHT, 20, 20);
				//Hotbar selection (Player Inv only)
				if (i == inv.getSpot()) {
					g.setColor(Color.WHITE);
					g.setStroke(new BasicStroke(5));
					g.drawRoundRect(bx, by, INV_BOX_WIDTH, INV_BOX_HEIGHT, 20, 20);
				}
			}
			else{
				g.setColor(new Color(0,50,255,100));
				g.fillRoundRect(bx, by, INV_BOX_WIDTH, INV_BOX_HEIGHT, 20, 20);
			}

			g.setColor(Color.BLACK);
			g.setFont(Resources.getFont(32));
			if (items[i] != null) {
				g.drawImage(items[i].getImage(), bx + INV_BOX_WIDTH/2 - items[i].getWidth()/2, by + INV_BOX_HEIGHT/2 - (int)(items[i].getHeight()/2), null);
				g.drawString(amountToString(items[i]), bx, by+INV_BOX_HEIGHT);
			}
		}

	}
}
