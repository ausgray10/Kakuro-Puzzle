package listeners;

import java.awt.Color;

public interface ColorChangeListener {

	public abstract void colorChangeCreate();
	public abstract void colorChangeDestroy();
	public abstract void onColorChange(Color color);
	
}
