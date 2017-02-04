package processing;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * This class has the process method that process the image 
 * so that makes OUTSTANDING green color pixel into a 1
 * and everything else into a 0
 * 
 * @author jialuo
 * 
 */
public class GreenImageProcesser {
	private float redIndex;
	private float blueIndex;
	private float greenIndex;
	private double upperBound;
	/**
	 * Construction method
	 */
	public GreenImageProcesser(){
		redIndex =  -.45f;
		greenIndex= 1.0f;
		blueIndex=  -.45f;
		upperBound= 0.6;
	}
	/**
	 * Construction method
	 * @param redIndex 		: multiplier of red color value, domain from 0f to -1f 
	 * 						  recommended input: -0.2f to -0.4f for bright camera
	 * 											 -0.4f to -0.7f for dim camera
	 * 											 -0.7f to -0.9f for very dim camera 
	 * @param greenIndex	: multiplier of green color value, domain from 0f to 1f
	 * 						  recommended input: 1f
	 * @param blueIndex		: multiplier of red color value, domain from 0f to -1f 
	 * 						  recommended input: same as red 
	 * @param upperBound	: remove all point that values below this bound
	 * 						  recommended input: for input G/R of -0.2 to -0.4f  : 0.7 to 0.9
	 * 											 for input G/R of -0.4f to -0.7f : 0.4 to 0.7
	 * 											 for input G/R of -0.7f to -0.9f : 0.1 to 0.4 
	 */
	public GreenImageProcesser(float redIndex,float greenIndex,float blueIndex,double upperBound)
	{
		this.redIndex=redIndex;
		this.greenIndex=greenIndex;
		this.blueIndex=blueIndex;
		this.upperBound=upperBound;
	}
	public float getRedIndex() {
		return redIndex;
	}
	public void setRedIndex(float redIndex) {
		this.redIndex = redIndex;
	}
	public float getBlueIndex() {
		return blueIndex;
	}
	public void setBlueIndex(float blueIndex) {
		this.blueIndex = blueIndex;
	}
	public float getGreenIndex() {
		return greenIndex;
	}
	public void setGreenIndex(float greenIndex) {
		this.greenIndex = greenIndex;
	}
	public double getUpperBound() {
		return upperBound;
	}
	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
	/**
	 * The process method makes OUTSTANDING green color pixel into 
	 * a 1 and everything else into a 0
	 * 
	 * @param image: input BufferedImage that needed to be processed
	 * @return a float 2D array that contains only 1 and 0
	 * */
	public float[][] process(BufferedImage image)
	{
	  Color[][] asColors=new Color[image.getWidth()][image.getHeight()];
	  for (int y=0; y<asColors[0].length; y++) {
	   for (int x=0; x<asColors.length; x++) {
	    asColors[x][y]=new Color(image.getRGB(x, y));
	   }
	  }
	  float[][] luminance=ImageProcessor.luminance(asColors, redIndex, greenIndex, blueIndex); //phone(-.3,1,-.3)
	  ImageProcessor.normalize(luminance);
	  
	  float [][] contrastedData=new float[luminance.length][luminance[0].length];
	  
	  
	  for(int y=0;y<luminance[0].length;y++)
	  {
		  for(int x=0; x<luminance.length;x++)
		  {
			  if (luminance[x][y]>upperBound)
			  {
				  contrastedData[x][y]=1;
			  }
			  else
			  {
				  contrastedData[x][y]=0;
			  }	  
		  }
	  }
	
	  float [][]modifiedData=new float [luminance.length][luminance[0].length];
	  int validMinimum=5;
	  for(int y=2;y<luminance[0].length-2;y++)
	  {
		  for(int x=2; x<luminance.length-2;x++)
		  {
			  int counter=0;
			  for(int i=0;i<3;i++)
			  {
				  for(int j=0;j<3;j++)
					  counter+=contrastedData[x-1+i][y-1+i];
				  if(counter>validMinimum)
				  {
					  modifiedData[x][y]=1;
				  }
				  else
				  {
					  modifiedData[x][y]=0;
				  }
			  }
		  }
	  }
	  
	  return modifiedData;
	}

}