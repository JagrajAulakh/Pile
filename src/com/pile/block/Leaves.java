package com.pile.block;

import com.pile.state.PlayState;

public class Leaves extends Block {
	private Block[] trunk;
	private boolean decaying;
	private int decayCounter, decayCounterMax;

	public Leaves(double x, double y, Block[] trunk) {
		super(x, y, 26);
		this.trunk = trunk;
		decayCounterMax = (int)(Math.random() * 110) + 10;
	}

	@Override
	public void update() {
		if (!decaying) {
			for (Block t:trunk) {
				if (t == null) {
					decaying = true;
				}
			}
		} else {
			decayCounter++;
			if (decayCounter >= decayCounterMax) {
				PlayState.world.removeBlockPermanent(this);
			}
		}
	}
}
