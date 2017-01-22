package processing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class PegFinder {

	private ArrayList<Point> points;

	public PegFinder(ArrayList<Point> thePoints) {
		points = thePoints;
	}

	public double getX() {
		double[] xValues = new double[8];
		for (int i = 0; i < 8; i++) {
			xValues[i] = points.get(i).getX();
		}
		Arrays.sort(xValues);

		System.out.println();
		for(double x: xValues)
		{
			System.out.print(x + "  ");
		}
		double mid1 = xValues[3];
		double mid2 = xValues[4];
		return (mid1 + mid2) / 2;
	}

	// should be useless but here it is
	public double getY() {
		double[] yValues = new double[8];
		for (int i = 0; i < 8; i++) {
			yValues[i] = points.get(i).getY();
		}
		Arrays.sort(yValues);
		double mid1 = yValues[3];
		double mid2 = yValues[4];
		return (mid1 + mid2) / 2;
	}
}
