package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pages.Board;
import pages.Menu;
import pages.Options;
import tiles.BlankTile;
import tiles.ControlTile;
import tiles.ITile;
import tiles.InputTile;

public class Main extends JFrame {
	private static Main main;
	
	private JPanel panel;
	
	
	public static void main(String[] args) {
		OptionsManager.Load();
		new Main();
	}
	
	public static void SetCurrentPage(JPanel panel) {
		main.panel = panel;
		main.getContentPane().removeAll();
		main.getContentPane().add(main.panel);
		main.validate();
	}
	
	public static JPanel GetCurrentPage() {
		return main.panel;
	}
	
	public Main() {
		this.main = this;
		setTitle("Kakuro Puzzle");
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		SetCurrentPage(new Menu());
	}
}
