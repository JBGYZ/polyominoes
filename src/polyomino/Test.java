package polyomino;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		
		// Test de la generation des polyominos
		
		int n = 5;
		/*
		LinkedList<Polyomino> liste2 = Polyomino.genererFixes(n);
		LinkedList<Polyomino> liste = Polyomino.genererLibres(n);
		System.out.println();
		
		for (Polyomino P2 : liste){
			P2.afficheConsole();
			System.out.println();
		}
		Polyomino[] polyominoes = new Polyomino[liste.size()];
		int i = 0;
		for (Polyomino p : liste){
			polyominoes[i] = p;
			i++;
		}
		Polyomino.creerFenetre(new Configuration(polyominoes));*/
		
		// Test de ExactCover
		
		//Integer[][] M = { { 0, 0, 1, 0, 1, 1, 0 }, { 1, 0, 0, 1, 0, 0, 1 }, { 0, 1, 1, 0, 0, 1, 0 },
		//		{ 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 1, 0, 1 } };
		//ExactCover.afficherExactCover(M);
		
		//Integer[][] M2 = ExactCover.subsets(7);
		//ExactCover.afficherExactCover(M2);
		
		// Test de DancingLinks

		//DancingLinks D = new DancingLinks(M);
		//System.out.println(D);
		
		// Test de la generation des polyominos facon Redelmeier (Fixed)
		LinkedList<Polyomino> liste = RedelmeierGenerator.genererFixe(n);
		Polyomino[] polyominoes = liste.toArray(new Polyomino[0]);
		Polyomino.creerFenetre(new Configuration(polyominoes));
	}

}