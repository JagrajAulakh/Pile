package com.pile;

import java.awt.event.*;
import java.util.Arrays;

// Handles all keyboard and mouse inputs
public class Input implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener {

	private enum KeyState {
		RELEASED,
		PRESSED,
		KEYDOWN,
		KEYUP
	}
	private enum MouseButtonState {
		MOUSEUP,
		MOUSEDOWN,
		PRESSED,
		RELEASED,
		WHEELUP,
		WHEELDOWN
	}
	private static boolean[] currentKeys;
	private static KeyState[] keys;

	public static boolean[] currmb;
	public static MouseButtonState[] mb;

	// Keeping track of where the player's mouse is
	public static int mx, my;

	public Input() {
		currentKeys = new boolean[KeyEvent.KEY_LAST];
		keys = new KeyState[KeyEvent.KEY_LAST];
		for( int i = 0; i < KeyEvent.KEY_LAST; i++) {
			keys[i] = KeyState.RELEASED;
		}
		currmb = new boolean[6];
		mb = new MouseButtonState[6];
		for (int i = 0; i < mb.length; i++) {
			mb[i] = MouseButtonState.RELEASED;
		}
	}

	public static synchronized void poll() {
		for(int i = 0; i < KeyEvent.KEY_LAST; i++) {
			// Set the key state
			if(currentKeys[i]) {
				if(keys[i] == KeyState.RELEASED)
					keys[i] = KeyState.KEYDOWN;
				else
					keys[i] = KeyState.PRESSED;
			} else {
				if (keys[i] == KeyState.KEYDOWN || keys[i] == KeyState.PRESSED) {
					keys[i] = KeyState.KEYUP;
				} else {
					keys[i] = KeyState.RELEASED;
				}
			}
		}
		for (int i = 0; i < mb.length; i++) {
			if (i == 4) {
				if (currmb[i]) {
					mb[i] = MouseButtonState.WHEELDOWN;
					currmb[i] = false;
				} else {
					mb[i] = MouseButtonState.RELEASED;
				}
			} else if (i == 5) {
				if (currmb[i]) {
					mb[i] = MouseButtonState.WHEELUP;
					currmb[i] = false;
				} else {
					mb[i] = MouseButtonState.RELEASED;
				}
			}
			else {
				if (currmb[i]) {
					if (mb[i] == MouseButtonState.RELEASED) {
						mb[i] = MouseButtonState.MOUSEDOWN;
					} else
						mb[i] = MouseButtonState.PRESSED;
				} else {
					if (mb[i] == MouseButtonState.PRESSED || mb[i] == MouseButtonState.MOUSEDOWN)
						mb[i] = MouseButtonState.MOUSEUP;
					else
						mb[i] = MouseButtonState.RELEASED;
				}
			}
		}
	}

	public static boolean keyDown(int keyCode) { return keys[keyCode] == KeyState.KEYDOWN|| keys[keyCode] == KeyState.PRESSED; }
	public static boolean keyDownOnce(int keyCode) { return keys[keyCode] == KeyState.KEYDOWN; }
	public static boolean keyUp(int keyCode) { return keys[keyCode] == KeyState.KEYUP || keys[keyCode] == KeyState.RELEASED; }
	public static boolean keyUpOnce(int keyCode) { return keys[keyCode] == KeyState.KEYUP; }

	public static boolean mouseUp(int button) { return mb[button] == MouseButtonState.MOUSEUP; }
	public static boolean mouseDown(int button) { return mb[button] == MouseButtonState.MOUSEDOWN; }
	public static boolean mousePressed(int button) { return mb[button] == MouseButtonState.PRESSED || mb[button] == MouseButtonState.MOUSEDOWN; }
	public static boolean wheelUp() { return mb[4] == MouseButtonState.WHEELDOWN; }
	public static boolean wheelDown() { return mb[5] == MouseButtonState.WHEELUP; }
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
		int rot = e.getUnitsToScroll();
		if (rot > 0) {
			currmb[4] = true;
		} else if (rot < 0) {
			currmb[5] = true;
		}
	}

	public static void update() {
		poll();
	}
}
