package tiles;

import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

import boards.Board;
import game.Handlers.*;
import game.Menu;

/**
 * Tile that handles Input from user
 * 
 * @author Austin Gray
 *
 */
public class InputTile extends JButton implements ITile {

	private int xPos;
	private int yPos;
	private int value;
	private ControlTile rowTile;
	private ControlTile colTile;

	private JPopupMenu popupSelection;

	public enum Result {
		NONE, EQUAL, GREATER, LESS, DUPLICATE
	}

	private Result colResult = Result.NONE;
	private Result rowResult = Result.NONE;

	/**
	 * Creates InputTile
	 * 
	 * @param x x position of tile
	 * @param y y position of tile
	 */
	public InputTile(int x, int y) {
		super();
		this.xPos = x;
		this.yPos = y;
		this.value = 0;
		setup();
	}

	/**
	 * Creates InputTile
	 * 
	 * @param x     x position of tile
	 * @param y     y position of tile
	 * @param value current value of tile
	 */
	public InputTile(int x, int y, int value) {
		super();
		this.xPos = x;
		this.yPos = y;
		this.value = value;
		setup();
	}

	/**
	 * reset the tile to default values
	 */
	public void resetTile() {
		this.value = 0;
		this.updateTile();
		this.updateText();
	}

	/**
	 * get current input of tile
	 * 
	 * @return int value
	 */
	public int getValue() {
		return value;
	}

	@Override
	public int getXPos() {
		return xPos;
	}

	@Override
	public int getYPos() {
		return yPos;
	}

	/**
	 * Sets column tile
	 * 
	 * @param tile column ControlTile
	 */
	public void setColTile(ControlTile tile) {
		colTile = tile;
	}

	/**
	 * Sets row tile
	 * 
	 * @param tile row ControlTile
	 */
	public void setRowTile(ControlTile tile) {
		rowTile = tile;
	}

	/**
	 * Checks if column is correct
	 * 
	 * @param result enum result
	 */
	public void colCorrect(Result result) {
		colResult = result;
		UpdateCorrect();
	}

	/**
	 * Checks if row is correct
	 * 
	 * @param result enum result
	 */
	public void rowCorrect(Result result) {
		rowResult = result;
		UpdateCorrect();
	}

	/**
	 * Update all results to calculate color of the text
	 */
	private void UpdateCorrect() {
		if (colResult == Result.EQUAL && rowResult == Result.EQUAL) {
			this.setForeground(Color.GREEN);
		}
		if ((colResult == Result.EQUAL || rowResult == Result.EQUAL)
				&& ((colResult == Result.LESS || rowResult == Result.LESS)
						|| (colResult == Result.DUPLICATE || rowResult == Result.DUPLICATE)
						|| (colResult == Result.GREATER || rowResult == Result.GREATER))) {
			this.setForeground(Color.YELLOW);
		} else if (colResult == Result.GREATER || rowResult == Result.GREATER) {
			this.setForeground(Color.RED);
		} else if (colResult == Result.DUPLICATE || rowResult == Result.DUPLICATE) {
			this.setForeground(Color.RED);
		} else if (colResult == Result.LESS || rowResult == Result.LESS) {
			this.setForeground(Color.RED);
		} else {
			this.setForeground(Color.GREEN);
		}
	}

	/**
	 * Sets up InputTile listeners
	 */
	private void setup() {
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setHorizontalAlignment(CENTER);
		this.updateText();
		InputTile tile = (InputTile) this;

		popupSelection = new JPopupMenu();

		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				if (tile.isEnabled()) {
					setMenuOptions();
					popupSelection.show(e.getComponent(), e.getX(), e.getY());
					popupSelection.setVisible(true);
				}
			}

			public void mouseReleased(MouseEvent e) {
				popupSelection.setVisible(false);
			}
		});

		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {

			}

			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					tile.setValue(0);
				}
			}

			public void keyTyped(KeyEvent arg0) {
				String key = String.valueOf(arg0.getKeyChar());
				if (tryParseInt(key)) {
					int parse = Integer.parseInt(key);
					if (parse > 0 && parse < 10) {
						tile.setValue(parse);
					}
				}
			}
		});

		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				int val = Math.min(tile.getWidth() / 2, tile.getHeight() / 2);
				Font font = new Font(tile.getFont().getName(), Font.PLAIN, val);
				tile.setFont(font);
				tile.repaint();

			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	/**
	 * Update both row and column ControlTiles
	 */
	public void updateTile() {
		rowTile.updateTile();
		colTile.updateTile();
	}

	/**
	 * Attempt to parse integer
	 * 
	 * @param value String value
	 * @return true if can be parsed
	 */
	private boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Sets InputTile value
	 * 
	 * @param value new value
	 */
	private void setValue(int value) {
		this.value = value;
		this.updateText();
		this.updateTile();
		SoundManager.playSound(SoundManager.SLIP);
		Menu.getBoard().checkForGameOver();
	}

	/**
	 * Create menu items for display
	 */
	private void setMenuOptions() {
		InputTile tile = this;
		popupSelection.removeAll();
		Integer[] colValues = colTile.getPossibleMaxCol();
		Integer[] rowValues = rowTile.getPossibleMaxRow();
		for (int i = 0; i < 10; i++) {
			if (colValues[i] > 0 && rowValues[i] > 0) {
				JMenuItem item = new JMenuItem(String.valueOf(i));
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						tile.setValue(Integer.parseInt(item.getText()));
					}
				});
				popupSelection.add(item);
			}
		}
	}

	/**
	 * flip InputTile ControlTiles
	 */
	public void flip() {
		ControlTile temp = this.rowTile;
		this.rowTile = this.colTile;
		this.colTile = temp;

	}

	/**
	 * Update the text that is shown
	 */
	public void updateText() {
		this.setText(this.value == 0 ? "" : String.valueOf(this.value));
	}

	@Override
	public String saveString() {
		return "w00" + String.format("%02d", value);
	}

	@Override
	public void destroy() {

	}
}
