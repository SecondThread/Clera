package processing;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class RectangleChecker {

	private ArrayList<Point> points, newPoints;
	private boolean validated;
	public final double HEIGHT_WIDTH_RATIO = 2.5, DISTANCE_WIDTH_RATIO = 4.125;
	public final double RATIO_TOLERANCE = .4, DISTANCE_TOLERANCE = .5, SLOPE_TOLERANCE = .6, ALIGNMENT_TOLERANCE = 50, AREA_TOLERANCE = 30000;

	public RectangleChecker(ArrayList<Point> somePoints) {
		points = somePoints;
		newPoints = new ArrayList<Point>();
		validated = false;
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

	private ArrayList<Rectangle> validateRectangles(ArrayList<Polygon[]> triangles) {
		ArrayList<Rectangle> goodRectangles = new ArrayList<Rectangle>();
		goodRectangles.add(new Rectangle());
		goodRectangles.add(new Rectangle());

		boolean hasOneGoodTriangle = false;
		boolean hasTwoGoodTriangle = false;

		

		for (Polygon[] g : triangles) {
			if (hasOneGoodTriangle && hasTwoGoodTriangle) {
				break;
			}
			for (Polygon t : g) {
				Rectangle bounds = t.getBounds();
				if ((bounds.equals(goodRectangles.get(0)))) {break;}
				if (Math.abs((bounds.getHeight() / bounds.getWidth()) - HEIGHT_WIDTH_RATIO) < RATIO_TOLERANCE) {
					if (checkRightTriangle(t)) {
						if (hasOneGoodTriangle) {
							
							hasTwoGoodTriangle = true;
							goodRectangles.set(1, bounds);
							

						} else {
							hasOneGoodTriangle = true;
							goodRectangles.set(0, bounds);
						}
						break;
					}
				}
			}
		}
		return goodRectangles;
	}

	private boolean checkDistance(Rectangle rect1, Rectangle rect2) {
		double distance = Math.abs(rect1.getCenterX() - rect2.getCenterX());
		double width = rect1.getWidth();

		

		return (Math.abs((distance / width) - DISTANCE_WIDTH_RATIO) < DISTANCE_TOLERANCE);

	}

	private double distanceFormula(int x1, int x2, int y1, int y2) {
		return (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
	}

	private boolean checkRightTriangle(Polygon triangle) {
		int[] xValues = triangle.xpoints;
		int[] yValues = triangle.ypoints;

		int x1 = xValues[0];
		int x2 = xValues[1];
		int x3 = xValues[2];

		int y1 = yValues[0];
		int y2 = yValues[1];
		int y3 = yValues[2];

		double sideA = distanceFormula(x1, x2, y1, y2); // 1 to 2
		double sideB = distanceFormula(x1, x3, y1, y3); // 1 to 3
		double sideC = distanceFormula(x3, x2, y3, y2); // 2 to 3
		double[] sides = new double[] { sideA, sideB, sideC };
		Arrays.sort(sides);
		double hypotenuse = sides[sides.length - 1];

		Point hypoPoint1 = new Point();
		Point hypoPoint2 = new Point();
		if (hypotenuse == sideA) {
			hypoPoint1 = new Point(x1, y1);
			hypoPoint2 = new Point(x2, y2);
		} else if (hypotenuse == sideB) {
			hypoPoint1 = new Point(x1, y1);
			hypoPoint2 = new Point(x3, y3);
		} else if (hypotenuse == sideC) {
			hypoPoint1 = new Point(x3, y3);
			hypoPoint2 = new Point(x2, y2);
		}

		// Law of cosines: angleC = arccos( c^2 / (a^2 + b^2 - 2ab))
		// turns out i didn't need this
		// double angleC = Math.acos( (sideC*sideC) /
		// ((sideA*sideA)+(sideB*sideB)-(2*sideA*sideB)));
		// double angleA = Math.acos( (sideA*sideA) /
		// ((sideC*sideC)+(sideB*sideB)-(2*sideB*sideC)));
		// double angleB = Math.acos( (sideB*sideB) /
		// ((sideA*sideA)+(sideC*sideC)-(2*sideA*sideC)));

		

		double slope = (Math.abs((hypoPoint1.getY() - hypoPoint2.getY()) / (hypoPoint1.getX() - hypoPoint2.getX())));

		System.out.println();
		System.out.println("Checked a hypotonuse slope,  slope: " + slope);
		System.out.println("How close to ratio? " + (slope - HEIGHT_WIDTH_RATIO));
		System.out.println("what triangle was checked? \n" );
		System.out.println("x's");
		for(int x : xValues) {
			System.out.print(x + ", ");
		}
		System.out.println("\n y's");
		for(int y : yValues) {
			System.out.print(y + ", ");
		}
		System.out.println("\n");
		return (Math.abs(slope - HEIGHT_WIDTH_RATIO) < SLOPE_TOLERANCE);
	}

	

	public boolean validatePoints() {
		validated = true;
		ArrayList<Polygon[]> triangles = generateTriangles();

		ArrayList<Rectangle> goodRects = validateRectangles(triangles);
		Rectangle goodRect1 = goodRects.get(0);
		Rectangle goodRect2 = goodRects.get(1);

		double area1 = goodRect1.getWidth() * goodRect1.getHeight();
		double area2 = goodRect2.getWidth() * goodRect2.getHeight();

		double heightWidthRatio1 = goodRect1.getHeight() / goodRect1.getWidth();
		double heightWidthRatio2 = goodRect2.getHeight() / goodRect2.getWidth();

		

		boolean horizontalAllign = Math.abs(goodRect1.getCenterY() - goodRect2.getCenterY()) < ALIGNMENT_TOLERANCE;
		boolean similiarAreas = Math.abs(area2 - area1) < AREA_TOLERANCE;
		boolean hasOneGoodTriangle = !(goodRect1.equals(new Rectangle()));
		boolean hasTwoGoodTriangle = !(goodRect2.equals(new Rectangle()));
		boolean distanceCheck = checkDistance(goodRect1, goodRect2);
		

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
		System.out.println("Area diff:  " + (Math.abs(area1 - area2)));
		System.out.println();
		System.out.println("Distance check? " + distanceCheck);
		System.out.println("Distance:Width Ratio  " + (Math.abs(goodRect1.getCenterX() - goodRect2.getCenterX()) / goodRect1.getWidth()));

		generateNewPoints(goodRect1, goodRect2);

		
		System.out.println("\n\n\n");
		System.out.println("Checks");
		System.out.println("Alignment: " + horizontalAllign);
		System.out.println("Distance Check: " + distanceCheck);
		System.out.println("SimiliarAreas: " + similiarAreas);
		System.out.println("OneRectangle: " + hasOneGoodTriangle);
		System.out.println("TwoRectangles: " + hasTwoGoodTriangle);
		
		return (hasOneGoodTriangle && hasTwoGoodTriangle && horizontalAllign && similiarAreas && distanceCheck);
		
	}

	private void generateNewPoints(Rectangle goodRect1, Rectangle goodRect2) {
		newPoints.add(new Point(goodRect1.x, goodRect1.y));// left top corner
		newPoints.add(new Point(goodRect1.x + goodRect1.width, goodRect1.y));// top right corner
		newPoints.add(new Point(goodRect1.x, goodRect1.y + goodRect1.height));// bottom left corner
		newPoints.add(new Point(goodRect1.x + goodRect1.width, goodRect1.y + goodRect1.height));// bottom right corner
		
		newPoints.add(new Point(goodRect2.x, goodRect2.y));// left top corner
		newPoints.add(new Point(goodRect2.x + goodRect2.width, goodRect2.y));// top right corner
		newPoints.add(new Point(goodRect2.x, goodRect2.y + goodRect2.height));// bottom left corner
		newPoints.add(new Point(goodRect2.x + goodRect2.width, goodRect2.y + goodRect2.height));// bottom right corner

	}

	public ArrayList<Point> getBetterPoints() {
		if (validated) {
			return newPoints;
		} else {
			validatePoints();
			return newPoints;
		}
	}

}