package com.pile;

import java.awt.event.*;

public class Input implements KeyListener,MouseListener,MouseMotionListener {
	// Arrays to keep track of Keys and Mouse
	public static boolean[] keys = new boolean[KeyEvent.KEY_LAST];
	//Todo maybe add scroll, so change array list size
	public static boolean[] mb = new boolean[3];
	// Keeping track of where the player's mouse is
	public static int mx, my;
	public static boolean mouseUp, keyUp;
	private static KeyEvent currentKeyEvent;
	private static MouseEvent currentMouseEvent;
	private static int counter;

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyUp = true;
		currentKeyEvent = e;
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mb[e.getButton()-1] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseUp = true;
		currentMouseEvent = e;
		mb[e.getButton()-1] = false;
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

	public static void update() {
		if (mouseUp) {
			if (counter == 0) {
				counter++;
				mb[currentMouseEvent.getButton()-1] = true;
			} else {
				counter = 0;
				mouseUp = false;
				mb[currentMouseEvent.getButton()-1] = false;
			}
		}
		if (keyUp) {
			if (counter == 0) {
				counter++;
				keys[currentKeyEvent.getKeyCode()] = true;
			} else {
				counter = 0;
				keyUp= false;
				keys[currentKeyEvent.getKeyCode()] = false;
			}
		}
	}
}
