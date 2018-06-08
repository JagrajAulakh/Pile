package com.pile.io;

import com.pile.World;
import com.pile.block.Block;
import com.pile.entity.Enemy;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class Writer {

	public static boolean findSave(String worldname) {
		File dataDir = new File("data/" + worldname);
		return dataDir.exists();
	}
	public static void writeDataFile(String worldname, World world) {
		boolean dup = findSave(worldname);
		if (!dup) {
			File worldDir = new File("data/" + worldname);
			worldDir.mkdirs();
		}
		ObjectOutputStream ous;
		try {
			// Todo write data file
			ous = new ObjectOutputStream(new FileOutputStream("data/" + worldname + "/data.dat"));
			for (Block b:world.getBlocksAround(new Enemy(world.getWidth()/2, world.getHeight()), 50)) {
				ous.writeObject(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
