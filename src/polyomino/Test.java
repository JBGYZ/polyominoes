package polyomino;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		
		// Test de la generation des polyominos
		
		int n = 5;
		long t1 = System.currentTimeMillis();
		@SuppressWarnings("unused")
		LinkedList<Polyomino> listeFixes = Polyomino.genererFixes(n);
		long t2 = System.currentTimeMillis();
		@SuppressWarnings("unused")
		LinkedList<Polyomino> listeLibres = Polyomino.genererLibres(n);
		// System.out.println(t2-t1+" operations");
		
		/*
		for (Polyomino P2 : listeLibres){
			P2.afficheConsole();
			System.out.println();
			System.out.println(P2);
			System.out.println();
		}
		*/
			
		// Test de l'affichage des polyominos
		
		LinkedList<Polyomino> liste = listeLibres;
		Polyomino[] polyominoes = liste.toArray(new Polyomino[liste.size()]);
		// Polyomino.creerFenetre(new Configuration(polyominoes));
		
		// Test de ExactCover et DancingLinks
		
		Integer[][] M = { 
				{ 0, 0, 1, 0, 1, 1, 0 }, 
				{ 1, 0, 0, 1, 0, 0, 1 }, 
				{ 0, 1, 1, 0, 0, 1, 0 },
				{ 1, 0, 0, 1, 0, 0, 0 }, 
				{ 0, 1, 0, 0, 0, 0, 1 }, 
				{ 0, 0, 0, 1, 1, 0, 1 } };
		
		Integer[][] M2 = ExactCover.subsets(5);
		
		//ExactCover.displayExactCover(M);
		//DancingLinks.displayExactCover(M);
		
		// Test de la generation des polyominos facon Redelmeier (Fixed)
		
		/*
		n = 5;
		long t3 = System.currentTimeMillis();
		LinkedList<Polyomino> liste1 = RedelmeierGenerator.genererFixe(n);
		long t4 = System.currentTimeMillis();
		Polyomino[] polyominoes = liste1.toArray(new Polyomino[0]);
		System.out.println(liste1.size());
		System.out.println(t4-t3+" op�rations");
		Polyomino.creerFenetre(new Configuration(polyominoes));
		*/
		
		// Test de la conversion en exactCover
		
		int s = 5;
		boolean[][] region = new boolean[5][6];
		for (int i = 0; i < region.length; i++){
			for (int j = 0; j < region[0].length; j++){
				region[i][j] = true;
			}
		}
		Integer[][] M3 = Polyomino.toExactCover(region, listeFixes);
		for (Integer[] X : M3){
			for (Integer x : X){
				//System.out.print(x);
			}
			//System.out.println();
		}
		// ExactCover.displayExactCover(M3);
		LinkedList<Integer[]> partition = ExactCover.findExactCover(M3);
		
		Polyomino[] polys = Polyomino.fromExactCover(region, partition);
		Polyomino.creerFenetre(new Configuration(polys, true));
		
	}

}