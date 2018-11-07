package game;

import java.io.File;

import tiles.ControlTile;
import tiles.ITile;
import tiles.InputTile;

public class Puzzle {

	private File file;
	private ITile[][] tiles;
	
	public Puzzle(File file, ITile[][] tiles) {
		this.file = file;
		this.tiles = tiles;
	}
	
	public ITile[][] getTiles(){
		return tiles;
	}
	
	public int getRows() {
		return tiles.length;
	}
	public int getCols() {
		return tiles[0].length;
	}
	
	public String puzzleToString() {
		String value = "";
		for(int i = 0; i < tiles.length; i++) {
			String line = "";
			for(int j = 0; j < tiles[i].length; j++) {
				line += tiles[i][j].saveString();
				if(j < tiles[i].length - 1) {
					line += ", ";
				}
			}
			value += line;
			if(i < tiles.length - 1) {
				value += System.getProperty("line.separator");
			}
		}
		return value;
	}
	
	public String getName() {
		return file.getName();
	}
	
	public void flip() {
		ITile[][] newTiles = new ITile[tiles[0].length][tiles.length];
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[y].length; x++) {
				ITile tile = newTiles[x][y] = tiles[y][x];
				if(tile.getClass() == ControlTile.class) {
					((ControlTile)tile).flip();
				}			
				else if(tile.getClass() == InputTile.class) {
					((InputTile)tile).flip();
				}
			}
		}
		this.tiles = newTiles;
	}
	
}

