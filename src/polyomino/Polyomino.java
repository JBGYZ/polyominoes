package polyomino;

import java.awt.Color;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Polyomino {
	public boolean[][] tuiles; // representation sous forme d'un tableau de
								// booleens

	public int n; // taille du polymino
	public int largeur, hauteur; // taille du plus petit rectangle le contenant

	// constructeurs

	public static int nombreCases(boolean[][] tuiles) {
		int l = tuiles.length;
		int h = tuiles[0].length;
		int n = 0;
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < h; j++) {
				if (tuiles[i][j]) {
					n++;
				}
			}
		}
		return n;
	}

	public Polyomino(boolean[][] tuiles) {
		this.tuiles = tuiles;
		this.n = nombreCases(this.tuiles);
		this.largeur = this.tuiles.length;
		this.hauteur = this.tuiles[0].length;
	}

	public Polyomino(String s) {
		// Ex : [(0,0), (0,4), (1,0), (1,1), (1,2), (1,3), (1,4), (2,0), (2,4)]
		// Parsing de la chaine en LinkedList
		LinkedList<Integer[]> tuilesList = new LinkedList<Integer[]>();

		int k = 1; // Indice courant dans la chaine : on commence a la premiere
					// parenthese
		while (k < s.length() - 1) {
			// On commence chaque boucle a un debut de parentheses
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
			// On regarde si on est arrives a la fin
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
		this.n = nombreCases(this.tuiles);
		this.largeur = this.tuiles.length;
		this.hauteur = this.tuiles[0].length;
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

	// ajout d'une case

	public boolean contientCase(Point p) {
		if (p.x < 0 || p.y < 0 || p.x >= largeur || p.y >= hauteur) {
			return false;
		}
		return this.tuiles[p.x][p.y];
	}

	public Polyomino ajouterCase(Point p) {
		// renvoie le polyomino resultant de l'ajout de la case p a this
		if (this.contientCase(p)) {
			System.out.println("Ce polyomino contient deja la case " + p);
			return this;
		} else {
			// a et b : reequilibrage eventuel si on passe en coordonnees
			// negatives
			// c et d : reequilibrage eventuel si on depasse hauteur ou largeur
			// on a toujours a,b,c,d nuls ou valant 1, et jamais à la fois a=1
			// et c=1 ou b=1 et d=1
			int a = 0;
			int b = 0;
			int c = 0;
			int d = 0;
			if (p.x < 0) {
				a = 1;
			}
			if (p.y < 0) {
				b = 1;
			}
			if (p.x >= this.largeur) {
				c = 1;
			}
			if (p.y >= this.hauteur) {
				d = 1;
			}
			boolean[][] tuiles2 = new boolean[this.largeur + a + c][this.hauteur + b + d];
			for (int i = 0; i < this.largeur; i++) {
				for (int j = 0; j < this.hauteur; j++) {
					tuiles2[a + i][b + j] = this.tuiles[i][j];
				}
			}
			tuiles2[p.x + a][p.y + b] = true;
			return new Polyomino(tuiles2);
		}

	}

	// retourne la liste des polyominos de taille n+1 obtenus en ajoutant une
	// case sur chaque cote de chaque case

	public LinkedList<Polyomino> ajouterVoisins() {
		LinkedList<Polyomino> nouveauxPolyo = new LinkedList<Polyomino>();
		for (int i = 0; i < this.largeur; i++) {
			for (int j = 0; j < this.hauteur; j++) {
				if (this.tuiles[i][j]) {
					Point p = new Point(i, j);
					for (Point v : p.voisins()) {
						if (!this.contientCase(v)) {
							nouveauxPolyo.add(this.ajouterCase(v));
						}
					}
				}
			}
		}
		return nouveauxPolyo;
	}

	// fonctions d'isometries

	public static boolean[][] rotation(boolean[][] tuiles, int n) {
		// fait tourner le tableau tuiles d'un angle +npi/2
		int largeur, hauteur;
		largeur = tuiles.length;
		hauteur = tuiles[0].length;
		if (n == 0) {
			return ((boolean[][]) tuiles.clone());
		} else if (n == 1) {
			boolean[][] nouvellesTuiles = new boolean[hauteur][largeur];
			for (int i = 0; i < hauteur; i++) {
				for (int j = 0; j < largeur; j++) {
					nouvellesTuiles[i][j] = tuiles[j][hauteur - 1 - i];
				}
			}
			return nouvellesTuiles;
		} else if (n == 2) {
			boolean[][] nouvellesTuiles = new boolean[largeur][hauteur];
			for (int i = 0; i < largeur; i++) {
				for (int j = 0; j < hauteur; j++) {
					nouvellesTuiles[i][j] = tuiles[largeur - 1 - i][hauteur - 1 - j];
				}
			}
			return nouvellesTuiles;
		} else if (n == 3) {
			boolean[][] nouvellesTuiles = new boolean[hauteur][largeur];
			for (int i = 0; i < hauteur; i++) {
				for (int j = 0; j < largeur; j++) {
					nouvellesTuiles[i][j] = tuiles[largeur - 1 - j][i];
				}
			}
			return nouvellesTuiles;
		} else {
			System.out.println("Attention : n doit etre compris entre 0 et 3");
			return null;
		}
	}

	public static boolean[][] symetrieX(boolean[][] tuiles) {
		int largeur, hauteur;
		largeur = tuiles.length;
		hauteur = tuiles[0].length;
		boolean[][] nouvellesTuiles = new boolean[largeur][hauteur];
		for (int i = 0; i < largeur; i++) {
			for (int j = 0; j < hauteur; j++) {
				nouvellesTuiles[i][j] = tuiles[i][hauteur - 1 - j];
			}
		}
		return nouvellesTuiles;
	}

	// renvoie la liste de tous les polyominos fixes obtenus a partir d'un
	// polyomino libre (au maximum 8)

	public LinkedList<Polyomino> copains() {
		LinkedList<Polyomino> cop = new LinkedList<Polyomino>();
		Polyomino Q;
		boolean[][] t = this.tuiles;
		boolean[][] t2 = symetrieX(t);
		Q = new Polyomino(rotation(t, 0));
		if (!(Q.estDans(cop))) {
			cop.add(Q);
		}
		Q = new Polyomino(rotation(t, 1));
		if (!(Q.estDans(cop))) {
			cop.add(Q);
		}
		Q = new Polyomino(rotation(t, 2));
		if (!(Q.estDans(cop))) {
			cop.add(Q);
		}
		Q = new Polyomino(rotation(t, 3));
		if (!(Q.estDans(cop))) {
			cop.add(Q);
		}
		Q = new Polyomino(rotation(t2, 0));
		if (!(Q.estDans(cop))) {
			cop.add(Q);
		}
		Q = new Polyomino(rotation(t2, 1));
		if (!(Q.estDans(cop))) {
			cop.add(Q);
		}
		Q = new Polyomino(rotation(t2, 2));
		if (!(Q.estDans(cop))) {
			cop.add(Q);
		}
		Q = new Polyomino(rotation(t2, 3));
		if (!(Q.estDans(cop))) {
			cop.add(Q);
		}
		return cop;
	}

	// Differentes fonctions d'egalite

	// a translation pres

	@Override
	public boolean equals(Object o) {
		if (o instanceof Polyomino) {
			if (((Polyomino) o).largeur != largeur || ((Polyomino) o).hauteur != hauteur)
				return false;
			for (int i = 0; i < largeur; i++) {
				for (int j = 0; j < hauteur; j++) {
					if (((Polyomino) o).tuiles[i][j] != tuiles[i][j])
						return false;
				}
			}
			return true;
		} else
			return false;
	}

	// a isometrie directe pres (rotations d'angle 0, pi/2, pi, 3pi/2)

	public boolean equalsRotations(Polyomino P) {
		Polyomino R0 = new Polyomino(rotation(P.tuiles, 0));
		Polyomino R1 = new Polyomino(rotation(P.tuiles, 1));
		Polyomino R2 = new Polyomino(rotation(P.tuiles, 2));
		Polyomino R3 = new Polyomino(rotation(P.tuiles, 3));
		return (this.equals(R0) || this.equals(R1) || this.equals(R2) || this.equals(R3));
	}

	// a isometrie pres (rotations d'angle 0, pi/2, pi, 3pi/2 avec ou sans
	// symetrie / x)

	public boolean equalsIsometries(Polyomino P) {
		Polyomino S = new Polyomino(symetrieX(P.tuiles));
		Polyomino S0 = new Polyomino(rotation(S.tuiles, 0));
		Polyomino S1 = new Polyomino(rotation(S.tuiles, 1));
		Polyomino S2 = new Polyomino(rotation(S.tuiles, 2));
		Polyomino S3 = new Polyomino(rotation(S.tuiles, 3));
		return (this.equalsRotations(P) || this.equals(S0) || this.equals(S1) || this.equals(S2) || this.equals(S3));
	}

	// Verifie si le polyomino se trouve dans une liste (a translation
	// pres)

	public boolean estDans(LinkedList<Polyomino> liste) {
		for (Polyomino P : liste) {
			if (this.equals(P))
				return true;
		}
		return false;
	}

	// Verifie si le polyomino se trouve dans une liste (a isometrie pres)

	public boolean estDansIsometries(LinkedList<Polyomino> liste) {
		for (Polyomino P : liste) {
			if (this.equalsIsometries(P))
				return true;
		}
		return false;
	}

	// Generation de tous les polyominos d'ordre n

	public static LinkedList<Polyomino> generer(int n, boolean isom) {
		if (n == 1) {
			LinkedList<Polyomino> liste = new LinkedList<Polyomino>();
			liste.add(new Polyomino("[(0,0)]"));
			return liste;
		} else {
			LinkedList<Polyomino> listePrecedente = generer(n - 1, isom);
			LinkedList<Polyomino> liste = new LinkedList<Polyomino>();
			for (Polyomino P : listePrecedente) {
				for (Polyomino P2 : P.ajouterVoisins()) {
					// On verifie que P2 n'est pas deja dans liste
					boolean estDedans;
					if (isom) { // on prend en compte les isometries
						estDedans = P2.estDansIsometries(liste);
					} else { // on ne prend en compte que les translations
						estDedans = P2.estDans(liste);
					}
					if (!estDedans)
						liste.add(P2);
				}
			}
			return liste;
		}
	}

	public static LinkedList<Polyomino> genererFixes(int n) {
		LinkedList<Polyomino> liste = generer(n, false);
		System.out.println("Il y a " + liste.size() + " polyominos fixes de taille " + n + ".");
		return liste;
	}

	public static LinkedList<Polyomino> genererLibres(int n) {
		LinkedList<Polyomino> liste = generer(n, true);
		System.out.println("Il y a " + liste.size() + " polyominos libres de taille " + n + ".");
		return liste;
	}


	// translation pour exactCover : retourne le polyomino translate de i0,j0
	// (attention son coin inferieur gauche n'est plus forcement en (0,0))

	public Polyomino translate(int i0, int j0) {
		boolean[][] tuiles2 = new boolean[this.largeur + i0][this.hauteur + j0];
		for (int i = 0; i < this.largeur + i0; i++) {
			for (int j = 0; j < this.hauteur + j0; j++) {
				if (i >= i0 && j >= j0) {
					tuiles2[i][j] = this.tuiles[i - i0][j - j0];
				} else {
					tuiles2[i][j] = false;
				}
			}
		}
		return new Polyomino(tuiles2);
	}

	// verifie qu'un polyomino est inclus dans la region de l'espace donnee par
	// une matrice

	public boolean includedIn(boolean[][] region) {
		int l = region.length;
		int h = region[0].length;
		if (this.largeur > l || this.hauteur > h) {
			return false;
		}
		for (int i = 0; i < this.largeur; i++) {
			for (int j = 0; j < this.hauteur; j++) {
				if (tuiles[i][j] == true && region[i][j] == false) {
					return false;
				}
			}
		}
		return true;

	}

	// retourne la concatenation ligne par ligne
	// des cases du polyomino se trouvant dans la region, qu'elles soient vides
	// ou pleines

	public Integer[] toLine(boolean[][] region) {
		if (!(this.includedIn(region))) {
			return new Integer[0];
		}
		LinkedList<Integer> lineList = new LinkedList<Integer>();
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				if (region[i][j]) {
					if (i >= this.largeur || j >= this.hauteur) {
						lineList.add(0);
					} else if (tuiles[i][j]) {
						lineList.add(1);
					} else {
						lineList.add(0);
					}
				}
			}
		}
		Integer[] lineArray = new Integer[lineList.size()];
		int i = 0;
		for (int x : lineList) {
			lineArray[i] = x;
			i++;
		}
		return lineArray;
	}

	// retourne la liste des points correspondant a chaque entree de la
	// concatenation ligne par ligne de region

	public static Point[] casesLine(boolean[][] region) {
		LinkedList<Point> pts = new LinkedList<Point>();
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				if (region[i][j]) {
					pts.add(new Point(i, j));
				}
			}
		}
		return pts.toArray(new Point[pts.size()]);
	}

	// retourne le polyomino correspondant a la ligne creee par toLine dans la
	// region

	public static Polyomino fromLine(boolean[][] region, Integer[] line) {
		boolean[][] tuiles = new boolean[region.length][region[0].length];
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				tuiles[i][j] = false;
			}
		}
		Point[] cases = casesLine(region);
		for (int k = 0; k < line.length; k++) {
			Point p = cases[k];
			if (line[k] == 1) {
				tuiles[p.x][p.y] = true;
			}
		}
		return new Polyomino(tuiles);
	}

	// retourne la formulation matricielle du problème ExactCover de region par
	// les polyominos de la liste polys

	public static Integer[][] toExactCover(boolean[][] region, LinkedList<Polyomino> polys) {
		LinkedList<Integer[]> lines = new LinkedList<Integer[]>();
		for (Polyomino P : polys) {
			for (int i0 = 0; i0 <= region.length - P.largeur; i0++) {
				for (int j0 = 0; j0 <= region[0].length - P.hauteur; j0++) {
					Polyomino P2 = P.translate(i0, j0);
					Integer[] l = P2.toLine(region);
					if (l.length > 0) {
						lines.add(l);
					}
				}
			}
		}
		int p = lines.size(); // nombre de polyominos considérés dans le pavage
		int c = Polyomino.nombreCases(region); // nombre de cases de la region
		Integer[][] M = new Integer[p][c];
		int i = 0;
		for (Integer[] line : lines) {
			M[i] = line;
			i++;
		}
		return M;
	}

	// la meme chose en utilisant exactement une fois chaque polyomino :
	// necessite de partir d'une liste de polyominos libres
	public static Integer[][] toExactCoverUnique(boolean[][] region, LinkedList<Polyomino> polys) {
		LinkedList<Integer[]> lines = new LinkedList<Integer[]>();
		for (int k = 0; k < polys.size(); k++) {
			Polyomino P = polys.get(k);
			for (Polyomino Q : P.copains()) {
				for (int i0 = 0; i0 <= region.length; i0++) {
					for (int j0 = 0; j0 <= region[0].length; j0++) {
						Polyomino R = Q.translate(i0, j0);
						if (R.includedIn(region)) {
							Integer[] l = R.toLine(region);
							Integer[] l2 = new Integer[polys.size() + l.length];
							for (int i = 0; i < polys.size(); i++) {
									l2[i] = 0;
							}
							l2[k] = 1;
							for (int i = polys.size(); i < polys.size() + l.length; i++) {
								l2[i] = l[i - polys.size()];
							}
							lines.add(l2);
						}
					}
				}
			}
		}
		int p = lines.size(); // nombre de polyominos considérés dans le pavage
		int c = Polyomino.nombreCases(region); // nombre de cases de la region
		Integer[][] M = new Integer[p][c];
		int i = 0;
		for (Integer[] line : lines) {
			M[i] = line;
			i++;
		}
		return M;
	}

	// retraduit une partition renvoyee par ExactCover en liste de Polyominos
	// pour l'affichage

	public static Polyomino[] fromExactCover(boolean[][] region, LinkedList<Integer[]> partition) {
		LinkedList<Polyomino> polys = new LinkedList<Polyomino>();
		Point[] cases = casesLine(region);
		// polys.add(new Polyomino(region));
		for (Integer[] line : partition) {
			Polyomino P = fromLine(region, line);
			polys.add(P);
		}
		return polys.toArray(new Polyomino[polys.size()]);
	}

	// meme chose en unique

	public static Polyomino[] fromExactCoverUnique(boolean[][] region, LinkedList<Integer[]> partition) {
		LinkedList<Polyomino> polys = new LinkedList<Polyomino>();
		Point[] cases = casesLine(region);
		int nbCases = casesLine(region).length;
		// polys.add(new Polyomino(region));
		for (Integer[] l2 : partition) {
			Integer[] l = new Integer[nbCases];
			for (int i = 0; i < nbCases; i++) {
				l[i] = l2[(l2.length-nbCases) + i];
			}
			Polyomino P = fromLine(region, l);
			polys.add(P);
		}
		return polys.toArray(new Polyomino[polys.size()]);
	}

	// Affichage console

	@Override
	public String toString() {
		String s = "[";
		for (int i = 0; i < this.largeur; i++) {
			for (int j = 0; j < this.hauteur; j++) {
				if (this.tuiles[i][j]) {
					s += "(" + i + "," + j + ") ";
				}
			}
		}
		s += "]";
		return s;
	}

	public static void afficherTuiles(boolean[][] tuiles) {
		String s = "";
		for (int j = tuiles[0].length - 1; j >= 0; j--) {
			for (int i = 0; i < tuiles.length; i++) {
				if (tuiles[i][j]) {
					s += "1";
				} else {
					s += "0";
				}
			}
			System.out.println(s);
			s = "";
		}
	}

	public void afficheConsole() {
		afficherTuiles(this.tuiles);
	}

	// Affichage graphique

	public void addPolygonAndEdges(Image2d img, int width, Color color, int tailleTuiles, int xmin, int ymin,
			int ymaxTot) {
		// Ajoute les carres du polyomino dans l'image img, plus precisement
		for (int i = 0; i < tuiles.length; i++) {
			for (int j = 0; j < tuiles[i].length; j++) {
				if (tuiles[i][j]) {
					int[] xcoords = { (xmin + i) * tailleTuiles, (xmin + i) * tailleTuiles,
							(xmin + i + 1) * tailleTuiles, (xmin + i + 1) * tailleTuiles },
							ycoords = { (ymaxTot - ymin - j) * tailleTuiles, (ymaxTot - ymin - (j + 1)) * tailleTuiles,
									(ymaxTot - ymin - (j + 1)) * tailleTuiles, (ymaxTot - ymin - j) * tailleTuiles };

					/*
					 * System.out.println("" + xcoords[0] / tailleTuiles + " " +
					 * xcoords[1] / tailleTuiles + " " + xcoords[2] /
					 * tailleTuiles + " " + xcoords[3] / tailleTuiles + " / " +
					 * ycoords[0] / tailleTuiles + " " + ycoords[1] /
					 * tailleTuiles + " " + ycoords[2] / tailleTuiles + " " +
					 * ycoords[3] / tailleTuiles);
					 */

					img.addPolygon(xcoords, ycoords, color);
					if (i == 0 || !tuiles[i - 1][j]) {
						img.addEdge((xmin + i) * tailleTuiles, (ymaxTot - ymin - j) * tailleTuiles,
								(xmin + i) * tailleTuiles, (ymaxTot - ymin - (j + 1)) * tailleTuiles, width);
					}
					if (j == 0 || !tuiles[i][j - 1]) {
						img.addEdge((xmin + i) * tailleTuiles, (ymaxTot - ymin - j) * tailleTuiles,
								(xmin + i + 1) * tailleTuiles, (ymaxTot - ymin - j) * tailleTuiles, width);
					}
					if (i == tuiles.length - 1 || !tuiles[i + 1][j]) {
						img.addEdge((xmin + i + 1) * tailleTuiles, (ymaxTot - ymin - j) * tailleTuiles,
								(xmin + i + 1) * tailleTuiles, (ymaxTot - ymin - (j + 1)) * tailleTuiles, width);
					}
					if (j == tuiles[i].length - 1 || !tuiles[i][j + 1]) {
						img.addEdge((xmin + i) * tailleTuiles, (ymaxTot - ymin - (j + 1)) * tailleTuiles,
								((xmin + i) + 1) * tailleTuiles, (ymaxTot - ymin - (j + 1)) * tailleTuiles, width);
					}
				}
			}
		}
	}

	public static Color generateRandomColor(Color mix) {
		Random random = new Random();
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);

		// mix the color
		if (mix != null) {
			red = (red + mix.getRed()) / 2;
			green = (green + mix.getGreen()) / 2;
			blue = (blue + mix.getBlue()) / 2;
		}

		Color color = new Color(red, green, blue);
		return color;
	}

	public static Image2dViewer creerFenetre(Configuration config) {
		Color[] colors = { Color.red, Color.yellow, Color.green, Color.blue, Color.gray, Color.cyan, Color.magenta,
				Color.orange, Color.lightGray };
		Image2d img = new Image2d(config.xmax * config.tailleTuiles, config.ymax * config.tailleTuiles);
		for (int i = 0; i < config.polyominoes.length; i++) {
			Polyomino p = config.polyominoes[i];
			int xmin = config.bottomLeft[i].x;
			int ymin = config.bottomLeft[i].y;
			Color col = colors[i % colors.length];
			// Color mix = new Color(100, 100, 150);
			// Color col = generateRandomColor(mix);
			p.addPolygonAndEdges(img, config.width, col, config.tailleTuiles, xmin, ymin, config.ymax);
		}

		Image2dViewer fenetre = new Image2dViewer(img);
		fenetre.setSize(config.tailleTuiles * config.xmax, config.tailleTuiles * config.ymax + 23);
		fenetre.setLocationRelativeTo(null);
		return fenetre;

	}

}
