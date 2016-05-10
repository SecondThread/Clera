package processing;

public class ComplicatedPattern {
	private boolean on, up, down, left, right, upLeft, upRight, downLeft, downRight;
	private int range;
	private ComplicatedPattern upSub, downSub, leftSub, rightSub, upLeftSub, upRightSub, downLeftSub, downRightSub;
	
	public ComplicatedPattern(boolean on, int range) {
		this.on=up=down=left=right=on;
		this.range=range;
	}
	
	public ComplicatedPattern(boolean on, boolean up, boolean down, boolean left, boolean right, boolean upLeft, boolean upRight, boolean downLeft, boolean downRight, int range, int subRange) {
		this.on=on;
		this.up=up;
		this.down=down;
		this.left=left;
		this.upLeft=upLeft;
		this.upRight=upRight;
		this.downLeft=downLeft;
		this.downRight=downRight;
		this.right=right;
		this.range=range;
		if (range!=0) {
			upSub=new ComplicatedPattern(up, subRange);
			downSub=new ComplicatedPattern(down, subRange);
			leftSub=new ComplicatedPattern(left, subRange);
			rightSub=new ComplicatedPattern(right, subRange);
			upLeftSub=new ComplicatedPattern(upLeft, subRange);
			upRightSub=new ComplicatedPattern(upRight, subRange);
			downLeftSub=new ComplicatedPattern(downLeft, subRange);
			downRightSub=new ComplicatedPattern(downRight, subRange);
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
			if (isMatch(image, x, y, on)) total+=0.2f;
			else total=-.5f;
			if (isMatch(image, x, y-range, up)) total+=0.1f;
			if (isMatch(image, x, y+range, down)) total+=0.1f;
			if (isMatch(image, x-range, y, left)) total+=0.1f;
			if (isMatch(image, x+range, y, right)) total+=0.1f;
			if (isMatch(image, x-range, y-range, upLeft)) total+=0.1f;
			if (isMatch(image, x+range, y-range, upRight)) total+=0.1f;
			if (isMatch(image, x-range, y+range, downLeft)) total+=0.1f;
			if (isMatch(image, x+range, y+range, downRight)) total+=0.1f;
		}
		else {
			if (isMatch(image, x, y, on)) total+=0.2f;
			else total=-.3f;
			total+=0.1f*upSub.checkSpace(image, x, y-range);
			total+=0.1f*downSub.checkSpace(image, x, y+range);
			total+=0.1f*leftSub.checkSpace(image, x-range, y);
			total+=0.1f*rightSub.checkSpace(image, x+range, y);
			total+=0.1f*upLeftSub.checkSpace(image, x-range, y-range);
			total+=0.1f*upRightSub.checkSpace(image, x+range, y-range);
			total+=0.1f*downLeftSub.checkSpace(image, x-range, y+range);
			total+=0.1f*downRightSub.checkSpace(image, x+range, y+range);
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
