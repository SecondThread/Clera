package processing;

public class Pattern {
	private boolean on, up, down, left, right;
	private int range;
	private Pattern upSub, downSub, leftSub, rightSub;
	
	public Pattern(boolean on, int range) {
		this.on=up=down=left=right=on;
		this.range=range;
	}
	
	public Pattern(boolean on, boolean up, boolean down, boolean left, boolean right, int range, int subRange) {
		this.on=on;
		this.up=up;
		this.down=down;
		this.left=left;
		this.right=right;
		this.range=range;
		if (range!=0) {
			upSub=new Pattern(up, subRange);
			downSub=new Pattern(down, subRange);
			leftSub=new Pattern(left, subRange);
			rightSub=new Pattern(right, subRange);
		}
	}
	
	/**
	 * Gets the chance that this pixels supports the pattern
	 * @param image
	 * The picture to check
	 * @param x
	 * @param y
	 * @return
	 * The chance between 0 and 1 that this pixels follows the pattern
	 */
	public float checkSpace(boolean[][] image, int x, int y) {
		float total=0f;
		if (upSub==null) {
			if (isMatch(image, x, y, on)) total+=0.4f;
			else total=-.5f;
			if (isMatch(image, x, y-range, up)) total+=0.15f;
			if (isMatch(image, x, y+range, down)) total+=0.15f;
			if (isMatch(image, x-range, y, left)) total+=0.15f;
			if (isMatch(image, x+range, y, right)) total+=0.15f;
		}
		else {
			if (isMatch(image, x, y, on)) total+=0.2f;
			else total=-.3f;
			total+=0.2f*upSub.checkSpace(image, x, y-range);
			total+=0.2f*downSub.checkSpace(image, x, y+range);
			total+=0.2f*leftSub.checkSpace(image, x-range, y);
			total+=0.2f*rightSub.checkSpace(image, x+range, y);
			total=Math.max(0, total);
		}
		return total;
	}
	
	/**
	 * Checks to see if the space on the image is what it should be
	 * @param image
	 * @param x
	 * @param y
	 * @param shouldBeOn
	 * @return
	 * true if the space is what it should be, false if it is not or is an illegal space
	 */
	private boolean isMatch(boolean[][] image, int x, int y, boolean shouldBeOn) {
		if (x>=image.length||x<0||y>=image[x].length||y<0) {
			return false;
		}
		return image[x][y]==shouldBeOn;
	}
	
}
