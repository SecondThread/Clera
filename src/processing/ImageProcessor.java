package processing;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Scanner;

public class ImageProcessor {

	public static void applySquareCurve(float[][] oldImage) {
		applyExponentialCurve(oldImage, 2);
	}

	/**
	 * Checks to see if each of the pixels are brighter or less bright than the
	 * minimum brightness If they are, they are set to aboveIsTrue, otherwise
	 * they are set to !aboveIsTrue
	 * 
	 * @param min
	 *            The minimum value
	 * @param aboveIsTrue
	 *            True if large values should be true, false othewise
	 * @return A new boolean array
	 */
	public static boolean[][] applyCutoff(float min, boolean aboveIsTrue, float[][] oldImage) {
		boolean[][] toReturn=new boolean[oldImage.length][oldImage[0].length];
		for (int x=0; x<oldImage.length; x++) {
			for (int y=0; y<oldImage[x].length; y++) {
				toReturn[x][y]=(oldImage[x][y]<min)^aboveIsTrue;
			}
		}
		return toReturn;
	}

	public static float[][] getAsFloatArray(boolean[][] boolArray) {
		float[][] floatArray=new float[boolArray.length][boolArray[0].length];
		for (int x=0; x<floatArray.length; x++) {
			for (int y=0; y<floatArray[x].length; y++) {
				if (boolArray[x][y]) {
					floatArray[x][y]=1f;
				}
				else {
					floatArray[x][y]=0f;
				}
			}
		}
		return floatArray;
	}

	public static void applyExponentialCurve(float[][] oldImage, float exponent) {
		for (int x=0; x<oldImage.length; x++) {
			for (int y=0; y<oldImage[x].length; y++) {
				oldImage[x][y]=(float)Math.pow(oldImage[x][y], exponent);
			}
		}
	}

	public static float[][] scaleImage(float[][] oldImage, int newWidth) {
		int newHeight=newWidth*oldImage[0].length/oldImage.length;
		float[][] newImage=new float[newWidth][newHeight];
		for (int x=0; x<newImage.length; x++) {
			for (int y=0; y<newImage[x].length; y++) {
				newImage[x][y]=oldImage[(int)((0.0+x)/newWidth*oldImage.length)][(int)((0.0+y)/newHeight
						*oldImage[0].length)];
			}
		}
		return newImage;
	}

	public static Color[][] scaleImageNearestNeighbor(Color[][] oldImage, int newWidth) {
		int newHeight=newWidth*oldImage[0].length/oldImage.length;
		Color[][] newImage=new Color[newWidth][newHeight];
		for (int x=0; x<newImage.length; x++) {
			for (int y=0; y<newImage[x].length; y++) {
				newImage[x][y]=oldImage[(int)((0.0+x)/newWidth*oldImage.length)][(int)((0.0+y)/newHeight
						*oldImage[0].length)];
			}
		}
		return newImage;
	}

	public static Color[][] scaleImage(Color[][] oldImage, int newWidth) {

		BufferedImage old=new BufferedImage(oldImage.length, oldImage[0].length, BufferedImage.TYPE_INT_RGB);

		// Set each pixel of the BufferedImage to the color from the Color[][].
		for (int x=0; x<oldImage.length; x++) {
			for (int y=0; y<oldImage[x].length; y++) {
				old.setRGB(x, y, oldImage[x][y].getRGB());
			}
		}
		int newHeight=newWidth*oldImage[0].length/oldImage.length;
		BufferedImage scaled=new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		AffineTransform at=new AffineTransform();
		at.scale(newWidth/(double)old.getWidth(), newHeight/(double)old.getHeight());
		AffineTransformOp scaleOp=new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		scaled=scaleOp.filter(old, scaled);

		Color[][] result=new Color[newWidth][newHeight];
		for (int x=0; x<newWidth; x++) {
			for (int y=0; y<newHeight; y++) {
				result[x][y]=new Color(scaled.getRGB(x, y));
			}
		}
		return result;
	}

	public static void normalize(float[][] image) {
		float max=0, min=1;
		for (float[] x : image) {
			for (float y : x) {
				max=Math.max(y, max);
				min=Math.min(y, min);
			}
		}
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				float oldValue=image[x][y];
				image[x][y]=(oldValue-min)/(max-min);
			}
		}
	}

	public static float[][] normalize(float[][] image, Point startPoint, Point endPoint) {
		float[][] toReturn=new float[endPoint.x-startPoint.x][endPoint.y-startPoint.y];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				toReturn[x][y]=image[x+startPoint.x][y+startPoint.y];
			}
		}
		normalize(toReturn);
		return toReturn;
	}

	public static float[][] luminance(Color[][] colors, float rScale, float gScale, float bScale) {
		float[][] luminance=new float[colors.length][colors[0].length];
		for (int x=0; x<colors.length; x++) {
			for (int y=0; y<colors[0].length; y++) {
				luminance[x][y]=Math.max(
						colors[x][y].getRed()*rScale+colors[x][y].getGreen()*gScale+colors[x][y].getBlue()*bScale, 0);
			}
		}
		return luminance;
	}

	public static String convertToString(Color[][] image) {
		StringBuilder colors=new StringBuilder("");
		colors.append(image.length);
		colors.append("R");
		colors.append(image[0].length);
		colors.append("C");
		for (int r=0; r<image.length; r++) {
			for (int c=0; c<image[0].length; c++) {
				Color cur=image[r][c];
				colors.append(cur.getRed());
				colors.append(",");
				colors.append(cur.getGreen());
				colors.append(",");
				colors.append(cur.getBlue());
				colors.append("-");
			}
			colors.append("|");
		}
		return colors.toString();
	}

	public static Color[][] convertToColorArray(String colors) {
		Color[][] image;
		int rows=0;
		int columns=0;
		int curRow=0;
		int curColumn=0;
		int cur=0;
		StringBuilder curString=new StringBuilder("");
		for (cur=0; cur<colors.length()&&(rows==0||columns==0); cur++) {
			char curChar=colors.charAt(cur);
			if (curChar=='R') {
				rows=Integer.parseInt(curString.toString());
				curString=new StringBuilder("");
				continue;
			}
			if (curChar=='C') {
				columns=Integer.parseInt(curString.toString());
				curString=new StringBuilder("");
				continue;
			}
			curString.append(curChar);
		}

		image=new Color[rows][columns];
		Color[] curRowValue=new Color[columns];
		for (cur=cur; cur<colors.length(); cur++) {
			char curChar=colors.charAt(cur);
			if (curChar=='-') {
				String[] curColor=curString.toString().split(",");
				int red=Integer.parseInt(curColor[0]);
				int green=Integer.parseInt(curColor[1]);
				int blue=Integer.parseInt(curColor[2]);
				curRowValue[curColumn]=new Color(red, green, blue);
				curString=new StringBuilder("");
				curColumn++;
				continue;
			}
			if (curChar=='|') {
				image[curRow]=Arrays.copyOf(curRowValue, curRowValue.length);
				curRow++;
				curColumn=0;
				curString=new StringBuilder("");
				continue;
			}
			curString.append(curChar);
		}

		return image;
	}
}