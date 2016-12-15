package polyomino;

class Data {

	public Data U, D, L, R;
	public Column C;

	public Data() {
		U = null;
		D = null;
		L = null;
		R = null;
		C = null;
	}

	public Data(Data u, Data d, Data l, Data r, Column c) {
		super();
		U = u;
		D = d;
		L = l;
		R = r;
		C = c;
	}
	
	@Override
	public String toString(){
		String s = "Cette donnee est dans la colonne " + this.C;
		return s;
	}

}

class Column extends Data {
	public int S;
	public String N;

	public Column(Data u, Data d, Data l, Data r, Column c, int s, String n) {
		super(u, d, l, r, c);
		S = s;
		N = n;
	}
	
	@Override
	public String toString(){
		String s = "Cette colonne a pour nom " + this.N + " et pour somme " + this.S + ".";
		return s;
	}

}

public class DancingLinks {
	public Column[] colonnes;

	public DancingLinks(Integer[][] M) {
		int cardC = M.length;
		int cardX = M[0].length;
		// on cree une matrice de donnees comprenant en premiere ligne les
		// objets Column, puis un objet Data pour chaque coefficient de M, qu'il soit 0 ou
		// 1
		Data[][] donnees = new Data[cardC + 1][cardX];
		for (int j = 0; j < cardX; j++) {
			Column c = new Column(null, null, null, null, null, 0, "" + (j+1));
			donnees[0][j] = c;
			for (int i = 0; i < cardC; i++) {
				donnees[i + 1][j] = new Data();
			}
		}
		// on remplit tous les liens du quadrillage
		for (int j = 0; j < cardX; j++) {
			for (int i = 0; i < cardC + 1; i++) {
				Data d0 = donnees[i][j];
				d0.C = (Column) donnees[0][j];
				// à gauche
				Data d1;
				if (j == 0) {
					d1 = donnees[i][cardX-1];
				} else {
					d1 = donnees[i][j-1];
				}
				d0.L = d1;
				d1.R = d0;
				// à droite
				Data d2;
				if (j == cardX - 1) {
					d2 = donnees[i][0];
				} else {
					d2 = donnees[i][j+1];
				}
				d0.R = d2;
				d2.L = d0;
				// en haut
				Data d3;
				if (i==0){
					d3 = donnees[cardC][j];
				}
				else{
					d3 = donnees[i-1][j];
				}
				d0.U = d1;
				d3.D = d0;
				// en bas
				Data d4;
				if (i==cardC){
					d4 = donnees[0][j];
				} else {
					d4 = donnees[i+1][j];
				}
				d0.D = d4;
				d4.U = d0;
			}
		}
		// on supprime les objets Data correspondant a des coefficiens nuls, et on remplit les compteurs S des colonnes en meme temps
		for (int j = 0; j < cardX; j++) {
			for (int i = 1; i < cardC + 1; i++) {
				if (M[i-1][j] == 1){
					Data d = donnees[0][j];
					Column c = (Column) d;
					c.S ++;
				}
				else {
					Data d = donnees[i][j];
					d.U.D = d.D;
					d.D.U = d.U;
					d.R.L = d.L;
					d.L.R = d.R;
				}
				
			}
		}
		this.colonnes = new Column[cardX];
		for (int j = 0; j < cardX; j++){
			Data d = donnees[0][j];
			this.colonnes[j] = (Column) d;
		}
	}
	
	@Override
	public String toString(){
		String s = "Affichage de la structure DancingLinks : \n";
		for (int j = 0; j < this.colonnes.length; j++){
			s += this.colonnes[j].toString();
			s += "\n";
		}
		return s;
	}

}
