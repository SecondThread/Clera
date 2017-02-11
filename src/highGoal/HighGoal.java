package highGoal;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

import compression.ConvertToString;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import processing.FindOnePtTop;
import processing.ImageProcessor;
import processing.TurnAngle;

public class HighGoal {

	private static Webcam webcam;
	private static Point shooterPoint;
	private static Color[][] image;
	
	public static void main(String[] a) {
		init();
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roboRIO-2202-FRC.local");
		NetworkTable table=NetworkTable.getTable("VisionTable");

		while (true) {
			//TODO, check the whether we should process after we have taken the image
			System.out.println("\n----------------------\nSeeing if we need to process");
			if (table.getBoolean("processVisionHighGoal", false)) {
				System.out.println("Processing...");
				table.putNumber("degreesToSetHighGoal", getShooterDegreesToTurn(0,getImageFromWebcam(webcam)));
				table.putBoolean("processVisionHighGoal", false);
			}
			
			System.out.println("Getting image...");
			getImage();
			if (table.getBoolean("NeedPictureHighGoal", false)) {
				System.out.println("Sending picture");
				image=ImageProcessor.scaleImage(image, 133);
				table.putString("Picture", ConvertToString.convertToString(image));
				table.putBoolean("NeedPictureHighGoal", false);
			}
		}
	}

	public static void init() {
		webcam=Webcam.getDefault();
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