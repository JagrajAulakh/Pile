package com.pile.entity.player.inv;

public class Item {
	private int id, amount;
	public Item(int id) {
		this.id = id;
		amount = 1;
	}

	public int getId() { return id; }
	public int getAmount() { return amount; }
	public void add() { amount++; }

	@Override
	public String toString() {
		return "{" + id + "," + amount + "}";
	}
}
