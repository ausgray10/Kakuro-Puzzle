package tiles;

import java.awt.Color;

/**
 * Tile interface
 * 
 * @author Austin Gray
 *
 */
public interface ITile {

	/**
	 * gets tile x position
	 * 
	 * @return x position of tile
	 */
	public abstract int getXPos();

	/**
	 * gets tile y position
	 * 
	 * @return y position of tile
	 */
	public abstract int getYPos();

	/**
	 * converts tile to string for save handling
	 * 
	 * @return tile as String
	 */
	public abstract String saveString();

	/**
	 * Cleans up pointers
	 */
	public abstract void destroy();

}
