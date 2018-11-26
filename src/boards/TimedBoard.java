package boards;

import javax.swing.JLabel;

import game.Handlers;
import game.Menu;
import game.Puzzle;
import game.Handlers.*;

/**
 * Time based Game Board
 * 
 * @author Austin Gray
 *
 */
public class TimedBoard extends Board {

	private int maxTimer;
	protected int timer;
	private java.util.Timer currentTimer;

	protected JLabel countdownLabel = new JLabel();

	/**
	 * Creates a new TimedBoard
	 * 
	 * @param puzzle puzzle to use for Board
	 */
	public TimedBoard(Puzzle puzzle) {
		super(puzzle);
		maxTimer = timer = (int) Math.pow(inputs.size(), 1.75f) * 1000;
		this.resetPuzzle();
		Menu.addMenuItem(countdownLabel);

	}
	@Override
	public void lockBoard() {
		super.lockBoard();
		currentTimer.cancel();
		currentTimer = null;
	}

	@Override
	public void destroy() {
		super.destroy();
		Menu.removeMenuItem(countdownLabel);
		currentTimer.cancel();
	}

	@Override
	protected void onGameOver() {
		super.onGameOver();
		currentTimer.cancel();
		currentTimer = null;
	}

	/**
	 * Timer that will count down while board is being played
	 */
	protected void callTimer() {
		if (timer <= 0) {
			SoundManager.playSound(SoundManager.EXPLOSION);
			currentTimer = null;
			resetPuzzle();
			return;
		}
		int timeInterval = 1000;
		if (timer < 15000 && timer > 5000) {
			timeInterval = 500;
		}
		if (timer < 5000) {
			timeInterval = 250;
		}
		final int lastTime = timeInterval;
		currentTimer = new java.util.Timer();
		currentTimer.schedule(new java.util.TimerTask() {
			public void run() {
				onRunInterval(lastTime);
			}
		}, lastTime);
	}

	/**
	 * called every time counter is updated
	 * 
	 * @param lastTime last time value
	 */
	protected void onRunInterval(int lastTime) {
		SoundManager.playSound(SoundManager.BEEP);
		timer -= lastTime;
		countdownLabel.setText(getTime());
		callTimer();
	}

	/**
	 * Gets time in hour/minute/second format
	 * 
	 * @return time as String
	 */
	protected String getTime() {
		long second = (timer / 1000) % 60;
		long minute = (timer / (1000 * 60)) % 60;
		long hour = (timer / (1000 * 60 * 60)) % 24;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	@Override
	public void resetPuzzle() {
		super.resetPuzzle();
		this.timer = maxTimer;
		if (currentTimer == null) {
			callTimer();
		}
	}

}
