package com.pile.entity.player;

public class Inventory {
	boolean inventoryToggle = false;

	//Todo arraylist of item types
	//public static ArrayList toolBar<items> = new ArrayList<items>();

	//Todo have the 0-9 keys point to item selection. COULD ADD Selected item icon.
	// ^^^ Small square that shows the current item selected. < Faster to implement than animating the item

	//Todo use items to search for it's crafting recipes. Remember each recipe calls for a number of items
	//Have different types of items, stackable, material,

	//Inventory should be able to check free slots, find
	//https://softwareengineering.stackexchange.com/questions/246454/structuring-a-storage-system-for-a-game

	//Todo Work on blocks breaking and inventory pickup
	public boolean isInventoryToggle() {
		return inventoryToggle;
	}

	public void setInventoryToggle(boolean inventoryToggle) {
		this.inventoryToggle = inventoryToggle;
	}

	public static void drawInventory(){
		//Todo
		//Todo Access the drawing. Always draw the inventory, but have 2 states (Maybe 3 for crafting)
	}
}
