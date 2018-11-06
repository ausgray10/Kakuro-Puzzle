package main;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import pages.Options;

public class SoundManager {
	
	public static String BEEP = "beep.wav";
	public static String PARTYHORN = "partyhorn.wav";
	public static String SLIP = "slip.wav";
	public static String EXPLOSION = "bomb.wav";
	
	public static synchronized void playSound(final String name) {
		  new Thread(new Runnable() {
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        AudioInputStream inputStream =AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + "/Sounds/" + name));
		        clip.open(inputStream);
		        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		        float dB = (float) (Math.log(OptionsManager.volume / 100f) / Math.log(10.0) * 20.0);
		        gainControl.setValue(dB);
		        
		        clip.start(); 
		      } catch (Exception e) {
		        System.err.println(e);
		      }
		    }
		  }).start();
		}
	
}
