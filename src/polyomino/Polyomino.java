package polyomino;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

class Point {
	public int x, y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getx() {
		return this.x;
	}

	public int gety() {
		return this.y;
	}

	public boolean equals(Point P) {
		return (P.x == this.x && P.y == this.y);
	}

	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	public LinkedList<Point> voisins() {
		LinkedList<Point> vois = new LinkedList<Point>();
		vois.add(new Point(this.x + 1, this.y));
		vois.add(new Point(this.x - 1, this.y));
		vois.add(new Point(this.x, this.y + 1));
		vois.add(new Point(this.x, this.y - 1));
		return vois;
	}
}

public class Polyomino {
	public boolean[][] tuiles; // représentation sous forme d'un tableau de
								// booléens
	public LinkedList<Point> cases; // représentation sous forme de liste de
									// cases
	public int n; // taille du polymino
	public int largeur, hauteur; // taille du plus petit rectangle le contenant

	// créateurs

	public Polyomino(boolean[][] tuiles) {
		this.tuiles = tuiles;
		this.cases = toCases(this.tuiles);
		this.n = this.cases.size();
		int[] form = format(this.cases);
		this.largeur = form[0];
		this.hauteur = form[1];
	}

	public Polyomino(LinkedList<Point> cases) {
		this.cases = cases;
		this.tuiles = toTuiles(this.cases);
		this.n = this.cases.size();
		int[] form = format(this.cases);
		this.largeur = form[0];
		this.hauteur = form[1];
	}

	public Polyomino(String s) {
		// Ex : [(0,0), (0,4), (1,0), (1,1), (1,2), (1,3), (1,4), (2,0), (2,4)]
		// Parsing de la cha�ne en LinkedList
		LinkedList<Integer[]> tuilesList = new LinkedList<Integer[]>();

		int k = 1; // Indice courant dans la cha�ne : on commence � la premi�re
					// parenth�se
		while (k < s.length() - 1) {
			// On commence chaque boucle � un d�but de parenth�ses
			k++;
			int x = 0, y = 0;
			while (s.charAt(k) != ',') {
				x = x * 10 + Integer.parseInt(Character.toString(s.charAt(k)));
				k++;
			}
			k++; // On passe la virgule de (_,_)
			while (s.charAt(k) != ')') {
				y = y * 10 + Integer.parseInt(Character.toString(s.charAt(k)));
				k++;
			}
			Integer[] tuile = { x, y };
			tuilesList.add(tuile);
			// On regarde si on est arriv�s � la fin
			k++;
			if (s.charAt(k) == ',')
				k += 2;
			else
				break;
		}

		// Transformation de la liste en boolean[][]
		int xmax = 0, ymax = 0;
		for (Integer[] tuile : tuilesList) {
			xmax = Math.max(xmax, tuile[0]);
			ymax = Math.max(ymax, tuile[1]);
		}
		boolean[][] tableauTuiles = new boolean[xmax + 1][ymax + 1];
		for (Integer[] tuile : tuilesList) {
			tableauTuiles[tuile[0]][tuile[1]] = true;
		}
		this.tuiles = tableauTuiles;
		this.cases = toCases(this.tuiles);
		this.n = this.cases.size();
		int[] form = format(this.cases);
		this.largeur = form[0];
		this.hauteur = form[1];
	}

