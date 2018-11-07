package game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import boards.Board;
import boards.CruelBoard;
import boards.TimedBoard;
import game.Handlers.*;

public class Menu extends JFrame{
	private static Menu menu;
	
	private Board current = null;
	private JMenuBar menuBar;
	
	public Menu(JMenuBar menuBar) {
		this.menuBar = menuBar;
		setTitle("Kakuro Puzzle");
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(menuBar);
		setVisible(true);
	}
	
	public static void addMenuItem(JComponent c) {
		menu.menuBar.add(c);
	}
	public static void removeMenuItem(JComponent c) {
		menu.menuBar.remove(c);
		menu.menuBar.repaint();
	}
	
	public static void setBoard(Board board) {
		if(menu.current != null)
			menu.current.destroy();
		menu.current = board;
		menu.getContentPane().removeAll();
		menu.getContentPane().add(board);
		menu.validate();
	}
	
	public static Board getBoard() {
		return menu.current;
	}
	
	//STATIC DATA
	
	public static void main(String[] args) {
		
		OptionsManager.Load();
		SoundManager.playBackgroundMusic(SoundManager.JOURNEY);
		JMenuBar menuBar = new JMenuBar();
		
		//Menu Game
		JMenu menuGame = new JMenu("Game");
		JMenuItem game_load = new JMenuItem("Load");
		game_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Puzzle puzzle = FileManager.OpenFile();
				if(puzzle != null)
					setBoard(new Board(puzzle));
			}
		});
		JMenuItem game_save = new JMenuItem("Save");
		game_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getBoard() != null && getBoard().getClass() == Board.class) {
					getBoard().savePuzzle();
				}
			}
		});
		JMenuItem game_reset = new JMenuItem("Reset");
		game_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getBoard() != null && getBoard().getClass() == Board.class) {
					getBoard().resetPuzzle();
				}
			}
		});
		JMenuItem game_flip = new JMenuItem("Flip");
		game_flip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getBoard() != null && getBoard().getClass() == Board.class) {
					getBoard().flipPuzzle();
				}
			}
		});
		JMenuItem game_exit = new JMenuItem("Exit");
		game_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OptionsManager.Save();
				System.exit(0);
			}
		});
		menuGame.add(game_load);
		menuGame.add(game_save);
		menuGame.add(game_reset);
		menuGame.add(game_flip);
		menuGame.add(game_exit);
		//Menu Game End
		//Menu TimedMode
		JMenu menuTimedMode = new JMenu("TimedMode");
		JMenuItem timedMode_load = new JMenuItem("Load");
		timedMode_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Puzzle puzzle = FileManager.OpenFile();
				if(puzzle != null)
					setBoard(new TimedBoard(puzzle));
			}
		});
		JMenuItem timedMode_reset = new JMenuItem("Reset");
		timedMode_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getBoard() != null && getBoard().getClass() == TimedBoard.class) {
					getBoard().resetPuzzle();
				}
			}
		});
		
		menuTimedMode.add(timedMode_load);
		menuTimedMode.add(timedMode_reset);
		//Menu TimedMode End
		//Menu CruelMode
		JMenu menuCruelMode = new JMenu("CruelMode");
		JMenuItem cruelMode_load = new JMenuItem("Load");
		cruelMode_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Puzzle puzzle = FileManager.OpenFile();
				if(puzzle != null)
					setBoard(new CruelBoard(puzzle));
			}
		});
		JMenuItem cruelMode_reset = new JMenuItem("Reset");
		cruelMode_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getBoard() != null && getBoard().getClass() == CruelBoard.class) {
					getBoard().resetPuzzle();
				}
			}
		});
		
		menuCruelMode.add(cruelMode_load);
		menuCruelMode.add(cruelMode_reset);
		//Menu CruelMode End
		
		//Menu Settings
		
		JMenu menuSettings = new JMenu("Settings");
		
		JMenuItem menuSettings_theme = new JMenuItem("Theme");
		menuSettings_theme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel popup = new JPanel();
				Color startingColor = OptionsManager.getColor();
				JColorChooser chooser = new JColorChooser(startingColor);
				chooser.getSelectionModel().addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						OptionsManager.setColor(chooser.getColor());
					}
				});
				popup.add(chooser);
				int result = JOptionPane.showConfirmDialog(null, popup, "Theme Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(result == JOptionPane.OK_OPTION) {
					OptionsManager.setColor(chooser.getColor());
					OptionsManager.Save();
				}else {
					OptionsManager.setColor(startingColor);
				}
			}
		});
		JMenuItem menuSettings_sound = new JMenuItem("Sound");
		menuSettings_sound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel popup = new JPanel();
				int startVolume = OptionsManager.getVolume();
				JSlider chooser = new JSlider(0, 100, startVolume);
				chooser.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						OptionsManager.setVolume(chooser.getValue());
					}
				});
				popup.add(chooser);
				int result = JOptionPane.showConfirmDialog(null, popup, "Sound Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(result == JOptionPane.OK_OPTION) {
					OptionsManager.setVolume(chooser.getValue());
					OptionsManager.Save();
				}else {
					OptionsManager.setVolume(startVolume);
				}
			}
		});
		
		
		menuSettings.add(menuSettings_theme);
		menuSettings.add(menuSettings_sound);
		//Menu Settings End
		
		menuBar.add(menuGame);
		menuBar.add(menuTimedMode);
		menuBar.add(menuCruelMode);
		menuBar.add(menuSettings);
		
		menu = new Menu(menuBar);
	}
}
