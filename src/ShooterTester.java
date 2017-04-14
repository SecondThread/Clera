import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import robot.Window;

public class ShooterTester {

	private static Webcam shooterWebcam;

	public static void main(String[] args) throws IOException {
		shooterWebcam = Webcam.getWebcams().get(1);
		
		System.out.println("Shooter webcam: " + shooterWebcam.getName());
		shooterWebcam.setViewSize(new Dimension(320, 240));
		shooterWebcam.open(true);
		BufferedImage shooterImage = shooterWebcam.getImage();
		Color[][] intoColor = new Color[shooterImage.getWidth()][shooterImage.getHeight()];

		for (int x = 0; x < intoColor.length; x++) {
			for (int y = 0; y < intoColor[x].length; y++) {
				intoColor[x][y] = new Color(shooterImage.getRGB(x, y));
			}
		}

		Window.displayPixels(intoColor, "shooterCam");
		File outputfile = new File("~");
		ImageIO.write(shooterImage, "jpg", outputfile);
		shooterWebcam.close();
	}

}
