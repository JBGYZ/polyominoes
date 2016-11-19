package polyomino;

import java.io.PrintStream;
import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
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
		
		PrintStream out = System.out;
		int n = 5;
		LinkedList<Polyomino> liste = Polyomino.generer(n);
		out.println("Nombre de polyominos de taille " + n + " : " + liste.size());
		for (Polyomino P2 : liste){
			//P2.afficheConsole();
			System.out.println(P2);
			Polyomino.afficherTuiles(P2.tuiles);
			//out.println();
		}
		Polyomino[] polyominoes = new Polyomino[liste.size()];
		int i = 0;
		for (Polyomino p : liste){
			polyominoes[i] = p;
			i++;
		}
		Polyomino.creerFenetre(new Configuration(polyominoes));
	}

}