package game;

import java.io.File;

import tiles.ControlTile;
import tiles.ITile;
import tiles.InputTile;

/**
 * Game Puzzle
 * 
 * @author Austin Gray
 *
 */
public class Puzzle {

	private File file;
	private ITile[][] tiles;

	/**
	 * Creates a Puzzle
	 * 
	 * @param file  file that was opened
	 * @param tiles tiles that were read in from file
	 */
	public Puzzle(File file, ITile[][] tiles) {
		this.file = file;
		this.tiles = tiles;
	}

	/**
	 * get all tiles
	 * 
	 * @return tiles[][]
	 */
	public ITile[][] getTiles() {
		return tiles;
	}

	/**
	 * get rows
	 * 
	 * @return height of puzzle
	 */
	public int getRows() {
		return tiles.length;
	}

	/**
	 * get cols
	 * 
	 * @return width of puzzle
	 */
	public int getCols() {
		return tiles[0].length;
	}

	/**
	 * convert puzzle to String for save handling
	 * 
	 * @return String format of puzzle object
	 */
	public String puzzleToString() {
		String value = "";
		for (int i = 0; i < tiles.length; i++) {
			String line = "";
			for (int j = 0; j < tiles[i].length; j++) {
				line += tiles[i][j].saveString();
				if (j < tiles[i].length - 1) {
					line += ", ";
				}
			}
			value += line;
			if (i < tiles.length - 1) {
				value += System.getProperty("line.separator");
			}
		}
		return value;
	}

	/**
	 * Get name of puzzle
	 * 
	 * @return String name of puzzle
	 */
	public String getName() {
		return file.getName();
	}

	/**
	 * Flips the puzzle
	 */
	public void flip() {
		ITile[][] newTiles = new ITile[tiles[0].length][tiles.length];
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				ITile tile = newTiles[x][y] = tiles[y][x];
				if (tile.getClass() == ControlTile.class) {
					((ControlTile) tile).flip();
				} else if (tile.getClass() == InputTile.class) {
					((InputTile) tile).flip();
				}
			}
		}
		this.tiles = newTiles;
	}

}
