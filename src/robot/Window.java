package robot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import saving.ScreenSetup;

public class Window {
	private static Robot r2d2;
	private static JPanel panel;
	private static Color[][] lastImage;
	
	private static int x = 0, y = 50;
	
	private static JPanel lastWindowCreated=null;
	private static JFrame frame;
	public static int lastClickX, lastClickY;
	
	public static void init() {
		try {
			r2d2 = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static float[][] getPixels(int startX, int startY, int endX, int endY) {
		Rectangle circle = new Rectangle(startX, startY, endX - startX, endY - startY);
		BufferedImage bufferedImage = r2d2.createScreenCapture(circle);
		float[][] brightness = new float[bufferedImage.getWidth()][bufferedImage.getHeight()];
		lastImage=new Color[bufferedImage.getWidth()][bufferedImage.getHeight()];
		
		for (int x = 0; x < bufferedImage.getWidth(); x++) {
			for (int y = 0; y < bufferedImage.getHeight(); y++) {
				brightness[x][y] = getNormalLuminence(x, y, bufferedImage);
				lastImage[x][y]=new Color(bufferedImage.getRGB(x, y));
			}
		}
		return brightness;
	}

	private static float getNormalLuminence(int x, int y, BufferedImage image) {
		Color color = new Color(image.getRGB(x, y));
		// I have no idea where these magic numbers came from. I just kinda
		// copied them from a formula on Wikepedia.
		return 0.2126f * color.getRed() / 255 + 0.7152f * color.getGreen() / 255 + 0.0722f * color.getBlue() / 255;
	}
	
	private static float getBlackLumenance(int x, int y, BufferedImage image) {
		Color color = new Color(image.getRGB(x, y));
		return (255-(.3f*color.getRed()+.3f*color.getGreen()+.3f*color.getBlue()))/255;
	}

	public static void displayPixels(float[][] pixels, String pictureName) {
		displayPixels(pixels, pictureName, true);
	}
	
	public static void displayPixels(float[][] pixels, String pictureName, boolean exitOnClose) {
		BufferedImage toDraw = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				Color inRGB = new Color(pixels[x][y], pixels[x][y], pixels[x][y]);
				toDraw.setRGB(x, y, inRGB.getRGB());
			}
		}
		drawImage(toDraw, pictureName, exitOnClose);
	}

