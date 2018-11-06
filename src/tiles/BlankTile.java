package tiles;

import java.awt.Color;

import javax.swing.*;

import main.OptionsManager;
import pages.Options;

public class BlankTile extends JPanel implements ITile {
	
	private static final long serialVersionUID = 1L;
	private int xPos;
	private int yPos;
	
	public BlankTile(int x, int y) {
		super();
		this.xPos = x;
		this.yPos = y;
		
		this.setBackground(OptionsManager.mainColor);
		
	}
	
	public int getXPos() {
		return this.xPos;
	}
	public int getYPos() {
		return this.yPos;
	}

	@Override
	public String saveString() {
		return "e0000";
	}
	
}
