package com.pile.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
	public static void writeToFile(String path, String val) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.write(val);
	}
	public static void appendToFile(String path, String val) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.append(val);
	}
}
