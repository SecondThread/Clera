package processing;

/**
 * A class for general vision processing algorythms 
 * @author David
 *
 */
public class General {
	
	/**
	 * clones the array
	 * @param original
	 * The array to clone
	 * @return
	 * A cloned version of the array
	 */
	public float[][] clone(float[][] original) {
		float[][] toReturn=new float[original.length][original[0].length];
		for (int x=0; x<original.length; x++) {
			for (int y=0; y<original[x].length; y++) {
				toReturn[x][y]=original[x][y];
			}
		}
		return toReturn;
	}
}
