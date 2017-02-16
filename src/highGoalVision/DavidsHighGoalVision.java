
package highGoalVision;

import java.awt.Color;
import java.awt.image.BufferedImage;

import processing.ImageProcessor;
import robot.Window;

public class DavidsHighGoalVision {
	
	private static final int MIN_PIXELS_IN_HIGHEST_ROW=5;
	private static final double HORIZONTAL_FOV=50, HEIGHT_OF_CAMERA_INCHES=10, HEIGHT_OF_TOP_STRIP_INCHES=70, ANGLE_OF_CAMERA_FROM_HORIZONTAL=20;
	
	private Color[][] toSend;
	private double degreesToTurn, distanceFromTarget;
	private boolean visionSucceeded;
	
	
	public void process(BufferedImage fromWebcam) {
		Color[][] colors=new Color[fromWebcam.getWidth()][fromWebcam.getHeight()];
		for (int x=0; x<colors.length; x++) {
			for (int y=0; y<colors[x].length; y++) {
				colors[x][y]=new Color(fromWebcam.getRGB(x, y));
			}
		}
		
		float[][] asLuminance=ImageProcessor.luminance(colors, -0.6f, 1, -0.6f);
		ImageProcessor.normalize(asLuminance);
		boolean[][] brightPixels=ImageProcessor.applyCutoff(0.6f, true, asLuminance);
		
		
		double finalX=0, finalY=0;
		visionSucceeded=false;
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
				visionSucceeded=true;
				break;
			}
		}
		
		markPixel((int)(finalX+0.5), (int)(finalY+0.5), colors);
		toSend=colors;//fromBooleans(brightPixels);
		
		Window.displayPixels(colors, "name");
		Window.displayPixels(asLuminance, "luminance");
		Window.displayPixels(brightPixels, "brightPixels");
		calculateValues(finalX, finalY, brightPixels.length, brightPixels[0].length);
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
	
	private void calculateValues(double xOnScreen, double yOnScreen, int screenWidth, int screenHeight) {
		degreesToTurn=(xOnScreen/screenWidth)*HORIZONTAL_FOV-HORIZONTAL_FOV/2;
		
		double verticleFOV=HORIZONTAL_FOV*screenHeight/screenWidth;
		double heightOfStripAboveCamera=HEIGHT_OF_TOP_STRIP_INCHES-HEIGHT_OF_CAMERA_INCHES;
		double angleFromTopOfScreen=yOnScreen/screenHeight*verticleFOV;
		double angleToHighGoal=verticleFOV/2-angleFromTopOfScreen+ANGLE_OF_CAMERA_FROM_HORIZONTAL;
		distanceFromTarget=heightOfStripAboveCamera/Math.tan(angleToHighGoal);		
	}
	
	public double getDegreesToTurn() {
		return degreesToTurn;
	}
	
	public double getDistanceFromTarget() {
		return distanceFromTarget;
	}
	
	public Color[][] getImage() {
		return toSend;
	}
	
	public boolean getVisionSucceeded() {
		return visionSucceeded;
	}
	
	
	public Color[][] fromBooleans(boolean[][] old) {
		Color[][] toReturn=new Color[old.length][old[0].length];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				toReturn[x][y]=old[x][y]?Color.green:Color.black;
			}
		}
		return toReturn;
	}
}
