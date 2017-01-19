package processing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import robot.Window;

public class testerMain {
	public static void main(String[] args) {
		BufferedImage image=null;
		try {
			image=ImageIO.read(new File("C:\\Users\\David\\Pictures\\Vision test\\PiTest.png"));
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
		float[][] luminance=ImageProcessor.luminance(asColors, -.2f, 1.0f, -.2f);
		ImageProcessor.normalize(luminance);
		ImageProcessor.applyExponentialCurve(luminance, 3);
		boolean[][] processed = ImageProcessor.applyCutoff(.4f, true, luminance);
		Window.displayPixels(luminance, "Peg");
		Window.displayPixels(processed, "PegPro");
	}
}