package images;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageLoader {
	private static BufferedImage loadImage(String path) {
		 URL url;
		 BufferedImage imageRead=null;
		try {
			imageRead=ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageRead;
	}
	
	public static void insertImage(String pathToImage, Color[][] background, Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
		BufferedImage image=loadImage(pathToImage);
		for (int x=0; x<image.getWidth(); x++) {
			for (int y=0; y<image.getHeight(); y++) {
				float xPercent=(float)x/image.getWidth(), yPercent=(float)y/image.getHeight();
				float leftMiddleBarX=topLeft.x*(1-yPercent)+bottomLeft.x*yPercent, leftMiddleBarY=topLeft.y*(1-yPercent)+bottomLeft.y*yPercent;
				float rightMiddleBarX=topRight.x*(1-yPercent)+bottomRight.x*yPercent, rightMiddleBarY=topRight.y*(1-yPercent)+bottomRight.y*yPercent;
				float newX=leftMiddleBarX*(1-xPercent)+rightMiddleBarX*xPercent, newY=leftMiddleBarY*(1-xPercent)+rightMiddleBarY*xPercent;
				background[(int)newX][(int)newY]=new Color(image.getRGB(x, y));
			}
		}
	}
}
