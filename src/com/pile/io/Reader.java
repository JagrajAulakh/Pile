package com.pile.io;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Reader {
	public static String readFile(String path) throws IOException {
		String tot = "";
		Scanner file = new Scanner(new File(path));
		while (file.hasNextLine()) {
			String line = file.nextLine();
			for (int i = 0; i < line.length(); i++) {
				if (i < line.length()-1) {
					if (line.charAt(i) == '/' && line.charAt(i+1) == '/') {
						continue;
					}
				}
				tot += "" + line.charAt(i);
			}
			tot += "\n";
		}
		return tot;
	}
}
