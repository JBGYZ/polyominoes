package polyomino;

import java.util.LinkedList;

import hexagonamino.HexaConfiguration;
import hexagonamino.Hexagonamino;

public class Test {

	public static void main(String[] args) {
		
		// Task 1 : Polyomino manipulation
		// System.out.println(task1());
		
		// Task 2 : Polyomino generation
		// System.out.println(task2());
		 
		// Task 3 : Redelmeier's method
		// System.out.println(task3());
		
		// Tasks 4,5,6 : Exact Cover & Dancing Links
		// System.out.println(task456());
		
		// Tasks 7,8 : Conversion from polyomino tiling to ExactCover and various applications
		
		// First example : covering a rectangle with fixed polyominoes
		// System.out.println(task78_1(5,6,5));
		// Various regions of area 60, all 12 free polyominoes of size 5 : you can change the region in the code below
		// System.out.println(task78_2());
		// Covering one's own dilate
		// System.out.println(task78_3(8,4));
		
		// Task 10 : Hexagonamino counting and exact cover tiling
		// System.out.println(task10());
		
		// Task 11 : Sudoku solving
		// System.out.println(task11());
		
	}
	
	@SuppressWarnings("unused")
	public static String task1() {
		Polyomino[] polys11 = Polyomino.importFile("polyminoesINF421.txt");
		Configuration config11 = new Configuration(polys11, false);
		Image2dViewer window11 = config11.createWindow();
		
		Polyomino P1 = polys11[1];
		Polyomino P1Rot = P1.rotation(1);
		Polyomino P1Sym = P1.symmetryY();
		Polyomino P1Dil = P1.dilatation(2);
		Polyomino[] polys12 = new Polyomino[4];
		polys12[0] = P1;
		polys12[1] = P1Rot;
		polys12[2] = P1Sym;
		polys12[3] = P1Dil;
		Configuration config12 = new Configuration(polys12,false);
		
		Image2dViewer window12 = config12.createWindow();
		
		return "Task 1 : Here's how we create and manipulate polyominoes.";
	}
	
	@SuppressWarnings("unused")
	public static String task2() {
		int n = 5;
		String s = "Task 2 : Here's how we generate all fixed and free polyominoes." + "\n" + "\n";
		
		long t1 = System.currentTimeMillis();
		LinkedList<Polyomino> polys21 = Polyomino.generateFixed(n);
		s += "[Naive method] There are " + polys21.size() + " fixed polyominoes of size " + n + ".\n";
		long t2 = System.currentTimeMillis();
		s += "Computation time : " + (int)(t2-t1) +" ms\n";
		
		t1 = System.currentTimeMillis();
		LinkedList<Polyomino> polys22 = Polyomino.generateFree(n);
		s += "[Naive method] There are " + polys22.size() + " free polyominoes of size " + n + ".\n";
		t2 = System.currentTimeMillis();
		s += "Computation time : " + (int)(t2-t1) +" ms\n";
		
		/*
		s += "\n";
		s += "Free polyominoes of size " + n + " : \n";
		s += "\n";
		for (Polyomino P2 : polys22) {
			s+= P2.displayTiles() + "\n" + "\n";
		}
		*/
		
		Polyomino[] polys22Array = polys22.toArray(new Polyomino[polys22.size()]);
		Configuration config22 = new Configuration(polys22Array, false);
		
		Image2dViewer window22 = config22.createWindow();
		
		return s;
	}
	
	@SuppressWarnings("unused")
	public static String task3Fixed() {
		String s = "";
		for (int m = 4; m <= 9; m++){
			s += "============" + "\n";
			
			long t1 = System.currentTimeMillis();
			LinkedList<Polyomino> polys311 = Polyomino.generateFixed(m);
			long t2 = System.currentTimeMillis();
			
			long t3 = System.currentTimeMillis();
			LinkedList<Polyomino> polys312 = RedelmeierGenerator.generateFixed(m);
			long t4 = System.currentTimeMillis();
			
			s += "Naive method for fixed " + m + "-polyomino generation : " + (int)(t2-t1) +" ms" + "\n";
			s += "Redelmeier's method for fixed " + m + "-polyomino generation : " + (int)(t4-t3) + " ms" + "\n";
		}
		return s;
	}
	
