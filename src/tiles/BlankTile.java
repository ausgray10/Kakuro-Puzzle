package tiles;

import java.awt.Color;
import game.*;
import game.Handlers.*;
import listeners.ColorChangeListener;

import javax.swing.*;

/**
 * Tile with no information
 * 
 * @author Austin Gray
 *
 */
public class BlankTile extends JPanel implements ITile, ColorChangeListener {

	private int xPos;
	private int yPos;

	/**
	 * Creats a blank tile
	 * 
	 * @param x x position of tile
	 * @param y y position of tile
	 */
	public BlankTile(int x, int y) {
		super();
		this.xPos = x;
		this.yPos = y;
		this.setBackground(OptionsManager.currentColor);
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
	public String saveString() {
		return "e0000";
	}

	@Override
	public void onColorChange(Color color) {
		this.setBackground(color);
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
