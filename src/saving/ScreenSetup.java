package saving;

import java.awt.MouseInfo;
import java.awt.Point;

public class ScreenSetup {
	public static int pictureStartX=-1, pictureStartY=-1, pictureEndX=-1, pictureEndY=-1;
	public static int boxX=-1, boxY=-1;
	public static int readStartX=-1, readStartY=-1, readEndX=-1, readEndY=-1;
	
	private static boolean printLoadedData=false; 
	
	public static void setupScreen() {
		System.out.println("Move the mouse to the TOP LEFT corner of the image...");
		stop(3);
		Point startLocation=MouseInfo.getPointerInfo().getLocation();
		pictureStartX=startLocation.x;
		pictureStartY=startLocation.y;
		
		System.out.println("\nMove the mouse to the BOTTOM RIGHT corner of the image...");
		stop(3);
		Point endLocation=MouseInfo.getPointerInfo().getLocation();
		pictureEndX=endLocation.x;
		pictureEndY=endLocation.y;
		
		saveData();
		
		System.out.println("\n\nThanks. Data has been saved.");
	}
	
	public static void stop(double secondsToWait) {
		try {
			Thread.sleep((int)(secondsToWait*1000));
		} catch (InterruptedException e) {
			//Catching exceptions is for communists.
		}
	}
	
	private static void saveData() {
		FileLoader.writeToFile("EryxPreferences", pictureStartX+" "+pictureStartY+"\n"+pictureEndX+" "+pictureEndY+"\n"+boxX+" "+boxY+
				"\n"+readStartX+" "+readStartY+'\n'+readEndX+' '+readEndY);
	}
	
	public static void loadData() {
		String[] returned=FileLoader.readFile("EryxPreferences");
		pictureStartX=Integer.parseInt(returned[0].split(" ")[0]);
		pictureStartY=Integer.parseInt(returned[0].split(" ")[1]);
		pictureEndX=Integer.parseInt(returned[1].split(" ")[0]);
		pictureEndY=Integer.parseInt(returned[1].split(" ")[1]);
		boxX=Integer.parseInt(returned[2].split(" ")[0]);
		boxY=Integer.parseInt(returned[2].split(" ")[1]);
		readStartX=Integer.parseInt(returned[3].split(" ")[0]);
		readStartY=Integer.parseInt(returned[3].split(" ")[1]);
		readEndX=Integer.parseInt(returned[4].split(" ")[0]);
		readEndY=Integer.parseInt(returned[4].split(" ")[1]);
		
		if (printLoadedData) {
			for (String thread:returned) {
				//multi-threading
				System.out.println(thread);
			}
		}
	}
	
}
