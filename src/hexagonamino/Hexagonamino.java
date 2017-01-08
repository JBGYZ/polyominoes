package hexagonamino;

import java.awt.Color;
import java.util.LinkedList;

import polyomino.DancingLinks;
import polyomino.Image2d;
import polyomino.Image2dViewer;

public class Hexagonamino {
	
	public boolean[][] tiles; // representation as a matrix of booleans
	public int n; // area of the hexagonamino
	public int width, height; // dimensions of the smallest rectangle containing
								// the hexagonamino

	public Hexagonamino(boolean[][] tiles) {
		this.tiles = tiles;
		this.n = nbSquares(this.tiles);
		this.width = this.tiles.length;
		this.height = this.tiles[0].length;
	}
	
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
	
	public boolean containsSquare(HexaPoint p) {
		if (p.x < 0 || p.y < 0 || p.x >= width || p.y >= height) {
			return false;
		}
		return this.tiles[p.x][p.y];
	}
	
	public Hexagonamino addSquare(HexaPoint p) {
		if (this.containsSquare(p)) {
			System.out.println("This hexagonamino already contains " + p);
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
			return new Hexagonamino(tiles2);
		}

	}
	
	// Returns the list of (n+1)-sized hexagonamino obtained by adding a square
		// in each possible neighboring spot of this
	
