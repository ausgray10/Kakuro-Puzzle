package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import tiles.*;

public class FileManager {

	public static Puzzle OpenFile() {
		final JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "/Puzzles");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PUZZLE FILES, puzzle", "puzzle");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			Scanner scanner = null;
			try {
				scanner = new Scanner(new FileReader(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (scanner != null) {
					ArrayList<String[]> lines = new ArrayList<String[]>();
					while (scanner.hasNext()) {
						String line = scanner.nextLine();
						lines.add(line.split(","));
					}
					ITile[][] tiles = new ITile[lines.size()][lines.get(0).length];
					for(int y = 0; y < lines.size(); y++) {
						for(int x = 0; x < lines.get(y).length; x++) {
							String value = lines.get(y)[x].trim();
							char id = value.charAt(0);
							int col = Integer.parseInt(value.substring(1, 3));
							int row = Integer.parseInt(value.substring(3, 5));
							switch(id) {
							case 'b':
								tiles[y][x] = new ControlTile(x,y, col, row);
								break;
							case 'w':
								tiles[y][x] = new InputTile(x, y, col + row);
								break;
							default:
							case 'e':
								tiles[y][x] = new BlankTile(x, y);
								break;
							}
						}
					}
					scanner.close();
					return new Puzzle(file, tiles);
				}
			}
		}
		return null;
	}

	public static void SaveFile(Puzzle puzzle) {
		final JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "/Puzzles");
		fc.setSelectedFile(new File(puzzle.getName()));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PUZZLE FILES, puzzle", "puzzle");
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(null);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				FileWriter writer = new FileWriter(file);
				writer.write(puzzle.puzzleToString());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
