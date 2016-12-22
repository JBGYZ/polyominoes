package polyomino;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		
		// Test de la generation des polyominos
		
		int n = 14;
		/*long t1 = System.currentTimeMillis();
		@SuppressWarnings("unused")
		LinkedList<Polyomino> liste2 = Polyomino.genererFixes(n);
		long t2 = System.currentTimeMillis();
		@SuppressWarnings("unused")
		LinkedList<Polyomino> liste = Polyomino.genererLibres(n);
		System.out.println(t2-t1+" opérations");*/
		
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
		
		// Test de ExactCover et DancingLinks
		
		Integer[][] M = { 
				{ 0, 0, 1, 0, 1, 1, 0 }, 
				{ 1, 0, 0, 1, 0, 0, 1 }, 
				{ 0, 1, 1, 0, 0, 1, 0 },
				{ 1, 0, 0, 1, 0, 0, 0 }, 
				{ 0, 1, 0, 0, 0, 0, 1 }, 
				{ 0, 0, 0, 1, 1, 0, 1 } };
		
		Integer[][] M2 = ExactCover.subsets(5);
		
		ExactCover.displayExactCover(M);
		DancingLinks.displayExactCover(M);
		
		// Test de la generation des polyominos facon Redelmeier (Fixed)
		//n = 5;
		long t3 = System.currentTimeMillis();
		LinkedList<Polyomino> liste1 = RedelmeierGenerator.genererFixe(n);
		long t4 = System.currentTimeMillis();
		Polyomino[] polyominoes = liste1.toArray(new Polyomino[0]);
		System.out.println(liste1.size());
		System.out.println(t4-t3+" opérations");
		//Polyomino.creerFenetre(new Configuration(polyominoes));
		
		
		// Test de la conversion en exactCover
		/*
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
		ExactCover.afficherExactCover(M3);*/
	}

}