	public static void displayPixels(boolean[][] pixels, String pictureName) {
		BufferedImage toDraw = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				Color inRGB;
				if (pixels[x][y]) {
					inRGB = Color.yellow;
				} else {
					inRGB = Color.black;
				}
				toDraw.setRGB(x, y, inRGB.getRGB());
			}
		}
		drawImage(toDraw, pictureName, true);
	}

	public static void displayPixels(Color[][] pixels, String pictureName) {
		BufferedImage toDraw = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				toDraw.setRGB(x, y, pixels[x][y].getRGB());
			}
		}
		drawImage(toDraw, pictureName, true);
	}
	
	public static void displayPixelsWithPeaks(float[][] pixels, ArrayList<Point> peaks, Point target,
			String pictureName) {
		Color[][] colors=new Color[pixels.length][pixels[0].length];
		for (int x=0; x<colors.length; x++) {
			for (int y=0; y<colors[x].length; y++) {
				colors[x][y]=new Color(pixels[x][y], pixels[x][y], pixels[x][y]);
			}
		}
		displayPixelsWithPeaks(colors, peaks, target, pictureName);
	}
	
	public static void displayPixelsWithPeaks(Color[][] pixels, ArrayList<Point> peaks, Point target,
			String pictureName) {
		BufferedImage toDraw = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < pixels.length; x++) {
			for (int y = 0; y < pixels[x].length; y++) {
				Color inRGB =pixels[x][y];
				toDraw.setRGB(x, y, inRGB.getRGB());
			}
		}
		Color peakColor = Color.red, targetColor = Color.green;
		
		if (peaks!=null) {
			for (Point peak : peaks) {
				if (peak.x>1&&peak.y>1&&peak.x+2<pixels.length&&peak.y+2<pixels[0].length) {
					toDraw.setRGB(peak.x, peak.y, peakColor.getRGB());
					toDraw.setRGB(peak.x - 1, peak.y, peakColor.getRGB());
					toDraw.setRGB(peak.x + 1, peak.y, peakColor.getRGB());
					toDraw.setRGB(peak.x, peak.y - 1, peakColor.getRGB());
					toDraw.setRGB(peak.x, peak.y + 1, peakColor.getRGB());
				}
			}
		}

		if (target.x>5&&target.y>5&&target.x<pixels.length-6&&target.y<pixels[0].length-6) {
			for (int x = -5; x <= 5; x++) {
				for (int y = -5; y <= 5; y++) {
					if (Math.max(Math.abs(x), Math.abs(y)) % 2 == 0)
						if (target!=null)toDraw.setRGB(target.x + x, target.y + y, targetColor.getRGB());
				}
			}
		}
		drawImage(toDraw, pictureName, true, true);
	}

	private static float[][] flipImageHorizontally(float[][] toFlip) {
		float[][] toReturn=new float[toFlip.length][toFlip[0].length];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				toReturn[x][y]=toFlip[toFlip.length-1-x][y];
			}
		}
		return toReturn;
	}
	
	private static Color[][] flipImageHorizontally(Color[][] toFlip) {
		Color[][] toReturn=new Color[toFlip.length][toFlip[0].length];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				toReturn[x][y]=toFlip[toFlip.length-1-x][y];
			}
		}
		return toReturn;
	}
	
	private static void drawImage(BufferedImage i, String pictureName, boolean exitOnClose) {
		drawImage(i, pictureName, exitOnClose, false);
	}
	
	private static void drawImage(BufferedImage i, String pictureName, boolean exitOnClose, boolean fullScreen) {
		if (!Main.useSameWindow || panel == null) {
			JPanel panel = new JPanel();
			if (Main.useSameWindow)
				Window.panel=panel;
			panel.setPreferredSize(new Dimension(i.getWidth(), i.getHeight()));
			lastWindowCreated=panel;
			lastWindowCreated.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
					Window.lastClickX=e.getX();
					Window.lastClickY=e.getY();
				}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
			});
			frame = new JFrame();
			frame.setTitle(pictureName);
			frame.add(panel);
			frame.pack();
			frame.setResizable(false);
			if (!Main.useSameWindow) {
				frame.setLocation(x, y);
			} else {
				frame.setLocation(0, 0);
			}
			x += i.getWidth();
			if (x > i.getWidth() * 3) {
				x = 0;
				y += i.getHeight();
			}
			frame.setForeground(Color.black);
			if (exitOnClose) frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			else frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			ScreenSetup.stop(.1);
			Graphics graphics = panel.getGraphics();
			graphics.drawImage(i, 0, 0, null);
			graphics.dispose();
		}
		else {
			if (fullScreen) {
				if (panel.getWidth()<500) {
					panel.setPreferredSize(new Dimension(500, 500*i.getHeight()/i.getWidth()));
				}
				frame.pack();
			}
			Graphics graphics = panel.getGraphics();
			if (fullScreen) {
				graphics.drawImage(i, 0, 0, panel.getWidth(), panel.getHeight(), null);
			}
			else {
				graphics.drawImage(i, 0, 0, null);
			}
			graphics.dispose();
		}
	}

	public static float getAverageInputColor() {
		float[][] screen = getPixels(ScreenSetup.readStartX, ScreenSetup.readStartY, ScreenSetup.readEndX,
				ScreenSetup.readEndY);
		float average = 0;
		for (int x = 0; x < screen.length; x++) {
			for (int y = 0; y < screen[x].length; y++) {
				average += screen[x][y];
			}
		}
		average /= screen.length * screen[0].length;
		return average;
	}

	public static void typeDegrees(float degrees) {
		r2d2.mouseMove(ScreenSetup.boxX, ScreenSetup.boxY);
		r2d2.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r2d2.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		r2d2.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r2d2.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		String asString = "" + degrees;
		StringSelection stringSelection = new StringSelection(asString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);

		r2d2.keyPress(KeyEvent.VK_CONTROL);
		r2d2.keyPress(KeyEvent.VK_V);
		r2d2.keyRelease(KeyEvent.VK_V);
		r2d2.keyRelease(KeyEvent.VK_CONTROL);
		
		r2d2.keyPress(KeyEvent.VK_ENTER);
		r2d2.keyRelease(KeyEvent.VK_ENTER);
	}

	public static Color[][] getLastImage() {
		return lastImage;
	}
}