	@SuppressWarnings("unused")
	public static String task3Free() {
		String s = "";
		for (int m = 4; m <= 9; m++){
			s += "============" + "\n";
			
			long t1 = System.currentTimeMillis();
			LinkedList<Polyomino> polys311 = Polyomino.generateFree(m);
			long t2 = System.currentTimeMillis();
			
			long t3 = System.currentTimeMillis();
			LinkedList<Polyomino> polys312 = RedelmeierGenerator.generateFree(m);
			long t4 = System.currentTimeMillis();
			
			s += "Naive method for free " + m + "-polyomino generation : " + (int)(t2-t1) +" ms" + "\n";
			s += "Redelmeier's method for free" + m + "-polyomino generation : " + (int)(t4-t3) + " ms" + "\n";
		}
		return s;
	}
	
	public static String task3(){
		String s = "Task 3 : Here's the comparison between both generation methods\n" + "\n";
		s +=  task3Fixed() + "\n" + "\n" + task3Free();
		return s;
	}
	
	public static String task456() {
		String s = "Tasks 4-5-6 : Here's our implementation of ExactCover and DancingLinks" + "\n" + "\n";
		Integer[][] M41 = { { 0, 0, 1, 0, 1, 1, 0 }, { 1, 0, 0, 1, 0, 0, 1 }, { 0, 1, 1, 0, 0, 1, 0 },
				{ 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 1, 0, 1 } };
		
		s += "Exact covers of the given matrix : " + "\n";
		s += ExactCover.displayMatrix(M41);
		LinkedList<LinkedList<Integer[]>> partitions41 = ExactCover.exactCover(M41);
		s += "\n";
		s += ExactCover.displayExactCover(partitions41);
		
		int m = 9;
		Integer[][] M42 = ExactCover.subsets(m);
		
		s += "\n";
		s += "Exact covers of 1..." + m + " by all its subsets" + "\n" + "\n";

		long t5 = System.currentTimeMillis();
		ExactCover.exactCover(M42);
		long t6 = System.currentTimeMillis();
		
		long t7 = System.currentTimeMillis();
		LinkedList<LinkedList<Integer[]>> partitions42 = DancingLinks.exactCover(M42);
		long t8 = System.currentTimeMillis();
		
		s += "There are " + partitions42.size() + " partitions of the matrix M in total" + "\n";
		s += "Naive method for ExactCover : " + (int)(t6-t5) +" ms" + "\n";
		s += "Dancing Links for ExactCover : " + (int)(t8-t7) + " ms" + "\n";
		return s;
	}
	
	@SuppressWarnings("unused")
	public static String task78_1(int S, int length, int height) {
		String s = "";
		boolean isom = true;
		// rectangular region sxs, all fixed polyominoes of size s
		LinkedList<Polyomino> polys81 = RedelmeierGenerator.generateFixed(S);
		boolean[][] region81 = new boolean[length][height];
		for (int i = 0; i < region81.length; i++) {
			for (int j = 0; j < region81[0].length; j++) {
				region81[i][j] = true;
			}
		}
		Integer[][] M81 = Polyomino.toExactCover(region81, polys81, !isom);
		LinkedList<LinkedList<Integer[]>> partitions81 = DancingLinks.exactCover(M81);
		s += "We find the covers of a " + length + "x" + height + " rectangle by fixed polyominoes of size " + S + " : there are " + partitions81.size() + " of those" + "\n";
		LinkedList<Integer[]> partition81 = DancingLinks.findExactCover(M81);
		Polyomino[] partition81polys = Polyomino.fromExactCover(region81, partition81);
		Configuration config81 = new Configuration(partition81polys, true);
		Image2dViewer window81 = config81.createWindow();
		return s;
	}
	
