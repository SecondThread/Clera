package compression;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import robot.Window;

public class ConvertToString {

	public static String convertToString(Color[][] image) {
		StringBuilder toReturn = new StringBuilder("");
		toReturn.append(image.length + " " + image[0].length + " ");
		for (int x = 0; x < image.length; x++) {
			for (int y = 0; y < image[x].length; y = y + 2) {
					char redGreen = (char) ((image[x][y].getRed() << 8) + image[x][y].getGreen());
					char blueRed = (char) ((image[x][y].getBlue() << 8) + image[x][y + 1].getRed());
					char greenBlue = (char) ((image[x][y + 1].getGreen() << 8) + image[x][y + 1].getBlue());
					toReturn.append(redGreen);
					toReturn.append(blueRed);
					toReturn.append(greenBlue);
			}
		}
		return toReturn.toString();
	}

	public static Color[][] convertBack(String image) {
		String first = image.substring(0, image.indexOf(' '));
		image = image.substring(image.indexOf(' ') + 1);
		String second = image.substring(0, image.indexOf(' '));
		image = image.substring(image.indexOf(' ') + 1);
		String imageString = image;
		int width = Integer.parseInt(first);
		int height = Integer.parseInt(second);

		Color[][] toReturn = new Color[width][height];
		int index = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y = y + 2) {
					toReturn[x][y] = new Color((imageString.charAt(index) & 65280) >> 8, imageString.charAt(index) & 255,
							(imageString.charAt(index + 1) & 65280) >> 8);
					toReturn[x][y + 1] = new Color(imageString.charAt(index + 1) & 255,
							(imageString.charAt(index + 2) & 65280) >> 8, imageString.charAt(index + 2) & 255);
					index += 3;
			}
		}
		return toReturn;
	}

}

/*
 * Billy's old conversion code public static String convertToString(Color[][]
 * image) { StringBuilder colors=new StringBuilder("");
 * colors.append(image.length); colors.append("R");
 * colors.append(image[0].length); colors.append("C"); for (int r=0;
 * r<image.length; r++) { for (int c=0; c<image[0].length; c++) { Color
 * cur=image[r][c]; colors.append(cur.getRed()); colors.append(",");
 * colors.append(cur.getGreen()); colors.append(",");
 * colors.append(cur.getBlue()); colors.append("-"); } colors.append("|"); }
 * return colors.toString(); }
 * 
 * public static Color[][] convertToColorArray(String colors) { Color[][] image;
 * int rows=0; int columns=0; int curRow=0; int curColumn=0; int cur=0;
 * StringBuilder curString=new StringBuilder(""); for (cur=0;
 * cur<colors.length()&&(rows==0||columns==0); cur++) { char
 * curChar=colors.charAt(cur); if (curChar=='R') {
 * rows=Integer.parseInt(curString.toString()); curString=new StringBuilder("");
 * continue; } if (curChar=='C') {
 * columns=Integer.parseInt(curString.toString()); curString=new
 * StringBuilder(""); continue; } curString.append(curChar); }
 * 
 * image=new Color[rows][columns]; Color[] curRowValue=new Color[columns]; for
 * (; cur<colors.length(); cur++) { char curChar=colors.charAt(cur); if
 * (curChar=='-') { String[] curColor=curString.toString().split(","); int
 * red=Integer.parseInt(curColor[0]); int green=Integer.parseInt(curColor[1]);
 * int blue=Integer.parseInt(curColor[2]); curRowValue[curColumn]=new Color(red,
 * green, blue); curString=new StringBuilder(""); curColumn++; continue; } if
 * (curChar=='|') { image[curRow]=Arrays.copyOf(curRowValue,
 * curRowValue.length); curRow++; curColumn=0; curString=new StringBuilder("");
 * continue; } curString.append(curChar); }
 * 
 * return image; }
 */
