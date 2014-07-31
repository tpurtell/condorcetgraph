 package aup.condorcetgraph;
public class IntegerMatrix {
	int[][] matrix;
	public IntegerMatrix(int s) { 
		matrix = new int[s][];
		for(int i = 0; i < s; i++) {
			matrix[i] = new int[s]; 
			matrix[i][i] = 1;
		}
		
	}
	public IntegerMatrix(boolean[][] m) {
		matrix = new int[m.length][];
		for(int i = 0; i < m.length; i++) {
			matrix[i] = new int[m[i].length];
			for(int j = 0; j < m[i].length; j++) {
				matrix[i][j] = m[i][j] ? 1 : 0;
			}
		}
	}
	public IntegerMatrix(IntegerMatrix m) {
		matrix = new int[m.matrix.length][];
		for(int i = 0; i < m.matrix.length; i++) {
			matrix[i] = (int[])m.matrix[i].clone(); 
		}
	}
	public IntegerMatrix(int[][] m) {
		matrix = new int[m.length][];
		for(int i = 0; i < m.length; i++) {
			matrix[i] = (int[])m[i].clone(); 
		}
	}
	public IntegerMatrix(int r, int c) {
		matrix = new int[r][];
		for(int i = 0; i < r; i++) {
			matrix[i] = new int[c]; 
		}
	}
	public IntegerMatrix multiply(IntegerMatrix m) {
		if(matrix[0].length != m.matrix.length) {
			throw new RuntimeException("bad sized matrixes");
		}
		IntegerMatrix r = new IntegerMatrix(matrix.length, m.matrix[0].length);
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < m.matrix[0].length; j++) {
				for(int k = 0; k < matrix[0].length; k++) {
					r.matrix[i][j] += matrix[i][k] * m.matrix[k][j];
				}
			}
		}
		return r;
	}
	public IntegerMatrix minOfPowers() {
		if(matrix.length != matrix[0].length) {
			throw new RuntimeException("bad sized matrixes");
		}
		IntegerMatrix r = new IntegerMatrix(matrix.length,matrix.length);
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {
				r.matrix[i][j] = Integer.MAX_VALUE;
			}
		}
		IntegerMatrix path_count = new IntegerMatrix(matrix.length);
		for(int n = 0; n < matrix.length; n++) {
			for(int i = 0; i < matrix.length; i++) {
				for(int j = 0; j < matrix.length; j++) {
					if(path_count.matrix[i][j] != 0 && n < r.matrix[i][j]) {
						r.matrix[i][j] = n;
					}
				}
			}
			path_count = path_count.multiply(this); 			
		}
		
		return r;
	}
	public int getRows() {
		return matrix.length;
	}
	public int getColumns() {
		return matrix[0].length;
	}
	public int getEntry(int r, int c) {
		return matrix[r][c];
	}
}
