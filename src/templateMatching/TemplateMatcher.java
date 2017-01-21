package templateMatching;

public class TemplateMatcher {
	public static float[][] matchTemplate(float[][] image, float[][] template) {
		float[][] toReturn=new float[image.length][image[0].length];
		for (int imageX=0; imageX<image.length; imageX++) {
			for (int imageY=0; imageY<image[imageX].length; imageY++) {
				toReturn[imageX][imageY]=matchTemplate(imageX, imageY, image, template);
			}
		}
		return toReturn;
	}
	
	private static float matchTemplate(int x, int y, float[][] image, float[][] template) {
		float totalError=0;
		float maxError=template.length*template[0].length;
		for (int templateImageX=0; templateImageX<template.length; templateImageX++) {
			for (int templateImageY=0; templateImageY<template[templateImageX].length; templateImageY++) {
				int xToCheck=Math.min(image.length-1, x+templateImageX);
				int yToCheck=Math.min(image[0].length-1, y+templateImageY);
				totalError+=Math.abs(image[xToCheck][yToCheck]-template[templateImageX][templateImageY]);
			}
		}
		return 1-totalError/maxError;
	}
	
	public static int[] getBestPoint(float[][] image) {
		int[] toReturn=new int[2];
		float max=0;
		for (int x=0; x<image.length; x++) {
			for (int y=0; y<image[x].length; y++) {
				if (image[x][y]>max) {
					toReturn[0]=x;
					toReturn[1]=y;
					max=image[x][y];
				}
			}
		}
		return toReturn;
	}
	
	public static void destroyArea(float[][] image, int x, int y, int width) {
		for (int xx=x-width; xx<x+width; xx++) {
			for (int yy=y-width; yy<y+width; yy++) {
				int xToCheck=Math.min(image.length-1, Math.max(0, xx));
				int yToCheck=Math.min(image[0].length-1, Math.max(0, yy));
				image[xToCheck][yToCheck]=0;
			}
		}
	}
	
}
