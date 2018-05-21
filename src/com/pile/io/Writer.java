package com.pile.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Writer {
	public static void writeToFile(String path, String val) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.write(val);
	}
	public static void appendToFile(String path, String val) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.append(val);
	}
	public static void writeDataFile() {
		File dataDir = new File("data/");
		if (!dataDir.mkdirs()) {
			File[] dataFiles = dataDir.listFiles();

			String[] files = new String[dataFiles.length];
			for (int i = 0; i < dataFiles.length; i++) {
				files[i] = dataFiles[i].getName();
			}
			Arrays.sort(files);
		}
	}
}
