package game;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import listeners.*;
import tiles.BlankTile;
import tiles.ControlTile;
import tiles.ITile;
import tiles.InputTile;

/**
 * Handlers that are used for Game
 * 
 * @author Austin Gray
 *
 */
public class Handlers {

	/**
	 * File Manager System
	 * 
	 * @author Austin Gray
	 *
	 */
	public static class FileManager {
		/**
		 * Opens File Chooser
		 * 
		 * @return Puzzle for game
		 */
		public static Puzzle OpenFile() {
			final JFileChooser fc = new JFileChooser("Resources/Puzzles");
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
						for (int y = 0; y < lines.size(); y++) {
							for (int x = 0; x < lines.get(y).length; x++) {
								String value = lines.get(y)[x].trim();
								char id = value.charAt(0);
								int col = Integer.parseInt(value.substring(1, 3));
								int row = Integer.parseInt(value.substring(3, 5));
								switch (id) {
								case 'b':
									tiles[y][x] = new ControlTile(x, y, col, row);
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

		/**
		 * Saves puzzle using File Chooser
		 * 
		 * @param puzzle puzzle to save
		 */
		public static void SaveFile(Puzzle puzzle) {
			final JFileChooser fc = new JFileChooser("Resources/Puzzles");
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

	/**
	 * Options Manager System
	 * 
	 * @author Austin Gray
	 *
	 */
	public static class OptionsManager {

		public static ArrayList<ColorChangeListener> colorChangeArray = new ArrayList<ColorChangeListener>();
		public static ArrayList<VolumeChangeListener> effectChangeArray = new ArrayList<VolumeChangeListener>();
		public static ArrayList<VolumeChangeListener> backgroundChangeArray = new ArrayList<VolumeChangeListener>();
		
		
		public static Color currentColor = Color.BLUE;
		public static int effectVolume = 100;
		public static int backgroundVolume = 100;

		/**
		 * Sets current color for all listeners
		 * 
		 * @param color new Color
		 */
		public static void setColor(Color color) {
			currentColor = color;
			for (ColorChangeListener listener : colorChangeArray) {
				listener.onColorChange(color);
			}
		}

		/**
		 * Sets current background volume for all listeners
		 * 
		 * @param value new volume
		 */
		public static void setBackgroundVolume(int value) {
			backgroundVolume = value;
			for (VolumeChangeListener listener : backgroundChangeArray) {
				listener.onVolumeChange(value);
			}
		}
		
		/**
		 * Sets current volume for all listeners
		 * 
		 * @param value new volume
		 */
		public static void setEffectVolume(int value) {
			effectVolume = value;
			for (VolumeChangeListener listener : effectChangeArray) {
				listener.onVolumeChange(value);
			}
		}

		/**
		 * Load Game.ini file
		 */
		public static void Load() {
			File file = new File("Resources/Game.ini");
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
						currentColor = new Color(Integer.parseInt(rgb[0].trim()), Integer.parseInt(rgb[1].trim()),
								Integer.parseInt(rgb[2].trim()));
						break;
					case "effectVolume":
						effectVolume = Integer.parseInt(split[1].trim());
						break;
					case "backgroundVolume":
						backgroundVolume = Integer.parseInt(split[1].trim());
						break;
					}
				}
			}
		}

		/**
		 * Save Game.ini file
		 */
		public static void Save() {
			File file = new File("Resources/Game.ini");
			try {
				FileWriter writer = new FileWriter(file);
				writer.write(
						"mainColor = " + currentColor.getRed() + ", " + currentColor.getGreen() + ", " + currentColor.getBlue());
				writer.write(System.getProperty("line.separator"));
				writer.write("effectVolume = " + effectVolume);
				writer.write(System.getProperty("line.separator"));
				writer.write("backgroundVolume = " + backgroundVolume);

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sound Manager System
	 * 
	 * @author Austin Gray
	 *
	 */
	public static class SoundManager {

		public static String BEEP = "beep.wav";
		public static String PARTYHORN = "partyhorn.wav";
		public static String SLIP = "slip.wav";
		public static String EXPLOSION = "bomb.wav";
		public static String JOURNEY = "Long Journey.wav";

		private static BackgroundClip background;

		/**
		 * Play Sound in game
		 * 
		 * @param name file name in Resources/Sounds/
		 */
		public static synchronized void playSound(final String name) {
			new Thread(new Runnable() {
				public void run() {
					try {
						EffectClip effect = new EffectClip(AudioSystem.getClip());
						AudioInputStream inputStream = AudioSystem
								.getAudioInputStream(new File("Resources/Sounds/" + name));
						effect.getClip().open(inputStream);
						effect.setVolume(OptionsManager.effectVolume);
						effect.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		/**
		 * Play backgound music in game
		 * 
		 * @param name file name in Resources/Sounds/
		 */
		public static synchronized void playBackgroundMusic(final String name) {
			new Thread(new Runnable() {
				public void run() {
					try {
						background = new BackgroundClip(AudioSystem.getClip());
						AudioInputStream inputStream = AudioSystem
								.getAudioInputStream(new File("Resources/Sounds/" + name));
						background.getClip().open(inputStream);
						background.getClip().loop(Clip.LOOP_CONTINUOUSLY);
						background.setVolume(OptionsManager.backgroundVolume);
						background.start();
						while(background.isLooping()) {
							Thread.sleep(1000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

		/**
		 * Stop playing background music
		 */
		public static void stopBackgroundMusic() {
			background.stop();
		}
	}

}
