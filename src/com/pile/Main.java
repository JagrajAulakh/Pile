package com.pile;

public class Main {

    public static void main(String[] args) {
    	System.setProperty("sun.java2d.opengl", "true"); // For Linux Usage. Prevents Frame Skipping
	    new Game();
    }
}
