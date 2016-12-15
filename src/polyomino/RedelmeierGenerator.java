package polyomino;

import java.util.LinkedList;

public class RedelmeierGenerator {
	
	public static LinkedList<Polyomino> liste = new LinkedList<Polyomino>();
	
	public static LinkedList<Polyomino> genererFixe(int n) {
		TableauRelatif tab = new TableauRelatif(-n+1, n-1, -1, n-1);
		LinkedList<Point> voisins = new Point(0, 0).voisins();
		tab.set(0, 0, true);
		auxFixed(tab, voisins, 1, n);
		for(Polyomino po : liste) {
			po.afficheConsole();
			System.out.println("");
		}
		return liste;
	}
	
	public static void auxFixed(TableauRelatif tab, LinkedList<Point> voisins, int p, int n) {
		System.out.println(p);
		System.out.println(voisins.size());
		while(!voisins.isEmpty()) {
			// 1
			Point P = voisins.pop();
			
			// 2
			tab.set(P.getx(), P.gety(), true);
			
			
			// 3
			if (p==n){
				Polyomino P1 = tab.getPolyomino();
				P1.afficheConsole();
				liste.add(P1);
			}
			
			// 4
			if(p + 1 <= n) {
				// 4.a
				int nombreNouveauxVoisins = 0;
				for(Point voisin : P.voisins()) {
					boolean estDansLaZone =
							(
									voisin.getx() > tab.xMin
									|| (voisin.getx() == tab.xMin && voisin.gety() >= 0)
							)
							&& voisin.gety() >= tab.yMin
							&& voisin.getx() <= tab.xMax
							&& voisin.gety() <= tab.yMax;
					if(estDansLaZone
							&& !tab.get(voisin.getx(), voisin.gety())) {
						nombreNouveauxVoisins++;
						voisins.add(voisin);
					}
					//System.out.println(voisin.getx() > tab.xMin);
				}
				
				// 4.b
				auxFixed(tab, voisins, p+1, n);
				
				// 4.c
				for(int i = 0; i < nombreNouveauxVoisins; i++) {
					if (!voisins.isEmpty()) voisins.pop();
				}
			}
			
			// 5
			tab.set(P.getx(), P.gety(), false);
		}
		
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
