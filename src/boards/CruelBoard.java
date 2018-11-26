package boards;

import game.Handlers;
import game.Puzzle;
import game.Handlers.SoundManager;

/**
 * Time/Flipping based Game Board
 * 
 * @author Austin Gray
 *
 */
public class CruelBoard extends TimedBoard {

	/**
	 * Creats a CruelBoard
	 * @param puzzle puzzle to use on Board
	 */
	public CruelBoard(Puzzle puzzle) {
		super(puzzle);
	}
	
	@Override
	protected void onRunInterval(int lastTime) {
    	SoundManager.playSound(SoundManager.BEEP);
    	timer -= lastTime;
    	if(timer % 10000 == 0 && timer != 0) {
    		this.flipPuzzle();
    		this.updatePuzzle();
    	}
    	countdownLabel.setText(getTime());
    	callTimer();
	}

}
