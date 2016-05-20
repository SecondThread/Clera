package templateMatching;

import java.awt.Point;

import processing.ImageProcessor;

public class Template {
	private float[][] template;
	
	public Template(String saveData) {
		String[] parts=saveData.split(" ");
		int width=Integer.parseInt(parts[0]);
		int height=Integer.parseInt(parts[1]);
		template=new float[width][height];
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				template[x][y]=Float.parseFloat(parts[x*width+y+2]);
			}
		}
	}
	
	public Template(float[][] image, Point topLeft, Point bottomRight) {
		template=new float[bottomRight.x-topLeft.x][bottomRight.y-topLeft.y];
		for (int x=topLeft.x; x<bottomRight.x; x++) {
			for (int y=topLeft.y; y<bottomRight.y; y++) {
				template[x-topLeft.x][y-topLeft.y]=image[x][y];
			}
		}
	}
	
	private Template(float[][] rawImage) {
		this.template=rawImage;
	}
	
	//returns the percent match of the template
	public float matchTemplate(float[][] image, int topLeftX, int topLeftY) {
		
		if (template.length+topLeftX>=image.length||template[0].length+topLeftY>=image[0].length) {
			return 0;
		}
		float[][] toMatch=ImageProcessor.normalize(image, new Point(topLeftX, topLeftY), new Point(topLeftX+template.length, topLeftY+template[0].length));
		
		float totalError=0;
		for (int x=0; x<template.length; x++) {
			for (int y=0; y<template[x].length&&y+topLeftY<image[x].length; y++) {
				totalError+=Math.abs(toMatch[x][y]-template[x][y]);
			}
		}
		return 1-totalError/(image.length*image[0].length);
	}
	
	public String toString() {
		//width height 0 0 0 0 0 0 0 
		String toReturn=""+template.length+" "+template[0].length+" ";
		for (int x=0; x<template.length; x++) {
			for (int y=0; y<template.length; y++) {
				toReturn+=template[x][y]+" ";
			}
		}
		return toReturn;
	}

	public float[][] getTemplate() {
		return template;
	}
	
	public void normalize() {
		ImageProcessor.normalize(template);
	}
}
