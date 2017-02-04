package processing;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import robot.Window;

/**
 * This class will find the top points of the HIGH GOAL reflection tape curve
 * by using the GreenImageProcesser Class
 * 
 * @author jialuo
 *
 */
public class FindTopTwoPts {
	/**
	 * return an array list that has top points of the two curve
	 * 
	 * @param image: image that needs to be processed
	 * @return 	An array list that has top points of the two curve
	 * 			there should be no more than 2 points.
	 * 			The first point is the top of the top tape.
	 * 			The second point is the top of the bottom tape.
	 *			If there are only one point, it means the top tape
	 *			was not shown entirely in the camera 
	 *			(only shows part of it which does not includes the top point)
	 *			then the only point will be
	 *			the top of the bottom tape
	 */
		public static ArrayList<Point> findTopPoints(BufferedImage image) {

		 
			 GreenImageProcesser processer=new GreenImageProcesser(-.5f , 1.0f , -.5f , 0.6);
			 float[][] processedImage=processer.process(image);
			 Window.displayPixels(processedImage, "");
			 ArrayList<Point> bestTopPts=new ArrayList<Point>();
			 int success=0;
			 int failed=0;
			 int startPoint=0;
			 int endPoint=0;
			 for(int y=0;y<processedImage[0].length;y++)
			 for(int x=0;x<processedImage.length;x++)
				 {

					 if(failed<4&&processedImage[x][y]==1)
					 {
						 success++;
						 failed=0;
						 if (success==1)
							 startPoint=x;
					 }
					 else if(failed<4)
					 {
						 if (success!=0)
							 failed++;
					 }
					 else if(success<4)
					 {
						 success=0;
						 failed=0;
						 startPoint=0;
					 }
					 else
					 {
						 success=0;
						 failed=0;
						 endPoint=x-4;
					 }
					 if(startPoint!=0&&endPoint!=0)
					 {
						 int midPoint=(startPoint+endPoint)/2;
						 int topCheck=0;
						 int botCheck=0;
						 if (midPoint!=0)
						 {
							 for(int i=1;i<4;i++)
							 {

								 if (y>3)
									 if(processedImage[midPoint][y-i]==0)
									 {
										 topCheck++;
									 }
								 if (y<processedImage[0].length-3)
									 if(processedImage[midPoint][y+i]==1)
									 {
										 botCheck++;
									 }
							 }
							 if(topCheck>2&&botCheck>1)
							 {
								 if(bestTopPts.isEmpty())
									 bestTopPts.add(new Point(midPoint,y-1));
								 else if(bestTopPts.size()==1)
									 if(Math.abs(bestTopPts.get(0).getY()-y)>7&&Math.abs(bestTopPts.get(0).getX()-midPoint)<8)
									 {
										 bestTopPts.add(new Point(midPoint,y-1)); 
									 }
								 midPoint=0;
								 startPoint=0;
								 endPoint=0;
							 }
							 else
							 {
								 midPoint=0;
								 startPoint=0;
								 endPoint=0;
							 }
						 }
					 }
				 }
		 return bestTopPts;
		 
	 }
}
