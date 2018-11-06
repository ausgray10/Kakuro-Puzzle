package tiles;

import java.awt.Color;

import java.awt.Font;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import main.Main;
import main.SoundManager;
import pages.*;

public class InputTile extends JButton implements ITile {
	
	private static final long serialVersionUID = 1L;
	
	private int xPos;
	private int yPos;
	private int value;
	private ControlTile rowTile;
	private ControlTile colTile;
	
	private JPopupMenu popupSelection;
	
	public enum Result{
		NONE,
		EQUAL,
		GREATER,
		LESS,
		DUPLICATE
	}
	
	private Result colResult = Result.NONE;
	private Result rowResult = Result.NONE;
	
	public InputTile(int x, int y) {
		super();
		this.xPos = x;
		this.yPos = y;
		this.value = 0;
		setup();
	}
	
	public InputTile(int x, int y, int value) {
		super();
		this.xPos = x;
		this.yPos = y;
		this.value = value;
		setup();
	}
	
	public void resetTile() {
		this.value = 0;
		this.updateTile();
		this.updateText();
	}
	
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
	
	public void setColTile(ControlTile tile) {
		colTile = tile;
	}
	public void setRowTile(ControlTile tile) {
		rowTile = tile;
	}
	
	public void colCorrect(Result result) {
		colResult = result;
		UpdateCorrect();
		}
	public void rowCorrect(Result result) {
		rowResult = result;
		UpdateCorrect();
	}
	
	private void UpdateCorrect() {
		if(colResult == Result.EQUAL && rowResult == Result.EQUAL) {
			this.setForeground(Color.GREEN);
		}
		if((colResult == Result.EQUAL || rowResult == Result.EQUAL) && ((colResult == Result.LESS || rowResult  == Result.LESS) || (colResult == Result.DUPLICATE || rowResult  == Result.DUPLICATE) || (colResult == Result.GREATER || rowResult  == Result.GREATER))) {
			this.setForeground(Color.YELLOW);
		}
		else if(colResult == Result.GREATER || rowResult == Result.GREATER) {
			this.setForeground(Color.RED);
		}
		else if(colResult == Result.DUPLICATE || rowResult == Result.DUPLICATE) {
			this.setForeground(Color.RED);
		}else if(colResult == Result.LESS ||rowResult == Result.LESS ) {
			this.setForeground(Color.RED);
		}
		else {
			this.setForeground(Color.GREEN);
		}
	}
	
	
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
				setMenuOptions();
				popupSelection.show(e.getComponent(), e.getX(), e.getY());
				popupSelection.setVisible(true);
			}
			public void mouseReleased(MouseEvent e) {
				popupSelection.setVisible(false);
			}
		});
		
		this.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				
			}
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					tile.setValue(0);
				}
			}

			public void keyTyped(KeyEvent arg0) {
				String key = String.valueOf(arg0.getKeyChar());
				if(tryParseInt(key)) {
					int parse = Integer.parseInt(key);
					if(parse > 0 && parse < 10) {
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
				int val = Math.min(tile.getWidth()/2, tile.getHeight()/2);
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
	
	public void updateTile() {
		rowTile.updateTile();
		colTile.updateTile();
	}
	
	private boolean tryParseInt(String value) {
		try {  
	         Integer.parseInt(value);  
	         return true;  
	      } catch (NumberFormatException e) {  
	         return false;  
	      } 
	}
	
	private void setValue(int value) {
		this.value = value;
		this.updateText();
		this.updateTile();
		SoundManager.playSound(SoundManager.SLIP);
		((Board)Main.GetCurrentPage()).CheckForGameOver();
	}
	
	private void setMenuOptions() {
		InputTile tile = this;
		popupSelection.removeAll();
		ArrayList<Integer> colValues = colTile.getPossibleMaxCol(this);
		ArrayList<Integer> rowValues = rowTile.getPossibleMaxRow(this);
		for(int i = 1; i < 10; i++) {
			if(colValues.contains(i) && rowValues.contains(i) && i < colTile.getColVal() && i < rowTile.getRowVal()) {
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
	
	public void flip() {
		ControlTile temp = this.rowTile;
		this.rowTile = this.colTile;
		this.colTile = temp;
		
	}
	
	public void updateText() {
		this.setText(this.value == 0 ? "" : String.valueOf(this.value));
	}

	@Override
	public String saveString() {
		return "w00" + String.format("%02d", value);
	}
	
}
