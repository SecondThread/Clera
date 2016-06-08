package game;

import java.awt.Color;

import processing.Edge;
import processing.EdgeDetection;
import robot.Window;

public class Simulation {
	private float[][] paper;
	private float[][] edges;
	private float cutoff=.07f;
	public Simulation(Color[][] paper) {
		this.paper=Window.getNonRedLuminanceOfImage(paper);
		edges=Edge.toFloatArray(EdgeDetection.sobelEdgeDetection(this.paper));
		double max=0;
		for (float[] a:edges)
			for (float b:a) {
				max=Math.max(max, b);
			}
		System.out.println("\t"+max);
	}
	
	public Color[][] render() {
		Color[][] toReturn=new Color[edges.length][edges[0].length];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				if (edges[x][y]>cutoff) {
					toReturn[x][y]=Color.blue;
				}
				else {
					toReturn[x][y]=new Color(0, 0, 0, 0);
				}
			}
		}
		return toReturn;
	}
}
