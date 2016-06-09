package physics;

public class Vector2Editable extends Vector2 {

	public Vector2Editable(double x, double y) {
		super(x, y);
	}
	
	public void setX(double newX) {
		this.x=newX;
	}
	
	public void setY(double newY) {
		this.y=newY;
	}

	public static Vector2Editable createFromPolar(double r, double theta) {
		return new Vector2Editable(r*Math.cos(theta), r*Math.sin(theta));
	}
}
