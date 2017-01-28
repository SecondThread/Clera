package processing;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class PegVisionUtils {

	// All of the tolerances used, can and should be changed after more testing is done

	public static final double RATIO_TOLERANCE = 3, DISTANCE_TOLERANCE = 3, SLOPE_TOLERANCE = 5,
			ALIGNMENT_TOLERANCE = 50, AREA_TOLERANCE = 30000, HEIGHT_WIDTH_RATIO = 5, DISTANCE_WIDTH_RATIO = 4.125;

	
	//lengthofDumbo is 10.25 inches and pixels is the calculated length of dumbo in pixels
	//lengthinPixels is the total width of the screen is pixels
	//this was written by gerch not by billy
	public static double distance(double lengthofDumbo, double fovDegrees, double lengthinPixels, ArrayList<Point> points){
		Point leftMost = new Point(Integer.MAX_VALUE, 0);
		Point rightMost = new Point(0,0);
		for(Point p: points)
		{
			if(p.getX()<leftMost.getX())
			{
				leftMost = p;
			}
			if(p.getX()>rightMost.getX())
			{
				rightMost = p;
			}
		}
		
		double pixels = rightMost.getX() - leftMost.getX();
		double distance = lengthofDumbo/(2*Math.tan((fovDegrees*pixels)/(2*lengthinPixels)));
	return distance;
	}
	
	
	/**
	 * generates the 16 possible quadrilaterals that can be made from the 8 points
	 * 
	 * @return ArrayList<Point[]> an ArrayList of Point[]'s that hold the four points of each quadrilateral
	 * @param points
	 *            ArrayList of 8 points, hopefully the corners of the retroflective tape
	 */

	private static ArrayList<Point[]> generateQuadrilaterals(ArrayList<Point> points) {

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

	/**
	 * Generates 64 triangles, 4 from each quadrilateral generated in <code>generateQuadrilaterals</code>
	 * 
	 * @param points
	 *            ArrayList of 8 points, hopefully the corners of the retroflective tape
	 * @return ArrayList<Polygon[]> groups of triangles, arranged in groups of four, based on the quadrilateral they came from
	 */

	private static ArrayList<Point[][]> generateTriangles(ArrayList<Point> points) {
		ArrayList<Point[]> rectangles = generateQuadrilaterals(points);
		ArrayList<Point[][]> triangleGroups = new ArrayList<Point[][]>();

		for (Point[] rect : rectangles) {
			Point[][] similiarTriangles = new Point[4][3];
			for (int i = 0; i < 4; i++) {
				Point[] triangle = new Point[3];
				int counter = 0;
				for (int n = 0; n < 4; n++) {
					
					if (n != i) {
						triangle[counter] = (new Point(rect[n].x, rect[n].y));
						counter++;
					}
				}
				similiarTriangles[i] = triangle;
			}
			if (similiarTriangles.length != 0) {
				triangleGroups.add(similiarTriangles);
			}
			
		}
		return triangleGroups;
	}
		
		
		
	

	private static boolean checkOrientation(Point[] triangle, ArrayList<Point> points) {
		System.out.println("checking orientation...");
		/*
		 * Actuall order of points 
		 * tl tl
		 * bl bl
		 * tr tr
		 * br br
		 */
		ArrayList<Point> tops = new ArrayList<Point>();
		ArrayList<Point> bottoms = new ArrayList<Point>();
		ArrayList<Point> rights = new ArrayList<Point>();
		ArrayList<Point> lefts = new ArrayList<Point>();
		for(Point p : triangle) {
			if(p.equals(points.get(0)) || p.equals(points.get(1)) ||p.equals(points.get(4)) ||p.equals(points.get(5))) {
				tops.add(p);
			}
			else if(p.equals(points.get(2)) || p.equals(points.get(3)) ||p.equals(points.get(6)) ||p.equals(points.get(7))) {
				bottoms.add(p);
			}
			if(p.equals(points.get(4)) || p.equals(points.get(5)) ||p.equals(points.get(6)) ||p.equals(points.get(7))) {
				rights.add(p);
			}
			if(p.equals(points.get(0)) || p.equals(points.get(1)) ||p.equals(points.get(2)) ||p.equals(points.get(3))) {
				lefts.add(p);
			}
		}
		
		
		for(Point top : tops) {
			for(Point bottom : bottoms) {
				if(top.y>bottom.y) {
					return false;
				}
			}
		}
		
		for(Point right : rights) {
			for(Point left : lefts) {
				if(right.x<left.x) {
					return false;
				}
			}
		}
		
		
		
		return true;

	}

	/**
	 * Validates thats the passed group of triangles contain two triangles whose rectangle bounds are 5:2
	 * 
	 * @param triangles
	 *            the group of triangles to be checked, sorted in Polygon[] by the quadrilaterals they came from
	 * @return ArrayList<Rectangle> holds the two good rectangles found, if good rectangles cannot be found then the arraylist will contain a <code> new Rectangle()</code> (x=0,y=0,width=0,height=0)
	 */

	private static ArrayList<Rectangle> validateRectangles(ArrayList<Point[][]> triangles, ArrayList<Point> points) {
		ArrayList<Rectangle> goodRectangles = new ArrayList<Rectangle>();
		goodRectangles.add(new Rectangle());
		goodRectangles.add(new Rectangle());

		boolean hasOneGoodTriangle = false;
		boolean hasTwoGoodTriangle = false;
		
		

		for (Point[][] g : triangles) {
			if (hasOneGoodTriangle && hasTwoGoodTriangle) {
				break;
			}
			for (Point[] t : g) {
				if (!(checkOrientation(t, points))) {
					System.out.println("check failed");
					continue;
				}
				System.out.println("check success");
					Polygon p = new Polygon();
					for (Point point : t) {
						p.addPoint(point.x, point.y);
						
					}
					Rectangle bounds = p.getBounds();
					if ((bounds.intersects((goodRectangles.get(0))))) {
						break;
					}
					if (Math.abs((bounds.getHeight() / bounds.getWidth()) - HEIGHT_WIDTH_RATIO) < RATIO_TOLERANCE) {
						if (checkRightTriangle(p)) {
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

	/**
	 * Checks the distance between two rectangles, relative to their width. the ratio should be 33:8 or 4.125
	 * 
	 * @param rect1
	 *            first rectangle
	 * @param rect2
	 *            second rectangle
	 * @return boolean true if the ratio is close enough to 33:8 (see <code>DISTANCE_TOLERANCE</code>)
	 */

	private static boolean checkDistance(Rectangle rect1, Rectangle rect2) {
		double distance = Math.abs(rect1.getCenterX() - rect2.getCenterX());
		double width = rect1.getWidth();

		return (Math.abs((distance / width) - DISTANCE_WIDTH_RATIO) < DISTANCE_TOLERANCE);

	}

	/**
	 * simple distance formula
	 * 
	 * @param x1
	 *            x value of first point
	 * @param x2
	 *            x value of second point
	 * @param y1
	 *            y value of first point
	 * @param y2
	 *            y value of second point
	 * @return double the distance between the two points
	 */

	private static double distanceFormula(int x1, int x2, int y1, int y2) {
		return (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
	}

	/**
	 * checks whether the longest side (hypotenuse) has a slope of 2:5 this check is useful for sifting out weirdly oriented, but still 2:5 triangles
	 * 
	 * @param triangle
	 *            the triangle to be checked
	 * @return boolean true if hypotenuse slope is close enough to 2:5 (see <code>SLOPE_TOLERANCE</code>)
	 */

	private static boolean checkRightTriangle(Polygon triangle) {
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

		double slope = (Math.abs((hypoPoint1.getY() - hypoPoint2.getY()) / (hypoPoint1.getX() - hypoPoint2.getX())));

		// some useful information is printed
		System.out.println();
		System.out.println("Checked a hypotonuse slope,  slope: " + slope);
		System.out.println("How close to ratio? " + (slope - HEIGHT_WIDTH_RATIO));
		System.out.println("what triangle was checked? \n");
		System.out.println("x's");
		for (int x : xValues) {
			System.out.print(x + ", ");
		}
		System.out.println("\n y's");
		for (int y : yValues) {
			System.out.print(y + ", ");
		}
		System.out.println("\n");

		return (Math.abs(slope - HEIGHT_WIDTH_RATIO) < SLOPE_TOLERANCE);
	}

	/**
	 * completes the final checks after 2 rectangles are found
	 * 
	 * @param goodRect1
	 *            first good rectangle
	 * @param goodRect2
	 *            second good rectangle
	 * @return true if all checks are passed (area check, alignment check, distance check, and two good rectangles were actually found)
	 */

	private static boolean validateEverything(Rectangle goodRect1, Rectangle goodRect2) {

		double area1 = goodRect1.getWidth() * goodRect1.getHeight();
		double area2 = goodRect2.getWidth() * goodRect2.getHeight();

		double heightWidthRatio1 = goodRect1.getHeight() / goodRect1.getWidth();
		double heightWidthRatio2 = goodRect2.getHeight() / goodRect2.getWidth();

		boolean horizontalAllign = Math.abs(goodRect1.getCenterY() - goodRect2.getCenterY()) < ALIGNMENT_TOLERANCE;
		boolean similiarAreas = Math.abs(area2 - area1) < AREA_TOLERANCE;
		boolean hasOneGoodTriangle = !(goodRect1.equals(new Rectangle()));
		boolean hasTwoGoodTriangle = !(goodRect2.equals(new Rectangle()));
		boolean distanceCheck = checkDistance(goodRect1, goodRect2);

		// some useful print outs
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
		System.out.println("Distance:Width Ratio  "
				+ (Math.abs(goodRect1.getCenterX() - goodRect2.getCenterX()) / goodRect1.getWidth()));

		System.out.println("\n\n\n");
		System.out.println("Checks");
		System.out.println("Alignment: " + horizontalAllign);
		System.out.println("Distance Check: " + distanceCheck);
		System.out.println("SimiliarAreas: " + similiarAreas);
		System.out.println("OneRectangle: " + hasOneGoodTriangle);
		System.out.println("TwoRectangles: " + hasTwoGoodTriangle);

		return (hasOneGoodTriangle && hasTwoGoodTriangle && horizontalAllign && similiarAreas && distanceCheck);

	}

	/**
	 * checks the 8 points given, and generates 8 new points after checking for error
	 * 
	 * @param points
	 *            8 points, hopefully the 8 corners of the retroflective tape
	 * @return ArrayList<Point> 8 new points generated after accounting for outliers- if good points cannot be found, returns <code>null</code>
	 */

	public static ArrayList<Point> generateNewPoints(ArrayList<Point> points) {
		System.out.println( "OG POINTS  \n\n" + points + "\n\n");
		ArrayList<Point[][]> triangles = generateTriangles(points);
		ArrayList<Rectangle> goodRects = validateRectangles(triangles, points);
		Rectangle goodRect1 = goodRects.get(0);
		Rectangle goodRect2 = goodRects.get(1);

		ArrayList<Point> newPoints = new ArrayList<Point>();
		if (validateEverything(goodRect1, goodRect2)) {
			newPoints.add(new Point(goodRect1.x, goodRect1.y));// left top corner
			newPoints.add(new Point(goodRect1.x + goodRect1.width, goodRect1.y));// top right corner
			newPoints.add(new Point(goodRect1.x, goodRect1.y + goodRect1.height));// bottom left corner
			newPoints.add(new Point(goodRect1.x + goodRect1.width, goodRect1.y + goodRect1.height));// bottom right corner

			newPoints.add(new Point(goodRect2.x, goodRect2.y));// left top corner
			newPoints.add(new Point(goodRect2.x + goodRect2.width, goodRect2.y));// top right corner
			newPoints.add(new Point(goodRect2.x, goodRect2.y + goodRect2.height));// bottom left corner
			newPoints.add(new Point(goodRect2.x + goodRect2.width, goodRect2.y + goodRect2.height));// bottom right corner
			return newPoints;

		} else {
			return null;
		}

	}

	/**
	 * finds the center (peg) of the 8 given points (corners of retroflective tape)
	 * 
	 * @param points
	 *            8 points (these should be generated by <code>generateNewPoints</code>
	 * @return Point the point of the peg/center. If the ArrayList of points is null, will return null
	 */

	public static Point findPeg(ArrayList<Point> points) {
		if (points == null) {
			return null;
		}

		double PegX;
		double PegY;

		double[] xValues = new double[8];
		for (int i = 0; i < 8; i++) {
			xValues[i] = points.get(i).getX();
		}
		Arrays.sort(xValues);

		double mid1X = xValues[3];
		double mid2X = xValues[4];
		PegX = (mid1X + mid2X) / 2;

		double[] yValues = new double[8];
		for (int i = 0; i < 8; i++) {
			yValues[i] = points.get(i).getY();
		}
		Arrays.sort(yValues);
		double mid1Y = yValues[3];
		double mid2Y = yValues[4];
		PegY = (mid1Y + mid2Y) / 2;

		return (new Point((int) PegX, (int) PegY));
	}
}
