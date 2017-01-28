package images;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import robot.Main;

public class ImageLoader {
	public static BufferedImage loadImage(String path) {
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
	
	public static void drawImageWithAlpha(Color[][] image, Color[][] background, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
		boolean[][] drewTo=new boolean[background.length][background[0].length];
		for (int x=0; x<drewTo.length; x++) {
			for (int y=0; y<drewTo[x].length; y++) {
				drewTo[x][y]=false;
			}
		}
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				float xPercent=(float)x/image.length, yPercent=(float)y/image[x].length;
				float leftMiddleBarX=topLeft.x*(1-yPercent)+bottomLeft.x*yPercent, leftMiddleBarY=topLeft.y*(1-yPercent)+bottomLeft.y*yPercent;
				float rightMiddleBarX=topRight.x*(1-yPercent)+bottomRight.x*yPercent, rightMiddleBarY=topRight.y*(1-yPercent)+bottomRight.y*yPercent;
				float newX=leftMiddleBarX*(1-xPercent)+rightMiddleBarX*xPercent, newY=leftMiddleBarY*(1-xPercent)+rightMiddleBarY*xPercent;
				if (!drewTo[(int)newX][(int)newY]) {
					float a=image[x][y].getAlpha()/255f;
					float r=image[x][y].getRed()/255f*a+background[(int)newX][(int)newY].getRed()/255f*(1-a);
					float g=image[x][y].getGreen()/255f*a+background[(int)newX][(int)newY].getGreen()/255f*(1-a);
					float b=image[x][y].getBlue()/255f*a+background[(int)newX][(int)newY].getBlue()/255f*(1-a);
					background[(int)newX][(int)newY]=new Color(r, g, b);
					drewTo[(int)newX][(int)newY]=true;
				}
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

	public static Color[][] getPaper(Color[][] background, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
		int height=Math.max(bottomLeft.y-topLeft.y, bottomRight.y-topRight.y);
		int width=(int)(height*Main.paperWidthOverHeight);
		Color[][] paper=new Color[width][height];
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				float xPercent=x/(float)width;
				float yPercent=y/(float)height;
				float leftAlignX=bottomLeft.x*yPercent+topLeft.x*(1-yPercent);
				float leftAlignY=bottomLeft.y*yPercent+topLeft.y*(1-yPercent);
				float rightAlignX=bottomRight.x*yPercent+topRight.x*(1-yPercent);
				float rightAlignY=bottomRight.y*yPercent+topRight.y*(1-yPercent);
				int newX=(int)(rightAlignX*xPercent+leftAlignX*(1-xPercent));
				int newY=(int)(rightAlignY*xPercent+leftAlignY*(1-xPercent));
				paper[x][y]=background[newX][newY];
			}
		}
		return paper;
	}
	
}
