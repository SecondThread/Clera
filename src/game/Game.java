package game;

import java.awt.Color;

public class Game {
	
	private Color color=Color.white, paperColor=Color.white;
	private int notPaperCounter=0, videoCounter=0, simulationCounter=0;
	private int maxProgress=30;
	
	private Movie movie;
	private boolean playingMovie=false;
	
	private Simulation sim;
	private boolean playingSim=false;
	private Color[][] paper;
	
	public Game() {
		movie=new Movie();
	}
	
	public void setPaperColor(Color paperColor) {
		this.paperColor=paperColor;
	}
	
	//to be called 20 times a second
	public void update() {
		float max=Math.max(videoCounter, simulationCounter);
		color=new Color(videoCounter/max, 0, simulationCounter/max);
		
		if (videoCounter>=maxProgress) {
			playingMovie=true;
		}
		if (simulationCounter>=maxProgress) {
			playingSim=true;
		}
		
		if (playingMovie) {
			if (movie.update()) {
				//Movie is over
				playingMovie=false;
				movie=new Movie();
			}
		}
	}
	
	public Color[][] render() {
		Color[][] toReturn=new Color[600][400];
		if (playingMovie) {
			toReturn=movie.getFrame();
			hideWhite(toReturn);
		}
		else {
			paintLoadingScreen(toReturn);
			fadeCorners(toReturn);
		}
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
				if ((x/(float)background.length-0.2)/0.6<proportionDone) {
					background[x][y]=Color.white;
				}
				else {
					background[x][y]=Color.black;
				}
			}
		}
	}
	
	private void fadeCorners(Color[][] toReturn) {
		final float horizontalBoarder=80, verticalBorder=55;
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				double alphaX=Math.min(Math.min(x, toReturn.length-x), horizontalBoarder)/horizontalBoarder;
				double alphaY=Math.min(Math.min(y, toReturn[0].length-y), verticalBorder)/verticalBorder;
				//alphaX=Math.pow(alphaX, 0.75);
				//alphaY=Math.pow(alphaY, 0.75);
				Color color=toReturn[x][y];
				Color newColor=new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alphaX*alphaY*color.getAlpha()));
				toReturn[x][y]=newColor;
			}
		}
	}
	
	private void hideWhite(Color[][] background) {
		for (int x=0; x<background.length; x++) {
			for (int y=0; y<background[x].length; y++) {
				if (background[x][y].equals(Color.white)) {
					background[x][y]=new Color(255, 255, 255, 0);
				}
			}
		}
	}
	
	
	
	public void setTypeOfPaper(PaperTypes paperType) {
		switch(paperType) {
		case SIMULATION_PAPER:
			simulationCounter++;
			if (simulationCounter>maxProgress) {
				simulationCounter=maxProgress;
				videoCounter=0;
			}
			break;
		case VIDEO_PAPER:
			videoCounter++;
			if (videoCounter>maxProgress) {
				videoCounter=maxProgress;
				simulationCounter=0;
			}
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

	public void setPaper(Color[][] paper) {
		this.paper=paper;
	}
}
