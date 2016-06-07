package game;

import java.awt.Color;

public class Game {
	
	private Color color=Color.white;
	private int notPaperCounter=0, videoCounter=0, simulationCounter=0;
	private int maxProgress=150;
	
	//to be called 20 times a second
	public void update() {
		float max=Math.max(videoCounter, simulationCounter);
		color=new Color(videoCounter/max, 0, simulationCounter/max);
	}
	
	public Color[][] render() {
		Color[][] toReturn=new Color[600][400];
		paintLoadingScreen(toReturn);
		fadeCorners(toReturn);
		return toReturn;
	}
	
	private void paintLoadingScreen(Color[][] background) {
		for (int x=0; x<background.length; x++) {
			for (int y=0; y<background[x].length; y++) {
				background[x][y]=color;
			}
		}
		
		float proportionDone=Math.max(videoCounter, simulationCounter+0f)/maxProgress;
		for (int x=(int)(background.length*0.2); x<background.length*0.8; x++) {
			for (int y=(int) (background[x].length*0.2); y<background[x].length*0.3; y++) {
				if ((x/(float)background.length-0.2)*0.6<proportionDone) {
					background[x][y]=Color.white;
				}
				else {
					background[x][y]=Color.black;
				}
			}
		}
	}
	
	private void fadeCorners(Color[][] toReturn) {
		final float horizontalBoarder=50, verticalBorder=35;
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				double alphaX=Math.min(Math.min(x, toReturn.length-x), horizontalBoarder)/horizontalBoarder;
				double alphaY=Math.min(Math.min(y, toReturn[0].length-y), verticalBorder)/verticalBorder;
				//alphaX=Math.pow(alphaX, 0.75);
				//alphaY=Math.pow(alphaY, 0.75);
				Color color=toReturn[x][y];
				Color newColor=new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alphaX*alphaY*255));
				toReturn[x][y]=newColor;
			}
		}
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
			videoCounter=0;
			simulationCounter=0;
			break;
		default:
			System.out.println("Invalid paper type in Game class");
		}
	}

}
