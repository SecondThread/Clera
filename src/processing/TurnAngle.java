package processing;
import java.awt.Point;
import java.util.ArrayList;

public class TurnAngle {
	
	private final double WIDTH=320;  		//width of screen
	private final double HEIGHT=240;		//length of screen
	private final double ANGLE=50;		//horizontal angle of the camera
	

	public TurnAngle()
	{
		
	}
	private Point getMidPoint(ArrayList<Point> bestPoints)
	{
		
		double totalX=0;
		double totalY=0;
		for(Point p: bestPoints)
		{
			totalX+=p.getX();
			totalY+=p.getY();
		}
		Point midPt=new Point((int)(totalX/8.0),(int)(totalY/8.0));
		
		return midPt;
	}
	
	/** 
	 * 
	 * @param midPt
	 * @return float variable "angle": 
	 * 	LeftTurn is needed if the angle is negative;
	 * 	RightTurn is needed if the angle is positive;
	 */
	public float getTurnAngle(ArrayList<Point> getBestPt)
	{
		float angle=0;
		Point midPoint=getMidPoint(getBestPt);
		double goalX=midPoint.getX();
		double myX=WIDTH/2;
		angle=(float)(((goalX-myX)/WIDTH)*ANGLE);		
		return angle;
	}
}