	@SuppressWarnings("unused")
	public static String task78_2() {
		String s = "";
		
		boolean isom = true;
		
		Integer[][] funnyShape = {
				{1,1,0,0,0,0,0,0,0,0,0},
				{1,1,1,1,0,0,0,0,0,0,0},
				{1,1,1,1,1,1,0,0,0,0,0},
				{1,1,1,1,1,1,1,1,0,0,0},
				{1,1,1,1,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,1,1,1,1},
				{0,0,0,1,1,1,1,1,1,1,1},
				{0,0,0,0,0,1,1,1,1,1,1},
				{0,0,0,0,0,0,0,1,1,1,1},
				{0,0,0,0,0,0,0,0,0,1,1},
		};
		
		Integer[][] diamond = {
				{0,0,0,0,1,1,0,0,0,0},
				{0,0,0,1,1,1,1,0,0,0},
				{0,0,1,1,1,1,1,1,0,0},
				{0,1,1,1,1,1,1,1,1,0},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{0,1,1,1,1,1,1,1,1,0},
				{0,0,1,1,1,1,1,1,0,0},
				{0,0,0,1,1,1,1,0,0,0},
				{0,0,0,0,1,1,0,0,0,0},
		};
		
		Integer[][] triangle = {
				{0,0,0,0,1,1,0,0,0,0},
				{0,0,0,0,1,1,0,0,0,0},
				{0,0,0,1,1,1,1,0,0,0},
				{0,0,0,1,1,1,1,0,0,0},
				{0,0,1,1,1,1,1,1,0,0},
				{0,0,1,1,1,1,1,1,0,0},
				{0,1,1,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,1,1,0},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1}
		};
				
		// change the shape to switch the problem
		Integer[][] shape = triangle;
		
		boolean[][] region82 = new boolean[shape[0].length][shape.length];
		for (int i = 0; i < region82.length; i++) {
			for (int j = 0; j < region82[0].length; j++) {
				if (shape[region82[0].length - j - 1][i] == 1) {
					region82[i][j] = true;
				} else {
					region82[i][j] = false;
				}
			}
		}

