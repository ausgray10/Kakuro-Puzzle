package boards;

import game.Handlers;
import game.Puzzle;
import game.Handlers.SoundManager;

public class CruelBoard extends TimedBoard {

	public CruelBoard(Puzzle puzzle) {
		super(puzzle);
	}
	
	protected void onRunInterval(int lastTime) {
    	SoundManager.playSound(SoundManager.BEEP);
    	timer -= lastTime;
    	if(timer % 10000 == 0) {
    		this.flipPuzzle();
    	}
    	countdownLabel.setText(getTime());
    	callTimer();
	}

}
