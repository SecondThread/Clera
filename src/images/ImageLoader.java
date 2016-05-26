package images;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	private static BufferedImage loadImage(String path) {
		 BufferedImage imageRead=null;
		try {
			imageRead=ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageRead;
	}
	
	public static void drawImage(Color[][] image, Color[][] background, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				float xPercent=(float)x/image.length, yPercent=(float)y/image[x].length;
				float leftMiddleBarX=topLeft.x*(1-yPercent)+bottomLeft.x*yPercent, leftMiddleBarY=topLeft.y*(1-yPercent)+bottomLeft.y*yPercent;
				float rightMiddleBarX=topRight.x*(1-yPercent)+bottomRight.x*yPercent, rightMiddleBarY=topRight.y*(1-yPercent)+bottomRight.y*yPercent;
				float newX=leftMiddleBarX*(1-xPercent)+rightMiddleBarX*xPercent, newY=leftMiddleBarY*(1-xPercent)+rightMiddleBarY*xPercent;
				background[(int)newX][(int)newY]=image[x][y];
			}
		}
	}
	
	public static void drawImage(String pathToImage, Color[][] background, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
		BufferedImage image=loadImage(pathToImage);
		Color[][] imageArray=imageToArray(image);
		drawImage(imageArray, background, topLeft, topRight, bottomLeft, bottomRight);
	}
	
	public static Color[][] imageToArray(BufferedImage image) {
		Color[][] imageArray=new Color[image.getWidth()][image.getHeight()];
		for (int x=0; x<image.getWidth(); x++) {
			for (int y=0; y<image.getHeight(); y++) {
				imageArray[x][y]=new Color(image.getRGB(x, y));
			}
		}
		return imageArray;
	}
}
