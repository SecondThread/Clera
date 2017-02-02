package processing;

import java.awt.Point;

public class ShooterVisionUtils {
	public static Point brightestPoint(float[][] image) {
		Point max = new Point(0, 0);
		float maxLuminance = image[0][0];
		for(int x = 0; x < image[0].length; x++) {
			for(int y = 0; y < image.length; y++)	 {
				if(image[x][y] > maxLuminance) {
					maxLuminance = image[x][y];
					max = new Point(x, y);
				}
			}
		}
		return max;
	}
}
