package listeners;

public interface VolumeChangeListener {

	public abstract void volumeChangeCreate();
	public abstract void volumechangeDestroy();
	public abstract void onVolumeChange(int value);
	
}
