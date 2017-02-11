import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import robot.Window;

public class PegTester {

	public static void main(String[] arms) throws IOException {
		Webcam pegWebcam = Webcam.getWebcams().get(0);
		System.out.println("Peg webcam: " + pegWebcam.getName());
		pegWebcam.setViewSize(new Dimension(320, 240));
		System.out.println("opening other");
		pegWebcam.open(true);
		BufferedImage pegImage = pegWebcam.getImage();
		Color[][] intoColor = new Color[pegImage.getWidth()][pegImage.getHeight()];

		for (int x = 0; x < intoColor.length; x++) {
			for (int y = 0; y < intoColor[x].length; y++) {
				intoColor[x][y] = new Color(pegImage.getRGB(x, y));
			}
		}
		Window.displayPixels(intoColor, "pegWebcam");
		File outputfile = new File("~");
		ImageIO.write(pegImage, "jpg", outputfile);
		pegWebcam.close();
	}

}
