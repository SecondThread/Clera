package mainClasses;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import compression.ConvertToString;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import processing.ImageProcessor;

public class DriverStationClient {

	private static JFrame frame;
	private static JPanel mainPanel, outerPanel;
	private static JButton visionToggle = new JButton();
//	private static JLabel visionStatus = new JLabel();
	private static boolean showHighGoalVision = false;

	public static void main(String[] args) {
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roboRIO-2202-FRC.local");
		NetworkTable table = NetworkTable.getTable("VisionTable");
		table.putBoolean("NeedPicture", true);
		table.putBoolean("NeedPictureHighGoal", true);

		visionToggle.setActionCommand("toggle");
		visionToggle.setText("Toggle Vision Mode");
		

		frame = new JFrame();
		mainPanel = new JPanel();
		outerPanel=new JPanel();
		frame.add(outerPanel);
		outerPanel.add(mainPanel);
		mainPanel.setPreferredSize(new Dimension(640, 480));
		outerPanel.add(visionToggle);
//		mainPanel.add(visionStatus);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		visionToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("button pressed");
				showHighGoalVision = !showHighGoalVision;
			}
		});

		System.out.println("Entering loop on Drivers station...");
		while (true) {
			if (showHighGoalVision) {
//				visionStatus.setText("Showing High Goal Vision");
				System.out.println("Showing Peg Vision");
				if (!table.getBoolean("NeedPicture", true)) {
					String pictureAsString = table.getString("Picture");
					table.putBoolean("NeedPicture", true);

					Color[][] imageAsColors = ConvertToString.convertBack(pictureAsString);
					imageAsColors = ImageProcessor.scaleImage(imageAsColors, 640);
					BufferedImage image = new BufferedImage(imageAsColors.length, imageAsColors[0].length,
							BufferedImage.TYPE_INT_RGB);
					for (int x = 0; x < imageAsColors.length; x++) {
						for (int y = 0; y < imageAsColors[x].length; y++) {
							image.setRGB(x, y, imageAsColors[x][y].getRGB());
						}
					}
					System.out.println("Displaying picture");
					mainPanel.getGraphics().drawImage(image, 0, 0, null);
				}
			} else {
				System.out.println("Showing High Goal Vision");
//				visionStatus.setText("Showing Peg Vision");
				if (!table.getBoolean("NeedPictureHighGoal", true)) {
					String pictureAsString = table.getString("PictureHighGoal");
					table.putBoolean("NeedPictureHighGoal", true);

					Color[][] imageAsColors = ConvertToString.convertBack(pictureAsString);
					imageAsColors = ImageProcessor.scaleImage(imageAsColors, 640);
					BufferedImage image = new BufferedImage(imageAsColors.length, imageAsColors[0].length,
							BufferedImage.TYPE_INT_RGB);
					for (int x = 0; x < imageAsColors.length; x++) {
						for (int y = 0; y < imageAsColors[x].length; y++) {
							image.setRGB(x, y, imageAsColors[x][y].getRGB());
						}
					}
					System.out.println("Displaying picture");
					mainPanel.getGraphics().drawImage(image, 0, 0, null);
				}
			}

		}

	}
}
