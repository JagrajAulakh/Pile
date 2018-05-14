package com.pile;

import java.awt.event.*;

public class Input implements KeyListener,MouseListener,MouseMotionListener {
	public static boolean[] keys = new boolean[KeyEvent.KEY_LAST];
	public static boolean[] mb = new boolean[3];
	public static int mx, my;
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mb[e.getButton()-1] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
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
}
