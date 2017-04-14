package processing;

import java.awt.Point;
import java.util.ArrayList;

import templateMatching.TemplateMatcher;

public class ImageSearchingThread implements Runnable {

	public volatile ArrayList<Point> bestPoints=null;
	public volatile boolean done=false;
	private float[][] template, image;
	
	public ImageSearchingThread(float[][] template, float[][] image) {
		this.template=template;
		this.image=image;
	}
	
	public void run() {
		try {
			bestPoints=getBestPoints(template, image);
		}
		catch(Exception e) {
			bestPoints=new ArrayList<>();
		}
		done=true;
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
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[0].length; y++) {
				toReturn[x][y]=image[x][y];
			}
		}
		return toReturn;
	}
	
	public synchronized boolean isDone() {
		return done;
	}
}
