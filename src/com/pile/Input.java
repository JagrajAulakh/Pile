package com.pile;

import java.awt.event.*;
import java.util.Arrays;

public class Input implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener {
	// Arrays to keep track of Keys and Mouse
	private enum KeyState {
		RELEASED,
		PRESSED,
		ONCE
	}
	private enum MouseButtonState {
		MOUSEUP,
		MOUSEDOWN,
		PRESSED,
		RELEASED
	}
	private boolean[] currentKeys;
	private KeyState[] keys;

	public boolean[] currmb;
	public MouseButtonState[] mb;

	// Keeping track of where the player's mouse is
	public int mx, my;

	public Input() {
		currentKeys = new boolean[KeyEvent.KEY_LAST];
		keys = new KeyState[KeyEvent.KEY_LAST];
		for( int i = 0; i < KeyEvent.KEY_LAST; i++) {
			keys[i] = KeyState.RELEASED;
		}
		currmb = new boolean[3];
		mb = new MouseButtonState[3];
		for (int i = 0; i < mb.length; i++) {
			mb[i] = MouseButtonState.RELEASED;
		}
	}

	public synchronized void poll() {
		for(int i = 0; i < KeyEvent.KEY_LAST; i++) {
			// Set the key state
			if(currentKeys[i]) {
				// If the key is down now, but was not
				// down last frame, set it to ONCE,
				// otherwise, set it to PRESSED
				if(keys[i] == KeyState.RELEASED)
					keys[i] = KeyState.ONCE;
				else
					keys[i] = KeyState.PRESSED;
			} else {
				keys[i] = KeyState.RELEASED;
			}
		}
		for (int i = 0; i < mb.length; i++) {
			if (currmb[i]) {
				if (mb[i] == MouseButtonState.RELEASED)
					mb[i] = MouseButtonState.MOUSEDOWN;
				else
					mb[i] = MouseButtonState.PRESSED;
			} else {
				if (mb[i] == MouseButtonState.PRESSED || mb[i] == MouseButtonState.MOUSEDOWN)
					mb[i] = MouseButtonState.MOUSEUP;
				else
					mb[i] = MouseButtonState.RELEASED;
			}
		}
	}

	public boolean keyDown(int keyCode) {
		return keys[keyCode] == KeyState.ONCE || keys[keyCode] == KeyState.PRESSED;
	}

	public boolean keyDownOnce(int keyCode) {
		return keys[keyCode] == KeyState.ONCE;
	}

	public boolean mouseUp(int button) {
		return mb[button] == MouseButtonState.MOUSEUP;
	}

	public boolean mouseDown(int button) {
		return mb[button] == MouseButtonState.MOUSEDOWN;
	}

	public boolean buttonPressed(int button) { return mb[button] == MouseButtonState.MOUSEDOWN || mb[button] == MouseButtonState.PRESSED; }

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		currentKeys[keyCode] = true;
	}

	@Override
	public synchronized void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		currentKeys[keyCode] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		currmb[e.getButton()-1] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		currmb[e.getButton()-1] = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
	}

	public void update() {
		poll();
	}
}
