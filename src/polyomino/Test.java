package polyomino;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		
		// Task 1 : Polyomino manipulation
		// task1();
		
		// Task 2 : Polyomino generation
		// task2();
		 
		// Task 3 : Redelmeier's method
		// task3Fixed();
		// task3Free();
		
		// Task 4,5,6 : Exact Cover & Dancing Links
		// task456();
		
		// Task 7,8 : Conversion from polyomino tiling to ExactCover and various applications
		// task78_1();

		// Various regions of area 60, all 12 free polyominoes of size 5
		// You can change the region in the code
		// task78_2();
		
		// Covering one's own dilate
		// task78_3();
		
		// Task 10 : Hexagonamino counting and exact cover tiling
		// 
		
		// Task 10 : Sudoku solving
		// sudoku();
		
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
		
		return "Je suis une chaîne \n de caractères";
	}
	
	@SuppressWarnings("unused")
	public static void task2() {
		int n = 5;
		
		long t1 = System.currentTimeMillis();
		LinkedList<Polyomino> polys21 = Polyomino.generateFixed(n);
		long t2 = System.currentTimeMillis();
		System.out.println("Naive method for fixed polyomino generation : " + (int)(t2-t1) +" ms");
		
		t1 = System.currentTimeMillis();
		LinkedList<Polyomino> polys22 = Polyomino.generateFree(n);
		t2 = System.currentTimeMillis();
		System.out.println("Naive method for free polyomino generation : " + (int)(t2-t1) +" ms");
		
		for (Polyomino P2 : polys22) {
			P2.displayTiles();
		}
		
		System.out.println("Displaying free polyominoes...");
		Polyomino[] polys22Array = polys22.toArray(new Polyomino[polys22.size()]);
		Configuration config22 = new Configuration(polys22Array, false);
		
		Image2dViewer window22 = config22.createWindow();
	}
	
	@SuppressWarnings("unused")
	public static void task3Fixed() {
		for (int m = 4; m <= 9; m++){
			System.out.println("============");
			
			long t1 = System.currentTimeMillis();
			LinkedList<Polyomino> polys311 = Polyomino.generateFixed(m);
			long t2 = System.currentTimeMillis();
			
			long t3 = System.currentTimeMillis();
			LinkedList<Polyomino> polys312 = RedelmeierGenerator.generateFixed(m);
			long t4 = System.currentTimeMillis();
			
			System.out.println("Naive method for fixed polyomino generation : " + (int)(t2-t1) +" ms");
			System.out.println("Redelmeier's method for fixed polyomino generation : " + (int)(t4-t3) + " ms");
			System.out.println();
		}
	}
	
	@SuppressWarnings("unused")
	public static void task3Free() {
		for (int m = 4; m <= 9; m++){
			System.out.println("============");
			
			long t1 = System.currentTimeMillis();
			LinkedList<Polyomino> polys311 = Polyomino.generateFree(m);
			long t2 = System.currentTimeMillis();
			
			long t3 = System.currentTimeMillis();
			LinkedList<Polyomino> polys312 = RedelmeierGenerator.generateFree(m);
			long t4 = System.currentTimeMillis();
			
			System.out.println("Naive method for free polyomino generation : " + (int)(t2-t1) +" ms");
			System.out.println("Redelmeier's method for free polyomino generation : " + (int)(t4-t3) + " ms");
			System.out.println();
		}
	}
	
	public static void task456() {
		Integer[][] M41 = { { 0, 0, 1, 0, 1, 1, 0 }, { 1, 0, 0, 1, 0, 0, 1 }, { 0, 1, 1, 0, 0, 1, 0 },
				{ 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 1, 0, 1 } };
		
		System.out.println("Exact covers of the given example : ");
		ExactCover.displayMatrix(M41);
		LinkedList<LinkedList<Integer[]>> partitions41 = ExactCover.exactCover(M41);
		ExactCover.displayExactCover(partitions41);
		
		int m = 9;
		Integer[][] M42 = ExactCover.subsets(m);
		
		System.out.println("Exact covers of 1..." + m + " by all its subsets");

		long t5 = System.currentTimeMillis();
		//ExactCover.exactCover(M42);
		long t6 = System.currentTimeMillis();
		
		long t7 = System.currentTimeMillis();
		DancingLinks.exactCover(M42);
		long t8 = System.currentTimeMillis();
		
		System.out.println("Naive method for ExactCover : " + (int)(t6-t5) +" ms");
		System.out.println("Dancing Links for ExactCover : " + (int)(t8-t7) + " ms");
		System.out.println();
	}
	
	@SuppressWarnings("unused")
	public static void task78_1() {
		boolean isom = true;
		// rectangular region sxs, all fixed polyominoes of size s
		int s = 5;
		LinkedList<Polyomino> polys81 = RedelmeierGenerator.generateFixed(s);
		boolean[][] region81 = new boolean[s][s];
		for (int i = 0; i < region81.length; i++) {
			for (int j = 0; j < region81[0].length; j++) {
				region81[i][j] = true;
			}
		}
		Integer[][] M81 = Polyomino.toExactCover(region81, polys81, !isom);
		LinkedList<Integer[]> partition81 = DancingLinks.findExactCover(M81);
		Polyomino[] partition81polys = Polyomino.fromExactCover(region81, partition81);
		Configuration config81 = new Configuration(partition81polys, true);
		Image2dViewer window81 = config81.createWindow();
	}
	
	@SuppressWarnings("unused")
	public static void task78_2() {
		boolean isom = true;
		
		int[][] funnyShape = {
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
		
		int[][] diamond = {
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
		
		int[][] triangle = {
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
		int[][] shape = triangle;
		
		boolean[][] region82 = new boolean[shape[0].length][shape.length];
		for (int i = 0; i < region82.length; i++) {
			for (int j = 0; j < region82[0].length; j++) {
				if (shape[j][region82.length - 1 - i] == 1) {
					region82[i][j] = true;
				} else {
					region82[i][j] = false;
				}
			}
		}

		LinkedList<Polyomino> polys82 = Polyomino.generateFree(5);
		Integer[][] M82 = Polyomino.toExactCoverUnique(region82, polys82, isom);

		LinkedList<Integer[]> partition82 = DancingLinks.findExactCover(M82);
		Polyomino[] partition82polys = Polyomino.fromExactCoverUnique(region82, partition82);
		Configuration config82 = new Configuration(partition82polys, true);
		Image2dViewer window82 = config82.createWindow();
	}
	
	@SuppressWarnings("unused")
	public static void task78_3() {
		boolean isom = true;
		
		int n83 = 8;
		int k83 = 4;
		int j83 = 1;
		LinkedList<Polyomino> polys830 = Polyomino.generateFree(n83);
		LinkedList<Polyomino> polys83good = new LinkedList<Polyomino>();
		for (Polyomino base83 : polys830){
			System.out.println(j83++);
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
	}
	
	public static void sudoku() {
		Integer[][] partial = { { 0, 0, 0, 0, 0, 0, 9, 0, 2 }, { 0, 0, 0, 1, 0, 5, 0, 4, 0 },
				{ 1, 0, 0, 0, 4, 0, 5, 6, 0 }, { 8, 0, 9, 0, 6, 0, 4, 2, 3 }, { 0, 2, 0, 8, 0, 3, 0, 9, 1 },
				{ 7, 0, 0, 9, 0, 0, 8, 5, 0 }, { 0, 6, 0, 0, 3, 0, 0, 0, 4 }, { 2, 0, 7, 4, 0, 9, 0, 0, 5 },
				{ 0, 0, 5, 0, 0, 6, 0, 7, 0 } };

		Sudoku.sudokuSolver(partial);
	}

}