package game;

import java.awt.Color;

import processing.Edge;
import processing.EdgeDetection;
import robot.Window;

public class Simulation {
	private float[][] paper;
	private Edge[][] edges;
	private Ball ball;
	
	public Simulation(Color[][] paper) {
		this.paper=Window.getNonRedLuminanceOfImage(paper);
		edges=EdgeDetection.sobelEdgeDetection(this.paper);
		createBall(paper);
	}
	
	public Color[][] render() {
		Color[][] toReturn=Edge.toColorArray(edges);
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
		ball=new Ball(minX, 10);
	}
	
	public void update(Color[][] paperAsColor) {
		if (ball!=null) {
			ball.update(edges);
		}
	}

}
