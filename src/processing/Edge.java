package processing;

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
}
