package com.pile;

import com.pile.block.Block;
import com.pile.crafting.Recipe;
import com.pile.entity.Drop;
import com.pile.entity.player.Player;
import com.pile.entity.player.inv.Crafting;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.image.Resources;
import com.pile.state.PlayState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class HUD {
	//Constants used to size inventories
	public static final int INV_BOX_WIDTH = 40;
	public static final int INV_BOX_HEIGHT = 40;
	public static final int SPACING = 5;

	//Allows player to pickup and move items from their inventory
	private static Item inHand = null;
	private static ArrayList<Recipe> craftable = new ArrayList<Recipe>();
	public static Recipe[] craftingArray = new Recipe[5];
	public static int craftingScrollAmount = -2;

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
		area = posX <= mx && mx < (INV_BOX_WIDTH+SPACING)*inv.getWidth() + posX && posY <= my && my < (INV_BOX_HEIGHT+SPACING)*inv.getHeight()+ posY;
		return area;
	}
	private static boolean inCraftingArea(Player player) {
		return player.getInventory().getX() <= Input.mx && Input.mx <= player.getInventory().getX() + INV_BOX_WIDTH+SPACING && player.getInventory().getY() + (INV_BOX_HEIGHT+SPACING)*7 <= Input.my && Input.my <= (INV_BOX_HEIGHT+SPACING)*12;
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
				int leftOver = items[spot].getAmount() + inHand.getAmount() - inHand.getStackMax();
				items[spot].setAmount(Math.min(inHand.getAmount()+items[spot].getAmount(), inHand.getStackMax()));
				if (leftOver >= 1) {
					inHand = new Item(inHand.getId(), leftOver);
				} else {
					inHand = null;
				}
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
				PlayState.world.addEntity(new Drop(x, y, inHand.getId(), (Math.random()*5 + 1) * (player.getVelX()>0 ? 1 : -1), -5, 60));
			}
			inHand = null;
		}
		else {
			player.toggleInventory();
			player.setChest(null);
		}
	}
	public static void interactItems(Inventory inv){
		Item[] items = inv.getItems();
		double posX = inv.getX();
		double posY = inv.getY();
		int ix = (int) ((Input.mx - posX) / (INV_BOX_WIDTH+SPACING));
		int iy = (int) ((Input.my - posY) / (INV_BOX_HEIGHT+SPACING));
		int spot = iy*inv.getWidth()+ ix;
		//On an item with nothing in your hand
		if (items[spot] != null && inHand == null) {
			//Picks up half of the items (or higher if amount is odd), leaves half in inventory
			int pickedUp = (items[spot].getAmount() > 1 ? items[spot].getAmount() / 2 + items[spot].getAmount() % 2 : 1);
			inHand = new Item(items[spot].getId(), pickedUp);
			items[spot].subtractAmount(pickedUp);
			//Prevents item amounts of <= 0
			inv.updateItem(items[spot]);
		}
		//Holding item, drops 1 unit
		else if (inHand != null) {
			int itemId = inHand.getId();
			//Flag to determine if we should subtract items from hand
			boolean drop = false;
			if(items[spot] == null){
				items[spot] = new Item(itemId,1);
				drop = true;
			} else if(items[spot].getId() == itemId){
				items[spot].add();
				drop = true;
			}
			if(drop){
				if(inHand.subtractAmount(1) <= 0){
					inHand = null;
				}
			}
		}
	}
	public static void update(Player player) {
		Inventory inventory = player.getInventory();
		if (player.inventoryState()) {
			if (Input.mouseUp(0)) {
				if (inInventoryArea(inventory)) {
					swapItems(inventory);
				} else if (player.getChest() != null && inInventoryArea(player.getChest().getStorage())) {
					swapItems(player.getChest().getStorage());
				} else if (inCraftingArea(player)) {

				} else {
					dropInHand(player);
				}
			}
			else if (Input.mouseUp(2)){
				if(inInventoryArea(inventory)){
					interactItems(inventory);
				} else if (player.getChest() != null && inInventoryArea(player.getChest().getStorage())){
					interactItems(player.getChest().getStorage());
				}
			}

		}
		if (player.inventoryState()) {
			if (Input.wheelDown() || Input.keyDownOnce(KeyEvent.VK_UP)) {
				craftingScrollAmount--;
			} else if (Input.wheelUp() || Input.keyDownOnce(KeyEvent.VK_DOWN)) {
				craftingScrollAmount++;
			}
			if (craftingScrollAmount < -2) craftingScrollAmount = -2;
			else if (craftingScrollAmount > craftable.size()-3) craftingScrollAmount = craftable.size()-3;
			craftable = Crafting.getRecipes(player);
			for (int i = 0; i < 5; i++) {
				craftingArray[i] = null;
				int index = i + craftingScrollAmount;
				if (0 <= index && index < craftable.size()) {
					craftingArray[i] = craftable.get(index);
				}
			}
			if (Input.keyDownOnce('\n') && craftingArray[2] != null) Crafting.giveItems(player, craftingArray[2]);
			int bx = Input.mx / INV_BOX_WIDTH;
			int by = Input.my/(INV_BOX_HEIGHT+SPACING) - player.getInventory().getHeight() - 2;
			if (bx == 0 && by == 2) {
				if (Input.mouseUp(0) && craftingArray[by] != null) {
					Crafting.giveItems(player, craftingArray[by]);
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

		// Crafting
		if (player.inventoryState()) {
			for (int i = 0; i < 5; i++) {
				g.setColor(new Color(0,0,255,100 - Math.abs(2-i)*50 + 50));
				g.fillRoundRect((int)(player.getInventory().getX()), (int)(player.getInventory().getY() + (INV_BOX_HEIGHT+SPACING)*7 + (INV_BOX_HEIGHT+SPACING)*i), INV_BOX_WIDTH, INV_BOX_HEIGHT, 20, 20);
				try {
					drawItem(g, craftingArray[i].getCrafting(), (int)player.getInventory().getX(), (int)(player.getInventory().getY() + (INV_BOX_HEIGHT + SPACING) * 7 + (INV_BOX_HEIGHT + SPACING) * i));
				} catch (NullPointerException e) {}
			}
			if (craftingArray[2] != null) {
				ArrayList<Item> items = craftingArray[2].getItems();
				for (int i = 0; i < items.size(); i++) {
					int x = (int)(player.getInventory().getX() + (INV_BOX_WIDTH+SPACING)*(1+i));
					int y = (INV_BOX_HEIGHT+SPACING)*(player.getInventory().getHeight()+4);
					g.setColor(new Color(0,0,255,150));
					g.fillRoundRect(x, y, INV_BOX_WIDTH, INV_BOX_HEIGHT, 20, 20);
					drawItem(g, items.get(i), x, y);
				}
				g.setColor(Color.BLACK);
				g.drawString(Resources.itemNames[craftingArray[2].getCrafting().getId()], (int)player.getInventory().getX(), (INV_BOX_HEIGHT+SPACING) * (player.getInventory().getHeight()+8));
				if (inCraftingArea(player)) {
					int by = (Input.my) / (INV_BOX_HEIGHT+SPACING) - player.getInventory().getHeight() - 2;
					if (0 <= by && by < craftingArray.length) {
						Recipe hover = craftingArray[by];
						if (hover != null) drawName(g, hover.getCrafting(), true);
					}
				} else {
					int bx = (int)((Input.mx-player.getInventory().getX()) / (INV_BOX_HEIGHT+SPACING) - 1);
					int by = Input.my / (INV_BOX_HEIGHT+SPACING) - player.getInventory().getHeight() - 2;
					if (0 <= bx && bx < craftingArray[2].getItems().size() && by == 2) drawName(g, craftingArray[2].getItems().get(bx), true);
				}
			}

			if (inHand == null && inInventoryArea(player.getInventory())) {
				int bx = (int)((Input.mx-player.getInventory().getX()) / (INV_BOX_WIDTH+SPACING));
				int by = (int)((Input.my-player.getInventory().getY()) / (INV_BOX_HEIGHT+SPACING));
				int spot = (by)*player.getInventory().getWidth()+ bx;
				Item hover = player.getInventory().getItems()[spot];
				if (hover != null) drawName(g, hover);
			}
		}

		if (inHand != null) {
			drawItem(g, inHand, Input.mx - inHand.getImage().getWidth()/2, Input.my - inHand.getImage().getHeight()/2);
		}
	}

	private static void drawItem(Graphics g, Item item, int x, int y) {
		g.setColor(Color.BLACK);
		g.setFont(Resources.getFont(32));
		g.drawImage(item.getImage(), x + INV_BOX_WIDTH/2 - item.getWidth()/2, y + INV_BOX_HEIGHT/2 - item.getHeight()/2, null);
		g.drawString(amountToString(item), x, y+INV_BOX_HEIGHT);
	}

	private static void drawName(Graphics g, Item item) { drawName(g, item, false); }
	private static void drawName(Graphics g, Item item, boolean showAmount) {
		g.setFont(Resources.getFont(32));
		g.setColor(Color.BLACK);
		String name = showAmount ? item.getAmount() + " x " : "";
		name += Resources.itemNames[item.getId()];
		g.drawString(name, Input.mx, Input.my);
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
				g.setColor(y==0 ? new Color(0,0,255,alpha) : new Color(100,100,255, alpha));
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
			if (items[i] != null) {
				drawItem(g, items[i], bx, by);
			}
		}

	}
}
