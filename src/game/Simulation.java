package game;

import java.awt.Color;

import processing.Edge;
import processing.EdgeDetection;
import robot.Window;

public class Simulation {
	private float[][] paper;
	private Edge[][] edges;
	private float cutoff=.07f;
	public Simulation(Color[][] paper) {
		this.paper=Window.getNonRedLuminanceOfImage(paper);
		edges=EdgeDetection.sobelEdgeDetection(this.paper);
	}
	
	public Color[][] render() {
		return Edge.toColorArray(edges, cutoff);
	}
}
