package com.pile;

import com.pile.image.Resources;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Game extends Canvas {
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	private JFrame frame;
	private GameLogic gameLogic;
	private boolean running = true;
	public static Input input;

	public Game() {
		frame = new JFrame("Piles");
		try{
			Resources.load();
		} catch (Exception e) { e.printStackTrace(); }
		gameLogic = new GameLogic();
		Dimension d = new Dimension(Game.WIDTH, Game.HEIGHT);
		setPreferredSize(d);

		setFocusable(true);		// These two lines allow
		requestFocusInWindow();	// the key listener to work
		input = new Input();
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setUndecorated(true);
		System.out.println(frame.getContentPane().getHeight());
		System.out.println(frame.getContentPane().getWidth());
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
		gameLogic.refresh();
	}
	// Render runs as many times as it can
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		gameLogic.render(g);
		g.dispose();
		bs.show();
	}
}