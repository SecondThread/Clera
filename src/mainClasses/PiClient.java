package mainClasses;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.github.sarxos.webcam.Webcam;

import compression.ConvertToString;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import processing.FindOnePtTop;
import processing.ImageProcessor;
import processing.ImageSearchingThread;
import processing.PegVisionUtils;
import processing.TurnAngle;
import robot.Window;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;


public class PiClient {

	private static Webcam pegWebcam;
	private static Color[][] image;
	private static double distance;
	
	static {
		Webcam.setDriver(new V4l4jDriver());
	}

	public static void main(String[] a) {
		init();
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roboRIO-2202-FRC.local");
		NetworkTable table=NetworkTable.getTable("VisionTable");

		while (true) {
			//TODO, check the whether we should process after we have taken the image
			System.out.println("\n----------------------\nSeeing if we need to process");
			if (table.getBoolean("processVision", false)) {
				System.out.println("Processing...");
				table.putNumber("degreesToTurn", getDegreesToTurn(0));
				table.putNumber("distanceToMove", distance);
				table.putBoolean("processVision", false);
				//getDegreesToTurn will automatically set the image variable
			}
			else {
				System.out.println("Getting image...");
				getImage(pegWebcam);
			}
			if (table.getBoolean("NeedPicture", false)) {
				//send the picture to the drivers station if they want it
				System.out.println("Sending picture");
				image=ImageProcessor.scaleImage(image, 133);
				table.putString("Picture", ConvertToString.convertToString(image));
				table.putBoolean("NeedPicture", false);
			}
		}
	}

	public static void init() {
		pegWebcam = Webcam.getWebcams().get(0);
		System.out.println("Peg webcam: " + pegWebcam.getName());
		pegWebcam.setViewSize(new Dimension(320, 240));
		pegWebcam.open();
	}
	
	public static void getImage(Webcam webcam) {
		BufferedImage image=getImageFromWebcam(webcam);
		Color[][] asColors=new Color[image.getWidth()][image.getHeight()];

		for (int x=0; x<asColors.length; x++) {
			for (int y=0; y<asColors[x].length; y++) {
				asColors[x][y]=new Color(image.getRGB(x, y));
			}
		}
		PiClient.image=asColors;
	}
	
	public static float getDegreesToTurn(int runCounter) {
		float[][] luminance = null;
		ArrayList<Point> bestPoints = null;
		System.out.println("start: " + System.currentTimeMillis());
		BufferedImage image = getImageFromWebcam(pegWebcam);
		Color[][] asColors = new Color[image.getWidth()][image.getHeight()];

		for (int x = 0; x < asColors.length; x++) {
			for (int y = 0; y < asColors[x].length; y++) {
				asColors[x][y] = new Color(image.getRGB(x, y));
			}
		}
		
		PiClient.image=asColors;
		
		luminance=ImageProcessor.luminance(asColors, -.6f, 1.0f, -.2f);
		ImageProcessor.normalize(luminance);
		ImageProcessor.applyExponentialCurve(luminance, 3);

		bestPoints = new ArrayList<Point>();
		bestPoints = runThreads(luminance);
		Window.displayPixels(luminance, "picture");
		// null if points are wrong
		if (bestPoints==null) {
			if (runCounter>=3) {
				return 0;
			}
			// try it again if we got things wrong
			return getDegreesToTurn(runCounter+1);
		}
		System.out.println("done: " + System.currentTimeMillis());
		Window.saveImage(luminance, bestPoints, "result.png");

		Point peg=PegVisionUtils.findPeg(bestPoints);
		distance=PegVisionUtils.calcDistance(bestPoints);
		return TurnAngle.getTurnAngle(peg);
	}

	public static float getShooterDegreesToTurn(int runCounter, BufferedImage shooterImage) {
		System.out.println("start: "+System.currentTimeMillis());
		Point shooter=FindOnePtTop.findTopPoint(shooterImage);
		// null if points are wrong
		if (shooter.equals(new Point(0, 0))) {
			if (runCounter>=3) {
				return 0;
			}
			// try it again if we got things wrong
			return getShooterDegreesToTurn(runCounter+1, shooterImage);
		}
		System.out.println("done: "+System.currentTimeMillis());
		return TurnAngle.getTurnAngle(shooter);
	}

	private static ArrayList<Point> runThreads(float[][] image) {
		ImageSearchingThread topLeft = new ImageSearchingThread(topLeftTemplate, image);
		ImageSearchingThread topRight = new ImageSearchingThread(topRightTemplate, image);
		ImageSearchingThread bottomLeft = new ImageSearchingThread(bottomLeftTemplate, image);
		ImageSearchingThread bottomRight = new ImageSearchingThread(bottomRightTemplate, image);

		new Thread(topRight).start();
		new Thread(bottomLeft).start();
		new Thread(bottomRight).start();
		// topRight.run();//52s
		// bottomLeft.run();
		// bottomRight.run();
		topLeft.run();

		while (!topLeft.isDone() || !topRight.isDone() || !bottomLeft.isDone() || !bottomRight.isDone()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ArrayList<Point> toReturn = new ArrayList<Point>();
		toReturn.addAll(topLeft.bestPoints);
		toReturn.addAll(topRight.bestPoints);
		toReturn.addAll(bottomLeft.bestPoints);
		toReturn.addAll(bottomRight.bestPoints);

		// null if the points are wrong
		toReturn=PegVisionUtils.generateNewPoints(toReturn);
		System.out.println("!!!!!!!!!!!!!!generated new points: "+toReturn);

		return toReturn;

	}

	private static BufferedImage getImageFromWebcam(Webcam webcam) {
		return webcam.getImage();
	}
	


	private static float[][] topLeftTemplate= {{0, 0, 0, 0, 0, 0,}, {0, 0, 0, 0, 0, 0,}, {0, 0, 0, 0, 0, 0,},
			{0, 0, 0, 1, 1, 1,}, {0, 0, 0, 1, 1, 1,}, {0, 0, 0, 1, 1, 1,},};

	private static float[][] topRightTemplate = { { 0, 0, 0, 0, 0, 0, }, { 0, 0, 0, 0, 0, 0, }, { 0, 0, 0, 0, 0, 0 },
			{ 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 0, 0, 0 }, };

	private static float[][] bottomLeftTemplate = { { 0, 0, 0, 1, 1, 1, }, { 0, 0, 0, 1, 1, 1, }, { 0, 0, 0, 1, 1, 1, },
			{ 0, 0, 0, 0, 0, 0, }, { 0, 0, 0, 0, 0, 0, }, { 0, 0, 0, 0, 0, 0, }, };

	private static float[][] bottomRightTemplate = { { 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 0, 0, 0 }, { 1, 1, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, };

}