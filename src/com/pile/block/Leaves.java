package com.pile.block;


public class Leaves extends Block {
	private int[][] trunk;
	private boolean decaying;
	private int decayCounter, decayCounterMax;

	public Leaves(double x, double y, int[][] trunk) {
		super(x, y, 26);
		this.trunk = trunk;
		decayCounterMax = (int)(Math.random() * 110) + 10;
		decayCounterMax *= 60;
	}

	public int[][] getTrunk() { return trunk; }
	public boolean isDecaying() { return decaying; }
	public void setDecaying(boolean decaying) { this.decaying = decaying; }
	public int getDecayCounter() { return decayCounter; }
	public void setDecayCounter(int decayCounter) { this.decayCounter = decayCounter; }
	public int getDecayCounterMax() { return decayCounterMax; }
}
