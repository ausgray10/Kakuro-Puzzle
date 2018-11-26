package tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import game.Handlers;
import game.Handlers.*;
import listeners.ColorChangeListener;
import tiles.InputTile.Result;

/**
 * Control Tile row/col counter
 * 
 * @author Austin Gray
 *
 */
public class ControlTile extends JComponent implements ITile, ColorChangeListener {

	private int xPos;
	private int yPos;
	private int rowVal;
	private int colVal;
	private ArrayList<InputTile> colTiles;
	private ArrayList<InputTile> rowTiles;
	private boolean isCorrect = false;

	/**
	 * Creates a new ControlTile
	 * 
	 * @param x      x position of tile
	 * @param y      y position of tile
	 * @param colVal value for the column numbers
	 * @param rowVal value for the row numbers
	 */
	public ControlTile(int x, int y, int colVal, int rowVal) {
		super();
		this.xPos = x;
		this.yPos = y;
		this.rowVal = rowVal;
		this.colVal = colVal;
		this.colTiles = new ArrayList<InputTile>();
		this.rowTiles = new ArrayList<InputTile>();
		colorChangeCreate();
	}

	@Override
	public void destroy() {
		colorChangeDestroy();
	}

	@Override
	public int getXPos() {
		return this.xPos;

	}

	@Override
	public int getYPos() {
		return this.yPos;
	}

	@Override
	public void onColorChange(Color color) {
		this.repaint();
	}

	/**
	 * gets row value
	 * 
	 * @return row value
	 */
	public int getRowVal() {
		return rowVal;
	}

	/**
	 * gets column value
	 * 
	 * @return column value
	 */
	public int getColVal() {
		return colVal;
	}

	/**
	 * Update all tiles in both rows and columns
	 */
	public void updateTile() {
		boolean col = updateColTiles();
		boolean row = updateRowTiles();
		isCorrect = row && col;
	}

	/**
	 * Chechs if ControlTile row and column adds up to correct value
	 * 
	 * @return true/false
	 */
	public boolean isThisCorrect() {
		return isCorrect;
	}

