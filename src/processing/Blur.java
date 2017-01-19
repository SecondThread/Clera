package processing;

public class Blur {
	
	private static int[][] smartGaussianFilter= {{2, 4, 5, 4, 2}, {4, 9, 12, 9, 4}, {5, 12, 15, 12, 5}, {4, 9, 12, 9, 4}, {2, 4, 5, 4, 2}};
	private static int smartGaussianFliterTotal=-1;
	
	@Deprecated
	//use square blur instead; it is much more efficient
	public static void applyGaussianBlur(float[][] image, int size) {
		if (size<1||size>10) {
			System.err.println("Error: Guassian blur size should be between 0 and 10, both exclusive");
		}
		
		float[][] blurredImage=new float[image.length][image[0].length];
		
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				blurredImage[x][y]=applyBlurToPixel(image, x, y, size);
			}
		}
		
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				image[x][y]=blurredImage[x][y];
			}
		}
	}
	
	@Deprecated
	private static float applyBlurToPixel(float[][] image, int x, int y, int size) {
		float total=0, maxTotal=0;
		if (size>x||size>y||x+size>=image.length||y+size>=image[0].length) {
			return 0f;
		}
		for (int dX=-size; dX<=size; dX++) {
			for (int dY=-size; dY<=size; dY++) {
				total+=image[x+dX][y+dY]*(2*size-Math.abs(dX)-Math.abs(dY));
				maxTotal+=2*size-Math.abs(dX)-Math.abs(dY);
			}
		}
		return total/maxTotal;
	}
	
	public static void applySmartGaussianBlur(float[][] image) {
		if (smartGaussianFliterTotal==-1)
			setSmartGaussianFilterTotal();
		
		float[][] blurredImage=new float[image.length][image[0].length];
		
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				blurredImage[x][y]=applySmartGaussianBlurToPixels(image, x, y);
			}
		}
		
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				image[x][y]=blurredImage[x][y];
			}
		}
	}
	
	private static float applySmartGaussianBlurToPixels(float[][] image, int x, int y) {
		if (3>x||3>y||x+3>=image.length||y+3>=image[0].length) {
			return 0;//image[x][y];
		}
		int total=0;
		for (int dx=0; dx<5; dx++) {
			for (int dy=0; dy<5; dy++) {
				total+=image[x+dx-2][y+dy-2]*smartGaussianFilter[dx][dy];
			}
		}
		
		return (float)total/smartGaussianFliterTotal;
	}
	
	private static void setSmartGaussianFilterTotal() {
		smartGaussianFliterTotal=0;
		for (int[] a:smartGaussianFilter) {
			for (int b:a) {
				smartGaussianFliterTotal+=b;
			}
		}
	}

	//TODO finish this
	public static void squareBlur(float[][] image, int size) {
		float[][] newImage=new float[image.length][image[0].length];
		for (int y=0; y<image[0].length; y++) {
			double total=(size-1)*image[0][y];
			for (int x=0; x<image.length+size; x++) {
				total+=image[Math.min(image.length-1, x)][y];
				total-=image[Math.max(0, x-size)][y];
				//newImage[x-size/2][y]=
			}
		}
	}

}
