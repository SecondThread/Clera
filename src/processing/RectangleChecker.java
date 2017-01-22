package processing;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

public class RectangleChecker {

	private ArrayList<Point> points, newPoints;
	

	public RectangleChecker(ArrayList<Point> somePoints) {
		points = somePoints;
		newPoints = new ArrayList<Point>();
	}

	/**
	 * generates the 16 possible rectangles that can be made from the 8 points
	 * 
	 * @return an ArrayList of Point[]'s that hold the four points in the
	 *         rectangle
	 */
	private ArrayList<Point[]> generateRectangles() {
		/*
		 * this is what the 16 rectangles will look like as Point[]'s
		 * java.awt.Point[x=362,y=144]java.awt.Point[x=362,y=335]java.awt.Point[
		 * x=772,y=140]java.awt.Point[x=772,y=337]
		 * java.awt.Point[x=362,y=144]java.awt.Point[x=362,y=335]java.awt.Point[
		 * x=772,y=140]java.awt.Point[x=429,y=335]
		 * java.awt.Point[x=362,y=144]java.awt.Point[x=362,y=335]java.awt.Point[
		 * x=429,y=144]java.awt.Point[x=772,y=337]
		 * java.awt.Point[x=362,y=144]java.awt.Point[x=362,y=335]java.awt.Point[
		 * x=429,y=144]java.awt.Point[x=429,y=335]
		 * java.awt.Point[x=362,y=144]java.awt.Point[x=710,y=337]java.awt.Point[
		 * x=772,y=140]java.awt.Point[x=772,y=337]
		 * java.awt.Point[x=362,y=144]java.awt.Point[x=710,y=337]java.awt.Point[
		 * x=772,y=140]java.awt.Point[x=429,y=335]
		 * java.awt.Point[x=362,y=144]java.awt.Point[x=710,y=337]java.awt.Point[
		 * x=429,y=144]java.awt.Point[x=772,y=337]
		 * java.awt.Point[x=362,y=144]java.awt.Point[x=710,y=337]java.awt.Point[
		 * x=429,y=144]java.awt.Point[x=429,y=335]
		 * java.awt.Point[x=145,y=31]java.awt.Point[x=362,y=335]java.awt.Point[x
		 * =772,y=140]java.awt.Point[x=772,y=337]
		 * java.awt.Point[x=145,y=31]java.awt.Point[x=362,y=335]java.awt.Point[x
		 * =772,y=140]java.awt.Point[x=429,y=335]
		 * java.awt.Point[x=145,y=31]java.awt.Point[x=362,y=335]java.awt.Point[x
		 * =429,y=144]java.awt.Point[x=772,y=337]
		 * java.awt.Point[x=145,y=31]java.awt.Point[x=362,y=335]java.awt.Point[x
		 * =429,y=144]java.awt.Point[x=429,y=335]
		 * java.awt.Point[x=145,y=31]java.awt.Point[x=710,y=337]java.awt.Point[x
		 * =772,y=140]java.awt.Point[x=772,y=337]
		 * java.awt.Point[x=145,y=31]java.awt.Point[x=710,y=337]java.awt.Point[x
		 * =772,y=140]java.awt.Point[x=429,y=335]
		 * java.awt.Point[x=145,y=31]java.awt.Point[x=710,y=337]java.awt.Point[x
		 * =429,y=144]java.awt.Point[x=772,y=337]
		 * java.awt.Point[x=145,y=31]java.awt.Point[x=710,y=337]java.awt.Point[x
		 * =429,y=144]java.awt.Point[x=429,y=335]done...
		 */

		ArrayList<Point[]> rectangles = new ArrayList<Point[]>();
		for (int i = 0; i < 2; i++) {
			for (int j = 2; j < 4; j++) {
				for (int n = 4; n < 6; n++) {
					for (int m = 6; m < 8; m++) {
						Point[] rectanglePoints = new Point[] { points.get(i), points.get(j), points.get(n),
								points.get(m) };
						rectangles.add(rectanglePoints);
					}
				}
			}
		}

		return rectangles;
	}

