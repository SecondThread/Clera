package game;

import java.awt.Color;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import images.ImageLoader;

public class Movie {
	private String[] frameLocations={"PhysicsVideoIntro.png", "PhysicsVideoIntroVision.png", "PhysicsVideoIntroVisionIlluminati.png",
			"PhysicsVideoPixels.png", "PhysicsVideoPixelsBlackWhite.png", "TemplateMatchingImage.png", "PhysicsVideoFindingCorners.png",
			"PhysicsVideoDrawPicture.png", "PhysicsVideoRemember.png", "MultiThreading.png"};
	private float[] lengthOfFramesInSeconds={3, 4.5f, 0.5f, 37, 12, 24, 25, 49, 9, 2000};
	private Color[][][] frames;
	private Clip clip;
	
	private float secondsBetweenUpdates=1/20f;
	private float secondsPassed=0;
	private int currentFrame=0;
	
	
	private Movie(Color[][][] frames) {
		this.frames=frames;
	}
	
	public Movie() {
		System.out.println("Loading");
		frames=new Color[frameLocations.length][][];
		for (int i=0; i<frames.length; i++) {
			System.out.println(i);
			frames[i]=ImageLoader.imageToArray(ImageLoader.loadImage("video/"+frameLocations[i]));
		}
		System.out.println("Loaded...");
	}
	
	/**
	 * updates the frame, and if the video is over, it returns true
	 * @return
	 * true if the video is over, false otherwise
	 */
	public boolean update() {
		
		secondsPassed+=secondsBetweenUpdates;
		while (secondsPassed>lengthOfFramesInSeconds[currentFrame]) {
			secondsPassed-=lengthOfFramesInSeconds[currentFrame];
			currentFrame++;
			if (currentFrame>=frames.length) {
				return true;
			}
		}
		return false;
	}
	
	public Color[][] getFrame() {
		return frames[currentFrame];
	}
	
	public void play() {
		try {
			URL url = this.getClass().getClassLoader().getResource("Screencast.wav");
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	         // Get a sound clip resource.
	         clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioIn);
	         clip.start();
	      } catch (Exception e) {
	        System.err.println(e.getMessage());
	      }
	}
	
	public Movie clone() {
		if (clip!=null) clip.stop();
		return new Movie(frames);
	}
}
