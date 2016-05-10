package templateMatching;

import java.awt.Point;

import robot.Window;

public class TemplateCreator {
	public static Template createTemplate(float[][] image) {
		int centerX=Window.lastClickX;
		int centerY=Window.lastClickY;
		Point topLeftCorner=new Point(centerX-14, centerY-14);
		Point bottomRightCorner=new Point(centerX+14, centerY+14);
		return new Template(image, topLeftCorner, bottomRightCorner);
	}
}
