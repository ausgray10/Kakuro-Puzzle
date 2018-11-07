package tiles;

import java.awt.Color;

public interface ITile {

	public abstract int getXPos();
	public abstract int getYPos();
	public abstract String saveString();
	public abstract void destroy();
	
}
