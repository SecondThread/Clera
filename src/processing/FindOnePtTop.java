package processing;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * This class will find the top point of the HIGH GOAL reflection tape curve by
 * using the GreenImageProcesser Class
 * 
 * @author jialuo
 *
 */
public class FindOnePtTop {
	/**
	 * return the top point
	 * @param image: image that needs to be processed
	 * @return Point Object which is the top point
	 */
	public static Point findTopPoint(BufferedImage image) {

		GreenImageProcesser processer = new GreenImageProcesser(-.5f, 1.0f, -.5f, 0.5);
		float[][] processedImage = processer.process(image);
		ArrayList<Point> bestTopPts = new ArrayList<Point>();
		int success = 0;
		int failed = 0;
		int startPoint = 0;
		int endPoint = 0;
		for (int y = 0; y < processedImage[0].length ; y++)
			for (int x = 0; x < processedImage.length ; x++) {
				if (bestTopPts.isEmpty()) {

					if (failed < 4 && processedImage[x][y] == 1) {
						success++;
						failed = 0;
						if (success == 1)
							startPoint = x;
					} else if (failed < 4) {
						if (success != 0)
							failed++;
					} else if (success < 4) {
						success = 0;
						failed = 0;
						startPoint = 0;
					} else {
						success = 0;
						failed = 0;
						endPoint = x - 4;
					}
					if (startPoint != 0 && endPoint != 0) {
						int midPoint = (startPoint + endPoint) / 2;
						int topCheck = 0;
						int botCheck = 0;
						if (midPoint != 0) {
							for (int i = 1; i < 4; i++) {

								if (y>3)
								if (processedImage[midPoint][y - i] == 0) {
									topCheck++;
								}
								if (y<processedImage[0].length-3)
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

		if (bestTopPts.size() > 0) {
			return bestTopPts.get(0);
		} else {
			return new Point(0, 0);
		}

	}
}
