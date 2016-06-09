package game;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import physics.Vector2Editable;
import processing.Edge;

public class Ball {
	private double x, y;
	private int radius=4;
	private Color color=Color.blue;
	private ArrayList<PastLocations> pastLocations=new ArrayList<PastLocations>(); 
	private int tailLength=200;
	
	private Vector2Editable velocity=new Vector2Editable(x, y);
	private double gravity=-.3, friction=0.03f, bounceFriction=.93;
	
	public Ball(int x, int y) {
		this.x=x;
		this.y=y;
		System.out.println("Ball created at "+x+" "+y);
	}
	
	public void update(Edge[][] edges) {
		velocity.setY(velocity.getY()+gravity);
		applyFriction();
		move(edges);
	}
	
	public void render(Color[][] background) {
		double alpha=0;
		for (PastLocations pl:pastLocations) {
			if (pl==null)
				continue;
			for (int x=(int)(pl.x)-radius; x<(int)(pl.x)+radius+1; x++) {
				for (int y=(int)(pl.y)-radius; y<(int)(pl.y)+radius+1; y++) {
					if (x<0||x>=background.length||y<0||y>=background[x].length) {
						continue;
					}
					if (Point.distance(pl.x, pl.y, x, y)<=radius) {
						background[x][y]=new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha*255));
					}
				}
			}
			alpha+=1.0/pastLocations.size();
		}
	}
	
	private void move(Edge[][] edges) {
		for (int i=0; i<20; i++) {
			x+=velocity.getX()/20;
			y-=velocity.getY()/20;
			if (y>edges[0].length+radius) {
				y=2*radius;
			}
			if (touchingEdge(edges)) {
				double angle=getAverageAngle(edges);
				reflectOffAngle(angle);
				moveUntilNotColliding(edges);
			}
			pastLocations.add(new PastLocations(x, y));
			while (pastLocations.size()>tailLength)pastLocations.remove(0);
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
		return Math.atan2(totalSin, totalCos)+Math.PI/2;
	}
	
	private void reflectOffAngle(double angle) {
		double angleOfIncidence=angle-(velocity.getAngle()+Math.PI);
		double reflectionAngle=angle+angleOfIncidence;
		velocity=Vector2Editable.createFromPolar(velocity.getLength()*bounceFriction, reflectionAngle);
	}

	private void moveUntilNotColliding(Edge[][] edges) {
		while (touchingEdge(edges)) {
			x+=velocity.getX()/20;
			y-=velocity.getY()/20;
		}
	}

	private void applyFriction() {
		if (velocity.getX()>0) {
			velocity.setX(velocity.getX()-friction);
		}
		else {
			velocity.setX(velocity.getX()+friction);
		}
		if (velocity.getY()>0) {
			//velocity.setY(velocity.getY()-friction);
		}
		else {
			//velocity.setY(velocity.getY()+friction);
		}
	}
}

class PastLocations {
	public double x, y;
	
	public PastLocations(double x, double y) {
		this.x=x;
		this.y=y;
	}
}