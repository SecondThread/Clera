package processing;

public class EdgeDetection {
	private static float minIntensity=.07f;//.07f;
	private static float[][] sobelXKernal={{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
	private static float[][] sobelYKernal={{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
	
	//public static void cannyEdgeDetection(float[][] image) {
	//	Blur.applySmartGaussianBlur(image);
	//}
	
	public static Edge[][] sobelEdgeDetection(float[][] image) {
		Blur.applySmartGaussianBlur(image);
		float[][] sobelX=sobelXEdgeDetection(image);
		float[][] sobelY=sobelYEdgeDetection(image);
		Edge[][] sobelTotal=new Edge[image.length][image[0].length];
		for (int x=0; x<sobelTotal.length; x++) {
			for (int y=0; y<sobelTotal[x].length; y++) {
				float intensity=(float)Math.sqrt(sobelX[x][y]*sobelX[x][y]+sobelY[x][y]*sobelY[x][y]);
				float angle=0;
				if (sobelY[x][y]>0) {
					angle=(float)Math.atan(sobelX[x][y]/Math.max(0.001f, sobelY[x][y]));
				}
				else {
					angle=(float)Math.atan(sobelX[x][y]/Math.min(-0.001f, sobelY[x][y]));
				}
				if (intensity>minIntensity) sobelTotal[x][y]=new Edge(intensity, angle);
			}
		}
		return sobelTotal;
	}
	
	private static float[][] sobelXEdgeDetection(float[][] image) {
		float[][] toReturn=applyKernal(image, sobelXKernal);
		return toReturn;
	}
	
	private static float[][] sobelYEdgeDetection(float[][] image) {
		float[][] toReturn=applyKernal(image, sobelYKernal);
		return toReturn;
	}
	
	private static float[][] applyKernal(float[][] image, float[][] kernal) {
		float[][] toReturn=new float[image.length][image[0].length];
		float kernalTotal=0;
		for (float[] outer:kernal)
			for (float i:outer)
				kernalTotal+=Math.abs(i);
		for (int x=kernal.length; x<image.length-kernal.length; x++) {
			for (int y=kernal[0].length; y<image[x].length-kernal[0].length; y++) {
				toReturn[x][y]=applyKernalToLocation(image, kernal, x, y, kernalTotal);
			}
		}
		return toReturn;
	}
	
	private static float applyKernalToLocation(float[][] image, float[][] kernal, int middleX, int middleY, float kernalTotal) {
		float total=0;
		int startX=middleX-kernal.length/2;
		int startY=middleY-kernal[0].length/2;
		for (int x=0; x<kernal.length; x++) {
			for (int y=0; y<kernal[x].length; y++) {
				total+=kernal[x][y]*image[startX+x][startY+y];
			}
		}
		return total/kernalTotal;
		
	}
	
}
