package processing;

public class PegDistance {

	public float calcDistance(float length, float FOV, float pixels, float totalPixels) {
		float distance = 0;
		distance = (float) (length / ((2 * Math.tan(FOV * pixels) / (2 * totalPixels))));
		return distance;
	}
}
