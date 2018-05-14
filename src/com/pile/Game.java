package com.pile;

import com.pile.image.Resources;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.*;
import javax.swing.*;

public class Game extends Canvas {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private JFrame frame;
	private GamePanel panel;
	private boolean running = true;

	public Game() {
		frame = new JFrame("Piles");
		try{
			Resources.load();
		} catch (Exception e) { e.printStackTrace(); }
		panel = new GamePanel();
		Dimension d = new Dimension(Game.WIDTH, Game.HEIGHT-1);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		Input i = new Input();
		setFocusable(true);		// These two lines allow
		requestFocusInWindow();	// the key listener to work
		addKeyListener(i);
		addMouseListener(i);
		addMouseMotionListener(i);
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		run();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while (delta >= 1) {
				tick();
				delta--;
			}

			if (running) {
				render();
			}
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
	}
	// Tick is running at exactly 60 times a second
	public void tick() {
		panel.refresh();
	}
	// Render runs as many times as it can
	public void render() {
		// repaint();
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		panel.render(g);
		// g.setColor(Color.RED);
		// g.fillRect(0, 0, WIDTH, HEIGHT);
		g.dispose();
		bs.show();
	}
}