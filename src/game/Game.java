package game;

import java.awt.Color;

import images.ImageLoader;

public class Game {
	
	private Color color=Color.white;
	private int counter=0;
	
	//to be called 20 times a second
	public void update() {
		counter+=5;
		if (counter>30) {
			counter=0;
		}
		color=Color.white;
		if (counter<20) {
			color=Color.red;
		}
		if (counter<10) {
			color=Color.blue;
		}
	}
	
	public Color[][] render() {
		//return ImageLoader.imageToArray(ImageLoader.loadImage("starryNight.jpg"));
		Color[][] toReturn=new Color[300][200];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				toReturn[x][y]=color;
			}
		}
		return toReturn;
	}
}
