package com.pile.block;

import com.pile.entity.Drop;
import com.pile.entity.player.Player;
import com.pile.entity.player.inv.Inventory;
import com.pile.entity.player.inv.Item;
import com.pile.image.Resources;

import static com.pile.state.PlayState.world;

public class Chest extends Block {
	private Inventory storage;
	public Chest(double x, double y) {
		super(x, y, false, 10);
		storage = new Inventory(490,270,10,4);
		storage.add(12);
		storage.add(13);
		storage.add(15);
		storage.add(27);
		storage.add(27);
		storage.add(27);
		storage.add(27);
	}

	public Inventory getStorage() { return storage; }

	public void emptyChest() {
		for (Item item:storage.getItems()) {
			if (item != null){
				for (int i = 0; i < item.getAmount(); i++) {
					world.addEntity(new Drop(x, y, Resources.blockDrop[item.getId()], Math.random() * 16 - 8, -5, 10));
				}
			}
		}
	}

}