	private ArrayList<Polygon[]> generateTriangles() {
		ArrayList<Point[]> rectangles = generateRectangles();
		ArrayList<Polygon[]> triangleGroups = new ArrayList<Polygon[]>();

		for (Point[] rect : rectangles) {
			Polygon[] similiarTriangles = new Polygon[4];
			for (int i = 0; i < 4; i++) {
				Polygon triangle = new Polygon();
				for (int n = 0; n < 4; n++) {
					if (n != i) {
						triangle.addPoint((int) rect[n].getX(), (int) rect[n].getY());
					}
				}
				similiarTriangles[i] = triangle;
			}
			triangleGroups.add(similiarTriangles);
		}
		return triangleGroups;
	}

	public boolean validatePoints() {
		ArrayList<Polygon[]> triangles = generateTriangles();
		boolean hasOneGoodTriangle = false;
		boolean hasTwoGoodTriangle = false;
		boolean horizontalAllign = false;
		boolean similiarAreas = false;

		Rectangle goodRect1 = new Rectangle();
		double heightWidthRatio1 = 0;
		double area1 = 0;

		Rectangle goodRect2 = new Rectangle();
		double heightWidthRatio2 = 0;
		double area2 = 0;

		// i have no idea what a real tolerance would be
		double tolerance = .2;
		double alignmentTolerance = 50;
		double areaTolerance = 1000000;

		for (Polygon[] g : triangles) {
			if (hasOneGoodTriangle && hasTwoGoodTriangle) {
				break;
			}
			for (Polygon t : g) {
				Rectangle bounds = t.getBounds();
				if(area1 == (bounds.getHeight()*bounds.getWidth())) {
					break;
				}
				if (Math.abs((bounds.getHeight() / bounds.getWidth()) - 2.5) < tolerance) {
					if (hasOneGoodTriangle) {
						hasTwoGoodTriangle = true;
						goodRect2 = bounds;
						heightWidthRatio2 = (bounds.getHeight() / bounds.getWidth());
						area2 = bounds.getHeight() * bounds.getWidth();
					} else {
						hasOneGoodTriangle = true;
						goodRect1 = bounds;
						heightWidthRatio1 = (bounds.getHeight() / bounds.getWidth());
						area1 = bounds.getHeight() * bounds.getWidth();
					}
					break;
				}
			}
		}

		// find if the two rectangles line up horizontally more or less

		horizontalAllign = Math.abs(goodRect1.getCenterY() - goodRect2.getCenterY()) < alignmentTolerance;
		similiarAreas = Math.abs(area2-area1) < areaTolerance;

		System.out.println("goodRect1    " + goodRect1.toString());
		System.out.println("Height:Width ratio  " + heightWidthRatio1);
		System.out.println("Area of rect  " + area1);
		System.out.println("goodRect2    " + goodRect2.toString());
		System.out.println("Height:Width ratio  " + heightWidthRatio2);
		System.out.println("Area of rect  " + area2);
		System.out.println();
		System.out.println("Aligned? " + horizontalAllign);
		System.out.println("Alignment off by how much? " + (goodRect1.getCenterY() - goodRect2.getCenterY()));
		System.out.println();
		System.out.println("Similiar area? " + similiarAreas);
		System.out.println("Area diff:  " + (Math.abs(area1-area2)));
		
		newPoints.add(new Point(goodRect1.x,goodRect1.y));//left top corner
		newPoints.add(new Point(goodRect1.x + goodRect1.width,goodRect1.y));//top right corner
		newPoints.add(new Point(goodRect1.x,goodRect1.y+goodRect1.height));//bottom left corner
		newPoints.add(new Point(goodRect1.x+goodRect1.width,goodRect1.y+goodRect1.height));//bottom right corner
		
		newPoints.add(new Point(goodRect2.x,goodRect2.y));//left top corner
		newPoints.add(new Point(goodRect2.x + goodRect2.width,goodRect2.y));//top right corner
		newPoints.add(new Point(goodRect2.x,goodRect2.y+goodRect2.height));//bottom left corner
		newPoints.add(new Point(goodRect2.x+goodRect2.width,goodRect2.y+goodRect2.height));//bottom right corner
	
		

		return (hasOneGoodTriangle && hasTwoGoodTriangle && horizontalAllign && similiarAreas);
		
		

	}
	
	public ArrayList<Point> getBetterPoints()
	{
		if(validatePoints()) {
			return newPoints;
		}
		else {
			return points;
		}
	}
	
	
}