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
			image = ImageIO.read(new File("result.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Color[][] asColors = new Color[image.getWidth()][image.getHeight()];

		for (int x = 0; x < asColors.length; x++) {
			for (int y = 0; y < asColors[x].length; y++) {
				asColors[x][y] = new Color(image.getRGB(x, y));
			}
		}
		long startTime = System.nanoTime();
		Window.displayPixels(asColors, "Original");
		long endTime1 = System.nanoTime();
		System.out.println("Original Displayed: " + (endTime1 - startTime) / 1000000);
		String colors = ImageProcessor.convertToString(asColors);
		long endTime2 = System.nanoTime();
		System.out.println("Converted to String: " + (endTime2 - startTime) / 1000000);
		Color[][] processedImage = ImageProcessor.convertToColorArray(colors);
		long endTime3 = System.nanoTime();
		System.out.println("Converted back to Color[][]: " + (endTime3 - startTime) / 1000000);
		Window.displayPixels(processedImage, "Processed");
		long endTime4 = System.nanoTime();
		System.out.println("Processed Displayed: " + (endTime4 - startTime) / 1000000);

	}
}
