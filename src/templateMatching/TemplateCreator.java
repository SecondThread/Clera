package templateMatching;

import java.awt.Point;

import robot.Window;

public class TemplateCreator {
	
	public static final int templateWidth=12;
	
	public static Template createTemplate(float[][] image) {
		int centerX=Window.lastClickX;
		int centerY=Window.lastClickY;
		Point topLeftCorner=new Point(centerX-templateWidth/2, centerY-templateWidth/2);
		Point bottomRightCorner=new Point(centerX+templateWidth/2, centerY+templateWidth/2);
		return new Template(image, topLeftCorner, bottomRightCorner);
	}
}
