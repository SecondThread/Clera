
package mainClasses;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	// private static JLabel visionStatus = new JLabel();
	private static boolean showHighGoalVision = false;

	public static void main(String[] args) {
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roboRIO-2202-FRC.local");
		NetworkTable table = NetworkTable.getTable("VisionTable");
		table.putBoolean("NeedPicture", true);
		table.putBoolean("NeedPictureHighGoal", true);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		visionToggle.setActionCommand("toggle");
		visionToggle.setText("Toggle Vision Mode");

		frame = new JFrame();
		outerPanel = new JPanel();

		mainPanel = new JPanel();
		outerPanel.add(mainPanel);
		frame.add(outerPanel);
		mainPanel.setPreferredSize(new Dimension(640, 480));
		outerPanel.add(visionToggle);
		// mainPanel.add(visionStatus);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		outerPanel.requestFocus();
		outerPanel.addKeyListener(new KeyListener() {

			
			@Override
			public void keyPressed(KeyEvent e) {
				
				System.out.println(e.getKeyCode() + "button pressed");
				if (e.getKeyCode() == KeyEvent.VK_S) {
					showHighGoalVision = !showHighGoalVision;
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		visionToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("button pressed");
				showHighGoalVision = !showHighGoalVision;
				if (showHighGoalVision) {
					System.out.println("Showing Peg Vision");
				}else{
					System.out.println("Showing High Goal Vision");
				}
			}
		});

		System.out.println("Entering loop on Drivers station...");
		while (true) {
			if (showHighGoalVision) {
				// visionStatus.setText("Showing High Goal Vision");
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
					//System.out.println("Displaying Low picture");
					mainPanel.getGraphics().drawImage(image, 0, 0, null);
				}
			} else {
				// visionStatus.setText("Showing Peg Vision");
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
					//System.out.println("Displaying High picture");
					mainPanel.getGraphics().drawImage(image, 0, 0, null);
				}
			}

		}

	}
}
