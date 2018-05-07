package com.pile.state;

import java.util.Stack;

public class GameStateManager {
	private Stack<GameState> states;

	public GameStateManager() {
		states = new Stack<GameState>();
	}

	public void push(GameState s) {
		states.push(s);
	}

	public GameState currentState() {
		return states.peek();
	}

	public void pop() {
		states.pop();
	}

	public void set(GameState s) {
		pop();
		push(s);
	}
}
