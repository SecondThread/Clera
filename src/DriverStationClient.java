import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import compression.ConvertToString;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import processing.ImageProcessor;

public class DriverStationClient {
	
	private static JFrame frame;
	private static JPanel mainPanel;
	
	public static void main(String[] args) {
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roboRIO-2202-FRC.local");
		NetworkTable table=NetworkTable.getTable("VisionTable");
		table.putBoolean("NeedPicture", true);
		
		frame=new JFrame();
		mainPanel=new JPanel();
		frame.add(mainPanel);
		mainPanel.setPreferredSize(new Dimension(640, 480));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		System.out.println("Entering loop on Drivers station...");
		while (true) {
			if (!table.getBoolean("NeedPicture", true)) {
				String pictureAsString=table.getString("Picture");
				table.putBoolean("NeedPicture", true);
	
				Color[][] imageAsColors=ConvertToString.convertBack(pictureAsString);
				imageAsColors=ImageProcessor.scaleImage(imageAsColors, 640);
				BufferedImage image=new BufferedImage(imageAsColors.length, imageAsColors[0].length, BufferedImage.TYPE_INT_RGB);
				for (int x=0; x<imageAsColors.length; x++) {
					for (int y=0; y<imageAsColors[x].length; y++) {
						image.setRGB(x, y, imageAsColors[x][y].getRGB());
					}
				}
				System.out.println("Displaying picture");
				mainPanel.getGraphics().drawImage(image, 0, 0, null);
			}
		}

		
	}
	
}
