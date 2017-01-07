package polyomino;

import java.util.LinkedList;

class Possibility {

	int row, col, n;

	public Possibility(int row, int col, int n) {
		this.row = row;
		this.col = col;
		this.n = n;
	}

	public String toString() {
		return "Put the number " + n + " in row " + row + " and column " + col;
	}
}

public class Sudoku {

	public static void displayGrid(Integer[][] M) {
		for (int i = 0; i < 3; i++) {
			String s = " ";
			for (int j = 0; j < 3; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
				;
			}
			s += " ";
			for (int j = 3; j < 6; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
				;
			}
			s += " ";
			for (int j = 6; j < 9; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
				;
			}
			System.out.println(s);
		}
		System.out.println();
		for (int i = 3; i < 6; i++) {
			String s = " ";
			for (int j = 0; j < 3; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
				;
			}
			s += " ";
			for (int j = 3; j < 6; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
				;
			}
			s += " ";
			for (int j = 6; j < 9; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
				;
			}
			System.out.println(s);
		}
		System.out.println();
		for (int i = 6; i < 9; i++) {
			String s = " ";
			for (int j = 0; j < 3; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
			}
			s += " ";
			for (int j = 3; j < 6; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
			}
			s += " ";
			for (int j = 6; j < 9; j++) {
				if (M[i][j] == 0) {
					s += "-";
				} else {
					s += M[i][j];
				}
			}
			System.out.println(s);
		}
	}

	public static int box(int row, int col) {
		// the boxes are numbered like this :
		// 0 1 2
		// 3 4 5
		// 6 7 8
		if (row < 3 && col < 3) {
			return 0;
		}
		if (row < 6 && col < 3) {
			return 1;
		}
		if (row < 9 && col < 3) {
			return 2;
		}
		if (row < 3 && col < 6) {
			return 3;
		}
		if (row < 6 && col < 6) {
			return 4;
		}
		if (row < 9 && col < 6) {
			return 5;
		}
		if (row < 3 && col < 9) {
			return 6;
		}
		if (row < 6 && col < 9) {
			return 7;
		}
		if (row < 9 && col < 9) {
			return 8;
		} else {
			return 9;
		}
	}

	public static void sudokuSolver(Integer[][] partial) {
		System.out.println("Partial grid :");
		System.out.println("");
		displayGrid(partial);
		System.out.println();
		boolean[] C = new boolean[4 * 9 * 9];
		// inactive constraints
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				for (int n = 0; n < 9; n++) {
					// constraint 1 : row-column
					C[0 * 81 + (9 * row + col)] = false;
					// constraint 2 : row-number
					C[1 * 81 + (9 * row + n)] = false;
					// constraint 3 : column-number;
					C[2 * 81 + (9 * col + n)] = false;
					// constraint 4 : box-number
					C[3 * 81 + (9 * box(row, col) + n)] = false;
				}
			}
		}
		// active constraints
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				int n = partial[row][col];
				if (n != 0) {
					n--;
					// constraint 1 : row-column
					C[0 * 81 + (9 * row + col)] = true;
					// constraint 2 : row-number
					C[1 * 81 + (9 * row + n)] = true;
					// constraint 3 : column-number;
					C[2 * 81 + (9 * col + n)] = true;
					// constraint 4 : box-number
					C[3 * 81 + (9 * box(row, col) + n)] = true;
				}
			}
		}
		LinkedList<LinkedList<Integer>> ML = new LinkedList<LinkedList<Integer>>();
		LinkedList<Possibility> P = new LinkedList<Possibility>();
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				for (int n = 0; n < 9; n++) {
					if (!C[0 * 81 + (9 * row + col)] && !C[1 * 81 + (9 * row + n)] && !C[2 * 81 + (9 * col + n)]
							&& !C[3 * 81 + (9 * box(row, col) + n)]) {
						LinkedList<Integer> L = new LinkedList<Integer>();
						// constraint 1 : row-column
						for (int j = 0; j < 81; j++) {
							if (!C[j]) {
								if (j == 0 * 81 + (9 * row + col)) {
									L.add(1);
								} else {
									L.add(0);
								}
							}
						}
						// constraint 2 : row-number
						for (int j = 81; j < 2 * 81; j++) {
							if (!C[j]) {
								if (j == 1 * 81 + (9 * row + n)) {
									L.add(1);
								} else {
									L.add(0);
								}
							}
						}
						// constraint 3 : column-number
						for (int j = 2 * 81; j < 3 * 81; j++) {
							if (!C[j]) {
								if (j == 2 * 81 + (9 * col + n)) {
									L.add(1);
								} else {
									L.add(0);
								}
							}
						}
						// constraint 4 : box-number
						for (int j = 3 * 81; j < 4 * 81; j++) {
							if (!C[j]) {
								if (j == 3 * 81 + (9 * box(row, col) + n)) {
									L.add(1);
								} else {
									L.add(0);
								}
							}
						}
						ML.add(L);
						P.add(new Possibility(row, col, n + 1));
					}
				}
			}
		}
		Integer[][] M = new Integer[ML.size()][ML.getFirst().size()];
		for (int i = 0; i < ML.size(); i++) {
			LinkedList<Integer> L = ML.get(i);
			M[i] = L.toArray(new Integer[0]);
		}
		LinkedList<Integer[]> partition = DancingLinks.findExactCover(M);
		System.out.println();
		System.out.println("Solution : ");
		for (Integer[] line : partition) {
			for (int i = 0; i < M.length; i++) {
				boolean eq = true;
				for (int j = 0; j < line.length; j++) {
					if (line[j] != M[i][j]) {
						eq = false;
					}
				}
				if (eq) {
					Possibility pos = P.get(i);
					System.out.println(pos);
					partial[pos.row][pos.col] = pos.n;
				}
			}
		}
		System.out.println("");
		System.out.println("Complete grid : ");
		System.out.println("");
		displayGrid(partial);
	}

	public static void main(String[] args) {
		Integer[][] partial = { { 0, 0, 0, 0, 0, 0, 9, 0, 2 }, { 0, 0, 0, 1, 0, 5, 0, 4, 0 },
				{ 1, 0, 0, 0, 4, 0, 5, 6, 0 }, { 8, 0, 9, 0, 6, 0, 4, 2, 3 }, { 0, 2, 0, 8, 0, 3, 0, 9, 1 },
				{ 7, 0, 0, 9, 0, 0, 8, 5, 0 }, { 0, 6, 0, 0, 3, 0, 0, 0, 4 }, { 2, 0, 7, 4, 0, 9, 0, 0, 5 },
				{ 0, 0, 5, 0, 0, 6, 0, 7, 0 } };

		sudokuSolver(partial);
	}

}