	public LinkedList<Hexagonamino> addHexaNeighbors() {
		LinkedList<Hexagonamino> newPolys = new LinkedList<Hexagonamino>();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (this.tiles[i][j]) {
					HexaPoint p = new HexaPoint(i, j);
					for (HexaPoint v : p.neighbors()) {
						if (!this.containsSquare(v)) {
							newPolys.add(this.addSquare(v));
						}
					}
				}
			}
		}
		return newPolys;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Hexagonamino) {
			if (((Hexagonamino) o).width != width || ((Hexagonamino) o).height != height)
				return false;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (((Hexagonamino) o).tiles[i][j] != tiles[i][j])
						return false;
				}
			}
			return true;
		} else
			return false;
	}
	
	public boolean isIn(LinkedList<Hexagonamino> polys) {
		for (Hexagonamino P : polys) {
			if (this.equals(P))
				return true;
		}
		return false;
	}
	
	public Hexagonamino symmetryY() {
		boolean[][] tiles = this.tiles;
		int width, height;
		width = tiles.length;
		height = tiles[0].length;
		HexaTableauRelatif tab = new HexaTableauRelatif(-2*Math.max(height, width), 2*Math.max(height, width), -2*Math.max(height, width), 2*Math.max(height, width));
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if(tiles[i][j]) {
					tab.set(-i, i+j, true);
				}
			}
		}
		return tab.getHexagonamino();
	}
	
	// Rotates an Hexagonamino by n * pi / 3
	
	public Hexagonamino rotation(int n) {
		boolean[][] tiles = this.tiles;
		int width, height;
		width = tiles.length;
		height = tiles[0].length;
		if (n == 0) {
			return this;
		} else if (n == 1) {
			HexaTableauRelatif tab = new HexaTableauRelatif(-2*Math.max(height, width), 2*Math.max(height, width), -2*Math.max(height, width), 2*Math.max(height, width));
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if(tiles[i][j]) {
						tab.set(-j, i+j, true);
					}
				}
			}
			return tab.getHexagonamino();
		} else if (n <= 5) {
			return rotation(n-1).rotation(1);
		} else {
			System.out.println("Beware : n must be between 0 and 5.");
			return null;
		}
	}
	
	public boolean equalsRotations(Hexagonamino P) {
		Hexagonamino R0 = P.rotation(0);
		Hexagonamino R1 = P.rotation(1);
		Hexagonamino R2 = P.rotation(2);
		Hexagonamino R3 = P.rotation(3);
		Hexagonamino R4 = P.rotation(4);
		Hexagonamino R5 = P.rotation(5);
		return (this.equals(R0) || this.equals(R1) || this.equals(R2) || this.equals(R3) || this.equals(R4) || this.equals(R5));
	}
	
	public boolean equalsIsometries(Hexagonamino P) {
		Hexagonamino S = P.symmetryY();
		return (this.equalsRotations(P) || this.equalsRotations(S));
	}

	// Checks whether this is contained in a list (up to any isometry)

	public boolean isInIsometries(LinkedList<Hexagonamino> polys) {
		for (Hexagonamino P : polys) {
			if (this.equalsIsometries(P))
				return true;
		}
		return false;
	}
	
	public void afficheConsole() {
		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[0].length; j++) {
				System.out.print(tiles[i][j] ? "1" : "0");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static LinkedList<Hexagonamino> generateHexa(int n, boolean isom) {
		if (n == 1) {
			LinkedList<Hexagonamino> polys = new LinkedList<Hexagonamino>();
			boolean[][] tmp = {{true}};
			polys.add(new Hexagonamino(tmp));
			return polys;
		} else {
			LinkedList<Hexagonamino> polysPrevious = generateHexa(n - 1, isom);
			LinkedList<Hexagonamino> polys = new LinkedList<Hexagonamino>();
			for (Hexagonamino P : polysPrevious) {
				for (Hexagonamino P2 : P.addHexaNeighbors()) {
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
	
	public static LinkedList<Hexagonamino> generateFixed(int n) {
		return generateHexa(n, false);
	}
	
	public static LinkedList<Hexagonamino> generateFree(int n) {
		return generateHexa(n, true);
	}

	public void addPolygonAndEdges(Image2d img, int width2, Color color, int sizeTiles2, int xmin, int ymin,
			int ymaxTot) {
		int sizeTiles = sizeTiles2 / 2;
		int rayon = (int) (sizeTiles / (2 * Math.cos(Math.PI / 6)) + 2);
		//int width = width2 / 3;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j]) {
					/*int xO = (i + 1) * sizeTiles,
						yO = (ymaxTot - j) * sizeTiles;*/
					int i2 = xmin + i + 2, j2 = ymaxTot - j - ymin;
					int xO = (int)((i2 - j2 * Math.cos(Math.PI/3)) * sizeTiles),
						yO = (int)(j2 * Math.sin(Math.PI/3) * sizeTiles);
					int[][] hexagon = getHexagon(rayon, xO, yO);
					img.addPolygon(hexagon[0], hexagon[1], color);
					
					HexaPoint P = new HexaPoint(i, j);
					LinkedList<HexaPoint> voisins = P.neighbors();
					
					for (int k = 0; k < voisins.size(); k++) {
						HexaPoint voisin = voisins.get(k);
						if (voisin.onBoard(tiles.length, tiles[0].length) && !tiles[voisin.getx()][voisin.gety()]) {
							int x_0 = xO + (int)(rayon * Math.cos((-k + 4.5) * Math.PI / 3)),
								y_0 = yO + (int)(rayon * Math.sin((-k + 4.5) * Math.PI / 3)),
								x_1 = xO + (int)(rayon * Math.cos((-k + 5.5) * Math.PI / 3)),
								y_1 = yO + (int)(rayon * Math.sin((-k + 5.5) * Math.PI / 3));
							img.addEdge(x_0, y_0, x_1, y_1, width);
						}
					}
				}
			}
		}
	}
	
	// Returns the coordinates for a hexagon with given center O and size
	
	private int[][] getHexagon(int size, int xO, int yO) {
		int[][] res = {{0,0,0,0,0,0},
						{0,0,0,0,0,0}};
		for (int k = 0; k < 6; k++) {
			res[0][k] = xO + (int)(size * Math.cos((k + 0.5) * Math.PI / 3));
			res[1][k] = yO + (int)(size * Math.sin((k + 0.5) * Math.PI / 3));
		}
		return res;
	}
	
	// Returns the list of all fixed Hexagonaminoes obtained by symmetries and
		// rotations from a given free Hexagonamino (maximum 8, minimum 1)

		public LinkedList<Hexagonamino> freeToFixed() {
			LinkedList<Hexagonamino> friends = new LinkedList<Hexagonamino>();
			Hexagonamino S = this.symmetryY();
			Hexagonamino Q;
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
			Q = this.rotation(4);
			if (!(Q.isIn(friends))) {
				friends.add(Q);
			}
			Q = this.rotation(5);
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
			Q = S.rotation(4);
			if (!(Q.isIn(friends))) {
				friends.add(Q);
			}
			Q = S.rotation(5);
			if (!(Q.isIn(friends))) {
				friends.add(Q);
			}
			return friends;
		}
	
		// Translation (for the ExactCover problem) : returns this translated by
		// (i0,j0)
		// Beware ! The boolean matrix is no longer tight, there might be some
		// useless space on the sides

		public Hexagonamino translate(int i0, int j0) {
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
			return new Hexagonamino(tiles2);
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

		// Returns the list of HexaPoints corresponding to the line-by-line
		// concatenation of region

		public static HexaPoint[] hexaPointsLine(boolean[][] region) {
			LinkedList<HexaPoint> pts = new LinkedList<HexaPoint>();
			for (int i = 0; i < region.length; i++) {
				for (int j = 0; j < region[0].length; j++) {
					if (region[i][j]) {
						pts.add(new HexaPoint(i, j));
					}
				}
			}
			return pts.toArray(new HexaPoint[pts.size()]);
		}

		// Returns the Hexagonamino corresponding to the line created by toLine(region)

		public static Hexagonamino fromLine(boolean[][] region, Integer[] line) {
			boolean[][] tiles = new boolean[region.length][region[0].length];
			for (int i = 0; i < region.length; i++) {
				for (int j = 0; j < region[0].length; j++) {
					tiles[i][j] = false;
				}
			}
			HexaPoint[] cases = hexaPointsLine(region);
			for (int k = 0; k < line.length; k++) {
				HexaPoint p = cases[k];
				if (line[k] == 1) {
					tiles[p.x][p.y] = true;
				}
			}
			return new Hexagonamino(tiles);
		}

		// Returns the matrix of the ExactCover problem where :
		// - region is the domain of the plane we have to cover
		// - polys is the list of Hexagonaminoes we are allowed to use
		// - isom is a boolean telling us whether we can rotate and symmetrize the
		// Hexagonaminoes of the list

		public static Integer[][] toExactCover(boolean[][] region, LinkedList<Hexagonamino> polys, boolean isom) {
			LinkedList<Integer[]> lines = new LinkedList<Integer[]>();
			for (Hexagonamino P : polys) {
				if (isom) {
					for (Hexagonamino Q : P.freeToFixed()) {
						for (int i0 = 0; i0 <= region.length; i0++) {
							for (int j0 = 0; j0 <= region[0].length; j0++) {
								Hexagonamino R = Q.translate(i0, j0);
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
							Hexagonamino R = P.translate(i0, j0);
							if (R.includedIn(region)) {
								Integer[] l = R.toLine(region);
								lines.add(l);
							}
						}
					}
				}
			}
			int p = lines.size();
			int c = Hexagonamino.nbSquares(region);
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
		// - polys is the list of Hexagonaminoes we are allowed to use, each Hexagonamino
		// being allowed only ONCE but in any possible position
		// - isom is a boolean telling us whether we can rotate and symmetrize the
		// Hexagonaminoes of the list

		public static Integer[][] toExactCoverUnique(boolean[][] region, LinkedList<Hexagonamino> polys, boolean isom) {
			LinkedList<Integer[]> lines = new LinkedList<Integer[]>();
			for (int k = 0; k < polys.size(); k++) {
				Hexagonamino P = polys.get(k);
				if (isom) {
					for (Hexagonamino Q : P.freeToFixed()) {
						for (int i0 = 0; i0 <= region.length; i0++) {
							for (int j0 = 0; j0 <= region[0].length; j0++) {
								Hexagonamino R = Q.translate(i0, j0);
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
							Hexagonamino R = P.translate(i0, j0);
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
			int c = Hexagonamino.nbSquares(region);
			Integer[][] M = new Integer[p][c];
			int i = 0;
			for (Integer[] line : lines) {
				M[i] = line;
				i++;
			}
			return M;
		}

		// Traduction from a partition returned by ExactCover into a list of
		// Hexagonaminoes, in order to display the result

		public static Hexagonamino[] fromExactCover(boolean[][] region, LinkedList<Integer[]> partition) {
			LinkedList<Hexagonamino> polys = new LinkedList<Hexagonamino>();
			// HexaPoint[] cases = HexaPointsLine(region);
			// polys.add(new Hexagonamino(region));
			for (Integer[] line : partition) {
				Hexagonamino P = fromLine(region, line);
				polys.add(P);
			}
			return polys.toArray(new Hexagonamino[polys.size()]);
		}

		// Same thing but with the "unique" convention

		public static Hexagonamino[] fromExactCoverUnique(boolean[][] region, LinkedList<Integer[]> partition) {
			LinkedList<Hexagonamino> polys = new LinkedList<Hexagonamino>();
			int nbSquares = hexaPointsLine(region).length;
			// polys.add(new Hexagonamino(region));
			for (Integer[] l2 : partition) {
				Integer[] l = new Integer[nbSquares];
				for (int i = 0; i < nbSquares; i++) {
					l[i] = l2[(l2.length - nbSquares) + i];
				}
				Hexagonamino P = fromLine(region, l);
				polys.add(P);
			}
			return polys.toArray(new Hexagonamino[polys.size()]);
		}

	
	public static void main(String[] args) {
		/*boolean[][] tiles = {{true, false, false},
							{true, true, false},
							{false, false, false}},
				tiles2 = {{false, true, true},
						{false, false, true},
						{true, true, true}};
		
		Hexagonamino[] hexTab = {new Hexagonamino(tiles), new Hexagonamino(tiles2)};
		HexaConfiguration hexConf = new HexaConfiguration(hexTab, true);
		hexConf.createWindow();*/
		
		/*for(int k=3; k<5; k++) {
		LinkedList<Hexagonamino> hexalist = generateHexa(k, false);
		HexaConfiguration hexConf2 = new HexaConfiguration(hexalist.toArray(new Hexagonamino[0]), false);
		hexConf2.createWindow();
		System.out.println(hexalist.size());
		}*/
		
		int s = 6;
		boolean isom = true;
		LinkedList<Hexagonamino> polys81 = Hexagonamino.generateHexa(s, isom);
		System.out.println(polys81.size());
		boolean[][] region81 = new boolean[s][s];
		for (int i = 0; i < region81.length; i++) {
			for (int j = 0; j < region81[0].length; j++) {
				region81[i][j] = true;
			}
		}
		
		Integer[][] M81 = Hexagonamino.toExactCover(region81, polys81, isom);
		LinkedList<Integer[]> partition81 = DancingLinks.findExactCover(M81);
		Hexagonamino[] partition81polys = Hexagonamino.fromExactCover(region81, partition81);
		HexaConfiguration config81 = new HexaConfiguration(partition81polys, true);
		Image2dViewer window81 = config81.createWindow();
	
	}
}

