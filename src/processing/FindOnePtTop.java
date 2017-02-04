package processing;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import robot.Window;

/**
 * This class will find the top point of the HIGH GOAL reflection tape curve by
 * using the GreenImageProcesser Class
 * 
 * @author jialuo
 *
 */
public class FindOnePtTop {
	public static Point findTopPoint(BufferedImage image) {

		GreenImageProcesser processer = new GreenImageProcesser(-.5f, 1.0f, -.5f, 0.5);
		float[][] processedImage = processer.process(image);
		ArrayList<Point> bestTopPts = new ArrayList<Point>();
		int success = 0;
		int failed = 0;
		int startPoint = 0;
		int endPoint = 0;
		for (int y = 10; y < processedImage[0].length - 10; y++)
			for (int x = 10; x < processedImage.length - 10; x++) {
				if (bestTopPts.isEmpty()) {
					// find top point
					if (failed < 7 && processedImage[x][y] == 1) {
						success++;
						failed = 0;
						if (success == 1)
							startPoint = x;
					} else if (failed < 7) {
						if (success != 0)
							failed++;
					} else if (success < 7) {
						success = 0;
						failed = 0;
						startPoint = 0;
					} else {
						success = 0;
						failed = 0;
						endPoint = x - 7;
					}
					if (startPoint != 0 && endPoint != 0) {
						int midPoint = (startPoint + endPoint) / 2;
						int topCheck = 0;
						int botCheck = 0;
						if (midPoint != 0) {
							for (int i = 1; i < 5; i++) {

								if (processedImage[midPoint][y - i] == 0) {
									topCheck++;
								}

								if (processedImage[midPoint][y + i] == 1) {
									botCheck++;
								}
							}
							if (topCheck > 2 && botCheck > 1) {
								if (bestTopPts.isEmpty())
									bestTopPts.add(new Point(midPoint, y - 1));
								midPoint = 0;
								startPoint = 0;
								endPoint = 0;
							} else {
								midPoint = 0;
								startPoint = 0;
								endPoint = 0;
							}
						}
					}
				}
			}

		Color[][] asColors = new Color[image.getWidth()][image.getHeight()];
		for (int x = 0; x < asColors.length; x++) {
			for (int y = 0; y < asColors[x].length; y++) {
				asColors[x][y] = new Color(image.getRGB(x, y));
			}
		}
		if (bestTopPts.size() > 0) {
			return bestTopPts.get(0);
		} else {
			return new Point(0, 0);
		}

	}
}
