package processing;

import java.awt.Point;
import java.util.ArrayList;

public class ProportionalTester {

	private ArrayList<Point> points;
	private Point tL1, tR1, bR1, bL1, tL2, tR2, bL2, bR2;

	public ProportionalTester() {

	}

	public void sendPoints(ArrayList<Point> SomePoints) {
		points = SomePoints;
	}

	/*
	 * determines which points are which
	 */
	private void assignPoints() {
		/*
		 * tests which top left corner is which (which one is further to the
		 * left)
		 */
		if (points.get(0).getX() < points.get(1).getX()) {
			tL1 = points.get(0);
			tL2 = points.get(1);
		} 
		else {
			tL1 = points.get(1);
			tL2 = points.get(0);
		}

		if (points.get(2).getX() < points.get(3).getX()) {
			tR1 = points.get(2);
			tR2 = points.get(3);
		} 
		else {
			tR1 = points.get(3);
			tR2 = points.get(2);
		}

		if (points.get(4).getX() < points.get(5).getX()) {
			bL1 = points.get(4);
			bL2 = points.get(5);
		} 
		else {
			bL1 = points.get(5);
			bL2 = points.get(4);
		}

		if (points.get(6).getX() < points.get(7).getX()) {
			bR1 = points.get(6);
			bR2 = points.get(7);
		} 
		else {
			bR1 = points.get(7);
			bR2 = points.get(6);
		}

	}

	private Point testTopPoints()
	{
		double slope1 = (tL1.getY()- tR1.getY()) / (tL1.getX() - tR1.getX());
		double slope2 = (tL1.getY()- tL2.getY()) / (tL1.getX() - tL2.getX());
		double slope3 = (tL1.getY()- tR2.getY()) / (tL1.getX() - tR2.getX());
		
		//this is very arbitrary
		double tolerance = .1;
		
		double avg;
		
		if(Math.abs(slope1 - slope2) < tolerance)
		{
			avg = (slope1 + slope2) / 2;
			if(Math.abs(slope3 - avg) < tolerance)
			{
				return null;
				//no outlier
			}
			else
			{
				return tR2;
			}
			
		}
		else if(Math.abs(slope2 - slope3) < tolerance)
		{
			
		}
		else
		{
			//first point is probably an outlier
		}
		
		
	}
	
	
		

}
