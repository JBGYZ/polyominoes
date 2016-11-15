package polyomino;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		String s = "[(0,0), (0,1), (1,1)]";
		Polyomino P = new Polyomino(s);
		//System.out.println(P);
		LinkedList<Polyomino> liste = Polyomino.generer(7);
		for (Polyomino P2 : liste){
			System.out.println(P2);
		}
	}

}
