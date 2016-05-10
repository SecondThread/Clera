package processing;

import java.awt.Point;
import java.util.ArrayList;

public class ShapeFinder {
	private static final float cutoff=0.6f;
	private static final int outerRange=12, innerRange=4;
	
	public static float[][] getPointsWithTopLeftCorner(boolean[][] image) {
		ComplicatedPattern topCorner=new ComplicatedPattern(true, false, true, false, true, false, false, false, false, outerRange, innerRange);
		return getPointsWithPattern(image, topCorner);
	}
	
	public static float[][] getPointsWithTopRightCorner(boolean[][] image) {
		ComplicatedPattern topCorner=new ComplicatedPattern(true, false, true, true, false, false, false, false, false, outerRange, innerRange);
		return getPointsWithPattern(image, topCorner);
	}
	
	public static float[][] getPointsWithBottomRightCorner(boolean[][] image) {
		ComplicatedPattern bottomRightCorner=new ComplicatedPattern(true, true, false, true, false, false, false, false, false, outerRange, innerRange);
		return getPointsWithPattern(image, bottomRightCorner);
	}
	
	public static float[][] getPointsWithBottomLeftCorner(boolean[][] image) {
		ComplicatedPattern bottomLeftCorner=new ComplicatedPattern(true, true, false, false, true, false, false, false, false, outerRange, innerRange);
		return getPointsWithPattern(image, bottomLeftCorner);
	}
	
