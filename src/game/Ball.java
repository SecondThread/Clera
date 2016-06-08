package game;

import java.awt.Color;
import java.awt.Point;

import physics.Vector2Editable;
import processing.Edge;

public class Ball {
	private double x, y;
	private int radius=4;
	private Color color=Color.cyan;
	
	private Vector2Editable velocity=new Vector2Editable(x, y);
	private double gravity=.2;
	
	public Ball(int x, int y) {
		this.x=x;
		this.y=y;
		System.out.println("Ball created at "+x+" "+y);
	}
	
	public void update(Edge[][] edges) {
		velocity.setY(velocity.getY()+gravity);
		move(edges);
	}
	
	public void render(Color[][] background) {
		for (int x=(int)(this.x)-radius; x<(int)(this.x)+radius+1; x++) {
			for (int y=(int)(this.y)-radius; y<(int)(this.y)+radius+1; y++) {
				if (x<0||x>=background.length||y<0||y>=background[x].length) {
					continue;
				}
				if (Point.distance(this.x, this.y, x, y)<=radius) {
					background[x][y]=color;
				}
			}
		}
	}
	
	private void move(Edge[][] edges) {
		x+=velocity.getX();
		y+=velocity.getY();
		if (touchingEdge(edges)) {
			double angle=getAverageAngle(edges);
			reflectOffAngle(angle);
			moveUntilNotColliding(edges);
		}
	}
	
	private boolean touchingEdge(Edge[][] edges) {
		for (int x=(int)(this.x)-radius; x<(int)(this.x)+radius+1; x++) {
			for (int y=(int)(this.y)-radius; y<(int)(this.y)+radius+1; y++) {
				if (x<0||x>=edges.length||y<0||y>=edges[x].length) {
					continue;
				}
				if (Point.distance(this.x, this.y, x, y)<=radius) {
					if (edges[x][y]!=null)
						return true;
				}
			}
		}
		return false;
	}
	
	private double getAverageAngle(Edge[][] edges) {
		double totalSin=0, totalCos=0;
		for (int x=(int)(this.x)-radius; x<(int)(this.x)+radius+1; x++) {
			for (int y=(int)(this.y)-radius; y<(int)(this.y)+radius+1; y++) {
				if (x<0||x>=edges.length||y<0||y>=edges[x].length) {
					continue;
				}
				if (Point.distance(this.x, this.y, x, y)<=radius) {
					if (edges[x][y]!=null) {
						totalSin+=Math.sin(edges[x][y].angle);
						totalCos+=Math.cos(edges[x][y].angle);
					}
				}
			}
		}
		return Math.atan2(totalSin, totalCos);
	}
	
	private void reflectOffAngle(double angle) {
		double angleOfIncidence=angle-velocity.getAngle();
		double reflectionAngle=angle+angleOfIncidence;
		velocity=Vector2Editable.createFromPolar(velocity.getLength(), reflectionAngle);
	}

	private void moveUntilNotColliding(Edge[][] edges) {
		while (touchingEdge(edges)) {
			x+=velocity.getX()/20;
			y+=velocity.getY()/20;
		}
	}
}
