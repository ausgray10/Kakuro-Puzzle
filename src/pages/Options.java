package pages;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Main;
import main.OptionsManager;

public class Options extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public Options() {
		JColorChooser mainColorTheme = new JColorChooser(OptionsManager.mainColor);
		mainColorTheme.getSelectionModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				OptionsManager.mainColor = mainColorTheme.getColor();
			}
		});
		
		JButton exit = new JButton("Back");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.SetCurrentPage(new Menu());
			}
		});
		
		JSlider volumeSlider = new JSlider(0, 100, OptionsManager.volume);
		volumeSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				OptionsManager.volume = volumeSlider.getValue();
			}
		});
		
		
		this.add(mainColorTheme);
		this.add(volumeSlider);
		this.add(exit);
		
	}
}
