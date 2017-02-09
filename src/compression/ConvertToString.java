package compression;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import robot.Window;

public class ConvertToString {
	
	public static void main(String[] args) {
		try {
			BufferedImage image=ImageIO.read(new File("face.png"));
			Color[][] asColors=new Color[image.getWidth()][image.getHeight()];

			for (int x=0; x<asColors.length; x++) {
				for (int y=0; y<asColors[x].length; y++) {
					asColors[x][y]=new Color(image.getRGB(x, y));
				}
			}
			long start=System.currentTimeMillis();
			String asString=convertToString(asColors);
			asColors=convertBack(asString);
			long end=System.currentTimeMillis();
			System.out.println(end-start);
			Window.displayPixels(asColors, "test");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String convertToString(Color[][] image) {
		StringBuilder toReturn=new StringBuilder("");
		toReturn.append(image.length+" "+image[0].length+" ");
		char red, green, blue;
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				red=(char)(image[x][y].getRed()/2);
				green=(char)(image[x][y].getGreen()/2);
				blue=(char)(image[x][y].getBlue()/2);
				toReturn.append(red);
				toReturn.append(green);
				toReturn.append(blue);
			}
		}
		return toReturn.toString();
	}
	
	public static Color[][] convertBack(String image) {
		String first=image.substring(0, image.indexOf(' '));
		image=image.substring(image.indexOf(' ')+1);
		String second=image.substring(0, image.indexOf(' '));
		image=image.substring(image.indexOf(' ')+1);
		String imageString=image;
		int width=Integer.parseInt(first);
		int height=Integer.parseInt(second);
		
		Color[][] toReturn=new Color[width][height];
		int index=0;
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				index=(x*height+y)*3;
				toReturn[x][y]=new Color(2*imageString.charAt(index), 2*imageString.charAt(index+1), 2*imageString.charAt(index+2));
			}
		}
		return toReturn;
	}
}