	/**
	 * Updates all column tiles
	 * 
	 * @return true if column ruleset met
	 */
	private boolean updateColTiles() {
		int value = 0;
		boolean hasNone = false;
		boolean hasDup = false;
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < colTiles.size(); i++) {
			int add = colTiles.get(i).getValue();
			if (add == 0) {
				hasNone = true;
			} else if (values.contains(add)) {
				hasDup = true;
			} else {
				values.add(add);
			}
			value += add;
		}
		Result result = Result.NONE;
		if (hasDup) {
			result = Result.DUPLICATE;
		} else if (value > colVal) {
			result = Result.GREATER;
		} else if (hasNone) {
			result = Result.NONE;
		} else if (value == colVal) {
			result = Result.EQUAL;
		} else if (value < colVal) {
			result = Result.LESS;
		}
		for (int i = 0; i < colTiles.size(); i++) {
			colTiles.get(i).colCorrect(result);
		}
		return value == colVal && !hasNone && !hasDup;
	}

	/**
	 * Updates all row tiles
	 * 
	 * @return true if row ruleset met
	 */
	private boolean updateRowTiles() {
		int value = 0;
		boolean hasNone = false;
		boolean hasDup = false;
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < rowTiles.size(); i++) {
			int add = rowTiles.get(i).getValue();
			if (add == 0) {
				hasNone = true;
			} else if (values.contains(add)) {
				hasDup = true;
			} else {
				values.add(add);
			}
			value += add;
		}
		Result result = Result.NONE;
		if (hasDup) {
			result = Result.DUPLICATE;
		} else if (value > rowVal) {
			result = Result.GREATER;
		} else if (hasNone) {
			result = Result.NONE;
		} else if (value == rowVal) {
			result = Result.EQUAL;
		} else if (value < rowVal) {
			result = Result.LESS;
		}
		for (int i = 0; i < rowTiles.size(); i++) {
			rowTiles.get(i).rowCorrect(result);
		}
		return value == rowVal && !hasNone && !hasDup;
	}

	/**
	 * adds InputTile to rowTiles
	 * 
	 * @param tile InputTile to add to rowTiles
	 */
	public void addRowInputTile(InputTile tile) {
		this.rowTiles.add(tile);
	}

	/**
	 * adds InputTile to colTiles
	 * 
	 * @param tile InputTile to add to colTiles
	 */
	public void addColInputTile(InputTile tile) {
		this.colTiles.add(tile);
	}

	/**
	 * Converts colVal to String for drawing
	 * 
	 * @return String of colVal
	 */
	private String colToString() {
		return colVal == 0 ? "" : String.valueOf(colVal);
	}

	/**
	 * Converts rowVal to String for drawing
	 * 
	 * @return String of rowVal
	 */
	private String rowToString() {
		return rowVal == 0 ? "" : String.valueOf(rowVal);
	}

	@Override
	public String toString() {
		return colToString() + "\\" + rowToString();
	}

	@Override
	public void paintComponent(Graphics graph) {
		super.paintComponent(graph);
		this.setBackground(Color.WHITE);
		Graphics2D g = (Graphics2D) graph;

		g.setColor(OptionsManager.currentColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.setColor(ContrastColor(OptionsManager.currentColor));
		g.setStroke(new BasicStroke(1));
		g.drawLine(0, 0, this.getWidth(), this.getHeight());

		int val = Math.min(this.getWidth() / 3, this.getHeight() / 3);
		g.setFont(new Font(this.getFont().getName(), Font.PLAIN, val));
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(colToString(), metrics.stringWidth(String.valueOf(colVal)) / 2, this.getHeight() - val / 2);
		g.drawString(rowToString(), this.getWidth() - metrics.stringWidth(String.valueOf(rowVal)),
				this.getHeight() / 2 - val / 2);
	}

	/**
	 * Changes the text color based on Contrast
	 * 
	 * @param color current background color
	 * @return black/white Color contrast
	 */
	private Color ContrastColor(Color color) {
		if (color.getRed() + color.getGreen() + color.getBlue() < 300) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}

	/**
	 * list of Integers that can be used in column tiles
	 * 
	 * @return Array of Integers
	 */
	public Integer[] getPossibleMaxCol() {

		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) {
			numbers.add(i);
		}

		ArrayList<Integer> found = coalesce(findNums(numbers, colVal, colTiles.size()));

		Integer[] values = new Integer[10];
		for (int i = 0; i < 10; i++) {
			values[i] = 0;
			if (found.contains(i)) {
				values[i] += 1;
			}
		}
		return values;
	}

	/**
	 * list of Integers that can be used in row tiles
	 * 
	 * @return Array of Integers
	 */
	public Integer[] getPossibleMaxRow() {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) {
			numbers.add(i);
		}

		ArrayList<Integer> found = coalesce(findNums(numbers, rowVal, rowTiles.size()));

		Integer[] values = new Integer[10];
		for (int i = 0; i < 10; i++) {
			values[i] = 0;
			if (found.contains(i)) {
				values[i] += 1;
			}
		}
		return values;
	}

	/**
	 * Find Sum values of row/column
	 * 
	 * @param numbers     all possible numbers
	 * @param target      target value
	 * @param groupLength length of group
	 * @param partial     current array
	 * @param resultList  final result
	 */
	private void subset_sum(ArrayList<Integer> numbers, int target, int groupLength, ArrayList<Integer> partial,
			ArrayList<ArrayList<Integer>> resultList) {
		int s = 0;
		for (int i : partial) {
			s += i;
		}
		if (s == target && partial.size() == groupLength) {
			resultList.add(partial);
		}
		for (int i = 0; i < numbers.size(); i++) {
			int n = numbers.get(i);
			ArrayList<Integer> remaining = new ArrayList<Integer>(numbers.subList(i + 1, numbers.size()));
			ArrayList<Integer> newPartial = new ArrayList<Integer>(partial);
			newPartial.add(n);
			subset_sum(remaining, target, groupLength, newPartial, resultList);
		}
	}

	/**
	 * Finds Number values of row/column
	 * 
	 * @param numbers     available numbers
	 * @param target      target value
	 * @param groupLength length of group
	 * @return All available pairs
	 */
	private ArrayList<ArrayList<Integer>> findNums(ArrayList<Integer> numbers, int target, int groupLength) {
		ArrayList<ArrayList<Integer>> resultList = new ArrayList<ArrayList<Integer>>();
		subset_sum(numbers, target, groupLength, new ArrayList<Integer>(), resultList);
		return resultList;
	}

	/**
	 * Convert ArrayList of ArrayList of Integers to single list
	 * 
	 * @param listOfLists all number pairs
	 * @return single ArrayList of Integers
	 */
	private ArrayList<Integer> coalesce(ArrayList<ArrayList<Integer>> listOfLists) {
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		for (ArrayList<Integer> eachList : listOfLists) {
			for (Integer eachItem : eachList) {
				if (!resultList.contains(eachItem)) {
					resultList.add(eachItem);
				}
			}
		}
		resultList.sort((Integer a, Integer b) -> a.compareTo(b));
		return resultList;
	}

	/**
	 * Flips ControlTile values
	 */
	public void flip() {
		int temp = this.rowVal;
		ArrayList<InputTile> tempArray = rowTiles;
		this.rowVal = this.colVal;
		this.colVal = temp;
		this.rowTiles = this.colTiles;
		this.colTiles = tempArray;
	}

	@Override
	public String saveString() {
		return "b" + String.format("%02d", colVal) + String.format("%02d", rowVal);
	}

	@Override
	public void colorChangeCreate() {
		Handlers.OptionsManager.colorChangeArray.add(this);
		
	}

	@Override
	public void colorChangeDestroy() {
		Handlers.OptionsManager.colorChangeArray.remove(this);
	}

}
