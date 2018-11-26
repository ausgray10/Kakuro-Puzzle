package game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import game.Handlers.OptionsManager;
import listeners.VolumeChangeListener;

public class EffectClip implements VolumeChangeListener {
	private Clip clip;

	/**
	 * Creats a new Effect Clip
	 * 
	 * @param clip current clip
	 */
	public EffectClip(Clip clip) {
		this.clip = clip;
		volumeChangeCreate();
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
	public boolean isRunning() {
		return clip.isRunning();
	}

	/**
	 * Stops the clip
	 */
	public void stop() {
		clip.stop();
		volumechangeDestroy();
	}

	/**
	 * Starts the clip
	 */
	public void start() {
		volumeChangeCreate();
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
		Handlers.OptionsManager.effectChangeArray.add(this);
	}

	@Override
	public void volumechangeDestroy() {
		Handlers.OptionsManager.effectChangeArray.remove(this);
	}
}
