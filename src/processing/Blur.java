package processing;

public class Blur {

	private static int[][] smartGaussianFilter = { { 2, 4, 5, 4, 2 }, { 4, 9, 12, 9, 4 }, { 5, 12, 15, 12, 5 },
			{ 4, 9, 12, 9, 4 }, { 2, 4, 5, 4, 2 } };
	private static int smartGaussianFliterTotal = -1;

	@Deprecated
	public static void applyGaussianBlur(float[][] image, int size) {
		if (size < 1 || size > 10) {
			System.err.println("Error: Guassian blur size should be between 0 and 10, both exclusive");
		}

		float[][] blurredImage = new float[image.length][image[0].length];

		for (int x = 0; x < image.length; x++) {
			for (int y = 0; y < image[x].length; y++) {
				blurredImage[x][y] = applyBlurToPixel(image, x, y, size);
			}
		}

		for (int x = 0; x < image.length; x++) {
			for (int y = 0; y < image[x].length; y++) {
				image[x][y] = blurredImage[x][y];
			}
		}
	}

	@Deprecated
	private static float applyBlurToPixel(float[][] image, int x, int y, int size) {
		float total = 0, maxTotal = 0;
		if (size > x || size > y || x + size >= image.length || y + size >= image[0].length) {
			return 0f;
		}
		for (int dX = -size; dX <= size; dX++) {
			for (int dY = -size; dY <= size; dY++) {
				total += image[x + dX][y + dY] * (2 * size - Math.abs(dX) - Math.abs(dY));
				maxTotal += 2 * size - Math.abs(dX) - Math.abs(dY);
			}
		}
		return total / maxTotal;
	}

	/**
	 * 
	 * @param image
	 *            The luminance 2D array of the image
	 */
	public static void applySmartGaussianBlur(float[][] image) {
		if (smartGaussianFliterTotal == -1)
			setSmartGaussianFilterTotal();

		float[][] blurredImage = new float[image.length][image[0].length];

		for (int x = 0; x < image.length; x++) {
			for (int y = 0; y < image[x].length; y++) {
				blurredImage[x][y] = applySmartGaussianBlurToPixels(image, x, y);
			}
		}

		for (int x = 0; x < image.length; x++) {
			for (int y = 0; y < image[x].length; y++) {
				image[x][y] = blurredImage[x][y];
			}
		}
	}

	private static float applySmartGaussianBlurToPixels(float[][] image, int x, int y) {
		if (3 > x || 3 > y || x + 3 >= image.length || y + 3 >= image[0].length) {
			return 0;// image[x][y];
		}
		int total = 0;
		for (int dx = 0; dx < 5; dx++) {
			for (int dy = 0; dy < 5; dy++) {
				total += image[x + dx - 2][y + dy - 2] * smartGaussianFilter[dx][dy];
			}
		}

		return (float) total / smartGaussianFliterTotal;
	}

	private static void setSmartGaussianFilterTotal() {
		smartGaussianFliterTotal = 0;
		for (int[] a : smartGaussianFilter) {
			for (int b : a) {
				smartGaussianFliterTotal += b;
			}
		}
	}

	public static void squareBlur(float[][] image, int size) {
		float[][] blurredImage = new float[image.length][image[0].length];
		double total = 0;

		// Horizontal blurring
		for (int y = 0; y < image[0].length; y++) {
			for (int x = 0; x < image.length; x++) {
				total = 0;
				if (2 * x < size) {
					total += Math.ceil((size - 2 * x) / 2) * image[0][y];
					for (int cur = x + 1; cur <= x + size / 2; cur++) {
						total += image[x][y];
					}
				}
				if (2 * (image[0].length - 1) < size) {
					total += Math.ceil(size - 2 * (image[0].length - 1 - x) / 2) * image[image.length - 1][y];
					for (int cur = x - size / 2; cur <= x; cur++) {
						total += image[x][y];
					}
				}
				for (int cur = x - size / 2; cur <= x + size / 2; cur++) {
					total += image[x][y];
				}
				blurredImage[x][y] = (float) total / size;
			}
		}

		// Vertical Blurring
		for (int x = 0; x < blurredImage[0].length; x++) {
			for (int y = 0; y < blurredImage.length; y++) {
				total = 0;
				if (2 * y < size) {
					total += Math.ceil((size - 2 * y) / 2) * blurredImage[0][x];
					for (int cur = y + 1; cur <= y + size / 2; cur++) {
						total += blurredImage[x][y];
					}
				}
				if (2 * (blurredImage[0].length - 1) < size) {
					total += Math.ceil(size - 2 * (blurredImage[0].length - 1 - y) / 2)
							* blurredImage[blurredImage.length - 1][x];
					for (int cur = y - size / 2; cur <= y; cur++) {
						total += blurredImage[x][y];
					}
				}
				for (int cur = y - size / 2; cur <= y + size / 2; cur++) {
					total += blurredImage[x][y];
				}
				blurredImage[x][y] = (float) total / size;
			}
		}
		image = blurredImage;
	}
}
