package game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import game.Handlers.*;
import listeners.VolumeChangeListener;

/**
 * Background Clip
 * 
 * @author Austin Gray
 *
 */
public class BackgroundClip implements VolumeChangeListener {
	private Clip clip;
	private boolean continueLoop = true;

	/**
	 * Creats a new Background Clip
	 * 
	 * @param clip current clip
	 */
	public BackgroundClip(Clip clip) {
		this.clip = clip;
	}

	/**
	 * Gets Clip
	 * 
	 * @return Clip
	 */
	public Clip getClip() {
		return clip;
	}
	
	/**
	 * Checks if clip is running
	 * @return trus if running
	 */
	public boolean isLooping() {
		return continueLoop;
	}

	/**
	 * Stops the clip
	 */
	public void stop() {
		clip.stop();
		continueLoop = false;
		volumechangeDestroy();
	}

	/**
	 * Starts the clip
	 */
	public void start() {
		volumeChangeCreate();
		continueLoop = true;
		clip.start();
	}

	@Override
	public void onVolumeChange(int value) {
		setVolume(value);
	}

	/**
	 * Sets current volume of clip
	 * 
	 * @param value 0-100 clip volume
	 */
	public void setVolume(int value) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float dB = (float) (Math.log(value / 200f) / Math.log(10.0) * 20.0);
		gainControl.setValue(dB);
	}

	@Override
	public void volumeChangeCreate() {
		Handlers.OptionsManager.backgroundChangeArray.add(this);
	}

	@Override
	public void volumechangeDestroy() {
		Handlers.OptionsManager.backgroundChangeArray.remove(this);
	}
}
