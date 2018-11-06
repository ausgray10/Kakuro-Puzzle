package pages;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.FileManager;
import main.Main;
import main.OptionsManager;
import main.Puzzle;
import tiles.BlankTile;
import tiles.ControlTile;
import tiles.ITile;
import tiles.InputTile;

public class Menu extends JPanel {
	
	public Menu() {
		
		JButton[] buttons = new JButton[] {
				 new JButton("FreeMode"),
				 new JButton("TimedMode"),
				 new JButton("CruelMode"),
					new JButton("Options"),
					new JButton("Exit")
		};
		
		buttons[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Puzzle puzzle = FileManager.OpenFile();
				if(puzzle != null)
					Main.SetCurrentPage(new Board(puzzle, false, false));
			}
		});
		buttons[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Puzzle puzzle = FileManager.OpenFile();
				if(puzzle != null)
					Main.SetCurrentPage(new Board(puzzle, true, false));
			}
		});
		buttons[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Puzzle puzzle = FileManager.OpenFile();
				if(puzzle != null)
					Main.SetCurrentPage(new Board(puzzle, true, true));
			}
		});
		buttons[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main.SetCurrentPage(new Options());
			}
		});
		buttons[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OptionsManager.Save();
				System.exit(0);
			}
		});
		
		GridLayout layout = new GridLayout(buttons.length, 1);
		this.setLayout(layout);
		
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].setAlignmentX(CENTER_ALIGNMENT);
			this.add(buttons[i]);
		}
		JPanel panel = this;
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
				for(int i = 0; i < buttons.length; i++) {
					int val = Math.min(panel.getWidth()/2, panel.getHeight()/(buttons.length + 2));
					Font font = new Font(buttons[i].getFont().getName(), Font.PLAIN, val);
					buttons[i].setFont(font);
				}
				panel.repaint();
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
