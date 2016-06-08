package processing;

import java.awt.Color;

public class Edge {
	public float intensity;
	public float angle;
	
	public Edge(float intensity, float angle) {
		this.intensity=intensity;
		this.angle=angle;
	}
	
	public static float[][] toFloatArray(Edge[][] edges) {
		float[][] toReturn=new float[edges.length][edges[0].length];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				toReturn[x][y]=edges[x][y].intensity;
			}
		}
		return toReturn;
	}
	
	public static Color[][] toColorArray(Edge[][] edges, float cutoff) {
		Color[][] toReturn=new Color[edges.length][edges[0].length];
		for (int x=0; x<toReturn.length; x++) {
			for (int y=0; y<toReturn[x].length; y++) {
				if (edges[x][y].intensity>cutoff) {
					float angle=edges[x][y].angle;
					double blue=Math.cos(angle);
					double red=Math.sin(angle);
					if (red>0)
						toReturn[x][y]=new Color((float)red, 0.0f, (float)blue, 1.0f);
					else 
						toReturn[x][y]=new Color(0f, -(float)red, (float)blue, 1.0f);
				}
				else {
					toReturn[x][y]=new Color(0, 0, 0, 0);
				}
			}
		}
		return toReturn;
	}
}
