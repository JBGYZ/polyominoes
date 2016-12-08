package polyomino;

import java.io.PrintStream;
import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		
		// Test des isométries
		
		/*System.out.println("Test des isométries :");
		boolean[][] tuiles = {{true, false, false}, {false, false, true}, {true, true, true}, {false, true, true}};
		Polyomino.afficherTuiles(tuiles);
		System.out.println("Rotation d'angle 0");
		Polyomino.afficherTuiles(Polyomino.rotation(tuiles,0));
		System.out.println("Rotation d'angle pi/2");
		Polyomino.afficherTuiles(Polyomino.rotation(tuiles,1));
		System.out.println("Rotation d'angle pi");
		Polyomino.afficherTuiles(Polyomino.rotation(tuiles,2));
		System.out.println("Rotation d'angle 3pi/2");
		Polyomino.afficherTuiles(Polyomino.rotation(tuiles,3));
		System.out.println("Symétrie par rapport à x");
		Polyomino.afficherTuiles(Polyomino.symetrieX(tuiles));
		System.out.println("Test génération des polyominos :");
		*/
		
		// Test de la génération des polyominos
		
		PrintStream out = System.out;
		int n = 5;
		LinkedList<Polyomino> liste2 = Polyomino.genererFixes(n);
		LinkedList<Polyomino> liste = Polyomino.genererLibres(n);
		System.out.println();
		for (Polyomino P2 : liste){
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
		}
		//Polyomino.creerFenetre(new Configuration(polyominoes));
		
		// Test de ExactCover
		
		Integer[][] M = { { 0, 0, 1, 0, 1, 1, 0 }, { 1, 0, 0, 1, 0, 0, 1 }, { 0, 1, 1, 0, 0, 1, 0 },
				{ 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 1, 0, 1 } };
		//ExactCover.afficherExactCover(M);
		
		//Integer[][] M2 = ExactCover.subsets(7);
		//ExactCover.afficherExactCover(M2);
		
		// Test de DancingLinks

		//DancingLinks D = new DancingLinks(M);
		//System.out.println(D);
		
		// Test de la conversion en exactCover
		
		int s = 5;
		boolean[][] region = new boolean[s][s];
		for (int i = 0; i < s; i++){
			for (int j = 0; j < s; j++){
				region[i][j] = true;
			}
		}
		Integer[][] M3 = ExactCover.toExactCover(region, liste2);
		/*for (Integer[] X : M3){
			for (Integer x : X){
				System.out.print(x);
			}
			System.out.println();
		}*/
		ExactCover.afficherExactCover(M3);
	}

}