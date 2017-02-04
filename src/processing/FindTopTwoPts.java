package processing;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	 * @return an array list that has top points of the two curve
	 */
		public static ArrayList<Point> findTopPoints(BufferedImage image) {

		 
		 GreenImageProcesser processer=new GreenImageProcesser(-.45f , 1.0f , -.45f , 0.5);
		 float[][] processedImage=processer.process(image);
		 ArrayList<Point> bestTopPts=new ArrayList<Point>();
		 int success=0;
		 int failed=0;
		 int startPoint=0;
		 int endPoint=0;
		 for(int y=10;y<processedImage[0].length-10;y++)
		 for(int x=10;x<processedImage.length-10;x++)
			 {

				 if(failed<7&&processedImage[x][y]==1)
				 {
					 success++;
					 failed=0;
					 if (success==1)
						 startPoint=x;
				 }
				 else if(failed<7)
				 {
					 if (success!=0)
						 failed++;
				 }
				 else if(success<7)
				 {
					 success=0;
					 failed=0;
					 startPoint=0;
				 }
				 else
				 {
					 success=0;
					 failed=0;
					 endPoint=x-7;
				 }
				 if(startPoint!=0&&endPoint!=0)
				 {
					 int midPoint=(startPoint+endPoint)/2;
					 int topCheck=0;
					 int botCheck=0;
					 if (midPoint!=0)
					 {
						 for(int i=1;i<5;i++)
						 {

							 if(processedImage[midPoint][y-i]==0)
							 {
								 topCheck++;
							 }
	
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
