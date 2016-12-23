package polyomino;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {

		// Test de la generation des polyominos

		/*
		 * int n = 5; long t1 = System.currentTimeMillis();
		 * 
		 * @SuppressWarnings("unused") LinkedList<Polyomino> listeFixes =
		 * Polyomino.genererFixes(n); long t2 = System.currentTimeMillis();
		 * 
		 * @SuppressWarnings("unused") LinkedList<Polyomino> listeLibres =
		 * Polyomino.genererLibres(n); // System.out.println(t2-t1+" operations"
		 * );
		 * 
		 * for (Polyomino P2 : listeLibres){ P2.afficheConsole();
		 * System.out.println(); System.out.println(P2); System.out.println(); }
		 */

		// Test de l'affichage des polyominos

		/*
		 * LinkedList<Polyomino> listeAffichage = Polyomino.genererFixes(4);
		 * Polyomino[] polyominoes = listeAffichage.toArray(new
		 * Polyomino[listeAffichage.size()]); boolean superposition = true;
		 * Polyomino.creerFenetre(new Configuration(polyominoes,
		 * !superposition));
		 */

		// Test de ExactCover et DancingLinks

		/*
		 * Integer[][] M = { { 0, 0, 1, 0, 1, 1, 0 }, { 1, 0, 0, 1, 0, 0, 1 }, {
		 * 0, 1, 1, 0, 0, 1, 0 }, { 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1
		 * }, { 0, 0, 0, 1, 1, 0, 1 } };
		 * 
		 * Integer[][] M2 = ExactCover.subsets(9,3);
		 * 
		 * ExactCover.displayExactCover(M2); DancingLinks.displayExactCover(M2);
		 */

		// Test de la generation des polyominos facon Redelmeier (Fixed)

		/*
		 * n = 5; long t3 = System.currentTimeMillis(); LinkedList<Polyomino>
		 * liste1 = RedelmeierGenerator.genererFixe(n); long t4 =
		 * System.currentTimeMillis(); Polyomino[] polyominoes =
		 * liste1.toArray(new Polyomino[0]); System.out.println(liste1.size());
		 * System.out.println(t4-t3+" op�rations"); Polyomino.creerFenetre(new
		 * Configuration(polyominoes));
		 */

		// Test de la conversion en exactCover et de l'affichage
		
		// region rectangulaire de taille s, tous les polyominos d'une taille donnée

		int taille = 5;
		LinkedList<Polyomino> matos = Polyomino.genererFixes(taille);
		
		int s = 5;
		boolean[][] region = new boolean[s][s];
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				region[i][j] = true;
			}
		}
		
		// region custom, un seul type de polyominos
		
		/*
		boolean[][] tuiles = { { true, true } };
		Polyomino base = new Polyomino(tuiles);
		LinkedList<Polyomino> matos = Polyomino.copains(base);

		int[][] diamond = { { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
				{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 }, { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, { 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, };
		boolean[][] region = new boolean[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (diamond[i][j] == 1) {
					region[i][j] = true;
				} else {
					region[i][j] = false;
				}
			}
		}
		*/

		Integer[][] M3 = Polyomino.toExactCover(region, matos);
		// DancingLinks.displayExactCover(M3);

		LinkedList<Integer[]> partition = DancingLinks.findExactCover(M3);
		Polyomino[] polys = Polyomino.fromExactCover(region, partition);
		Polyomino.creerFenetre(new Configuration(polys, true));

	}

}