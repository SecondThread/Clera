package physics;

public class Vector2 {
	protected double x, y;
	
	public Vector2(double x, double y) {
		this.x=x; this.y=y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getLength() {
		return Math.sqrt(x*x+y*y);
	}
	
	/**
	 * Gets the angle in radians between -pi and pi
	 * @return
	 * the angle of the vector in radians
	 */
	public double getAngle() {
		if (y==0) {
			if (x>0) {
				return Math.PI/2;
			}
			else {
				return -Math.PI/2;
			}
		}
		if (x>0) {
			return Math.atan(y/x);
		}
		else {
			if (y>0) {
				return Math.atan(y/x)+Math.PI;
			}
			else {
				return Math.atan(y/x)-Math.PI;
			}
		}
	}

	public static Vector2 createFromPolar(double r, double theta) {
		return new Vector2(r*Math.sin(theta), r*Math.cos(theta));
	}
}
