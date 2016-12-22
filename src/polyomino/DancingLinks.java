package polyomino;

import java.util.LinkedList;

class Data {

	public Data U, D, L, R;
	public int i, j;
	public Column C;

	public Data() {
		U = null;
		D = null;
		L = null;
		R = null;
		C = null;
		i = -1;
		j = -1;
	}

	public Data(Data u, Data d, Data l, Data r, Column c, int i, int j) {
		super();
		U = u;
		D = d;
		L = l;
		R = r;
		C = c;
		this.i = i;
		this.j = j;
	}

	public boolean equalsData(Data x) {
		return (this.i == x.i && this.j == x.j);
	}

	@Override
	public String toString() {
		String s = "donnee (" + this.i + "," + this.j + ")";
		return s;
	}

	public void printNeighbors() {
		System.out.println("La " + this.toString() + " a pour voisins :");
		System.out.println("Haut : " + this.U);
		System.out.println("Bas : " + this.D);
		System.out.println("Droite : " + this.R);
		System.out.println("Gauche : " + this.L);
	}

	public LinkedList<Integer> toList() {
		LinkedList<Integer> setList = new LinkedList<Integer>();
		setList.add(this.C.N);
		for (Data t = this.R; !(t == this); t = t.R) {
			setList.add(t.C.N);
		}
		return setList;
	}

	public Integer[] toArray(int cardX) {
		LinkedList<Integer> setList = this.toList();
		Integer[] setArray = new Integer[cardX];
		for (int i = 0; i < cardX; i++) {
			setArray[i] = 0;
		}
		for (int i : setList) {
			setArray[i] = 1;
		}
		return setArray;
	}

}

class Column extends Data {
	public int S;
	public int N;

	public Column() {
		U = null;
		D = null;
		L = null;
		R = null;
		C = null;
		i = -1;
		j = -1;
		S = 0;
		N = -1;
	}

	public Column(Data u, Data d, Data l, Data r, Column c, int i, int j, int s, int n) {
		super(u, d, l, r, c, i, j);
		S = s;
		N = n;
	}

	public int sum() {
		int s = 0;
		for (Data t = this.D; !(t == this); t = t.D) {
			s++;
		}
		return s;
	}

	public boolean checkSum() {
		return this.sum() == this.S;
	}

	@Override
	public String toString() {
		String s = "colonne " + this.N + " de somme " + this.S;
		return s;
	}

	public String columnContent() {
		String s = "La " + this.toString() + " contient : ";
		for (Data t = this.D; !(t == this); t = t.D) {
			s += t.toString();
		}
		return s;
	}

	public boolean equalsColumn(Column x) {
		Data y1 = this.D;
		Data y2 = x.D;
		while (y1.equalsData(y2) && !(y1.equalsData(this))) {
			y1 = y1.D;
			y2 = y2.D;
		}
		return (y1.equalsData(this));
	}

}

public class DancingLinks {
	public Column[] colonnes;
	public Column H;

	public DancingLinks(Integer[][] M) {
		int cardC = M.length;
		int cardX = M[0].length;
		// on cree une matrice de donnees comprenant en premiere ligne les
		// objets Column, puis un objet Data pour chaque coefficient de M, qu'il
		// soit 0 ou 1
		Data[][] donnees = new Data[cardC + 1][cardX];
		for (int j = 0; j < cardX; j++) {
			Column c = new Column();
			c.i = 0;
			c.j = j;
			c.S = 0;
			c.N = j;
			c.C = c;
			donnees[0][j] = c;
			for (int i = 1; i < cardC + 1; i++) {
				Data d = new Data();
				d.i = i;
				d.j = j;
				d.C = c;
				donnees[i][j] = d;
			}
		}
		// on remplit tous les liens du quadrillage
		for (int j = 0; j < cardX; j++) {
			for (int i = 0; i < cardC + 1; i++) {
				Data d0 = donnees[i][j];
				// à gauche
				Data d1 = donnees[i][Math.floorMod(j - 1, cardX)];
				d0.L = d1;
				d1.R = d0;
				// à droite
				Data d2 = donnees[i][Math.floorMod(j + 1, cardX)];
				d0.R = d2;
				d2.L = d0;
				// en haut
				Data d3 = donnees[Math.floorMod(i - 1, cardC + 1)][j];
				d0.U = d3;
				d3.D = d0;
				// en bas
				Data d4 = donnees[Math.floorMod(i + 1, cardC + 1)][j];
				d0.D = d4;
				d4.U = d0;
			}
		}
		// on supprime les objets Data correspondant a des coefficients nuls, et
		// on remplit les compteurs S des colonnes en meme temps
		for (int j = 0; j < cardX; j++) {
			for (int i = 1; i < cardC + 1; i++) {
				if (M[i - 1][j] == 1) {
					Column c = (Column) donnees[0][j];
					c.S++;
				} else {
					Data d = donnees[i][j];
					d.U.D = d.D;
					d.D.U = d.U;
					d.R.L = d.L;
					d.L.R = d.R;
				}

			}
		}
		this.colonnes = new Column[cardX];
		for (int j = 0; j < cardX; j++) {
			Column c = (Column) donnees[0][j];
			this.colonnes[j] = c;
		}
		this.H = new Column();
		this.H.R = this.colonnes[0];
		this.H.L = this.colonnes[cardX - 1];
		this.H.L.R = this.H;
		this.H.R.L = this.H;
	}

