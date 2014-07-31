package aup.condorcetgraph;

public class TallyGraph {
	IntegerMatrix distances;
	double[] votes;
	public TallyGraph(MatrixGraph graph) {
		distances = new IntegerMatrix(graph.edges).minOfPowers();
		votes = (double[])graph.votes.clone();
	}
	public int getCount() {
		return votes.length;
	}
	public double getVotes(int i ) {
		return votes[i];
	}
	public void setVotes(int i, double v) {
		votes[i] = v;
	}
	public int[] tally(int depth) {
		int[] tally = new int[votes.length];
		for(int i = 0; i < votes.length; i++) { 
			for(int j = i + 1; j < votes.length; j++) {
				double s_i = 0;
				double s_j = 0;
				for(int k =  0; k < votes.length; k++) {
					int d_ki = distances.getEntry(k, i);
					int d_kj = distances.getEntry(k, j);
					if(d_ki < depth && d_ki < d_kj) {
						s_i += votes[k];
					}
					if(d_kj < depth && d_kj < d_ki) {
						s_j += votes[k];
					}
				}
				if(s_i > s_j) {
					tally[i]++;
				}
				else if (s_j > s_i) {
					tally[j]++;
				}
			}
		}
		int max = Integer.MIN_VALUE;
		int count = 0;
		for(int i = 0; i < votes.length; i++) {
			if(tally[i] > max) {
				max = tally[i];
				count = 1;
			}
			else if(tally[i] == max) {
				count ++;
			}
		}
		int[] winners = new int[count]; 
		for(int i = 0, j = 0; i < votes.length; i++) {
			if(tally[i] == max) {
				winners[j++] = i;
			}
		}
		return winners;
	}
	public int[] tallyCycles(int depth) {
		int[] tally = new int[votes.length];
		boolean[][] beat = new boolean[votes.length][];
		for(int i = 0; i < votes.length; i++) {
			beat[i] = new boolean[votes.length];
		}
		for(int i = 0; i < votes.length; i++) {
			for(int j = i + 1; j < votes.length; j++) {
				double s_i = 0;
				double s_j = 0;
				for(int k =  0; k < votes.length; k++) {
					int d_ki = distances.getEntry(k, i);
					int d_kj = distances.getEntry(k, j);
					if(d_ki < depth && d_ki < d_kj) {
						s_i += votes[k];
					}
					if(d_kj < depth && d_kj < d_ki) {
						s_j += votes[k];
					}
				}
				if(s_i > s_j) {
					beat[i][j] = true;
					beat[j][i] = false;
					tally[i]++;
				}
				else if (s_j > s_i) {
					beat[j][i] = true;
					beat[i][j] = false;
					tally[j]++;
				}
			}
		}
		int max = Integer.MIN_VALUE; 
		int count = 0;
		for(int i = 0; i < votes.length; i++) {
			if(tally[i] > max) {
				max = tally[i];
				count = 1;
			}
			else if(tally[i] == max) {
				count ++;
			}
		}
		int[] most = new int[count]; 
		for(int i = 0, j = 0; i < votes.length; i++) {
			if(tally[i] == max) {
				most[j++] = i;
			}
		}
		if(most.length == 1) {
			return most;
		}
		
		for(int i = 0; i < most.length; i++) {
			for(int j = i + 1; j < most.length; j++) {
				if(beat[most[j]][most[i]]) {
					int temp = most[i];
					most[i] = most[j];
					most[j] = temp;
				}
			}		
		}
		int[] most_r = (int[])most.clone();
		for(int i = 0; i < most_r.length; i++) {
			for(int j = i + 1; j < most_r.length; j++) {
				if(beat[most_r[j]][most_r[i]]) {
					int temp = most_r[i];
					most_r[i] = most_r[j];
					most_r[j] = temp;
				}
			}		
		}
		int[] cycle = new int[most.length];
		int j = 0;
		for(int i = 0; i < most.length; i++) {
			if(most[i] != most_r[i]) {
				cycle[j++] = most[i];
			}
		}
		if(j == 0) {
			int k;
			for(k = 0; k < most.length - 1; k++) {
				if(beat[most[k]][most[k + 1]]) {
					k++;
					break;
				}
			}
			int[] winners = new int[k];
			for(int i = 0 ; i < k; i++) {
				winners[i] = most[i];
			}
			return winners;
		}
		
		int[] winners = new int[j];
		for(int i = 0 ; i < j; i++) {
			winners[i] = cycle[i];
		}
		return winners;
	}
}
