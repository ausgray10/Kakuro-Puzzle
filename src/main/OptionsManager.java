package main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class OptionsManager {
	public static Color mainColor = Color.BLUE;
	public static int volume = 100;

	public static void Load() {
		File file = new File(System.getProperty("user.dir") + "/Game.ini");
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] split = line.split("=");
				switch (split[0].trim()) {
				case "mainColor":
					String[] rgb = split[1].split(",");
					mainColor = new Color(Integer.parseInt(rgb[0].trim()),Integer.parseInt(rgb[1].trim()),Integer.parseInt(rgb[2].trim()));
					break;
				case "volume":
					volume = Integer.parseInt(split[1].trim());
					break;
				}
			}
		}
	}

	public static void Save() {
		File file = new File(System.getProperty("user.dir") + "/Game.ini");
		try {
			FileWriter writer = new FileWriter(file);
		    writer.write("mainColor = " + mainColor.getRed() + ", " + mainColor.getGreen() + ", " + mainColor.getBlue());
		    writer.write(System.getProperty("line.separator"));
		    writer.write("volume = " + volume);
		    
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
