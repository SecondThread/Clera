package templateMatching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import processing.ImageProcessor;
import robot.Main;
import robot.Window;
import saving.FileLoader;
import saving.ScreenSetup;

public class TemplateSaver {
	public static void saveTemplates(ArrayList<Template> templates, String fileName) {
		String total="";
		for (Template t:templates) {
			total+=t.toString()+"\n";
		}
		FileLoader.writeToFile(fileName, total);
	}

	public static ArrayList<Template> loadTemplates(String fileName) {
		ArrayList<Template> toReturn=new ArrayList<Template>();
		String[] file=FileLoader.readFile(fileName);
		for (String s:file) {
			toReturn.add(new Template(s));
		}
		return toReturn;
	}

	public static void templateRunner(ArrayList<Template> templates, String fileName) {
		Scanner s=new Scanner(System.in);
		ScreenSetup.loadData();
		Window.init();
		while (true) {
			System.out.println("Type command: \"view\", \"remove\", \"view all\", \"add\", \"clear\", \"exit\"");
			String command=s.nextLine();
			int index=-1;
			switch (command) {
				case "view":
					boolean oldUseSameWindow=Main.useSameWindow;
					Main.useSameWindow=false;
					System.out.println("What index? (0 to "+templates.size()+")");
					index=s.nextInt();
					s.nextLine();
					Window.displayPixels(templates.get(index).getTemplate(), "Template #"+index, false);
					Main.useSameWindow=oldUseSameWindow;
					break;
				case "remove":
					System.out.println("What index? (0 to "+templates.size()+")");
					index=s.nextInt();
					s.nextLine();
					templates.remove(index);
					saveTemplates(templates, fileName);
					break;
				case "view all":
					oldUseSameWindow=Main.useSameWindow;
					Main.useSameWindow=false;
					for (int i=0; i<templates.size(); i++) {
						Window.displayPixels(templates.get(i).getTemplate(), "Template #"+i, false);
					}
					Main.useSameWindow=oldUseSameWindow;
					break;
				case "clear":
					for (int i=0; i<100; i++)
						System.out.println();
					break;
				case "add":
					float[][] window=Window.getPixels(ScreenSetup.pictureStartX, ScreenSetup.pictureStartY, ScreenSetup.pictureEndX, ScreenSetup.pictureEndY);
					window=ImageProcessor.scaleImage(window, Main.sizeOfImage);
					Window.displayPixels(window, "Create Template");
					System.out.println("Click on the corner");
					ScreenSetup.stop(8);
					System.out.println("Thanks");
					templates.add(TemplateCreator.createTemplate(window));
					saveTemplates(templates, fileName);
					break;
				case "exit":
					saveTemplates(templates, fileName);
					System.out.println("Goodbye for now!");
					return;
				default:
					System.out.println("Error: not a command.");
					break;
			}
		}
	}

}
