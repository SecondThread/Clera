package highGoalVision;

import java.awt.Color;
import java.awt.image.BufferedImage;

import processing.ImageProcessor;

public class DavidsHighGoalVision {
	
	private static final int MIN_PIXELS_IN_HIGHEST_ROW=5;
	private static Color[][] toSend;

	public void process(BufferedImage fromWebcam) {
		Color[][] colors=new Color[fromWebcam.getWidth()][fromWebcam.getHeight()];
		for (int x=0; x<colors.length; x++) {
			for (int y=0; y<colors[x].length; y++) {
				colors[x][y]=new Color(fromWebcam.getRGB(x, y));
			}
		}
		
		float[][] asLuminance=ImageProcessor.luminance(colors, -0.6f, 1, -0.6f);
		boolean[][] brightPixels=ImageProcessor.applyCutoff(0.6f, true, asLuminance);
		
		
		double finalX=0, finalY=0;
		for (int y=0; y<brightPixels[0].length; y++) {
			int count=countTrueValuesInArray(brightPixels, y);
			if (count>=MIN_PIXELS_IN_HIGHEST_ROW) {
				finalY=y;
				for (int x=0; x<brightPixels.length; x++) {
					if (brightPixels[x][y]) {
						finalX+=x;
					}
				}
				finalX/=count;
				break;
			}
		}
		
		markPixel((int)(finalX+0.5), (int)(finalY+0.5), colors);
		toSend=colors;
	}
	
	private int countTrueValuesInArray(boolean[][] array, int y) {
		int counter=0;
		for (int x=0; x<array.length; x++) {
			if (array[x][y]) {
				counter++;
			}
		}
		return counter;
	}
	
	private void markPixel(int x, int y, Color[][] pixels) {
		for (int drawX=x-3; drawX<=x+3; drawX++) {
			for (int drawY=y-3; drawY<=y+3; drawY++) {
				if (drawX>=0&&drawY>=0&&drawX<pixels.length&&drawY<pixels[0].length) {
					pixels[drawX][drawY]=Color.red;
				}
			}
		}
	}
	
}
