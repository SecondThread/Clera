package robot;

import java.awt.Point;

import templateMatching.Template;
import templateMatching.TemplateCreator;

public class SecondThread {
	private static Point lastPointToSearch;
	private static Template corner;
	private static float[][] image;
	
	private static final int horizontalSearchRange=10, verticalSearchRange=10;
	private static final int horizontalSearchRangeLarge=30, verticalSearchRangeLarge=20;

	static void setImage(float[][] image) {
		SecondThread.image=image;
	}
	
	public static void setCenterOfCorner(Point center) {
		lastPointToSearch=center;
		int startX=Math.min(Math.max(0, center.x-TemplateCreator.templateWidth/2), image.length-1-TemplateCreator.templateWidth);
		int startY=Math.min(Math.max(0, center.y-TemplateCreator.templateWidth/2), image[0].length-1-TemplateCreator.templateWidth);
		Point topLeftCorner=new Point(startX, startY);
		Point bottomRightCorner=new Point(startX+TemplateCreator.templateWidth, startY+TemplateCreator.templateWidth);
		corner=new Template(image, topLeftCorner, bottomRightCorner);
	}
	
	public static void search() {
		float maxConfidence=0;
		Point bestCenter=new Point(0,0);

		for (int x=Math.max(0, lastPointToSearch.x-horizontalSearchRange); x<Math.min(image.length, lastPointToSearch.x+horizontalSearchRange); x++) {
			for (int y=Math.max(0, lastPointToSearch.y-verticalSearchRange); y<Math.min(image[x].length, lastPointToSearch.y+verticalSearchRange); y++) {
				float currentMatch=corner.matchTemplateWithoutNormalizing(image, x, y);
				if (currentMatch>maxConfidence) {
					maxConfidence=currentMatch;
					bestCenter=new Point(x+corner.getTemplate().length/2, y+corner.getTemplate()[0].length/2);
				}
			}
		}
		lastPointToSearch=bestCenter;
	}
	
	public static void searchAllPixels() {
		float maxConfidence=0;
		Point bestCenter=new Point(0,0);

		for (int x=Math.max(0, lastPointToSearch.x-horizontalSearchRangeLarge); x<Math.min(image.length, lastPointToSearch.x+horizontalSearchRangeLarge); x++) {
			for (int y=Math.max(0, lastPointToSearch.y-verticalSearchRangeLarge); y<Math.min(image[x].length, lastPointToSearch.y+verticalSearchRangeLarge); y++) {
				float currentMatch=corner.matchTemplateWithoutNormalizing(image, x, y);
				if (currentMatch>maxConfidence) {
					maxConfidence=currentMatch;
					bestCenter=new Point(x+corner.getTemplate().length/2, y+corner.getTemplate()[0].length/2);
				}
			}
		}
		lastPointToSearch=bestCenter;
	}
	
	public static Point getCenter() {
		return lastPointToSearch;
	}
}
