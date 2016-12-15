package polyomino;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		
		// Test de la generation des polyominos
		
		int n = 5;
		
		LinkedList<Polyomino> liste2 = Polyomino.genererFixes(n);
		@SuppressWarnings("unused")
		LinkedList<Polyomino> liste = Polyomino.genererLibres(n);
		System.out.println();
		
		/*for (Polyomino P2 : liste){
			//P2.afficheConsole();
			//out.println();
			//System.out.println(P2);
			//System.out.println();
			
		// Test de l'affichage des polyominos
			
		}
		Polyomino[] polyominoes = new Polyomino[liste.size()];
		int k = 0;
		for (Polyomino p : liste){
			polyominoes[k] = p;
			k++;
		}*/
		//Polyomino.creerFenetre(new Configuration(polyominoes));
		
		// Test de ExactCover
		
		@SuppressWarnings("unused")
		Integer[][] M = { { 0, 0, 1, 0, 1, 1, 0 }, { 1, 0, 0, 1, 0, 0, 1 }, { 0, 1, 1, 0, 0, 1, 0 },
				{ 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 1, 0, 1 } };
		//ExactCover.afficherExactCover(M);
		
		//Integer[][] M2 = ExactCover.subsets(7);
		//ExactCover.afficherExactCover(M2);
		
		// Test de DancingLinks

		//DancingLinks D = new DancingLinks(M);
		//System.out.println(D);
		
		// Test de la generation des polyominos facon Redelmeier (Fixed)
		//LinkedList<Polyomino> liste = RedelmeierGenerator.genererFixe(n);
		//Polyomino[] polyominoes = liste.toArray(new Polyomino[0]);
		//Polyomino.creerFenetre(new Configuration(polyominoes));
		// Test de la conversion en exactCover
		
		int s = 5;
		boolean[][] region = new boolean[s][s];
		for (int i = 0; i < s; i++){
			for (int j = 0; j < s; j++){
				region[i][j] = true;
			}
		}
		Integer[][] M3 = ExactCover.toExactCover(region, liste2);
		for (Integer[] X : M3){
			for (Integer x : X){
				System.out.print(x);
			}
			System.out.println();
		}
		ExactCover.afficherExactCover(M3);
	}

}