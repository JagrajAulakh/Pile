package com.pile.io;

import com.pile.World;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Writer {
	public static void writeToFile(String path, String val) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.write(val);
	}
	public static void appendToFile(String path, String val) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.append(val);
	}
	public static boolean findSave(String worldname) {
		new File("data/").mkdirs();
		File dataDir = new File("data/" + worldname);
		return dataDir.exists();
	}
	public static void writeDataFile(String worldname, World world) {
		boolean dup = findSave(worldname);
		if (dup) { return; }
		else {
			File worldDir = new File("data/" + worldname);
			worldDir.mkdirs();
		}
	}
}
