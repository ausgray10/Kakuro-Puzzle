package boards;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import game.Handlers;
import game.Puzzle;
import game.Handlers.*;
import tiles.*;
/**
 * Main Game Board
 * @author Austin Gray
 *
 */
public class Board extends JPanel {
	
	protected Puzzle puzzle;
	protected ArrayList<ControlTile> controls;
	protected ArrayList<InputTile> inputs;
	
	private GridLayout gameLayout;

	/**
	 * Creats a new Board
	 * @param puzzle puzzle to use for board
	 */
	public Board(Puzzle puzzle) {
		this.puzzle = puzzle;
		gameLayout = new GridLayout(puzzle.getRows(), puzzle.getCols());
		gameLayout.setHgap(2);
		gameLayout.setVgap(2);
		this.setLayout(gameLayout);
		
		controls = new ArrayList<ControlTile>();
		inputs = new ArrayList<InputTile>();
		ControlTile[] colControlTiles = new ControlTile[puzzle.getCols()];
		for (int y = 0; y < puzzle.getRows(); y++) {
			ControlTile rowTile = null;
			for (int x = 0; x < puzzle.getCols(); x++) {
				ITile tile = puzzle.getTiles()[y][x];
				if(tile.getClass() == ControlTile.class) {
					controls.add((ControlTile) tile);
					colControlTiles[x] = rowTile = (ControlTile) tile;
				}
				else if(tile.getClass() == InputTile.class) {
					inputs.add((InputTile) tile);
					colControlTiles[x].addColInputTile((InputTile) tile);
					((InputTile)tile).setColTile(colControlTiles[x]);
					rowTile.addRowInputTile((InputTile) tile);
					((InputTile)tile).setRowTile(rowTile);
					((InputTile)tile).updateTile();
				}
				this.add((JComponent) tile);
			}
		}
	}
	/**
	 * Update the Puzzle
	 */
	public void updatePuzzle() {
		for(int i = 0; i < controls.size(); i++) {
			controls.get(i).updateTile();
		}
	}
	/**
	 * Reset the Puzzle
	 */
	public void resetPuzzle() {
		for(int i = 0; i < inputs.size(); i++) {
			inputs.get(i).resetTile();
			inputs.get(i).setEnabled(true);
		}
	}
	
	public void lockBoard() {
		for(int i = 0; i < inputs.size(); i++) {
			inputs.get(i).setEnabled(false);
		}
	}
	
	/**
	 * Save Puzzle to text file
	 */
	public void savePuzzle() {
		FileManager.SaveFile(puzzle);
	}
	/**
	 * Flips the puzzle
	 */
	public void flipPuzzle() {
		puzzle.flip();
		this.removeAll();
		for(int y = 0; y < puzzle.getRows(); y++) {
			for(int x = 0; x < puzzle.getCols(); x++) {
				this.add((JComponent) puzzle.getTiles()[y][x]);
			}	
		}
		gameLayout.setColumns(puzzle.getCols());
		gameLayout.setRows(puzzle.getRows());
		this.validate();
	}
	/**
	 * Check if Board has game over
	 */
	public void checkForGameOver() {
		
		for(int i = 0; i < controls.size(); i++) {
			if(!controls.get(i).isThisCorrect()) {
				return;
			}
		}
		onGameOver();
	}
	/**
	 * Called on GameOver event
	 */
	protected void onGameOver() {
		for(int i = 0; i < inputs.size(); i++) {
			inputs.get(i).setEnabled(false);
		}
		SoundManager.playSound(SoundManager.PARTYHORN);
	}
	/**
	 * Cleans up pointers on the Board
	 */
	public void destroy() {
		for(int y = 0; y < puzzle.getRows(); y++) {
			for(int x = 0; x < puzzle.getCols(); x++) {
				ITile tile = puzzle.getTiles()[y][x];
				tile.destroy();
			}
		}
	}
}
