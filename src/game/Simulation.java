package game;

import java.awt.Color;

import processing.Edge;
import processing.EdgeDetection;
import robot.Window;

public class Simulation {
	private float[][] paper;
	private Edge[][] edges;
	private Ball ball;
	private boolean showLines=true;
	
	public Simulation(Color[][] paper) {
		this.paper=Window.getNonRedLuminanceOfImage(paper);
		edges=EdgeDetection.sobelEdgeDetection(this.paper);
		createBall(paper);
	}
	
	public Color[][] render() {
		Color[][] toReturn=Edge.toColorArray(edges);
		if (!showLines) {
			Color clear=new Color(0, 0, 0, 0);
			for (int x=0; x<toReturn.length; x++) {
				for (int y=0; y<toReturn[x].length; y++) {
					toReturn[x][y]=clear;
				}
			}
		}
		
		if (ball!=null) {
			ball.render(toReturn);
		}
		return toReturn;
	}
	
	private void createBall(Color[][] paperAsColor) {
		float[][] paper=Window.getNonRedLuminanceOfImage(paperAsColor);
		int minX=paper.length/2, y=paper[0].length/10;
		for (int x=paper.length/10; x<paper.length*9/10; x++) {
			if (paper[x][y]<paper[minX][y]) {
				minX=x;
			}
		}
		ball=new Ball(minX, 2*y);
	}
	
	public void update(Color[][] paperAsColor) {
		if (ball!=null) {
			ball.update(edges);
		}
	}

}
