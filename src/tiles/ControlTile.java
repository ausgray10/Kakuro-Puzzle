package tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.*;

import game.Handlers.*;
import tiles.InputTile.Result;

public class ControlTile extends JComponent implements ITile, ColorChangeListener {

	private static final long serialVersionUID = 1L;
	private int xPos;
	private int yPos;
	private int rowVal;
	private int colVal;
	private ArrayList<InputTile> colTiles;
	private ArrayList<InputTile> rowTiles;
	private boolean isCorrect = false;
	
	public ControlTile(int x, int y, int colVal, int rowVal) {
		super();
		this.xPos = x;
		this.yPos = y;
		this.rowVal = rowVal;
		this.colVal = colVal;
		this.colTiles = new ArrayList<InputTile>();
		this.rowTiles = new ArrayList<InputTile>();
		OptionsManager.onMainColorChange.add(this);
	}
	
	public void destroy() {
		OptionsManager.onMainColorChange.remove(this);
	}
	
	public int getXPos() {
		return this.xPos;
		
	}
	public int getYPos() {
		return this.yPos;
	}
	
	@Override
	public void onColorChange(Color color) {
		this.repaint();
	}
	
	public int getRowVal() {
		return rowVal;
	}
	public int getColVal() {
		return colVal;
	}
	
	public void updateTile() {
		boolean col = updateColTiles();
		boolean row = updateRowTiles();
		isCorrect = row && col;
	}
	
	public boolean isThisCorrect() {
		return isCorrect;
	}
	
	private boolean updateColTiles() {
		int value = 0;
		boolean hasNone = false;
		boolean hasDup = false;
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i < colTiles.size(); i++) {
			int add = colTiles.get(i).getValue();
			if(add == 0) {
				hasNone = true;
			}
			else if(values.contains(add)) {
				hasDup = true;
			}else {
				values.add(add);
			}
			value += add;
		}
		Result result = Result.NONE;
		if(hasDup) {
			result = Result.DUPLICATE;
		}else if(value > colVal) {
			result = Result.GREATER;
		}else if(hasNone) {
			result = Result.NONE;
		}else if(value == colVal) {
			result = Result.EQUAL;
		}
		else if(value < colVal) {
			result = Result.LESS;
		}
		for(int i = 0; i < colTiles.size(); i++) {
			colTiles.get(i).colCorrect(result);
		}
		return value == colVal && !hasNone && !hasDup;
	}
	private boolean updateRowTiles() {
		int value = 0;
		boolean hasNone = false;
		boolean hasDup = false;
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i < rowTiles.size(); i++) {
			int add = rowTiles.get(i).getValue();
			if(add == 0) {
				hasNone = true;
			}
			else if(values.contains(add)) {
				hasDup = true;
			}else {
				values.add(add);
			}
			value += add;
		}
		Result result = Result.NONE;
		if(hasDup) {
			result = Result.DUPLICATE;
		}else if(value > rowVal) {
			result = Result.GREATER;
		}else if(hasNone) {
			result = Result.NONE;
		}else if(value == rowVal) {
			result = Result.EQUAL;
		}
		else if(value < rowVal) {
			result = Result.LESS;
		}
		for(int i = 0; i < rowTiles.size(); i++) {
			rowTiles.get(i).rowCorrect(result);
		}
		return value == rowVal && !hasNone && !hasDup;
	}
	
	public void addRowInputTile(InputTile tile) {
		this.rowTiles.add(tile);
	}
	
	public void addColInputTile(InputTile tile) {
		this.colTiles.add(tile);
	}
	
	private String colToString() {
		return colVal == 0 ? "" : String.valueOf(colVal);
	}
	private String rowToString() {
		return rowVal == 0 ? "" : String.valueOf(rowVal);
	}
	
	public String toString() {
		return colToString() + "\\" + rowToString();
	}
	
	@Override
	public void paintComponent(Graphics graph) {
		super.paintComponent(graph);
		this.setBackground(Color.WHITE);
		Graphics2D g = (Graphics2D)graph;
		
		g.setColor(OptionsManager.getColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(ContrastColor(OptionsManager.getColor()));
		g.setStroke(new BasicStroke(1));
		g.drawLine(0, 0, this.getWidth(), this.getHeight());
		
		int val = Math.min(this.getWidth()/3, this.getHeight()/3);
		g.setFont(new Font(this.getFont().getName(), Font.PLAIN, val));
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(colToString(), metrics.stringWidth(String.valueOf(colVal))/2, this.getHeight() - val/2);
		g.drawString(rowToString(), this.getWidth() - metrics.stringWidth(String.valueOf(rowVal)), this.getHeight()/2 - val/2);
	}
	
	private Color ContrastColor(Color color)
	{
		if(color.getRed() + color.getGreen() + color.getBlue() < 300) {
			return Color.WHITE;
		}else {
			return Color.BLACK;
		}
	}
	
	public ArrayList<Integer> getPossibleMaxCol(InputTile tile){
		
		
		int sum = 0;
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i < colTiles.size(); i++) {
			InputTile _tile = colTiles.get(i);
			if(_tile != tile) {
				values.add(_tile.getValue());
				sum += _tile.getValue();
			}
		}
		ArrayList<Integer> returnValues = new ArrayList<Integer>();
		for(int i = 1; i <= Math.min(9, colVal - sum); i++) {
			if(!values.contains(i)) {
				returnValues.add(i);
			}
		}
		return returnValues;
	}
	
	public ArrayList<Integer> getPossibleMaxRow(InputTile tile){
		int sum = 0;
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i < rowTiles.size(); i++) {
			InputTile _tile = rowTiles.get(i);
			if(_tile != tile) {
				values.add(_tile.getValue());
				sum += _tile.getValue();
			}
		}
		ArrayList<Integer> returnValues = new ArrayList<Integer>();
		for(int i = 1; i <= Math.min(9, rowVal - sum); i++) {
			if(!values.contains(i)) {
				returnValues.add(i);
			}
		}
		return returnValues;
	}
	
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

}
