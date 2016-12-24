package polyomino;

import java.util.LinkedList;
import java.util.Random;

public class ExactCover {

	public static void afficherTableau(Integer[] tableau) {
		String s = "";
		for (int i = 0; i < tableau.length; i++) {
			s += tableau[i];
		}
		System.out.println(s);
	}

	public static void afficherMatrice(Integer[][] matrice) {
		String s = "";
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[0].length; j++) {
				if (matrice[i][j] == 1) {
					s += "1";
				} else {
					s += "0";
				}
			}
			System.out.println(s);
			s = "";
		}
	}
	
	// genere tous les exactCover sous forme d'une liste de partitions, chaque partitions étant une liste de tableaux d'entier, chaque tableau correspondant a une part et contenant des zeros et des uns
	
	@SuppressWarnings("unchecked")
	public static LinkedList<LinkedList<Integer[]>> exactCover(Integer[][] M, LinkedList<Integer> X,
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
				for (LinkedList<Integer[]> P : exactCover(M, X2, C2)) {
					P.add(M[S]);
					partitions.add(P);
				}
			}
		}
		return partitions;
	}

	// affiche toutes les partitions possibles
	
	public static void displayExactCover(Integer[][] M) {
		System.out.println("We look for all partitions of the matrix M (naive method) :");
		//afficherMatrice(M);
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
		LinkedList<LinkedList<Integer[]>> partitions = exactCover(M, X, C);
		
		int k = 0;
		for (LinkedList<Integer[]> P : partitions) {
			k += 1;
			System.out.println("Partition numero " + k);
			for (Integer[] partie : P) {
				afficherTableau(partie);
			}
		}
		
		System.out.println("There are " + partitions.size() + " partitions in total.");
		System.out.println();
	}

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
	
	// renvoie la matrice binaire contenant tous les sous-ensembles de 1...n

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
	
	// renvoie la matrice binaire contenant tous les sous-ensembles de 1...n de card k
	
	public static Integer[][] subsets(int n, int k){
		LinkedList<Integer[]> sets = new LinkedList<Integer[]>();
		Integer[][] M = subsets(n);
		for (Integer[] s : M){
			int card = 0;
			for (int x : s){
				if (x==1){
					card++;
				}
			}
			if (card == k){
				sets.add(s);
			}
		}
		Integer[][] setsArray = new Integer[sets.size()][];
		int i = 0;
		for (Integer[] s : sets){
			setsArray[i] = s;
			i++;
		}
		return setsArray;
	}

}
