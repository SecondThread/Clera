package processing;
import java.awt.Point;
import java.util.ArrayList;
public class DistenceFromTarget {
	private final double WIDTH=320;  		//width of screen
	private final double HEIGHT=240;		//length of screen
	private final double ANGLE=50;		//horizontal angle of the camera
	
	public DistenceFromTarget(){
	}
	
	public double getDistence(ArrayList<Point> bestPoints){
		if(bestPoints.get(0).x>bestPoints.get(1).x){
		double lessX=bestPoints.get(1).x;
		}
		else{
		double lessX=bestPoints.get(0).x;
		}
	}
}