	public boolean equalsDL(DancingLinks d) {
		Column x1 = (Column) this.H.R;
		Column x2 = (Column) d.H.R;
		while (x1.equalsColumn(x2) && !(x1.equalsData(this.H))) {
			if (x1.R == this.H) {
				return true;
			}
			x1 = (Column) x1.R;
			x2 = (Column) x2.R;
		}
		return (x1.equalsData(this.H));
	}

	@Override
	public String toString() {
		String s = "Affichage de la structure DancingLinks : \n";
		for (Data c = this.H.R; !(c == this.H); c = c.R) {
			s += c.C.columnContent();
			s += "\n";
		}
		return s;
	}

	public static void coverColumn(Data x) {
		x.R.L = x.L;
		x.L.R = x.R;
		for (Data t = x.D; !(t == x); t = t.D) {
			for (Data y = t.R; !(y == t); y = y.R) {
				if (!(y.i == -1)) {
					y.D.U = y.U;
					y.U.D = y.D;
					y.C.S--;
				}
			}
		}
	}

	public static void uncoverColumn(Data x) {
		x.R.L = x;
		x.L.R = x;
		for (Data t = x.U; !(t == x); t = t.U) {
			for (Data y = t.L; !(y == t); y = y.L) {
				if (!(y.i == -1)) {
					y.D.U = y;
					y.U.D = y;
					y.C.S++;
				}
			}
		}
	}

	public static LinkedList<LinkedList<Data>> exactCover(Data H) {
		if (H.R == H) {
			// aucun element, on renvoie un ensemble de partitions constitue
			// d'une partition vide P
			LinkedList<LinkedList<Data>> partitions = new LinkedList<LinkedList<Data>>();
			LinkedList<Data> P = new LinkedList<Data>();
			partitions.add(P);
			return partitions;
		}
		LinkedList<LinkedList<Data>> partitions = new LinkedList<LinkedList<Data>>();
		Data x = H.L; // premier element a couvrir
		coverColumn(x.C);
		for (Data t = x.U; !(t == x); t = t.U) { // t est sur la ligne du sous-ensemble par lequel on le couvre
			for (Data y = t.L; !(y == t); y = y.L) {
				coverColumn(y.C);
			}
			for (LinkedList<Data> P : exactCover(H)) {
				P.add(t);
				partitions.add(P);
			}
			for (Data y = t.R; !(y == t); y = y.R) {
				uncoverColumn(y.C);
			}
		}
		return partitions;
	}

	public static void displayExactCover(Integer[][] M) {
		System.out.println("On cherche les partitions de la matrice M :");
		ExactCover.afficherMatrice(M);
		System.out.println();
		int k = 0;
		DancingLinks D = new DancingLinks(M);
		LinkedList<LinkedList<Data>> partitions = exactCover(D.H);
		for (LinkedList<Data> P : partitions) {
			k += 1;
			System.out.println("Partition numero " + k);
			for (Data partie : P) {
				String s = "";
				for (int i : partie.toArray(M[0].length)) {
					s += "" + i;
				}
				System.out.println(s);
			}
		}
		System.out.println();
		System.out.println("Il en a " + k + " au total.");
		System.out.println();
	}

}
