package polyomino;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Polyomino {

	public boolean[][] tiles; // representation as a matrix of booleans
	public int n; // area of the polyomino
	public int width, height; // dimensions of the smallest rectangle containing
								// the polyomino
	public double hash, hashFree;
	
	// Returns the number of full squares in a matrix of booleans

	public static int nbSquares(boolean[][] tiles) {
		int l = tiles.length;
		int h = tiles[0].length;
		int n = 0;
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < h; j++) {
				if (tiles[i][j]) {
					n++;
				}
			}
		}
		return n;
	}

	// Constructs a polyomino from a matrix of booleans

	public Polyomino(boolean[][] tiles) {
		this.tiles = tiles;
		this.n = nbSquares(this.tiles);
		this.width = this.tiles.length;
		this.height = this.tiles[0].length;
		hashFree = Math.pow(2, width*height+1);
	}

	// Constructs a polyomino from a string of coordinates

	public Polyomino(String s) {

		// Ex : [(0,0), (0,4), (1,0), (1,1), (1,2), (1,3), (1,4), (2,0), (2,4)]
		// Parsing of the String into a LinkedList
		LinkedList<Integer[]> tilesList = new LinkedList<Integer[]>();
		int k = 1; // current index in the string
		while (k < s.length() - 1) {
			// each loop starts at an opening bracket
			k++;
			int x = 0, y = 0;
			while (s.charAt(k) != ',') {
				x = x * 10 + Integer.parseInt(Character.toString(s.charAt(k)));
				k++;
			}
			k++; // we go through the comma (_,_)
			while (s.charAt(k) != ')') {
				y = y * 10 + Integer.parseInt(Character.toString(s.charAt(k)));
				k++;
			}
			Integer[] tuile = { x, y };
			tilesList.add(tuile);
			// we check whether we've reached the end yet
			k++;
			if (s.charAt(k) == ',')
				k += 2;
			else
				break;
		}
		// conversion of the list into a boolean[][]
		int xmax = 0, ymax = 0;
		for (Integer[] tile : tilesList) {
			xmax = Math.max(xmax, tile[0]);
			ymax = Math.max(ymax, tile[1]);
		}
		boolean[][] tilesArray = new boolean[xmax + 1][ymax + 1];
		for (Integer[] tile : tilesList) {
			tilesArray[tile[0]][tile[1]] = true;
		}
		this.tiles = tilesArray;
		this.n = nbSquares(this.tiles);
		this.width = this.tiles.length;
		this.height = this.tiles[0].length;
	}

	// Constructs a list of polyominoes from a file containing strings

	public static Polyomino[] importFile(String fichier) {
		LinkedList<Polyomino> polys = new LinkedList<Polyomino>();
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(fichier)));
			String line;
			while ((line = buffer.readLine()) != null) {
				polys.add(new Polyomino(line));
			}
			buffer.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return polys.toArray(new Polyomino[0]);
	}
	
	// Generates the hash of a Polyomino
	
	public double generateHash() {
		hash = 0;
		for(int i=0; i<tiles.length; i++) {
			for(int j=0; j<tiles[0].length; j++) {
				if (tiles[i][j]) {
					hash+=Math.pow(2, j+i*(tiles[0].length+2));
				}
			}
		}
		return hash;
	}
	
	public double generateHashFree() {
		hashFree = generateHash();
		LinkedList<Polyomino> friends = freeToFixed();
		for(Polyomino P : friends) {
			double hashTmp = P.generateHash();
			hashFree = Math.min(hashFree, hashTmp);
		}
		return hashFree;
	}

	// Returns a boolean stating whether the polyomino contains a given point

	public boolean containsSquare(Point p) {
		if (p.x < 0 || p.y < 0 || p.x >= width || p.y >= height) {
			return false;
		}
		return this.tiles[p.x][p.y];
	}

	// Returns the polyomino resulting from the union between this and the point
	// p (always use with points that are adjacent to this)

	public Polyomino addSquare(Point p) {
		if (this.containsSquare(p)) {
			System.out.println("This polyomino already contains " + p);
			return this;
		} else {
			// a and b : useful if p has negative coordinates => we widen or
			// heighten the grid by 1
			// c and d : useful if p has coordinates beyond this.width or
			// this.height => we widen or heighten the grid by 1
			// normally a,b,c,d are all 0 or 1, with only one nonzero value if
			// this function is used properly
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
			if (p.x >= this.width) {
				c = 1;
			}
			if (p.y >= this.height) {
				d = 1;
			}
			boolean[][] tiles2 = new boolean[this.width + a + c][this.height + b + d];
			for (int i = 0; i < this.width; i++) {
				for (int j = 0; j < this.height; j++) {
					tiles2[a + i][b + j] = this.tiles[i][j];
				}
			}
			tiles2[p.x + a][p.y + b] = true;
			return new Polyomino(tiles2);
		}

	}

	// Returns the list of (n+1)-sized polyominoes obtained by adding a square
	// in each possible neighboring spot of this

	public LinkedList<Polyomino> addNeighbors() {
		LinkedList<Polyomino> newPolys = new LinkedList<Polyomino>();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (this.tiles[i][j]) {
					Point p = new Point(i, j);
					for (Point v : p.neighbors()) {
						if (!this.containsSquare(v)) {
							newPolys.add(this.addSquare(v));
						}
					}
				}
			}
		}
		return newPolys;
	}

	// Transformation functions

	// Returns the rotation of this by an angle of + n pi / 2

	public Polyomino rotation(int n) {
		boolean[][] tiles = this.tiles;
		int width, height;
		width = tiles.length;
		height = tiles[0].length;
		if (n == 0) {
			return this;
		} else if (n == 1) {
			boolean[][] newTiles = new boolean[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					newTiles[i][j] = tiles[j][height - 1 - i];
				}
			}
			return new Polyomino(newTiles);
		} else if (n == 2) {
			boolean[][] newTiles = new boolean[width][height];
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					newTiles[i][j] = tiles[width - 1 - i][height - 1 - j];
				}
			}
			return new Polyomino(newTiles);
		} else if (n == 3) {
			boolean[][] newTiles = new boolean[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					newTiles[i][j] = tiles[width - 1 - j][i];
				}
			}
			return new Polyomino(newTiles);
		} else {
			System.out.println("Beware : n must be between 0 and 3.");
			return null;
		}
	}

	// Returns the symmetric polyomino of this with respect to the Y axis

	public Polyomino symmetryY() {
		boolean[][] tiles = this.tiles;
		int width, height;
		width = tiles.length;
		height = tiles[0].length;
		boolean[][] newTiles = new boolean[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				newTiles[i][j] = tiles[i][height - 1 - j];
			}
		}
		return new Polyomino(newTiles);
	}

	// Returns the polyomino this dilated by a factor of k (integer)

	public Polyomino dilatation(int k) {
		boolean[][] tiles = this.tiles;
		int width, height;
		width = tiles.length;
		height = tiles[0].length;
		boolean[][] newTiles = new boolean[k * width][k * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (int k1 = 0; k1 < k; k1++) {
					for (int k2 = 0; k2 < k; k2++) {
						newTiles[k * i + k1][k * j + k2] = tiles[i][j];
					}
				}
			}
		}
		return new Polyomino(newTiles);
	}

	// Returns the list of all fixed polyominoes obtained by symmetries and
	// rotations from a given free polyomino (maximum 8, minimum 1)

	public LinkedList<Polyomino> freeToFixed() {
		LinkedList<Polyomino> friends = new LinkedList<Polyomino>();
		Polyomino S = this.symmetryY();
		Polyomino Q;
		Q = this.rotation(0);
		if (!(Q.isIn(friends))) {
			friends.add(Q);
		}
		Q = this.rotation(1);
		if (!(Q.isIn(friends))) {
			friends.add(Q);
		}
		Q = this.rotation(2);
		if (!(Q.isIn(friends))) {
			friends.add(Q);
		}
		Q = this.rotation(3);
		if (!(Q.isIn(friends))) {
			friends.add(Q);
		}
		Q = S.rotation(0);
		if (!(Q.isIn(friends))) {
			friends.add(Q);
		}
		Q = S.rotation(1);
		if (!(Q.isIn(friends))) {
			friends.add(Q);
		}
		Q = S.rotation(2);
		if (!(Q.isIn(friends))) {
			friends.add(Q);
		}
		Q = S.rotation(3);
		if (!(Q.isIn(friends))) {
			friends.add(Q);
		}
		return friends;
	}

	// Different notions of equality

	// Up to a translation

	@Override
	public boolean equals(Object o) {
		if (o instanceof Polyomino) {
			if (((Polyomino) o).width != width || ((Polyomino) o).height != height)
				return false;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (((Polyomino) o).tiles[i][j] != tiles[i][j])
						return false;
				}
			}
			return true;
		} else
			return false;
	}

	// Up to a direct isometry (rotations of angle 0, pi/2, pi, 3pi/2)

	public boolean equalsRotations(Polyomino P) {
		Polyomino R0 = P.rotation(0);
		Polyomino R1 = P.rotation(1);
		Polyomino R2 = P.rotation(2);
		Polyomino R3 = P.rotation(3);
		return (this.equals(R0) || this.equals(R1) || this.equals(R2) || this.equals(R3));
	}

	// Up to any isometry (rotations of angle 0, pi/2, pi, 3pi/2, with or
	// without symmetry)

	public boolean equalsIsometries(Polyomino P) {
		Polyomino S = P.symmetryY();
		return (this.equalsRotations(P) || this.equalsRotations(S));
	}

	// Checks whether this is contained in a list (up to a translation)

	public boolean isIn(LinkedList<Polyomino> polys) {
		for (Polyomino P : polys) {
			if (this.equals(P))
				return true;
		}
		return false;
	}

	// Checks whether this is contained in a list (up to any isometry)

	public boolean isInIsometries(LinkedList<Polyomino> polys) {
		for (Polyomino P : polys) {
			if (this.equalsIsometries(P))
				return true;
		}
		return false;
	}

	// Generation of all n-sized polyominoes (free if isom == true, fixed if
	// isom == false)

	public static LinkedList<Polyomino> generate(int n, boolean isom) {
		if (n == 1) {
			LinkedList<Polyomino> polys = new LinkedList<Polyomino>();
			polys.add(new Polyomino("[(0,0)]"));
			return polys;
		} else {
			LinkedList<Polyomino> polysPrevious = generate(n - 1, isom);
			LinkedList<Polyomino> polys = new LinkedList<Polyomino>();
			for (Polyomino P : polysPrevious) {
				for (Polyomino P2 : P.addNeighbors()) {
					// we check whether P2 is already in polys
					boolean alreadyIn;
					if (isom) { // taking isometries into account => free
						alreadyIn = P2.isInIsometries(polys);
					} else { // taking only translations into account => fixed
						alreadyIn = P2.isIn(polys);
					}
					if (!alreadyIn)
						polys.add(P2);
				}
			}
			return polys;
		}
	}

	public static LinkedList<Polyomino> generateFixed(int n) {
		LinkedList<Polyomino> polys = generate(n, false);
		System.out.println("[Naive method] There are " + polys.size() + " fixed polyominoes of size " + n + ".");
		System.out.println();
		return polys;
	}

	public static LinkedList<Polyomino> generateFree(int n) {
		LinkedList<Polyomino> polys = generate(n, true);
		System.out.println("[Naive method] There are " + polys.size() + " free polyominoes of size " + n + ".");
		System.out.println();
		return polys;
	}

	// Translation (for the ExactCover problem) : returns this translated by
	// (i0,j0)
	// Beware ! The boolean matrix is no longer tight, there might be some
	// useless space on the sides

	public Polyomino translate(int i0, int j0) {
		boolean[][] tiles2 = new boolean[this.width + i0][this.height + j0];
		for (int i = 0; i < this.width + i0; i++) {
			for (int j = 0; j < this.height + j0; j++) {
				if (i >= i0 && j >= j0) {
					tiles2[i][j] = this.tiles[i - i0][j - j0];
				} else {
					tiles2[i][j] = false;
				}
			}
		}
		return new Polyomino(tiles2);
	}

	// Checks whether this is included in a region of the plane given by a
	// boolean matrix (we assume that the origin of both this.tiles and region
	// is set in (0,0))

	public boolean includedIn(boolean[][] region) {
		int l = region.length;
		int h = region[0].length;
		if (this.width > l || this.height > h) {
			return false;
		}
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (this.tiles[i][j] == true && region[i][j] == false) {
					return false;
				}
			}
		}
		return true;

	}

	// Returns the line-by-line concatenation of the tiles of this.tiles that
	// are contained in the region, whether these tiles are empty or full

	public Integer[] toLine(boolean[][] region) {
		if (!(this.includedIn(region))) {
			return new Integer[0];
		}
		LinkedList<Integer> lineList = new LinkedList<Integer>();
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				if (region[i][j]) {
					if (i >= this.width || j >= this.height) {
						lineList.add(0);
					} else if (this.tiles[i][j]) {
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

	// Returns the list of points corresponding to the line-by-line
	// concatenation of region

	public static Point[] pointsLine(boolean[][] region) {
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

	// Returns the polyomino corresponding to the line created by toLine(region)

	public static Polyomino fromLine(boolean[][] region, Integer[] line) {
		boolean[][] tiles = new boolean[region.length][region[0].length];
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				tiles[i][j] = false;
			}
		}
		Point[] cases = pointsLine(region);
		for (int k = 0; k < line.length; k++) {
			Point p = cases[k];
			if (line[k] == 1) {
				tiles[p.x][p.y] = true;
			}
		}
		return new Polyomino(tiles);
	}

	// Returns the matrix of the ExactCover problem where :
	// - region is the domain of the plane we have to cover
	// - polys is the list of polyominoes we are allowed to use
	// - isom is a boolean telling us whether we can rotate and symmetrize the
	// polyominoes of the list

	public static Integer[][] toExactCover(boolean[][] region, LinkedList<Polyomino> polys, boolean isom) {
		LinkedList<Integer[]> lines = new LinkedList<Integer[]>();
		for (Polyomino P : polys) {
			if (isom) {
				for (Polyomino Q : P.freeToFixed()) {
					for (int i0 = 0; i0 <= region.length; i0++) {
						for (int j0 = 0; j0 <= region[0].length; j0++) {
							Polyomino R = Q.translate(i0, j0);
							if (R.includedIn(region)) {
								Integer[] l = R.toLine(region);
								lines.add(l);
							}
						}
					}
				}
			} else {
				for (int i0 = 0; i0 <= region.length; i0++) {
					for (int j0 = 0; j0 <= region[0].length; j0++) {
						Polyomino R = P.translate(i0, j0);
						if (R.includedIn(region)) {
							Integer[] l = R.toLine(region);
							lines.add(l);
						}
					}
				}
			}
		}
		int p = lines.size();
		int c = Polyomino.nbSquares(region);
		Integer[][] M = new Integer[p][c];
		int i = 0;
		for (Integer[] line : lines) {
			M[i] = line;
			i++;
		}
		return M;
	}

	// Returns the matrix of the ExactCover problem where :
	// - region is the domain of the plane we have to cover
	// - polys is the list of polyominoes we are allowed to use, each polyomino
	// being allowed only ONCE but in any possible position
	// - isom is a boolean telling us whether we can rotate and symmetrize the
	// polyominoes of the list

	public static Integer[][] toExactCoverUnique(boolean[][] region, LinkedList<Polyomino> polys, boolean isom) {
		LinkedList<Integer[]> lines = new LinkedList<Integer[]>();
		for (int k = 0; k < polys.size(); k++) {
			Polyomino P = polys.get(k);
			if (isom) {
				for (Polyomino Q : P.freeToFixed()) {
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
			} else {
				for (int i0 = 0; i0 <= region.length; i0++) {
					for (int j0 = 0; j0 <= region[0].length; j0++) {
						Polyomino R = P.translate(i0, j0);
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
		int p = lines.size();
		int c = Polyomino.nbSquares(region);
		Integer[][] M = new Integer[p][c];
		int i = 0;
		for (Integer[] line : lines) {
			M[i] = line;
			i++;
		}
		return M;
	}

	// Traduction from a partition returned by ExactCover into a list of
	// polyominoes, in order to display the result

	public static Polyomino[] fromExactCover(boolean[][] region, LinkedList<Integer[]> partition) {
		LinkedList<Polyomino> polys = new LinkedList<Polyomino>();
		// Point[] cases = pointsLine(region);
		// polys.add(new Polyomino(region));
		for (Integer[] line : partition) {
			Polyomino P = fromLine(region, line);
			polys.add(P);
		}
		return polys.toArray(new Polyomino[polys.size()]);
	}

	// Same thing but with the "unique" convention

	public static Polyomino[] fromExactCoverUnique(boolean[][] region, LinkedList<Integer[]> partition) {
		LinkedList<Polyomino> polys = new LinkedList<Polyomino>();
		int nbSquares = pointsLine(region).length;
		// polys.add(new Polyomino(region));
		for (Integer[] l2 : partition) {
			Integer[] l = new Integer[nbSquares];
			for (int i = 0; i < nbSquares; i++) {
				l[i] = l2[(l2.length - nbSquares) + i];
			}
			Polyomino P = fromLine(region, l);
			polys.add(P);
		}
		return polys.toArray(new Polyomino[polys.size()]);
	}

	// Functions for displaying polyominoes in the terminal

	@Override
	public String toString() {
		String s = "[";
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (this.tiles[i][j]) {
					s += "(" + i + "," + j + ") ";
				}
			}
		}
		s += "]";
		return s;
	}

	public static String tilesToString(boolean[][] tiles) {
		String s = "";
		for (int j = tiles[0].length - 1; j >= 0; j--) {
			for (int i = 0; i < tiles.length; i++) {
				if (tiles[i][j]) {
					s += "1";
				} else {
					s += "0";
				}
			}
			System.out.println(s);
			s = "";
		}
		return s;
	}

	public void displayTiles() {
		System.out.println(tilesToString(this.tiles));
	}

	// Adds every square of this into the image img

	public void addPolygonAndEdges(Image2d img, int width, Color color, int tailletiles, int xmin, int ymin,
			int ymaxTot) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j]) {
					int[] xcoords = { (xmin + i) * tailletiles, (xmin + i) * tailletiles, (xmin + i + 1) * tailletiles,
							(xmin + i + 1) * tailletiles },
							ycoords = { (ymaxTot - ymin - j) * tailletiles, (ymaxTot - ymin - (j + 1)) * tailletiles,
									(ymaxTot - ymin - (j + 1)) * tailletiles, (ymaxTot - ymin - j) * tailletiles };
					img.addPolygon(xcoords, ycoords, color);
					if (i == 0 || !tiles[i - 1][j]) {
						img.addEdge((xmin + i) * tailletiles, (ymaxTot - ymin - j) * tailletiles,
								(xmin + i) * tailletiles, (ymaxTot - ymin - (j + 1)) * tailletiles, width);
					}
					if (j == 0 || !tiles[i][j - 1]) {
						img.addEdge((xmin + i) * tailletiles, (ymaxTot - ymin - j) * tailletiles,
								(xmin + i + 1) * tailletiles, (ymaxTot - ymin - j) * tailletiles, width);
					}
					if (i == tiles.length - 1 || !tiles[i + 1][j]) {
						img.addEdge((xmin + i + 1) * tailletiles, (ymaxTot - ymin - j) * tailletiles,
								(xmin + i + 1) * tailletiles, (ymaxTot - ymin - (j + 1)) * tailletiles, width);
					}
					if (j == tiles[i].length - 1 || !tiles[i][j + 1]) {
						img.addEdge((xmin + i) * tailletiles, (ymaxTot - ymin - (j + 1)) * tailletiles,
								((xmin + i) + 1) * tailletiles, (ymaxTot - ymin - (j + 1)) * tailletiles, width);
					}
				}
			}
		}
	}

}
