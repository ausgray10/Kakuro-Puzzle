package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import boards.Board;
import boards.CruelBoard;
import boards.TimedBoard;
import game.Handlers.*;

/**
 * Main Menu of Game
 * 
 * @author Austin Gray
 *
 */
public class Menu extends JFrame{
	private static Menu menu;
	
	private Board current = null;
	private JMenuBar menuBar;
	
	/**
	 * Creats the Menu
	 * @param menuBar menu bar
	 */
	public Menu(JMenuBar menuBar) {
		this.menuBar = menuBar;
		setTitle("Kakuro Puzzle");
		setSize(800, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(menuBar);
		setVisible(true);
	}
	
	/**
	 * addMenuItem to menu
	 * @param c component to add
	 */
	public static void addMenuItem(JComponent c) {
		menu.menuBar.add(c);
	}
	/**
	 * removeMenuItem from menu
	 * @param c component to remove
	 */
	public static void removeMenuItem(JComponent c) {
		menu.menuBar.remove(c);
		menu.menuBar.repaint();
	}
	/**
	 * Set current board for menu
	 * @param board current board
	 */
	public static void setBoard(Board board) {
		if(menu.current != null)
			menu.current.destroy();
		menu.current = board;
		menu.getContentPane().removeAll();
		menu.getContentPane().add(board);
		menu.validate();
	}
	
	/**
	 * Gets current board
	 * @return Board Object
	 */
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
		JMenuItem timedMode_stop = new JMenuItem("Stop");
		timedMode_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getBoard() != null && getBoard().getClass() == TimedBoard.class) {
					getBoard().lockBoard();
				}
			}
		});
		
		menuTimedMode.add(timedMode_load);
		menuTimedMode.add(timedMode_reset);
		menuTimedMode.add(timedMode_stop);
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
		JMenuItem cruelMode_stop = new JMenuItem("Stop");
		cruelMode_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getBoard() != null && getBoard().getClass() == CruelBoard.class) {
					getBoard().lockBoard();
				}
			}
		});
		
		menuCruelMode.add(cruelMode_load);
		menuCruelMode.add(cruelMode_reset);
		menuCruelMode.add(cruelMode_stop);
		//Menu CruelMode End
		
		//Menu Settings
		
		JMenu menuSettings = new JMenu("Settings");
		
		JMenuItem menuSettings_theme = new JMenuItem("Theme");
		menuSettings_theme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel popup = new JPanel();
				Color startingColor = OptionsManager.currentColor;
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
				popup.setLayout(new GridLayout(2,2));
				//Effect
				int effectVolume = OptionsManager.effectVolume;
				JSlider effectSlider = new JSlider(0, 100, effectVolume);
				effectSlider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						OptionsManager.setEffectVolume(effectSlider.getValue());
					}
				});
				//Background
				int backgroundVolume = OptionsManager.backgroundVolume;
				JSlider backgroundSlider = new JSlider(0, 100, backgroundVolume);
				backgroundSlider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						OptionsManager.setBackgroundVolume(backgroundSlider.getValue());
					}
				});
				popup.add(new JLabel("Effect volume"));
				popup.add(effectSlider);
				popup.add(new JLabel("Background volume"));
				popup.add(backgroundSlider);
				int result = JOptionPane.showConfirmDialog(null, popup, "Sound Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(result == JOptionPane.OK_OPTION) {
					OptionsManager.setEffectVolume(effectSlider.getValue());
					OptionsManager.setBackgroundVolume(backgroundSlider.getValue());
					OptionsManager.Save();
				}else {
					OptionsManager.setEffectVolume(effectVolume);
					OptionsManager.setBackgroundVolume(backgroundVolume);
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
