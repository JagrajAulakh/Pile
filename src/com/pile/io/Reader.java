package com.pile.io;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

// Class that reads txt files
public class Reader {
	// Ignores first line and reads rest
	public static String readDataFile(String path) throws IOException{
		String tot = "";
		Scanner file = new Scanner(new File(path));
		file.nextLine();
		while (file.hasNextLine()) {
			tot += file.nextLine() + "\n";
		}
		return tot;
	}
	// Reads every line of file
	public static String readFile(String path) throws IOException {
		String tot = "";
		Scanner file = new Scanner(new File(path));
		while (file.hasNextLine()) {
			tot += file.nextLine() + "\n";
		}
		return tot;
	}
}
