package mainClasses;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;

import compression.ConvertToString;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import highGoalVision.DavidsHighGoalVision;
import processing.FindOnePtTop;
import processing.ImageProcessor;
import processing.TurnAngle;


public class HighGoal {

	private static Webcam webcam;
	private static Point shooterPoint;
	private static Color[][] image;
	
	static {
		Webcam.setDriver(new V4l4jDriver());
	}
	
	public static void main(String[] a) {
		init();
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roboRIO-2202-FRC.local");
		NetworkTable table=NetworkTable.getTable("VisionTable");

		System.out.println("Entering loop...");
		while (true) {
			//TODO, check the whether we should process after we have taken the image
			if (table.getBoolean("processVisionHighGoal", false)) {
				System.out.println("Processing");
				DavidsHighGoalVision vision=new DavidsHighGoalVision();
				
				System.out.println("Processing...");
				vision.process(getImageFromWebcam(webcam));
				table.putNumber("degreesToSetHighGoal", vision.getDegreesToTurn());
				table.putNumber("distanceFromHighGoal", vision.getDistanceFromTarget());
				table.putBoolean("processVisionHighGoal", false);
				
				if (table.getBoolean("NeedPictureHighGoal", false)) {
					System.out.println("Sending picture");
					image=vision.getImage();
					image=ImageProcessor.scaleImage(image, 134);
					table.putBoolean("HighGoalVisionSucceeded", value);
					table.putString("PictureHighGoal", ConvertToString.convertToString(image));
					table.putBoolean("NeedPictureHighGoal", false);
				}
				else {
					System.out.println("Not sending picture");
				}
			}
			webcam.getImage();
		}
	}

	public static void init() {
		webcam=Webcam.getWebcams().get(1);
		if (webcam.isOpen())
			webcam.close();
		webcam.setViewSize(new Dimension(320, 240));
		webcam.open();
	}
	
	public static void getImage() {
		BufferedImage image=getImageFromWebcam(webcam);
		Color[][] asColors=new Color[image.getWidth()][image.getHeight()];

		for (int x=0; x<asColors.length; x++) {
			for (int y=0; y<asColors[x].length; y++) {
				asColors[x][y]=new Color(image.getRGB(x, y));
			}
		}
		HighGoal.image=asColors;
	}

	public static float getShooterDegreesToTurn(int runCounter, BufferedImage shooterImage) {
		System.out.println("start: "+System.currentTimeMillis());
		Point shooter=FindOnePtTop.findTopPoint(shooterImage);
		shooterPoint=shooter;
		// null if points are wrong
		if (shooter.equals(new Point(0, 0))) {
			if (runCounter>=3) {
				return 0;
			}
			// try it again if we got things wrong
			return getShooterDegreesToTurn(runCounter+1, shooterImage);
		}
		System.out.println("done: "+System.currentTimeMillis());
		return TurnAngle.getTurnAngle(shooterPoint);
	}
	
	private static BufferedImage getImageFromWebcam(Webcam webcam) {
		return webcam.getImage();
	}
}