package processing;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import robot.Window;
import templateMatching.TemplateMatcher;

public class testerMain {
	public static void main(String[] args) {
		BufferedImage image=null;
		try {
			image=ImageIO.read(new File("D:\\Code\\1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Color[][] asColors=new Color[image.getWidth()][image.getHeight()];

		for (int x=0; x<asColors.length; x++) {
			for (int y=0; y<asColors[x].length; y++) {
				asColors[x][y]=new Color(image.getRGB(x, y));
			}
		}
		float[][] luminance=ImageProcessor.luminance(asColors, .33f, .33f, .33f);
		ImageProcessor.normalize(luminance);
		ImageProcessor.applyExponentialCurve(luminance, 3);
		//Window.displayPixels(luminance, "Peg");
		
		
		
		ArrayList<Point> bestPoints=new ArrayList<Point>();
		bestPoints.addAll(getBestPoints(topLeftTemplate, luminance));
		bestPoints.addAll(getBestPoints(topRightTemplate, luminance));
		bestPoints.addAll(getBestPoints(bottomLeftTemplate, luminance));
		bestPoints.addAll(getBestPoints(bottomRightTemplate, luminance));
		
		
		
		
		ArrayList<Point> betterPoints = PegVisionUtils.generateNewPoints(bestPoints);
		
		
		
		
		Point peg = PegVisionUtils.findPeg(betterPoints);
		System.out.println("Peg: " + peg);
		
		
		
		System.out.println("done...");
		//Window.displayPixelsWithPeaks(luminance, bestPoints, bestPoints.get(0), "");
		
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