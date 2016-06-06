package game;

import java.awt.Color;

public class Game {
	
	private Color color=Color.white;
	private int counter=0;
	private int notPaperCounter=0, videoCounter=0, simulationCounter=0;
	
	//to be called 20 times a second
	public void update() {
		float max=Math.max(Math.max(notPaperCounter, videoCounter), simulationCounter);
		color=new Color(videoCounter/max, notPaperCounter/max, simulationCounter/max);
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
	
	public void setTypeOfPaper(PaperTypes paperType) {
		switch(paperType) {
		case SIMULATION_PAPER:
			simulationCounter++;
			break;
		case VIDEO_PAPER:
			videoCounter++;
			break;
		case NOT_PAPER:
			notPaperCounter++;
			break;
		default:
			System.out.println("Invalid paper type in Game class");
		}
	}
}
