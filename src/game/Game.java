package game;

import java.awt.Color;

public class Game {
	
	Color color=Color.white;
	
	//to be called 20 times a second
	public void update() {
	}
	
	public Color[][] render() {
		Color[][] toReturn=new Color[300][200];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				toReturn[x][y]=color;
			}
		}
		return toReturn;
	}
}
