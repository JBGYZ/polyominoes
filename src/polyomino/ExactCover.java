package polyomino;

import java.util.LinkedList;
import java.util.Random;

public class ExactCover {

	public static void displayArray(Integer[] a) {
		String s = "";
		for (int i = 0; i < a.length; i++) {
			s += a[i];
		}
		System.out.println(s);
	}

	public static void displayMatrix(Integer[][] m) {
		for (int i = 0; i < m.length; i++) {
			displayArray(m[i]);
		}
	}

	// Returns the solution to the ExactCover problem as a list of partitions,
	// each partitions being a list of binary arrays

	public static LinkedList<LinkedList<Integer[]>> exactCover(Integer[][] M) {
		System.out.print("We search for the exact covers of M with the naive implementation of algorithm X : ");
		//displayMatrix(M);
		int cardC = M.length;
		int cardX = M[0].length;
		LinkedList<Integer> X = new LinkedList<Integer>();
		LinkedList<Integer> C = new LinkedList<Integer>();
		for (int x = 0; x < cardX; x++) {
			X.add(x);
		}
		for (int c = 0; c < cardC; c++) {
			C.add(c);
		}
		LinkedList<LinkedList<Integer[]>> partitions = exactCoverAux(M, X, C);
		System.out.println("done");
		System.out.println("There are " + partitions.size() + " partitions of M in total.");
		return partitions;
	}

	// Auxiliary function for the recursive calls

	@SuppressWarnings("unchecked")
	public static LinkedList<LinkedList<Integer[]>> exactCoverAux(Integer[][] M, LinkedList<Integer> X,
			LinkedList<Integer> C) {
		if (X.isEmpty()) {
			LinkedList<LinkedList<Integer[]>> partitions = new LinkedList<LinkedList<Integer[]>>();
			LinkedList<Integer[]> P = new LinkedList<Integer[]>();
			partitions.add(P);
			return partitions;
		}
		int x = X.poll();
		LinkedList<LinkedList<Integer[]>> partitions = new LinkedList<LinkedList<Integer[]>>();
		for (int S : C) {
			if (M[S][x] == 1) {
				LinkedList<Integer> X2 = (LinkedList<Integer>) X.clone();
				LinkedList<Integer> C2 = (LinkedList<Integer>) C.clone();
				for (int y = 0; y < M[S].length; y++) {
					if (M[S][y] == 1) {
						X2.removeFirstOccurrence(y);
						for (int T : C) {
							if (M[T][y] == 1) {
								C2.removeFirstOccurrence(T);
							}
						}
					}
				}
				for (LinkedList<Integer[]> P : exactCoverAux(M, X2, C2)) {
					P.add(M[S]);
					partitions.add(P);
				}
			}
		}
		return partitions;
	}

	// Displays every partition found

	public static void displayExactCover(LinkedList<LinkedList<Integer[]>> partitions) {
		int k = 0;
		for (LinkedList<Integer[]> P : partitions) {
			k += 1;
			System.out.println("Partition n° " + k);
			for (Integer[] part : P) {
				displayArray(part);
			}
		}
	}

	// Returns x^n

	public static int pow(int x, int n) {
		if (n == 1) {
			return x;
		} else {
			int y = pow(x, n / 2);
			if (n % 2 == 0) {
				return y * y;
			} else {
				return y * y * x;
			}
		}
	}

	// Returns the binary ExactCover matrix of 1...n by all its subsets

	public static Integer[][] subsets(int n) {
		Integer[][] M = new Integer[pow(2, n)][n];
		for (int i = 0; i < pow(2, n); i++) {
			String s = Integer.toBinaryString(i);
			while (s.length() < n) {
				s = "0" + s;
			}
			for (int j = 0; j < n; j++) {
				M[i][j] = Integer.decode("" + s.charAt(j));
			}
		}
		return M;
	}

	// Returns the binary ExactCover matrix of 1...n by all its subsets of size
	// k

	public static Integer[][] subsets(int n, int k) {
		LinkedList<Integer[]> sets = new LinkedList<Integer[]>();
		Integer[][] M = subsets(n);
		for (Integer[] s : M) {
			int card = 0;
			for (int x : s) {
				if (x == 1) {
					card++;
				}
			}
			if (card == k) {
				sets.add(s);
			}
		}
		Integer[][] setsArray = new Integer[sets.size()][];
		int i = 0;
		for (Integer[] s : sets) {
			setsArray[i] = s;
			i++;
		}
		return setsArray;
	}

}
