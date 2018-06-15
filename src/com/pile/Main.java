package com.pile;

// Main class that runs
public class Main {

    public static void main(String[] args) {
    	System.setProperty("sun.java2d.opengl", "false"); // For Linux Usage. Prevents frame Skipping
	    new Game();
    }
}
