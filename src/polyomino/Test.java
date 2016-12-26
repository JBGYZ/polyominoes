package polyomino;

import java.util.LinkedList;
import java.util.Random;

public class Test {

	public static void main(String[] args) {
		
		ExtracteurImage ext = new ExtracteurImage();
		String imgName;
		String path = "/Users/Guillaume/Google Drive/Cours X/INF/INF421/ProjetINF/Dossier/";
		
		// Task 1 : Polyomino manipulation
		
		/*

		Polyomino[] polys11 = Polyomino.importFile("polyminoesINF421.txt");
		Configuration config11 = new Configuration(polys11, false);
		Image2dViewer window11 = config11.createWindow();
		imgName = "image11";
		ext.ecrire(window11.imgComponent,
				path + imgName + ".jpg");
		
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
		imgName = "image12";
		ext.ecrire(window12.imgComponent,
				path + imgName + ".jpg");
		
		*/
		
		// Task 2 : Polyomino generation

		/*
		int n = 5;
		
		long t1 = System.currentTimeMillis();
		LinkedList<Polyomino> polys22 = Polyomino.generateFree(n);
		long t2 = System.currentTimeMillis();
		System.out.println("Naive method for free polyomino generation : " + (int)(t2-t1) +" ms");
		
		LinkedList<Polyomino> polys21 = Polyomino.generateFixed(n);
		
		for (Polyomino P2 : polys22) {
			P2.displayTiles();
		}

		Polyomino[] polys22Array = polys22.toArray(new Polyomino[polys22.size()]);
		Configuration config22 = new Configuration(polys22Array, false);
		
		Image2dViewer window22 = config22.createWindow();
		imgName = "image22";
		ext.ecrire(window22.imgComponent,
				path + imgName + ".jpg");
		
		*/
		 
		// Task 3 : Redelmeier's method
		
		/*
		for (int m = 4; m <= 9; m++){
		
		long t1 = System.currentTimeMillis();
		LinkedList<Polyomino> polys311 = Polyomino.generateFixed(m);
		long t2 = System.currentTimeMillis();
		
		long t3 = System.currentTimeMillis();
		LinkedList<Polyomino> polys312 = RedelmeierGenerator.genererFixe(m);
		long t4 = System.currentTimeMillis();
		
		System.out.println("Naive method for fixed polyomino generation : " + (int)(t2-t1) +" ms");
		System.out.println("Redelmeier's method for fixed polyomino generation : " + (int)(t4-t3) + " ms");
		System.out.println();
		}
		*/
		
		// Task 4,5,6 : Exact Cover & Dancing Links
		
		/*
		
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
		
		*/
	
		// Task 7,8 : Conversion from polyomino tiling to ExactCover and various applications
		
		boolean isom = true;

		// rectangular region sxs, all fixed polyominoes of size s
		
		/*
		int s = 6;
		LinkedList<Polyomino> polys81 = Polyomino.generateFixed(s);
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
		imgName = "image81"+s;
		ext.ecrire(window81.imgComponent,
				path + imgName + ".jpg");
		*/

		// triangle region of area 60, all 12 free polyominoes of size 5

		/*
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
				
		boolean[][] region82 = new boolean[10][10];
		for (int i = 0; i < region82.length; i++) {
			for (int j = 0; j < region82[0].length; j++) {
				if (triangle[9-j][i] == 1) {
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
		imgName = "image82";
		ext.ecrire(window82.imgComponent,
				path + imgName + ".jpg");
				
		*/
		
		// covering one's own dilate
		
		/*
		int n83 = 8;
		int k83 = 4;
		int N83 = 0;
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
				N83 += 1;
				polys83good.add(base83);
			}
		}
		Configuration config83 = new Configuration(polys83good.toArray(new Polyomino[0]), false);
		Image2dViewer window83 = config83.createWindow();
		imgName = "image83";
		ext.ecrire(window83.imgComponent,
				path + imgName + ".jpg");
		System.out.println("Out of the " + polys830.size() + " free polyominoes of size " + n83 + ", only " + N83 + " can cover their own " + k83 + "-dilate.");
		
		*/
		
	}

}