package aup.condorcetgraph;

import java.io.Serializable;
import java.util.Random;

public class MatrixGraph implements Serializable {
	public String title;
	public double[] votes;
	public boolean[][] edges;
	public MatrixGraph(int size) {
		title = "";
		votes = new double[size];
		edges = new boolean[size][];
		for(int i = 0; i < size; i++) {
			edges[i] = new boolean[size];
		}
		for(int i = 0; i < size; i++) {
			edges[i][i] = true;
		}
	}
	public void addVertices(int n) {
		int vertices = votes.length;
		double[] new_votes = new double[vertices + n];
		for(int i = 0; i < vertices; i++) {
			new_votes[i] = votes[i];
		}
		boolean[][] new_edges = new boolean[vertices + n][];
		for(int i = 0; i < vertices + n; i++) {
			new_edges[i] = new boolean[vertices + n];
		}
		for(int i = 0; i < vertices; i++) {
			for(int j = 0; j < vertices; j++) {
				new_edges[i][j] = edges[i][j];
			}
		}
		for(int i = vertices; i < vertices + n; i++) {
			new_edges[i][i] = true;
		}
		votes = new_votes;
		edges = new_edges;
	}
	public void removeVertex(int v) {
		int vertices = votes.length;
		double[] new_votes = new double[vertices - 1];
		for(int i = 0; i < vertices; i++) {
			if(i == v) continue;
			new_votes[i < v ? i : i - 1] = votes[i];
		}
		boolean[][] new_edges = new boolean[vertices - 1][];
		for(int i = 0; i < vertices - 1; i++) {
			new_edges[i] = new boolean[vertices - 1];
		}
		for(int i = 0; i < vertices; i++) {
			if(i == v) continue;
			for(int j = 0; j < vertices; j++) {
				if(j == v) continue;
				new_edges[i < v ? i : i - 1][j < v ? j : j - 1] = edges[i][j];
			}
		}
		votes = new_votes;
		edges = new_edges;
	}
	public String toString() {
		return Base64.encodeObject(this,false);
	}
	public void randomize(boolean d, double p, boolean v) {
		for(int i = 0; i < votes.length; i++ ) {
			for(int j = d ? 0 : i + 1; j < votes.length; j++ ) {
				if(i == j) continue;
				edges[i][j] = Math.random() < p;
				if(!d) {
					edges[j][i] = edges[i][j];
				}
			}
			
		}
		if(v) {
			Random random = new Random();
			for(int i = 0; i < votes.length; i++ ) {
				votes[i] = random.nextFloat() * 10.0 ;
				votes[i] -= votes[i] % .01;
			}
		}
	}
}
