package game;

import java.awt.Color;

import processing.ImageProcessor;

public class PaperProcessor {
	
	private static boolean processUp=false;
	
	public static PaperTypes getTypeOfPaper(Color[][] paper) {
		float[][] luminance=toLuminanceAsBlack(paper);
		ImageProcessor.normalize(luminance);
		boolean[][] cutoff=getCutoff(luminance, 0.5f);
		boolean left=cutoff[0][cutoff[0].length/2];
		boolean up=cutoff[cutoff.length/2][0]||!processUp;
		boolean right=cutoff[cutoff.length-1][cutoff[0].length/2];
		boolean down=cutoff[cutoff.length/2][cutoff[0].length-1];
		boolean videoTag=cutoff[3*cutoff.length/4][cutoff[0].length-1];
		if (!up||!left||right||down) {
			return PaperTypes.NOT_PAPER;
		}
		if (!videoTag) {
			return PaperTypes.VIDEO_PAPER;
		}
		return PaperTypes.SIMULATION_PAPER;
	}
	
	private static float[][] toLuminanceAsBlack(Color[][] image) {
		float[][] toReturn=new float[image.length][image[0].length];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				Color color=image[x][y];
				toReturn[x][y]=0.2126f * color.getRed() / 255 + 0.7152f * color.getGreen() / 255 + 0.0722f * color.getBlue() / 255;;
			}
		}
		return toReturn;
	}
	
	private static boolean[][] getCutoff(float[][] image, float cutoff) {
		boolean[][] toReturn=new boolean[image.length][image[0].length];
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				toReturn[x][y]=image[x][y]>=cutoff;
			}
		}
		return toReturn;
	}
}
