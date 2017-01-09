package polyomino;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class RedelmeierGenerator {
	
	public static LinkedList<Polyomino> liste;
	public static HashMap<Double, Polyomino> listeFree;
	
	public static LinkedList<Polyomino> generateFixed(int n) {
		liste = new LinkedList<Polyomino>();
		TableauRelatif tab = new TableauRelatif(-n+1, n, 0, n);
		LinkedList<Point> voisins = new LinkedList<Point>();
		LinkedList<Point> vide = new LinkedList<Point>();
		/*
		 * 
		 * 
		 * ======X
		 * =============
		 * =============
		 */
		voisins.add(new Point(0,1));
		voisins.add(new Point(1,0));
		tab.set(0, 0, true);
		auxFixed(tab, vide, voisins, 1, n);
		return liste;
	}
	
	public static LinkedList<Polyomino> generateFree(int n) {
		TableauRelatif tab = new TableauRelatif(-n+1, n, 0, n);
		LinkedList<Point> voisins = new LinkedList<Point>();
		LinkedList<Point> vide = new LinkedList<Point>();
		listeFree = new HashMap<Double, Polyomino>();
		/*
		 * 
		 * 
		 * ======X
		 * =============
		 * =============
		 */
		voisins.add(new Point(0,1));
		voisins.add(new Point(1,0));
		tab.set(0, 0, true);
		auxFree(tab, vide, voisins, 1, n);
		
		Collection<Polyomino> col = listeFree.values();
		liste = new LinkedList<Polyomino>();
		for(Polyomino P : col) {
			liste.add(P);
		}
		
		System.out.println("[Redelmeier] There are " + liste.size() + " free polyominoes of size " + n + ".");
		System.out.println();
		
		return liste;
	}
	
	public static void auxFixed(TableauRelatif tab, LinkedList<Point> triedSet, LinkedList<Point> untriedSet, int p, int n) {
		int tailleTriedSetInitial = triedSet.size();
		while(!untriedSet.isEmpty()) {
			// 1
			Point P = untriedSet.pop();
			// 2
			tab.set(P.getx(), P.gety(), true);
			// 3
			if (p + 1 == n){
				Polyomino P1 = tab.getPolyomino();
				liste.add(P1);
			}
			
			// 4
			if(p + 1 < n) {
				// 4.a
				LinkedList<Point> copieUntriedSet = cloneList(untriedSet);
				for(Point voisin : P.neighbors()) {
					boolean estDansLaZone = (voisin.getx() > 0 && voisin.gety() >= 0)
							|| (voisin.gety() > 0);
					if(estDansLaZone
							&& !tab.get(voisin.getx(), voisin.gety())
							&& !estDans(voisin, triedSet)
							&& !estDans(voisin, untriedSet)) {
						copieUntriedSet.add(voisin);
					}
				}
				
				// 4.b
				auxFixed(tab, triedSet, copieUntriedSet, p+1, n);
			}
			
			// 5
			triedSet.add(P);
			tab.set(P.getx(), P.gety(), false);
		}
		
		while(triedSet.size() > tailleTriedSetInitial) triedSet.pollLast();
		
	}
	
	public static void auxFree(TableauRelatif tab, LinkedList<Point> triedSet, LinkedList<Point> untriedSet, int p, int n) {
		int tailleTriedSetInitial = triedSet.size();
		while(!untriedSet.isEmpty()) {
			// 1
			Point P = untriedSet.pop();
			// 2
			tab.set(P.getx(), P.gety(), true);
			// 3
			if (p + 1 == n){
				Polyomino P1 = tab.getPolyomino();
				double hashFree = P1.generateHashFree();
				listeFree.put(hashFree, P1);
				/*if (!P1.isInIsometries(liste)) {
					liste.add(P1);
				}*/
			}
			
			// 4
			if(p + 1 < n) {
				// 4.a
				LinkedList<Point> copieUntriedSet = cloneList(untriedSet);
				for(Point voisin : P.neighbors()) {
					boolean estDansLaZone = (voisin.getx() > 0 && voisin.gety() >= 0)
							|| (voisin.gety() > 0);
					if(estDansLaZone
							&& !tab.get(voisin.getx(), voisin.gety())
							&& !estDans(voisin, triedSet)
							&& !estDans(voisin, untriedSet)) {
						copieUntriedSet.add(voisin);
					}
				}
				
				// 4.b
				auxFree(tab, triedSet, copieUntriedSet, p+1, n);
			}
			
			// 5
			triedSet.add(P);
			tab.set(P.getx(), P.gety(), false);
		}
		
		while(triedSet.size() > tailleTriedSetInitial) triedSet.pollLast();
		
	}

	public static LinkedList<Point> cloneList(LinkedList<Point> untriedSet) {
		LinkedList<Point> res = new LinkedList<Point>();
		for(Point P : untriedSet) {
			res.add(new Point(P.getx(), P.gety()));
		}
		return res;
	}
	
	public static void printList(LinkedList<Point> l) {
		String s = "";
		for(Point P : l) {
			s += P.toString()+" ";
		}
		System.out.println(s);
	}
	
	public static void printList(LinkedList<Point> l, String s1) {
		String s = s1+" : ";
		for(Point P : l) {
			s += P.toString()+" ";
		}
		System.out.println(s);
	}
	
	public static boolean estDans(Point A, LinkedList<Point> liste) {
		for (Point P : liste) {
			if (A.equals(P))
				return true;
		}
		return false;
	}

}

