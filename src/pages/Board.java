package pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.*;

import main.FileManager;
import main.Main;
import main.Puzzle;
import main.SoundManager;
import tiles.*;

public class Board extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Board currentBoard;
	private Puzzle puzzle;
	private JPanel game;
	private boolean timedMode;
	private boolean cruelMode;
	private int totalTimer;
	private int timer;
	private java.util.Timer currentTimer;
	private ArrayList<ControlTile> controls;
	private ArrayList<InputTile> inputs;
	
	private GridLayout gameLayout;
	private JLabel timeLabel;

	public Board(Puzzle puzzle, boolean timedMode, boolean cruelMode) {
		this.puzzle = puzzle;
		this.timedMode = timedMode;
		this.cruelMode = cruelMode;
		currentBoard = this;
		BorderLayout thisLayout = new BorderLayout();
		this.setLayout(thisLayout);
		game = new JPanel();
		gameLayout = new GridLayout(puzzle.getRows(), puzzle.getCols());
		gameLayout.setHgap(2);
		gameLayout.setVgap(2);
		game.setLayout(gameLayout);
		
		controls = new ArrayList<ControlTile>();
		inputs = new ArrayList<InputTile>();
		ControlTile[] colControlTiles = new ControlTile[puzzle.getCols()];
		for (int y = 0; y < puzzle.getRows(); y++) {
			ControlTile rowTile = null;
			for (int x = 0; x < puzzle.getCols(); x++) {
				ITile tile = puzzle.getTiles()[y][x];
				if(tile.getClass() == ControlTile.class) {
					controls.add((ControlTile) tile);
					colControlTiles[x] = rowTile = (ControlTile) tile;
				}
				else if(tile.getClass() == InputTile.class) {
					inputs.add((InputTile) tile);
					colControlTiles[x].addColInputTile((InputTile) tile);
					((InputTile)tile).setColTile(colControlTiles[x]);
					rowTile.addRowInputTile((InputTile) tile);
					((InputTile)tile).setRowTile(rowTile);
					((InputTile)tile).updateTile();
				}
				game.add((JComponent) tile);
			}
		}
		if(timedMode) {
			totalTimer = timer = inputs.size() * (int)Math.sqrt(Math.pow(puzzle.getCols(), 2) + Math.pow(puzzle.getRows(), 2)) * 1000;
			this.resetTiles();
			updateTiles();
			callTimer();
		}
		
		JPanel end = new JPanel();
		
		JButton flip = new JButton("Flip");
		flip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentBoard.flip();
			}
		});
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(timedMode) {
					currentTimer.cancel();
				}
				Main.SetCurrentPage(new Menu());
			}
		});
		
		timeLabel = new JLabel(getTime());
		
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileManager.SaveFile(puzzle);
			}
		});
		
		JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Puzzle puzzle = FileManager.OpenFile();
				if(puzzle != null)
					Main.SetCurrentPage(new Board(puzzle, timedMode, cruelMode));
			}
		});
		
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetTiles();
				updateTiles();
				if(timedMode) {
					timer = totalTimer;
					currentTimer.cancel();
					callTimer();
				}
			}
		});
		if(timedMode) {
			end.add(timeLabel);
		}
		end.add(reset);
		if(!timedMode) {
		end.add(flip);
		end.add(save);
		end.add(load);
		}
		end.add(back);
		
		this.add(game, BorderLayout.CENTER);
		this.add(end, BorderLayout.SOUTH);
		
		this.CheckForGameOver();
	}
	
	public void updateTiles() {
		for(int i = 0; i < controls.size(); i++) {
			controls.get(i).updateTile();
		}
	}
	
	public void resetTiles() {
		for(int i = 0; i < inputs.size(); i++) {
			inputs.get(i).resetTile();
			inputs.get(i).setEnabled(true);
		}
	}
	
	public void CheckForGameOver() {
		
		for(int i = 0; i < controls.size(); i++) {
			if(!controls.get(i).isThisCorrect()) {
				return;
			}
		}
		
		if(timedMode) {
			currentTimer.cancel();
		}
		
		for(int i = 0; i < inputs.size(); i++) {
			inputs.get(i).setEnabled(false);
		}
		System.out.println("Game Over!");
		SoundManager.playSound(SoundManager.PARTYHORN);
	}
	
	private void callTimer() {
		if(timer <= 0) {
			for(int i = 0; i < inputs.size(); i++) {
				inputs.get(i).setEnabled(false);
			}
			System.out.println("Game Over!");
			SoundManager.playSound(SoundManager.EXPLOSION);
			return;
		}
		int timeInterval = 1000;
		if(timer < 15000 && timer > 5000) {
			timeInterval  = 500;
		}
		if(timer < 5000) {
			timeInterval  = 250;
		}
		final int lastTime = timeInterval;
		currentTimer = new java.util.Timer();
		currentTimer.schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	SoundManager.playSound(SoundManager.BEEP);
		            	timer -= lastTime;
		            	if(cruelMode && timer % 30000 == 0) {
		            		currentBoard.flip();
		            	}
		            	timeLabel.setText(getTime());
		            	callTimer();
		            }
		        }, 
		        lastTime
		);
	}
	
	private void flip() {
		puzzle.flip();
		for(int y = 0; y < puzzle.getRows(); y++) {
			for(int x = 0; x < puzzle.getCols(); x++) {
				game.add((JComponent) puzzle.getTiles()[y][x]);
			}	
		}
		gameLayout.setColumns(puzzle.getCols());
		gameLayout.setRows(puzzle.getRows());
		game.validate();
	}
	
	private String getTime() {
		long second = (timer / 1000) % 60;
		long minute = (timer / (1000 * 60)) % 60;
		long hour = (timer / (1000 * 60 * 60)) % 24;

		return String.format("%02d:%02d:%02d", hour, minute, second);
	}
}
