package processing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import robot.Window;

public class BillyTesterMain {
	public static void main(String[] args) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Color[][] asColors=new Color[image.getWidth()][image.getHeight()];

		for (int x=0; x<asColors.length; x++) {
			for (int y=0; y<asColors[x].length; y++) {
				asColors[x][y]=new Color(image.getRGB(x, y));
			}
		}
		
		asColors = ImageProcessor.scaleImage(asColors, 400);
		
		String colors = ImageProcessor.convertToString(asColors);
		Color[][] processedImage = ImageProcessor.convertToColorArray(colors);
		Window.displayPixels(processedImage, "picture");
		Window.displayPixels(asColors, "picture");
	}
}
