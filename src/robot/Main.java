package robot;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import processing.Blur;
import processing.ImageProcessor;
import processing.ShapeFinder;
import saving.ScreenSetup;
import templateMatching.Template;
import templateMatching.TemplateSaver;

public class Main {
	private static boolean setupPicturePosition=false, editTemplates=false;
	private static boolean displayPictures=false, displayFinalPicture=true;
	public static boolean useSameWindow=true;

	private static final int secondGausianBlurWidth=8; 
	
	private static final String templateLocation="templates.txt";
	public static final int sizeOfImage=300;
	
	private static Point lastCenter=new Point(0, 0);
	private static float lastConfidence=0f;
	
	public static void main(String[] args) {
		if (setupPicturePosition) {
			ScreenSetup.setupScreen();
		}
		else if (editTemplates) {
			TemplateSaver.templateRunner(TemplateSaver.loadTemplates(templateLocation), templateLocation);
		}
		else {
			ScreenSetup.loadData();
			Window.init();
			try {
				for (int i=0; i<10000000; i++) {
					runProgram();
				}
			} catch (Exception e) {
				e.printStackTrace();
				main(null);
			}
		}
	}

	private static void runProgram() {
		long startTime=System.currentTimeMillis();
		ScreenSetup.loadData();
		Window.init();

		float[][] pixels=Window.getPixels(ScreenSetup.pictureStartX, ScreenSetup.pictureStartY, ScreenSetup.pictureEndX, ScreenSetup.pictureEndY);
		pixels=ImageProcessor.scaleImage(pixels, sizeOfImage);
		Color[][] oldPixels=ImageProcessor.scaleImage(Window.getLastImage(), sizeOfImage);//300
		if (displayPictures) Window.displayPixels(pixels, "Pre-ExponentialCurve");

		ArrayList<Template> templates=TemplateSaver.loadTemplates(templateLocation);
		float highestMatch=0;
		Point best=new Point(0,0);
		Point topLeftCorner=getTopLeftSearchPoint();
		Point bottomRightCorner=getBottomRightSearchPoint(pixels);
		for (int x=topLeftCorner.x; x<bottomRightCorner.x; x++) {
			for (int y=topLeftCorner.y; y<bottomRightCorner.y; y++) {
				for (Template t:templates) {
					float currentMatch=t.matchTemplate(pixels, x, y);
					if (currentMatch>highestMatch) {
						highestMatch=currentMatch;
						best=new Point(x+t.getTemplate().length/2, y+t.getTemplate()[0].length/2);
					}
				}
			}
		}
		System.out.println(highestMatch+" "+best);
		templates.get(0).matchTemplate(pixels, 0, 0);
		if (displayFinalPicture) Window.displayPixelsWithPeaks(pixels, null, best, "asdfd");
		System.out.println("Took "+(0.0+System.currentTimeMillis()-startTime)/1000+" seconds.");
		lastCenter=best;
		lastConfidence=highestMatch;
	}
	
	private static Point getTopLeftSearchPoint() {
		if (lastConfidence<0.999f) {
			return new Point(0, 0);
		}
		return new Point(Math.max(0, lastCenter.x-60), Math.max(0, lastCenter.y-60));
	}
	
	private static Point getBottomRightSearchPoint(float[][] pixels) {
		if (lastConfidence<0.999f) {
			return new Point(pixels.length, pixels[0].length);
		}
		return new Point(lastCenter.x+60, lastCenter.y+60);
	}

	/*
	private static ArrayList<Point> getTopLeftCorners(float[][] pixels, boolean[][] cutoff) {
		float[][] corners=ShapeFinder.getPointsWithTopLeftCorner(cutoff);
		ImageProcessor.applyExponentialCurve(corners, 4);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners");

		Blur.applyGaussianBlur(corners, secondGausianBlurWidth);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners, Blurred");

		ImageProcessor.normalize(corners);
		ImageProcessor.applyExponentialCurve(corners, 4);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners, Normalized");
		
		return ShapeFinder.getPeaks(corners);
	}
	
	private static ArrayList<Point> getTopRightCorners(float[][] pixels, boolean[][] cutoff) {
		float[][] corners=ShapeFinder.getPointsWithTopRightCorner(cutoff);
		ImageProcessor.applyExponentialCurve(corners, 4);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners");

		Blur.applyGaussianBlur(corners, secondGausianBlurWidth);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners, Blurred");

		ImageProcessor.normalize(corners);
		ImageProcessor.applyExponentialCurve(corners, 4);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners, Normalized");
		
		return ShapeFinder.getPeaks(corners);
	}
	
	private static ArrayList<Point> getBottomLeftCorners(float[][] pixels, boolean[][] cutoff) {
		float[][] corners=ShapeFinder.getPointsWithBottomLeftCorner(cutoff);
		ImageProcessor.applyExponentialCurve(corners, 4);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners");

		Blur.applyGaussianBlur(corners, secondGausianBlurWidth);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners, Blurred");

		ImageProcessor.normalize(corners);
		ImageProcessor.applyExponentialCurve(corners, 4);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners, Normalized");
		
		return ShapeFinder.getPeaks(corners);
	}
	
	private static ArrayList<Point> getBottomRightCorners(float[][] pixels, boolean[][] cutoff) {
		float[][] corners=ShapeFinder.getPointsWithBottomRightCorner(cutoff);
		ImageProcessor.applyExponentialCurve(corners, 4);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners");

		Blur.applyGaussianBlur(corners, secondGausianBlurWidth);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners, Blurred");

		ImageProcessor.normalize(corners);
		ImageProcessor.applyExponentialCurve(corners, 4);
		if (displayPictures) Window.displayPixels(corners, "Likely Corners, Normalized");
		
		return ShapeFinder.getPeaks(corners);
	}
*/
}
