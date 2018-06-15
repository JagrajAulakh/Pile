package com.pile.ui;

import com.pile.GameLogic;
import com.pile.Input;
import com.pile.image.Resources;
import com.pile.state.MenuState;
import com.pile.state.PlayState;

import java.awt.image.BufferedImage;

public class Button {
	private String text;
	private int x, y, width, height;
	//curImg - the current button image being displayed
	//normImg - the original button image
	//hoverImg - the button image when hovering
	private BufferedImage curImg, normImg, hoverImg;
	public Button(String text, BufferedImage img, int x, int y, BufferedImage hover){
		this.text = text;
		this.x = x;
		this.y = y;
		this.normImg = img;
		this.hoverImg = hover;
		this.width = img.getWidth();
		this.height = img.getHeight();
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getX() { return x - width/2; }
	public int getY() { return y - height/2; }
	public BufferedImage getImage() { return curImg; }

	public void update() {
		//If Mouse is on button
		if (getX() < Input.mx && Input.mx < getX() + width && getY() < Input.my && Input.my < getY() + height) {
			//Changing image to "hover"
			curImg = hoverImg;
			if (Input.mousePressed(0)) {
				//Changed image to "clicked"
				if(!text.toLowerCase().equals("pause")) curImg = Resources.scale(hoverImg,.7);
			}
			//When the button is pressed, goes to specific state
			if (Input.mouseUp(0)) {
				if (text.toLowerCase().equals("play")) {
					GameLogic.gsm.set(new PlayState());
				} else if (text.toLowerCase().equals("exit")) {
					GameLogic.gsm.pop();
					GameLogic.gsm.set(new MenuState());
				} else if (text.toLowerCase().equals("resume")) {
					GameLogic.gsm.pop();
				}
			}
		} else {
			//Sets button image back to original image
			curImg = normImg;
		}
	}
}
