package polyomino;

import java.io.PrintStream;
import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		PrintStream out = System.out;
		LinkedList<Polyomino> liste = Polyomino.generer(4);
		out.println("Nombre de polyominos de taille 4 : " + liste.size());
		/*for (Polyomino P2 : liste){
			P2.afficheConsole();
			out.println();
		}*/
	}

}