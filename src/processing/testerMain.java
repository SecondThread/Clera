package processing;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.github.sarxos.webcam.Webcam;

import robot.Window;
import templateMatching.TemplateMatcher;

public class testerMain {
	
	private static Webcam webcam;
	
	public static void main(String[] args) {
		BufferedImage image=null;
		webcam=Webcam.getDefault();
		image=getImageFromWebcam(webcam);
		//image=ImageIO.read(new File("C:\\Users\\David\\Pictures\\Vision test\\PiTest.png"));
		Color[][] asColors=new Color[image.getWidth()][image.getHeight()];

		for (int x=0; x<asColors.length; x++) {
			for (int y=0; y<asColors[x].length; y++) {
				asColors[x][y]=new Color(image.getRGB(x, y));
			}
		}
		float[][] luminance=ImageProcessor.luminance(asColors, -.2f, 1.0f, -.2f);
		ImageProcessor.normalize(luminance);
		ImageProcessor.applyExponentialCurve(luminance, 3);
		//Window.displayPixels(luminance, "Peg");
		
		
		ArrayList<Point> bestPoints=new ArrayList<Point>();
		bestPoints.addAll(getBestPoints(topLeftTemplate, luminance));
		bestPoints.addAll(getBestPoints(topRightTemplate, luminance));
		bestPoints.addAll(getBestPoints(bottomLeftTemplate, luminance));
		bestPoints.addAll(getBestPoints(bottomRightTemplate, luminance));
		System.out.println("done...");
		//Window.displayPixelsWithPeaks(luminance, bestPoints, bestPoints.get(0), "");
		for (Point p:bestPoints) {
			System.out.println(p);
		}
		Window.displayPixelsWithPeaks(luminance, bestPoints, bestPoints.get(0), "");
		System.out.println("done for real");
	}
	
	private static ArrayList<Point> getBestPoints(float[][] template, float[][] image) {
		ArrayList<Point> points=new ArrayList<Point>();
		image=clone(image);
		float[][] bestResults=TemplateMatcher.matchTemplate(image, template);
		ImageProcessor.normalize(bestResults);
		ImageProcessor.applyExponentialCurve(bestResults, 3);
		int[] best=TemplateMatcher.getBestPoint(bestResults);
		TemplateMatcher.destroyArea(bestResults, best[0], best[1], 12);
		int[] newBest=TemplateMatcher.getBestPoint(bestResults);
		points.add(new Point(best[0]+template.length/2, best[1]+template[0].length/2));
		points.add(new Point(newBest[0]+template.length/2, newBest[1]+template[0].length/2));
		return points;
	}
	
	private static float[][] clone(float[][] image) {
		float[][] toReturn=new float[image.length][image[0].length];
		for(int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[0].length; y++) {
				toReturn[x][y]=image[x][y];
			}
		}
		return toReturn;
	}
	
	private static BufferedImage getImageFromWebcam(Webcam webcam) {
		return webcam.getImage();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	private static float[][] topLeftTemplate={
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 1, 1, 1, 1, 1}, 
			{0, 0, 0, 1, 1, 1, 1, 1},
			{0, 0, 0, 1, 1, 1, 1, 1},
			{0, 0, 0, 1, 1, 1, 1, 1},
			{0, 0, 0, 1, 1, 1, 1, 1},};
	
	private static float[][] topRightTemplate={
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{1, 1, 1, 1, 1, 0, 0, 0}, 
			{1, 1, 1, 1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1, 0, 0, 0},};
	
	private static float[][] bottomLeftTemplate={
			{0, 0, 0, 1, 1, 1, 1, 1}, 
			{0, 0, 0, 1, 1, 1, 1, 1},
			{0, 0, 0, 1, 1, 1, 1, 1},
			{0, 0, 0, 1, 1, 1, 1, 1},
			{0, 0, 0, 1, 1, 1, 1, 1},
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0, 0, 0, 0},};
	
	private static float[][] bottomRightTemplate={
			{1, 1, 1, 1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1, 0, 0, 0},
			{1, 1, 1, 1, 1, 0, 0, 0}, 
			{1, 1, 1, 1, 1, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0, 0, 0, 0}, };
	
	
	
}