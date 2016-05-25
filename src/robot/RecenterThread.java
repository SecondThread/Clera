package robot;

import java.awt.Point;

public class RecenterThread implements Runnable {

	
	public volatile Point lastCenter;
	public volatile float[][] pixels;
	public volatile boolean processFlag=false;
	
	public void run() {
		while (true) {
			if (processFlag) {
				processFlag=false;
				float[][] currentPixels=pixels;
				lastCenter=Main.findCenter(currentPixels, Main.topLeftTemplatesLocation);
			}
		}
	}

	//called from main thread
	public void setPixels(float[][] pixels) {
		this.pixels=pixels;
		processFlag=true;
	}
}