		LinkedList<Polyomino> polys82 = Polyomino.generateFree(5);
		Integer[][] M82 = Polyomino.toExactCoverUnique(region82, polys82, isom);
		LinkedList<LinkedList<Integer[]>> partitions82 = DancingLinks.exactCover(M82);
		s += "We find the covers of this region by free polyominoes of size 5 :" + "\n";
		s += ExactCover.displayMatrix((Integer[][]) shape);
		s += "There are " + partitions82.size() + " such covers" + "\n";
		LinkedList<Integer[]> partition82 = DancingLinks.findExactCover(M82);
		Polyomino[] partition82polys = Polyomino.fromExactCoverUnique(region82, partition82);
		Configuration config82 = new Configuration(partition82polys, true);
		Image2dViewer window82 = config82.createWindow();
		return s;
	}
	
	@SuppressWarnings("unused")
	public static String task78_3(int n83, int k83) {
		boolean isom = true;
		String s = "";
		int j83 = 1;
		LinkedList<Polyomino> polys830 = Polyomino.generateFree(n83);
		LinkedList<Polyomino> polys83good = new LinkedList<Polyomino>();
		for (Polyomino base83 : polys830){
			LinkedList<Polyomino> polys83 = new LinkedList<Polyomino>();
			polys83.add(base83);
			Polyomino dilated83 = base83.dilatation(k83);
			boolean[][] region83 = dilated83.tiles;			
			Integer[][] M83 = Polyomino.toExactCover(region83, polys83, isom);
			LinkedList<LinkedList<Integer[]>> partitions83 = DancingLinks.exactCover(M83);
			if (partitions83.size() > 0){
				polys83good.add(base83);
			}
		}
		Configuration config83 = new Configuration(polys83good.toArray(new Polyomino[0]), 5);
		Image2dViewer window83 = config83.createWindow();
		s += "Among the " + polys830.size() + " polyominoes of size " + n83 + ", there are " + polys83good.size() + " that can cover their " + k83 + "-dilate" + "\n";
		return s;
	}
	
	public static String task78(){
		String s = "Tasks 7-8 : Here are various applications of ExactCover to the polyomino tiling problem" + "\n";
		s += "1) ";
		s += task78_1(5,5,5);
		s += "\n";
		s += "2) ";
		s += task78_2();
		s += "\n";
		s += "3) ";
		s += task78_3(8,4);
		return s;
	}

	public static String task10() {
		String s = "Task 10 : Here's how we handle Hexagonaminoes" + "\n" + "\n";
		/*
		 * boolean[][] tiles = {{true, false, false}, {true, true, false},
		 * {false, false, false}}, tiles2 = {{false, true, true}, {false, false,
		 * true}, {true, true, true}};
		 * 
		 * Hexagonamino[] hexTab = {new Hexagonamino(tiles), new
		 * Hexagonamino(tiles2)}; HexaConfiguration hexConf = new
		 * HexaConfiguration(hexTab, true); hexConf.createWindow();
		 */

		/*
		 * for(int k=3; k<5; k++) { LinkedList<Hexagonamino> hexalist =
		 * generateHexa(k, false); HexaConfiguration hexConf2 = new
		 * HexaConfiguration(hexalist.toArray(new Hexagonamino[0]), false);
		 * hexConf2.createWindow(); System.out.println(hexalist.size()); }
		 */
		
		boolean[][] region101;
		LinkedList<Hexagonamino> polys101;
		boolean isom = true;
		for (int S = 3; S < 7; S++){
			polys101 = Hexagonamino.generateHexa(S, isom);
			s += "There are " + polys101.size() + " hexagonaminoes of size " + S + "\n";
		}
		s += "\n";
		polys101 = Hexagonamino.generateHexa(4, isom);
		region101 = new boolean[7][4];
		for (int i = 0; i < region101.length; i++) {
			for (int j = 0; j < region101[0].length; j++) {
				region101[i][j] = true;
			}
		}

		Integer[][] M101 = Hexagonamino.toExactCoverUnique(region101, polys101, isom);
		LinkedList<LinkedList<Integer[]>> partitions101 = DancingLinks.exactCover(M101);
		s += "There are " + partitions101.size() + " ways to pave the 4*7 rectangle with free hexagonaminoes of size 4 using each one exactly once" + "\n";
		LinkedList<Integer[]> partition101 = DancingLinks.findExactCover(M101);
		Hexagonamino[] partition101polys = Hexagonamino.fromExactCoverUnique(region101, partition101);
		HexaConfiguration config101 = new HexaConfiguration(partition101polys, true);
		Image2dViewer window101 = config101.createWindow();
		return s;
	}

	
	public static String task11() {
		String s = "Task 11 : Here's how we solve a Sudoku with DLX" + "\n" + "\n";
		Integer[][] partial = { { 0, 0, 0, 0, 0, 0, 9, 0, 2 }, { 0, 0, 0, 1, 0, 5, 0, 4, 0 },
				{ 1, 0, 0, 0, 4, 0, 5, 6, 0 }, { 8, 0, 9, 0, 6, 0, 4, 2, 3 }, { 0, 2, 0, 8, 0, 3, 0, 9, 1 },
				{ 7, 0, 0, 9, 0, 0, 8, 5, 0 }, { 0, 6, 0, 0, 3, 0, 0, 0, 4 }, { 2, 0, 7, 4, 0, 9, 0, 0, 5 },
				{ 0, 0, 5, 0, 0, 6, 0, 7, 0 } };

		s += Sudoku.sudokuSolver(partial);
		return s;
	}

}