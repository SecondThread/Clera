package game;

import java.awt.Color;

import images.ImageLoader;

public class Movie {
	private String[] frameLocations={"PhysicsVideoIntro.png", "PhysicsVideoIntroVision.png", "PhysicsVideoIntroVisionIlluminati.png",
			"PhysicsVideoPixels.png", "PhysicsVideoPixelsBlackWhite.png", "TemplateMatchingImage.png", "PhysicsVideoFindingCorners.png",
			"PhysicsVideoDrawPicture.png", "PhysicsVideoRemember.png", "MultiThreading.png"};
	private float[] lengthOfFramesInSeconds={10, 10, .5f, 10, 10, 10, 10, 10, 10, 20};
	private Color[][][] frames;
	
	private float secondsBetweenUpdates=1/20f;
	private float secondsPassed=0;
	private int currentFrame=0;
	
	
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
}