	public static Polyomino[] importer(String fichier) {
		LinkedList<Polyomino> liste = new LinkedList<Polyomino>();
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(fichier)));
			String ligne;
			while ((ligne = buffer.readLine()) != null) {
				liste.add(new Polyomino(ligne));
			}
			buffer.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return liste.toArray(new Polyomino[0]);
	}

	// fonctions de conversion

	public static LinkedList<Point> toCases(boolean[][] tuiles) {
		// convertit les tuiles en cases
		LinkedList<Point> cases = new LinkedList<Point>();
		int largeur = tuiles.length;
		int hauteur = tuiles[0].length;
		for (int i = 0; i < largeur; i++) {
			for (int j = 0; j < hauteur; j++) {
				if (tuiles[i][j]) {
					cases.add(new Point(i, j));
				}
			}
		}
		return cases;
	}

	public static int[] format(LinkedList<Point> cases) {
		// renvoie un couple (largeur,hauteur)
		int largeur = 0;
		int hauteur = 0;
		for (Point p : cases) {
			if (p.x > largeur) {
				largeur = p.x;
			}
			if (p.y > hauteur) {
				hauteur = p.y;
			}
		}
		int[] form = { largeur, hauteur };
		return form;
	}

	public static boolean[][] toTuiles(LinkedList<Point> cases) {
		// convertit les cases en tuiles, en supposant que les cases ont pour
		// coin inférieur gauche 0
		int[] form = format(cases);
		int largeur = form[0];
		int hauteur = form[1];
		boolean[][] tuiles = new boolean[largeur + 1][hauteur + 1];
		for (int i = 0; i < largeur; i++) {
			for (int j = 0; j < hauteur; j++) {
				tuiles[i][j] = false;
			}
		}
		for (Point p : cases) {
			tuiles[p.x][p.y] = true;
		}
		return tuiles;

	}

	// ajout d'une case

	public boolean contientCase(Point p) {
		if (p.x < 0 || p.y < 0 || p.x > largeur || p.y > hauteur) {
			return false;
		}
		return this.tuiles[p.x][p.y];
	}

	public Polyomino ajouterCase(Point p) {
		// renvoie le polyomino résultant de l'ajout de la case p à this
		if (this.contientCase(p)) {
			System.out.println("Ce polyomino contient déjà la case " + p);
			return this;
		} else {
			int a = 0;
			int b = 0; // rééquilibrage si on passe en coordonnées négatives
						// (jamais de plus de 1 puisqu'on travaille de proche en
						// proche)
			if (p.x < 0) {
				a = 1;
			}
			if (p.y < 0) {
				b = 1;
			}
			LinkedList<Point> cases2 = new LinkedList<Point>();
			for (Point c : this.cases) {
				cases2.add(new Point(c.x + a, c.y + b));
			}
			cases2.add(new Point(p.x + a, p.y + b));
			return new Polyomino(cases2);
		}

	}

	public LinkedList<Polyomino> ajouterVoisins() {
		LinkedList<Polyomino> nouveauxPolyo = new LinkedList<Polyomino>();
		for (Point c : this.cases) {
			for (Point v : c.voisins()) {
				if (!this.contientCase(v)) {
					nouveauxPolyo.add(this.ajouterCase(v));
				}
			}
		}
		return nouveauxPolyo;
	}

	// génération de tous les polyominos d'ordre n

	public static LinkedList<Polyomino> generer(int n) {
		if (n == 1) {
			LinkedList<Polyomino> liste = new LinkedList<Polyomino>();
			liste.add(new Polyomino("[(0,0)]"));
			return liste;
		} else {
			LinkedList<Polyomino> listePrecedente = generer(n - 1);
			LinkedList<Polyomino> liste = new LinkedList<Polyomino>();
			for (Polyomino P : listePrecedente) {
				for (Polyomino P2 : P.ajouterVoisins()) {
					liste.add(P2);
				}
			}
			return liste;
		}
	}

	// affichage graphique

	public void addPolygonAndEdges(Image2d img, int width, Color color, int tailleTuiles, int xmax, int ymax) {
		// Ajoute les Edge et les carr�s de la tuile dans l'image img
		for (int i = 0; i < tuiles.length; i++) {
			for (int j = 0; j < tuiles[i].length; j++) {
				if (tuiles[i][j]) {
					int[] xcoords = { i * tailleTuiles, i * tailleTuiles, (i + 1) * tailleTuiles,
							(i + 1) * tailleTuiles },
							ycoords = { (ymax - j) * tailleTuiles, (ymax - (j + 1)) * tailleTuiles,
									(ymax - (j + 1)) * tailleTuiles, (ymax - j) * tailleTuiles };
					img.addPolygon(xcoords, ycoords, color);
					if (i == 0 || !tuiles[i - 1][j]) {
						// System.out.println("a");
						img.addEdge(i * tailleTuiles, (ymax - j) * tailleTuiles, i * tailleTuiles,
								(ymax - (j + 1)) * tailleTuiles, width);
					}
					if (j == 0 || !tuiles[i][j - 1]) {
						// System.out.println("b");
						img.addEdge(i * tailleTuiles, (ymax - j) * tailleTuiles, (i + 1) * tailleTuiles,
								(ymax - j) * tailleTuiles, width);
					}
					if (i == tuiles.length - 1 || !tuiles[i + 1][j]) {
						// System.out.println("c");
						img.addEdge((i + 1) * tailleTuiles, (ymax - j) * tailleTuiles, (i + 1) * tailleTuiles,
								(ymax - (j + 1)) * tailleTuiles, width);
					}
					if (j == tuiles[i].length - 1 || !tuiles[i][j + 1]) {
						// System.out.println("d");
						img.addEdge(i * tailleTuiles, (ymax - (j + 1)) * tailleTuiles, (i + 1) * tailleTuiles,
								(ymax - (j + 1)) * tailleTuiles, width);
					}
				}
			}
		}
	}

	public Polyomino translate(int dx, int dy) {

		return null;
	}

	public void afficheConsole() {
		for (boolean[] bTab : tuiles) {
			for (boolean b : bTab) {
				System.out.print(b ? "O" : " ");
			}
			System.out.println();
		}
	}

	public String toString() {
		String s = "[";
		for (Point p : this.cases) {
			s += "(" + p.x + "," + p.y + "), ";
		}
		s += "]";
		return s;
	}

	public static void creerFenetre(Polyomino[] polyominoes, int tailleTuiles, int width) {
		// On d�termine la taille de l'image
		Color[] colors = { Color.red, Color.yellow, Color.green, Color.blue, Color.gray, Color.cyan, Color.magenta,
				Color.orange, Color.lightGray };
		int xmax = 0, ymax = 0;
		for (Polyomino p : polyominoes) {
			xmax = Math.max(xmax, p.tuiles.length);
			ymax = Math.max(ymax, p.tuiles[0].length);
		}
		Image2d img = new Image2d(xmax * tailleTuiles, ymax * tailleTuiles);
		int k = 0;
		for (Polyomino p : polyominoes) {
			p.addPolygonAndEdges(img, width, colors[k % colors.length], tailleTuiles, xmax, ymax);
			k++;
		}

		@SuppressWarnings("unused")
		Image2dViewer fenetre = new Image2dViewer(img);
	}

	public static void main(String[] args) {
		String chemin_fichier = "/Users/Guillaume/Documents/Informatique/Java-Eclipse/polyominoes/polyminoesINF421.txt";
		Polyomino[] tab = importer(chemin_fichier);
		for (Polyomino P : tab) {
			System.out.println(P);
			// P.afficheConsole();
		}
		// Polyomino[] tab2 = { tab[1] };
		// creerFenetre(tab2, 100, 2);
	}
}
