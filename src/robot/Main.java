package robot;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Point;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import processing.ImageProcessor;
import saving.ScreenSetup;
import templateMatching.Template;
import templateMatching.TemplateSaver;

public class Main {
	private static boolean setupPicturePosition=false, editTemplates=false;
	private static boolean displayFinalPicture=true;
	public static boolean useSameWindow=true;
	
	public static final String topLeftTemplatesLocation="templates.txt", topRightTemplatesLocation="templatesTR.txt",
					bottomRightTemplatesLocation="templatesBR.txt", bottomLeftTemplatesLocation="templatesBL.txt";
	private static final String templateLocationToPracticeWith=topLeftTemplatesLocation;
	public static final int sizeOfImage=300;
	
	private static final RecenterThread recenterThread=new RecenterThread();
	private static boolean startedThread=false;
	private static Point lastCenter=null;
	private static Color[][] recenterImage;
	private static float[][] recenterPixels;
	
	public static void main(String[] args) {
		if (setupPicturePosition) {
			ScreenSetup.setupScreen();
		}
		else if (editTemplates) {
			TemplateSaver.templateRunner(TemplateSaver.loadTemplates(templateLocationToPracticeWith), templateLocationToPracticeWith);
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
				//main(null);
			}
		}
	}

	private static void runProgram() {
		long startTime=System.currentTimeMillis();
		ScreenSetup.loadData();
		Window.init();

		float[][] pixels=Window.getPixels(ScreenSetup.pictureStartX, ScreenSetup.pictureStartY, ScreenSetup.pictureEndX, ScreenSetup.pictureEndY);
		pixels=ImageProcessor.scaleImage(pixels, sizeOfImage);
		Color[][] oldImage=ImageProcessor.scaleImage(Window.getLastImage(), sizeOfImage);
			
		if (recenterThread.lastCenter!=null) {
			Point newCenter=recenterThread.lastCenter;
			if (lastCenter!=newCenter) {
				lastCenter=newCenter;
				SecondThread.setImage(recenterPixels);
				SecondThread.setCenterOfCorner(newCenter);
				SecondThread.setImage(pixels);
				SecondThread.searchAllPixels();
				recenterThread.setPixels(pixels);
				recenterPixels=pixels;
				recenterImage=oldImage;
			} 
			else {
				SecondThread.setImage(pixels);				
			}
			SecondThread.search();
			ArrayList<Point> peaks=new ArrayList<Point>();
			peaks.add(SecondThread.getCenter());
			if (displayFinalPicture) Window.displayPixelsWithPeaks(oldImage, peaks, peaks.get(0), "asdfd");
			//SecondThread.setCenterOfCorner(lastCenter);
		}
		
		if (!startedThread) {
			(new Thread(recenterThread)).start();
			startedThread=true;
			recenterThread.setPixels(pixels);
			recenterPixels=pixels;
			recenterImage=oldImage;
		}
		System.out.println("Took "+(0.0+System.currentTimeMillis()-startTime)/1000+" seconds.");
	}
	
	public static Point findCenter(float[][] pixels, String templateLocation, Point lastCenter) {
		ArrayList<Template> templates=TemplateSaver.loadTemplates(templateLocation);
		float maxConfidence=0;
		Point bestCenter=new Point(0,0);

		for (int x=0; x<pixels.length; x++) {
			for (int y=0; y<pixels[x].length; y++) {
				for (Template t:templates) {
					float currentMatch=t.matchTemplate(pixels, x, y);
					if (currentMatch>maxConfidence) {
						maxConfidence=currentMatch;
						bestCenter=new Point(x+t.getTemplate().length/2, y+t.getTemplate()[0].length/2);
					}
				}
			}
		}
		return bestCenter;
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
