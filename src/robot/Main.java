package robot;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import game.Game;
import game.PaperProcessor;
import game.PaperTypes;
import images.ImageLoader;
import processing.ImageProcessor;
import saving.ScreenSetup;
import templateMatching.Template;
import templateMatching.TemplateSaver;

public class Main implements Runnable{
	private static boolean setupPicturePosition=false, editTemplates=false;
	public static boolean useSameWindow=true;
	
	public static final String topLeftTemplatesLocation="templates.txt", topRightTemplatesLocation="templatesTR.txt",
					bottomRightTemplatesLocation="templatesBR.txt", bottomLeftTemplatesLocation="templatesBL.txt";
	private static final String templateLocationToPracticeWith=bottomRightTemplatesLocation;
	public static final int sizeOfImage=200;
	
	private static final RecenterThread recenterThread=new RecenterThread();
	private static boolean startedThread=false;
	private static Point lastCenter=null;
	private static float[][] recenterPixels;
	
	private static int paperWidth=(int)(130*2/3f);
	public static float paperWidthOverHeight=22/14.3f;
	
	private static volatile Point topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner;
	private static volatile Color[][] lastImage;
	
	public static void main(String[] args) {
		(new Thread(new Main())).start();
		while(true) {
			if (lastImage!=null&&topLeftCorner!=null&&topRightCorner!=null&&bottomLeftCorner!=null&&bottomRightCorner!=null) {
				runGameLoop();
			}
		}
	}

	private static void runProgram() {
		ScreenSetup.loadData();
		Window.init();

		float[][] pixels=Window.getPixels(ScreenSetup.pictureStartX, ScreenSetup.pictureStartY, ScreenSetup.pictureEndX, ScreenSetup.pictureEndY);
		pixels=ImageProcessor.scaleImage(pixels, sizeOfImage);
		Color[][] oldImage=Window.getLastImage();
		
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
			} 
			else {
				SecondThread.setImage(pixels);				
			}
			SecondThread.search();
			ArrayList<Point> peaks=new ArrayList<Point>();
			Point center=SecondThread.getCenter();
			peaks.add(center);
			if (isReasonableCorner(center, pixels)) {
				int xRange=45, yRange=30;
				
				Point topRight=new Point(center.x+paperWidth, center.y);
				topRight=findCenterInBox(pixels, topRightTemplatesLocation, new Point(topRight.x-xRange, topRight.y-yRange), new Point(topRight.x+xRange, topRight.y+yRange));
				
				Point bottomLeft=new Point(center.x, center.y+(int)(paperWidth/paperWidthOverHeight));
				bottomLeft=findCenterInBox(pixels, bottomLeftTemplatesLocation, new Point(bottomLeft.x-xRange, bottomLeft.y-yRange), new Point(bottomLeft.x+xRange, bottomLeft.y+yRange));
				
				Point bottomRight=new Point(center.x+paperWidth, center.y+(int)(paperWidth/paperWidthOverHeight));
				bottomRight=findCenterInBox(pixels, bottomRightTemplatesLocation, new Point(bottomRight.x-xRange, bottomRight.y-yRange), new Point(bottomRight.x+xRange, bottomRight.y+yRange));
				
				convertToHD(oldImage, pixels, center, topRight, bottomLeft, bottomRight);
			}
			else {
				convertToHD(oldImage, pixels, center, center, center, center);
			}
		}
		
		if (!startedThread) {
			(new Thread(recenterThread)).start();
			startedThread=true;
			recenterThread.setPixels(pixels);
			recenterPixels=pixels;
		}
		//System.out.println("Took "+(0.0+System.currentTimeMillis()-startTime)/1000+" seconds.");
	}
	
	public static Point findCenter(float[][] pixels, String templateLocation) {
		return findCenterInBox(pixels, templateLocation, new Point(0, 0), new Point(pixels.length, pixels[0].length));
	}
	
	//finds the center looking throughout the box, normalizing
	private static Point findCenterInBox(float[][] pixels, String templateLocation, Point topLeftCorner, Point bottomRightCorner) {
		ArrayList<Template> templates=TemplateSaver.loadTemplates(templateLocation);
		float maxConfidence=0;
		Point bestCenter=new Point(0,0);

		for (int x=Math.max(topLeftCorner.x, 0); x<Math.min(bottomRightCorner.x, pixels.length); x++) {
			for (int y=Math.max(topLeftCorner.y, 0); y<Math.min(bottomRightCorner.y, pixels[0].length); y++) {
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
	
	public static boolean isReasonableCorner(Point topLeft, float[][] pixels) {
		return topLeft.x>0&&topLeft.y>0&&topLeft.x+paperWidth<pixels.length-1&&topLeft.y+paperWidth/paperWidthOverHeight<pixels[0].length-1;
	}

	private static void convertToHD(Color[][] original, float[][] smaller, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
		float newScale=Math.min(original.length/(float)smaller.length, original[0].length/(float)smaller[0].length);
		topLeftCorner=new Point((int)(topLeft.x*newScale), (int)(topLeft.y*newScale));
		topRightCorner=new Point((int)((topRight.x+.5)*newScale), (int)(topRight.y*newScale));
		bottomLeftCorner=new Point((int)(bottomLeft.x*newScale), (int)((bottomLeft.y+.5)*newScale));
		bottomRightCorner=new Point((int)((bottomRight.x+.5)*newScale), (int)((bottomRight.y+.5)*newScale));
		lastImage=original;
	}
	
	public void run() {
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
			}
		}
	}

	
	private static void runGameLoop() {
		final double millisBetweenUpdates=1000f/20;
		double nextUpdateTime=System.currentTimeMillis()+millisBetweenUpdates;
		Game game=new Game();
		while (true) {
			while (System.currentTimeMillis()>nextUpdateTime) {
				game.update();
				nextUpdateTime+=millisBetweenUpdates;
			}
			render(game.render(), game);
		}
	}
	
	private static void render(Color[][] image, Game game) {
		Color[][] lastImageClone=new Color[lastImage.length][lastImage[0].length];
		for (int x=0; x<lastImage.length; x++)
			for (int y=0; y<lastImage[x].length; y++)
				lastImageClone[x][y]=lastImage[x][y];
		
		if (topLeftCorner.x>5&&topLeftCorner.x<topRightCorner.x-5) {
			Color[][] paper=ImageLoader.getPaper(lastImageClone, topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner);
			PaperTypes paperType=PaperProcessor.getTypeOfPaper(paper);
			if (paperType==PaperTypes.VIDEO_PAPER) {
				ImageLoader.drawImageWithAlpha(image, lastImageClone, topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner);			
			}
			if (paperType==PaperTypes.SIMULATION_PAPER) {
				ImageLoader.drawImageWithAlpha(image, lastImageClone, topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner);
			}
			if (paperType==PaperTypes.NOT_PAPER) {
			}
			game.setTypeOfPaper(paperType);
		}
		ArrayList<Point> peaks=new ArrayList<Point>();
		peaks.add(topLeftCorner);
		peaks.add(topRightCorner);
		peaks.add(bottomLeftCorner);
		peaks.add(bottomRightCorner);

		Window.displayPixelsWithPeaks(lastImageClone, peaks, topLeftCorner, "FinalImage");
	}
	
}