	private static float[][] getPointsWithPattern(boolean[][] image, Pattern pattern) {;
		float[][] patternCorrelations=new float[image.length][image[0].length];
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				patternCorrelations[x][y]=pattern.checkSpace(image, x, y);
			}
		}
		return patternCorrelations;
	}
	
	private static float[][] getPointsWithPattern(boolean[][] image, ComplicatedPattern pattern) {;
	float[][] patternCorrelations=new float[image.length][image[0].length];
	for (int x=0; x<image.length; x++) {
		for (int y=0; y<image[x].length; y++) {
			patternCorrelations[x][y]=pattern.checkSpace(image, x, y);
		}
	}
	return patternCorrelations;
}

	
	private static float[] getColumnTotals(float[][] image) {
		float[] total=new float[image.length];
		for (int x=0; x<total.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				total[x]+=image[x][y];
			}
		}
		float max=-1;
		for (float x:total) {
			max=Math.max(x, max);
		}
		
		for (int x=0; x<total.length; x++) {
			total[x]/=max;
		}
		return total;
	}
	
	private static boolean[] getAboveLevel(float[] averages, float cutoff) {
		boolean[] peaks=new boolean[averages.length];
		for (int x=0; x<averages.length; x++) {
			peaks[x]=averages[x]>cutoff;
		}
		peaks[0]=peaks[peaks.length-1]=false;
		return peaks;
	}
	
	private static ArrayList<Point> getIslands(float[] averages) {
		boolean[] aboveLevel=getAboveLevel(averages, cutoff);
		ArrayList<Point> islands=new ArrayList<Point>();
		boolean wasAboveLevel=false;
		for (int x=0; x<aboveLevel.length; x++) {
			if (wasAboveLevel&&!aboveLevel[x]) {
				islands.get(islands.size()-1).y=x-1;
			}
			if (!wasAboveLevel&&aboveLevel[x]) {
				islands.add(new Point(x, -1));
			}
			wasAboveLevel=aboveLevel[x];
		}
		System.out.println("Number of islands: "+islands.size());
		return islands;
	}
	
	private static ArrayList<Integer> getXPeaks(float[][] image) {
		float[] averages=getColumnTotals(image);
		ArrayList<Point> islands=getIslands(averages);
		ArrayList<Integer> weightedCenters=new ArrayList<Integer>();
		for (Point range:islands) {
			float total=0, totalWeight=0;
			for (int x=range.x; x<=range.y; x++) {
				totalWeight+=averages[x];
				total+=x*averages[x];
			}
			weightedCenters.add((int)(total/totalWeight));
		}
		
		return weightedCenters;
	}
	
	public static ArrayList<Point> getPeaks(float[][] image) {
		ArrayList<Integer> xPeaks=getXPeaks(image);
		ArrayList<Point> peaks=new ArrayList<Point>();
		for (int i:xPeaks) {
			peaks.add(new Point(i, -1));
		}
		for (Point p:peaks) {
			float total=0, totalWeight=0;
			for (int y=0; y<image[p.x].length; y++) {
				if (image[p.x][y]>=cutoff) {
					totalWeight+=image[p.x][y];
					total+=y*image[p.x][y];
				}
			}
			p.y=(int)(total/totalWeight);
		}
		
		for (int i=peaks.size()-1; i>=0; i--) {
			if (peaks.get(i).x<5||peaks.get(i).y<5||image.length-peaks.get(i).x<5||image[0].length-peaks.get(i).y<5) {
				peaks.remove(i);
			}
		}
		return peaks;
	}
	
	/*
	public static ArrayList<Point> getPeaks(float[][] image) {
		ArrayList<Point> points=new ArrayList<Point>();
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				if (isPeak(x, y, image)) {
					points.add(new Point(x, y));
				}
			}
		}
		return points;
	}
	
	private static boolean isPeak(int x, int y, float[][] image) {
		if (x<=0||x+1>=image.length||y<=0||y+1>=image[0].length) {
			return false;
		}
		float max=Math.max(Math.max(Math.max(image[x+1][y], image[x-1][y]), Math.max(image[x][y+1], image[x][y-1])),
				Math.max(Math.max(image[x+1][y+1], image[x+1][y-1]), Math.max(image[x-1][y+1], image[x-1][y-1])));
		max=Math.max(max, image[x][y]);
		return Math.abs(max-image[x][y])<0.000001;
	}
	
	private static void clearTouchingPoints(float[][] image, int x, int y) {
		if (x<0||x>=image.length||y<0||y>=image.length||image[x][y]<=0.1) {
			return;
		}
		image[x][y]=0;
		clearTouchingPoints(image, x+1, y);
		clearTouchingPoints(image, x-1, y);
		clearTouchingPoints(image, x, y+1);
		clearTouchingPoints(image, x, y-1);
	}
	
	private static float getMaxOfTouchingPoints(float[][] image, int x, int y) {
		if (x<0||x>=image.length||y<0||y>=image.length||image[x][y]<=0.1) {
			return 0;
		}
		float max=image[x][y];
		float oldValue=image[x][y];
		image[x][y]=0;
		max=Math.max(max, getMaxOfTouchingPoints(image, x+1, y));
		max=Math.max(max, getMaxOfTouchingPoints(image, x-1, y));
		max=Math.max(max, getMaxOfTouchingPoints(image, x, y+1));
		max=Math.max(max, getMaxOfTouchingPoints(image, x, y-1));
		image[x][y]=oldValue;
		return max;
	}
	
	public static boolean[][] getPeaksAsImage(float[][] image) {
		ArrayList<Point> peaks=getPeaks(image);
		boolean[][] newImage=new boolean[image.length][image[0].length];
		for (int x=0; x<newImage.length; x++) {
			for (int y=0; y<newImage[x].length; y++) {
				newImage[x][y]=false;
			}
		}
		
		for (Point p:peaks) {
			newImage[p.x][p.y]=true;
		}
		
		return newImage;
	}

	public static boolean[][] getPeaksAsImageRecursively(float[][] image) {
		boolean[][] peaks=new boolean[image.length][image[0].length];
		float[][] imageCopy=new float[image.length][image[0].length];
		for (int x=0; x<imageCopy.length; x++) {
			for (int y=0; y<imageCopy.length; y++) {
				imageCopy[x][y]=image[x][y];
				peaks[x][y]=false;
			}
		}
		
		for (int x=0; x<imageCopy.length; x++) {
			for (int y=0; y<imageCopy.length; y++) {
				if (imageCopy[x][y]!=0) {
					float max=getMaxOfTouchingPoints(imageCopy, x, y);
					if (Math.abs(imageCopy[x][y]-max)<0.00001) {
						peaks[x][y]=true;
						clearTouchingPoints(imageCopy, x, y);
					}
				}
			}
		}
		
		return peaks;
	}*/
	/*
	public static boolean[][] getPeaks(float[][] image) {
		boolean[][] peaks=new boolean[image.length][image[0].length];
		for (int x=0; x<peaks.length; x++) 
			for(int y=0; y<peaks[x].length; y++)
				peaks[x][y]=false;
		Point max=null;
		do {
			max=getBiggestPeak(image);
			if (max!=null) {
				peaks[max.x][max.y]=true;
			}
		} while(max!=null);
		return peaks;
	}
	
	public static Point getBiggestPeak(float[][] image) {
		float max=0;
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				max=Math.max(max, image[x][y]);
			}
		}
		if (max<0.1) {
			return null;
		}
		
		System.out.println(max);
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				if (Math.abs(max-image[x][y])<0.00001) {
					destroyLowerPoints(image, x, y, max);
					return new Point(x, y);
				}
			}
		}
		return null;
	}
	
	private static void destroyLowerPoints(float[][] image, int x, int y, float minValue) {
		if (x<0||x>=image.length||y<0||y>=image[x].length||minValue<0) {
			//out of bounds
			return;
		}
		if (image[x][y]<0.0001||image[x][y]>minValue+0.0001) {
			return;
		}
		minValue=image[x][y];
		image[x][y]=0;
		destroyLowerPoints(image, x+1, y, minValue);
		destroyLowerPoints(image, x-1, y, minValue);
		destroyLowerPoints(image, x, y+1, minValue);
		destroyLowerPoints(image, x, y-1, minValue);
	}
	*/
}
