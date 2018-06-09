package com.pile;

import com.pile.block.Block;
import com.pile.entity.Drop;
import com.pile.entity.player.Player;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.image.Resources;
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
	private static int scrollamount;

	private static String amountToString(Item item) { return "" + (item.getAmount() == 1 ? "" : item.getAmount()); }

	private static boolean inInventoryArea(int mx, int my, Inventory inv) {
		boolean area;
		int posX = inv.getInvX();
		int posY = inv.getInvY();
		area = posX <= mx && mx <= (INV_BOX_WIDTH+SPACING)*inv.width() + posX && posY <= my && my <= (INV_BOX_HEIGHT+SPACING)*inv.height()+ posY;
		return area;
	}
	private static void pickUpItem(Inventory inv){
		Item[] items = inv.getItems();
		int posX = inv.getInvX();
		int posY = inv.getInvY();
		int ix = (Input.mx - posX) / (INV_BOX_WIDTH+SPACING);
		int iy = (Input.my - posY) / (INV_BOX_HEIGHT+SPACING);
		int spot = iy*inv.width()+ ix;
		if (Input.mouseUp(0)) {
			Item tmp = items[spot];
			items[spot] = inHand;
			inHand = tmp;
		}
	}
	private static void dropInHand(Player player){
		if (inHand != null) {
			int a = inHand.getAmount();
			int id = inHand.getId();
			for (int i = 0; i < a; i++) {
				double x = player.getVelX() > 0 ? player.getX() + player.getWidth() : player.getX() - Block.WIDTH;
				double y = player.getY() - player.getHealth() / 2;
				PlayState.world.addEntity(new Drop(x, y, id, player, (Math.random()*5 + 1) * (player.getVelX()>0 ? 1 : -1), -5, 60));
				inHand = null;
			}
		}
		else{
			player.toggleInventory();
			player.setChest(null);
		}
	}

	public static void update(Player player) {
		Inventory inventory = player.getInventory();
		Item[] items = inventory.getItems();
		Inventory chest = null;
		if(player.getChest() != null){
			chest = player.getChest().getStorage();
			Item[] chest_items = chest.getItems();
		}

		if (player.inventoryState()) {
			if (inInventoryArea(Input.mx, Input.my,inventory)) {
				pickUpItem(inventory);
			} else {
				if (Input.mouseUp(0)) {
					dropInHand(player);
				}
			}
		}
		if(player.chestState()){
			if(inInventoryArea(Input.mx, Input.my, chest)){
				pickUpItem(chest);
			} else {
				if (Input.mouseUp(0)){
					dropInHand(player);
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
			g2.drawImage(Resources.heart2, (int)(Game.WIDTH - hw*2 - i*hw*1.2), 50, null);
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
		int posX = inv.getInvX();
		int posY = inv.getInvY();
		int y = -1;
		g.setFont(Resources.getFont(32));
		if(type == Inventory.P_INV){
			inventorySquares = player.inventoryState() ? items.length:inv.width();
		}
//		for (int i = 0; i < (player.inventoryState()?items.length:inv.width()); i++) {
		for (int i = 0; i < inventorySquares; i++) {
			if (i % inv.width() == 0) y++;
			int bx = posX + (i % inv.width()) * (INV_BOX_WIDTH+SPACING);
			int by = posY + y * (INV_BOX_HEIGHT+SPACING);
			if(type == Inventory.P_INV){
				int alpha = inInventoryArea(Input.mx, Input.my,inv) ? 255 : 100;
				g.setColor(y==0 ? new Color(255,0,0,alpha) : new Color(0,50,255,100));
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
				g.drawImage(items[i].getImage(), bx + INV_BOX_WIDTH/2 - (int)(items[i].getWidth()/2), by + INV_BOX_HEIGHT/2 - (int)(items[i].getHeight()/2), null);
				g.drawString(amountToString(items[i]), bx, by+INV_BOX_HEIGHT);
			}
		}

	}
}
