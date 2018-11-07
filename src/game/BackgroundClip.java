package game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import game.Handlers.*;

public class BackgroundClip implements VolumeChangeListener {
	private Clip clip;
	
	public BackgroundClip(Clip clip) {
		this.clip = clip;
	}
	
	public Clip getClip() {
		return clip;
	}
	
	public void stop() {
		OptionsManager.onVolumeChange.remove(this);
		clip.stop();
	}
	
	public void start() {
		OptionsManager.onVolumeChange.add(this);
		clip.start();
	}
	
	public void onVolumeChange(int value) {
		setVolume(value);
	}
	
	public void setVolume(int value) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(value / 200f) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
	}
}
