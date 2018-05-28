package com.pile.entity.player.inv;

public class Inventory {

	public static final int WIDTH = 10;
	public static final int HEIGHT = 5;

	private int spot;
	public static Item[] spots;

	//Todo have the 0-9 keys point to item selection. COULD ADD Selected item icon.
	// ^^^ Small square that shows the current item selected. < Faster to implement than animating the item

	//Todo use items to search for it's crafting recipes. Remember each recipe calls for a number of items
	//Have different types of items, stackable, material,

	//Inventory should be able to check free slots, find
	//https://softwareengineering.stackexchange.com/questions/246454/structuring-a-storage-system-for-a-game

	public Inventory() {
		spots = new Item[WIDTH*HEIGHT];
		spot = 0;
	}

	public int getSpot() { return spot; }
	public void moveSpotLeft() { spot = (spot+1) % WIDTH; }
	public void moveSpotRight() { spot = spot-1 < 0 ? WIDTH-1 : spot-1; }

	//Todo Work on blocks breaking and inventory pickup

	//Todo vvv INSTEAD MAKE A HEADS UP DISPLAY (HUD) TO DRAW INVENTORY, HEALTH, CRAFTING, MAP, ETC
	public static void drawInventory(){
		//Todo
		//Todo Access the drawing. Always draw the inventory, but have 2 states (Maybe 3 for crafting)
	}

}
