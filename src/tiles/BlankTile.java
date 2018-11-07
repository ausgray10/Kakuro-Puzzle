package tiles;

import java.awt.Color;
import game.*;
import game.Handlers.*;

import javax.swing.*;

public class BlankTile extends JPanel implements ITile, ColorChangeListener   {
	
	private static final long serialVersionUID = 1L;
	private int xPos;
	private int yPos;
	
	public BlankTile(int x, int y) {
		super();
		this.xPos = x;
		this.yPos = y;
		OptionsManager.onMainColorChange.add(this);
		this.setBackground(OptionsManager.getColor());
		
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
	public String saveString() {
		return "e0000";
	}

	@Override
	public void onColorChange(Color color) {
		this.setBackground(color);
	}
	
}
