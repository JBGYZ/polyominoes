package hexagonamino;

public class HexaTableauRelatif {
	//Structure de tableau permettant de travailler avec des indices relatifs
	
	boolean[][] tab;
	int xMin, xMax, yMin, yMax;
	
	public HexaTableauRelatif(boolean[][] tab, int xMin, int xMax, int yMin, int yMax) {
		this.tab = tab;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}
	
	public HexaTableauRelatif(int xMin, int xMax, int yMin, int yMax) {
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
	
	public Hexagonamino getHexagonamino() {
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
		
		return new Hexagonamino(res);
	}
}