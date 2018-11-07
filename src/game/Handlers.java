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

import tiles.BlankTile;
import tiles.ControlTile;
import tiles.ITile;
import tiles.InputTile;

public class Handlers {

	public static class FileManager{
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
	
	public static class OptionsManager {
		
		public static ArrayList<ColorChangeListener> onMainColorChange = new ArrayList<ColorChangeListener>();
		public static ArrayList<VolumeChangeListener> onVolumeChange = new ArrayList<VolumeChangeListener>();
		
		private static Color mainColor = Color.BLUE;
		private static int volume = 100;
		
		public static void setColor(Color color) {
			mainColor = color;
			for(ColorChangeListener listener : onMainColorChange) {
				listener.onColorChange(color);
			}
		}
		
		public static Color getColor() {
			return mainColor;
		}
		
		public static void setVolume(int value) {
			volume = value;
			for(VolumeChangeListener listener : onVolumeChange) {
				listener.onVolumeChange(volume);
			}
		}
		public static int getVolume() {
			return volume;
		}

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
	
	public interface ColorChangeListener{
		abstract void onColorChange(Color color);
	}
	public interface VolumeChangeListener{
		abstract void onVolumeChange(int value);
	}
	
	public static class SoundManager {
		
		public static String BEEP = "beep.wav";
		public static String PARTYHORN = "partyhorn.wav";
		public static String SLIP = "slip.wav";
		public static String EXPLOSION = "bomb.wav";
		public static String JOURNEY = "Long Journey.wav";
		
		private static BackgroundClip background;
		
		public static synchronized void playSound(final String name) {
			  new Thread(new Runnable() {
			    public void run() {
			      try {
			        Clip clip = AudioSystem.getClip();
			        AudioInputStream inputStream =AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "/Sounds/" + name));
			        clip.open(inputStream);
			        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			        float dB = (float) (Math.log(OptionsManager.volume / 100f) / Math.log(10.0) * 20.0);
			        gainControl.setValue(dB);
			        
			        clip.start(); 
			      } catch (Exception e) {
			        System.err.println(e);
			      }
			    }
			  }).start();
			}
		
		public static synchronized void playBackgroundMusic(final String name) {
			  new Thread(new Runnable() {
				    public void run() {
				      try {
				    	  Clip clip = AudioSystem.getClip();
				    	  background = new BackgroundClip(clip);
				        AudioInputStream inputStream =AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "/Sounds/" + name));
				        background.getClip().open(inputStream);
				        background.getClip().loop(0);
				        background.setVolume(OptionsManager.getVolume());
				        background.start();
				      } catch (Exception e) {
				        System.err.println(e);
				      }
				    }
				  }).start();
		}
		
		public static void stopBackgroundMusic() {
			background.stop();
		}
	}
	
}
