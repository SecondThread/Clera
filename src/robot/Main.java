package robot;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import processing.ImageProcessor;
import saving.ScreenSetup;
import templateMatching.Template;
import templateMatching.TemplateSaver;

public class Main {
	private static boolean setupPicturePosition=false, editTemplates=false;
	private static boolean displayFinalPicture=true;
	public static boolean useSameWindow=true;
	
	private static final String topLeftTemplatesLocation="templates.txt", topRightTemplatesLocation="templatesTR.txt",
					bottomRightTemplatesLocation="templatesBR.txt", bottomLeftTemplatesLocation="templatesBL.txt";
	public static final int sizeOfImage=300;
	
	private static Point lastTopLeftCenter=new Point(0, 0), lastTopRightCenter=new Point(0, 0), lastBottomLeftCenter=new Point(0, 0), lastBottomRightCenter=new Point(0, 0);
	private static float lastConfidence=0f, lastTopLeftConfidence=0f, lastTopRightConfidence=0f, lastBottomLeftConfidence=0f, lastBottomRightConfidence=0f;
	private static final float minConfidenceToStay=0.999f;
	private static final int moveFrameRangeX=60, moveFrameRangeY=40;
	
	public static void main(String[] args) {
		if (setupPicturePosition) {
			ScreenSetup.setupScreen();
		}
		else if (editTemplates) {
			TemplateSaver.templateRunner(TemplateSaver.loadTemplates(bottomLeftTemplatesLocation), bottomLeftTemplatesLocation);
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
		Color[][] oldPixels=ImageProcessor.scaleImage(Window.getLastImage(), sizeOfImage);
		
		lastTopLeftCenter=findCenter(pixels, topLeftTemplatesLocation, lastTopLeftCenter, lastTopLeftConfidence);
		lastTopLeftConfidence=lastConfidence;
		lastTopRightCenter=findCenter(pixels, topRightTemplatesLocation, lastTopRightCenter, lastTopRightConfidence);
		lastTopRightConfidence=lastConfidence;
		//lastBottomLeftCenter=findCenter(pixels, bottomLeftTemplatesLocation, lastBottomLeftCenter);
		//lastBottomLeftConfidence=lastConfidence;
		//lastBottomRightCenter=findCenter(pixels, bottomRightTemplatesLocation, lastBottomRightCenter);
		//lastBottomRightConfidence=lastConfidence;
		
		ArrayList<Point> peaks=new ArrayList<Point>();
		peaks.add(lastTopLeftCenter);
		peaks.add(lastBottomLeftCenter);
		peaks.add(lastTopRightCenter);
		peaks.add(lastBottomRightCenter);
		if (displayFinalPicture) Window.displayPixelsWithPeaks(oldPixels, peaks, lastTopLeftCenter, "asdfd");
		System.out.println("Took "+(0.0+System.currentTimeMillis()-startTime)/1000+" seconds.");
		System.out.println(lastConfidence);
	}
	
	private static Point findCenter(float[][] pixels, String templateLocation, Point lastCenter, float lastConfidence) {
		ArrayList<Template> templates=TemplateSaver.loadTemplates(templateLocation);
		float maxConfidence=0;
		Point bestCenter=new Point(0,0);
		Point topLeftCorner=getTopLeftSearchPoint(lastCenter, lastConfidence);
		Point bottomRightCorner=getBottomRightSearchPoint(lastCenter, lastConfidence, pixels);
		for (int x=topLeftCorner.x; x<bottomRightCorner.x; x++) {
			for (int y=topLeftCorner.y; y<bottomRightCorner.y; y++) {
				for (Template t:templates) {
					float currentMatch=t.matchTemplate(pixels, x, y);
					if (currentMatch>maxConfidence) {
						maxConfidence=currentMatch;
						bestCenter=new Point(x+t.getTemplate().length/2, y+t.getTemplate()[0].length/2);
					}
				}
			}
		}
		Main.lastConfidence=maxConfidence;
		return bestCenter;
	}
	
	private static Point getTopLeftSearchPoint(Point lastCenter, float lastConfidence) {
		if (lastConfidence<minConfidenceToStay) {
			System.out.println("Bad "+lastConfidence);
			return new Point(0, 0);
		}
		System.out.println("Good");
		return new Point(Math.max(0, lastCenter.x-moveFrameRangeX), Math.max(0, lastCenter.y-moveFrameRangeY));
	}
	
	private static Point getBottomRightSearchPoint(Point lastCenter, float lastConfidence, float[][] pixels) {
		if (lastConfidence<minConfidenceToStay) {
			return new Point(pixels.length, pixels[0].length);
		}
		return new Point(lastCenter.x+moveFrameRangeX, lastCenter.y+moveFrameRangeY);
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
