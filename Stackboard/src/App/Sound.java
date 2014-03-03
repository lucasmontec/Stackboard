package App;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	public static void play(InputStream stream) {
		AudioInputStream audioInputStream = null;
		Clip clip = null;

		try {
			audioInputStream = AudioSystem.getAudioInputStream(stream);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}

		clip.start();// This plays the audio
		while (!clip.isRunning())
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		while (clip.isRunning())
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		clip.close();
		try {
			stream.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
