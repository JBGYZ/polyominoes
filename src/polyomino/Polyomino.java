package polyomino;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.io.File;
import javax.imageio.ImageIO;

public class Polyomino {
	public boolean[][] tuiles; // repr�sentation sous forme d'un tableau de
								// bool�ens
	public LinkedList<Point> cases; // repr�sentation sous forme de liste de
									// cases
	public int n; // taille du polymino
	public int largeur, hauteur; // taille du plus petit rectangle le contenant

	// constructeurs

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
		int[] form = { largeur + 1, hauteur + 1 };
		return form;
	}

	public static boolean[][] toTuiles(LinkedList<Point> cases) {
		// convertit les cases en tuiles, en supposant que les cases ont pour
		// coin inf�rieur gauche 0
		int[] form = format(cases);
		int largeur = form[0];
		int hauteur = form[1];
		boolean[][] tuiles = new boolean[largeur][hauteur];
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
		if (p.x < 0 || p.y < 0 || p.x >= largeur || p.y >= hauteur) {
			return false;
		}
		return this.tuiles[p.x][p.y];
	}

	public Polyomino ajouterCase(Point p) {
		// renvoie le polyomino r�sultant de l'ajout de la case p � this
		if (this.contientCase(p)) {
			System.out.println("Ce polyomino contient d�j� la case " + p);
			return this;
		} else {
			int a = 0;
			int b = 0; // r��quilibrage si on passe en coordonn�es n�gatives
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

	// retourne la liste des polyominos de taille n+1 obtenus en ajoutant une
	// case sur chaque c�t� de chaque case

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

	// g�n�ration de tous les polyominos d'ordre n

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
					// On v�rifie que P2 n'est pas d�j� dans liste
					if (!P2.estDans(liste))
						liste.add(P2);
				}
			}
			return liste;
		}
	}

	// fonctions d'isométries

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

	// V�rifie si le polyomino se trouve d�j� dans une liste (en prenant en
	// compte toutes les isométries)

	public boolean estDans(LinkedList<Polyomino> liste) {
		for (Polyomino P : liste) {
			// il suffit de modifier la ligne suivante pour prendre en compte ou
			// non les rotations et les symétries
			if (this.equals_isometries(P))
				return true;
		}
		return false;
	}

	public void afficheConsole() {
		for (boolean[] bTab : tuiles) {
			for (boolean b : bTab) {
				System.out.print(b ? "O" : " ");
			}
			System.out.println();
		}
	}

	@Override
	public String toString() {
		String s = "[";
		for (Point p : this.cases) {
			s += "(" + p.x + "," + p.y + "), ";
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

	// différentes fonctions d'égalité

	// à translation près
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

	// à isométries directes près (rotations d'angle 0, pi/2, pi, 3pi/2)
	public boolean equals_rotations(Polyomino P) {
		Polyomino R0 = new Polyomino(rotation(P.tuiles, 0));
		Polyomino R1 = new Polyomino(rotation(P.tuiles, 1));
		Polyomino R2 = new Polyomino(rotation(P.tuiles, 2));
		Polyomino R3 = new Polyomino(rotation(P.tuiles, 3));
		return (this.equals(R0) || this.equals(R1) || this.equals(R2) || this.equals(R3));
	}

	// à isométries près (rotations d'angle 0, pi/2, pi, 3pi/2 avec ou sans
	// symetrie / x)
	public boolean equals_isometries(Polyomino P) {
		Polyomino S = new Polyomino(symetrieX(P.tuiles));
		Polyomino S0 = new Polyomino(rotation(S.tuiles, 0));
		Polyomino S1 = new Polyomino(rotation(S.tuiles, 1));
		Polyomino S2 = new Polyomino(rotation(S.tuiles, 2));
		Polyomino S3 = new Polyomino(rotation(S.tuiles, 3));
		return (this.equals_rotations(P) || this.equals(S0) || this.equals(S1) || this.equals(S2) || this.equals(S3));
	}

	// affichage graphique

	public void addPolygonAndEdges(Image2d img, int width, Color color, int tailleTuiles, int xmin, int ymin, int xmax,
			int ymax) {
		// Ajoute les carrés du polyomino dans l'image img, plus précisément
		// dans le cadre [xmin,xmax]*[ymin,ymax]
		for (int i = 0; i < tuiles.length; i++) {
			for (int j = 0; j < tuiles[i].length; j++) {
				if (tuiles[i][j]) {
					int[] xcoords = { (xmin + i) * tailleTuiles, (xmin + i) * tailleTuiles,
							(xmin + i + 1) * tailleTuiles, (xmin + i + 1) * tailleTuiles },
							ycoords = { (ymin + ymax - j) * tailleTuiles, (ymin + ymax - (j + 1)) * tailleTuiles,
									(ymin + ymax - (j + 1)) * tailleTuiles, (ymin + ymax - j) * tailleTuiles };
					/*System.out.println("" + xcoords[0] / tailleTuiles + " " + xcoords[1] / tailleTuiles + " "
							+ xcoords[2] / tailleTuiles + " " + xcoords[3] / tailleTuiles + " / " + ycoords[0] / tailleTuiles + " " + ycoords[1] / tailleTuiles + " "
							+ ycoords[2] / tailleTuiles + " " + ycoords[3] / tailleTuiles);*/
					img.addPolygon(xcoords, ycoords, color);
					if (i == 0 || !tuiles[i - 1][j]) {
						// System.out.println("a");
						img.addEdge((xmin + i) * tailleTuiles, (ymin + ymax - j) * tailleTuiles,
								(xmin + i) * tailleTuiles, (ymin + ymax - (j + 1)) * tailleTuiles, width);
					}
					if (j == 0 || !tuiles[i][j - 1]) {
						// System.out.println("b");
						img.addEdge((xmin + i) * tailleTuiles, (ymin + ymax - j) * tailleTuiles,
								(xmin + i + 1) * tailleTuiles, (ymin + ymax - j) * tailleTuiles, width);
					}
					if (i == tuiles.length - 1 || !tuiles[i + 1][j]) {
						// System.out.println("c");
						img.addEdge((xmin + i + 1) * tailleTuiles, (ymin + ymax - j) * tailleTuiles,
								(xmin + i + 1) * tailleTuiles, (ymin + ymax - (j + 1)) * tailleTuiles, width);
					}
					if (j == tuiles[i].length - 1 || !tuiles[i][j + 1]) {
						// System.out.println("d");
						img.addEdge((xmin + i) * tailleTuiles, (ymin + ymax - (j + 1)) * tailleTuiles,
								((xmin + i) + 1) * tailleTuiles, (ymin + ymax - (j + 1)) * tailleTuiles, width);
					}
				}
			}
		}
	}

	public static void creerFenetre(Configuration config) {
		Color[] colors = { Color.red, Color.yellow, Color.green, Color.blue, Color.gray, Color.cyan, Color.magenta,
				Color.orange, Color.lightGray };
		Image2d img = new Image2d(config.xmax * config.tailleTuiles, config.ymax * config.tailleTuiles);
		for (int i = 0; i < config.polyominoes.length; i++) {
			Polyomino p = config.polyominoes[i];
			int xmin = config.bottomLeft[i].x;
			int ymin = config.bottomLeft[i].y;
			int xmax = xmin + p.largeur;
			int ymax = ymin + p.hauteur;
			p.addPolygonAndEdges(img, config.width, colors[i % colors.length], config.tailleTuiles, xmin, ymin, xmax,
					ymax);
		}

		@SuppressWarnings("unused")
		Image2dViewer fenetre = new Image2dViewer(img);

	}
}