class TableauRelatif {
	//Structure de tableau permettant de travailler avec des indices relatifs
	
	boolean[][] tab;
	int xMin, xMax, yMin, yMax;
	
	public TableauRelatif(boolean[][] tab, int xMin, int xMax, int yMin, int yMax) {
		this.tab = tab;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}
	
	public TableauRelatif(int xMin, int xMax, int yMin, int yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
		tab = new boolean[xMax-xMin+1][yMax-yMin+1];
		
		for(int i = xMin; i<xMax; i++) {
			for(int j = yMin; j<yMax; j++) {
				set(i, j, false);
			}
		}
		
	}

	public int[] relatifToNaturel(int a, int b) {
		int[] res = {a - xMin, b - yMin};
		return res;
	}
	
	public void set(int a, int b, boolean valeur) {
		int[] coords = relatifToNaturel(a, b);
		tab[coords[0]][coords[1]] = valeur;
	}
	
	public boolean get(int a, int b) {
		int[] coords = relatifToNaturel(a, b);
		/*System.out.println("---");
		System.out.println(a+" "+b);
		System.out.println(xMin+" "+xMax+" "+yMin+" "+yMax);
		System.out.println(coords[0]+" "+coords[1]);
		System.out.println(tab.length+" "+tab[0].length);*/
		return tab[coords[0]][coords[1]];
	}
	
	public Polyomino getPolyomino() {
		int xm = 0, xM = 0, ym = 0, yM = 0;  // vraies limites du polyomino
		
		for(int i = 0; i < tab.length; i++) {
			for(int j = 0; j < tab[i].length; j++) {
				if(tab[i][j]) {
					xm = i;
					xM = i;
					ym = j;
					yM = j;
					break;
				}
			}
		}
		
		for(int i = 0; i < tab.length; i++) {
			for(int j = 0; j < tab[i].length; j++) {
				if(tab[i][j]) {
					if (xm > i) xm = i;
					if (xM < i) xM = i;
					if (ym > j) ym = j;
					if (yM < j) yM = j;
				}
			}
		}
		
		boolean[][] res = new boolean[xM-xm+1][yM-ym+1];
		for(int i = 0; i < res.length; i++) {
			for(int j = 0; j < res[i].length; j++) {
				if(tab[i + xm][j + ym]) res[i][j] = true;
			}
		}
		
		return new Polyomino(res);
	}
	
}
