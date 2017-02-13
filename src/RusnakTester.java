import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;

import robot.Window;

public class RusnakTester {
	
	static {
		Webcam.setDriver(new V4l4jDriver());
	}
	
	public static void main(String[] args) {
		Webcam shooterWebcam = Webcam.getWebcams().get(0);
		System.out.println("Peg webcam: " + shooterWebcam.getName());
		shooterWebcam.setViewSize(new Dimension(320, 240));
		System.out.println("opening other");
		shooterWebcam.open();
		BufferedImage shooterImage = shooterWebcam.getImage();
		Color[][] shooterIntoColor = new Color[shooterImage.getWidth()][shooterImage.getHeight()];

		for (int x = 0; x < shooterIntoColor.length; x++) {
			for (int y = 0; y < shooterIntoColor[x].length; y++) {
				shooterIntoColor[x][y] = new Color(shooterImage.getRGB(x, y));
			}
		}
		Window.displayPixels(shooterIntoColor, "shooterWebcam");
		
		Webcam pegWebcam = Webcam.getWebcams().get(1);
		System.out.println("Peg webcam: " + pegWebcam.getName());
		pegWebcam.setViewSize(new Dimension(320, 240));
		System.out.println("opening other");
		pegWebcam.open();
		BufferedImage pegImage = pegWebcam.getImage();
		Color[][] pegIntoColor = new Color[pegImage.getWidth()][pegImage.getHeight()];

		for (int x = 0; x < pegIntoColor.length; x++) {
			for (int y = 0; y < pegIntoColor[x].length; y++) {
				pegIntoColor[x][y] = new Color(pegImage.getRGB(x, y));
			}
		}
		Window.displayPixels(pegIntoColor, "pegWebcam");
		
	}
}
