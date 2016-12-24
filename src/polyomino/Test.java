package polyomino;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {
		
		ExtracteurImage ext = new ExtracteurImage();
		String imgName;
		String path = "/Users/Guillaume/Google Drive/Cours X/INF/INF421/ProjetINF/Images/";
		
		// Task 1 : Polyomino manipulation
		
		/*

		Polyomino[] polys11 = Polyomino.importFile("polyminoesINF421.txt");
		Configuration config11 = new Configuration(polys11, false);
		Image2dViewer window11 = Polyomino.creerFenetre(config11);
		imgName = "image11";
		ext.ecrire(window11.imgComponent,
				path + imgName + ".jpg");
		
		Polyomino P1 = polys11[1];
		Polyomino P1Rot = P1.rotation(1);
		Polyomino P1Sym = P1.symetrieY();
		Polyomino P1Dil = P1.dilatation(2);
		Polyomino[] polys12 = new Polyomino[4];
		polys12[0] = P1;
		polys12[1] = P1Rot;
		polys12[2] = P1Sym;
		polys12[3] = P1Dil;
		Configuration config12 = new Configuration(polys12,false);
		
		Image2dViewer window12 = Polyomino.creerFenetre(config12);
		imgName = "image12";
		ext.ecrire(window12.imgComponent,
				path + imgName + ".jpg");
		
		*/
		
		// Task 2 : Polyomino generation
		
		/*

		int n = 5;
		
		// long t1 = System.currentTimeMillis();
		LinkedList<Polyomino> polys21 = Polyomino.genererFixes(n);
		// long t2 = System.currentTimeMillis();
		// System.out.println(t2-t1+"operations");
		
		LinkedList<Polyomino> polys22 = Polyomino.genererLibres(n);
		
		for (Polyomino P2 : polys22) {
			System.out.println();
			P2.afficheConsole();
		}
		

		Polyomino[] polys22Array = polys22.toArray(new Polyomino[polys22.size()]);
		Configuration config22 = new Configuration(polys22Array, false);
		
		Image2dViewer window22 = Polyomino.creerFenetre(config22);
		imgName = "image22";
		ext.ecrire(window22.imgComponent,
				path + imgName + ".jpg");
		
		*/
		 
		// Task 3 : Redelmeier's method
		
		int m = 9;
		
		long t1 = System.currentTimeMillis();
		LinkedList<Polyomino> polys311 = Polyomino.genererFixes(m);
		long t2 = System.currentTimeMillis();
		
		long t3 = System.currentTimeMillis();
		LinkedList<Polyomino> polys312 = RedelmeierGenerator.genererFixe(m);
		long t4 = System.currentTimeMillis();
		
		System.out.println("Naive method for polyomino generation : " + (int)(t2-t1) +" ms");
		System.out.println("Redelmeier's method for polyomino generation : " + (int)(t4-t3) + " ms");
		System.out.println();
		
		// Task 4,5,6 : Exact Cover & Dancing Links
		
		/*
		
		Integer[][] M = { { 0, 0, 1, 0, 1, 1, 0 }, { 1, 0, 0, 1, 0, 0, 1 }, { 0, 1, 1, 0, 0, 1, 0 },
				{ 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 1, 0, 1 } };
		
		ExactCover.displayExactCover(M);
		DancingLinks.displayExactCover(M);
		
		
		int m = 9;
		
		Integer[][] M2 = ExactCover.subsets(9);

		long t5 = System.currentTimeMillis();
		int cardC = M2.length;
		int cardX = M2[0].length;
		LinkedList<Integer> X = new LinkedList<Integer>();
		LinkedList<Integer> C = new LinkedList<Integer>();
		for (int x = 0; x < cardX; x++) {
			X.add(x);
		}
		for (int c = 0; c < cardC; c++) {
			C.add(c);
		}
		ExactCover.exactCover(M2,X,C);
		long t6 = System.currentTimeMillis();
		
		
		long t7 = System.currentTimeMillis();
		DancingLinks D = new DancingLinks(M2);
		DancingLinks.exactCover(D.H);
		long t8 = System.currentTimeMillis();
		
		System.out.println("We look for the exact covers of 1..." + m + " by all possible subsets");
		System.out.println("Naive method for ExactCover : " + (int)(t6-t5) +" ms");
		System.out.println("Dancing Links for ExactCover : " + (int)(t8-t7) + " ms");

		*/
	
		// Task 7,8 : Conversion from polyomino tiling to ExactCover and various tests
		
		/*

		// rectangular region, all fixed polyominoes

		int taille = 5;
		LinkedList<Polyomino> matos = Polyomino.genererFixes(taille);

		int s = 5;
		boolean[][] region = new boolean[s][s];
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				region[i][j] = true;
			}
		}

		// custom region, only one type of fixed polyominoes

		boolean[][] tuiles = { { true, true } };
		Polyomino base = new Polyomino(tuiles);
		LinkedList<Polyomino> matos = Polyomino.copains(base);

		int[][] diamond = { { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
				{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 }, { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, { 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, };
		boolean[][] region = new boolean[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (diamond[i][j] == 1) {
					region[i][j] = true;
				} else {
					region[i][j] = false;
				}
			}
		}

		Integer[][] M3 = Polyomino.toExactCover(region, matos);
		DancingLinks.displayExactCover(M3);

		LinkedList<Integer[]> partition = DancingLinks.findExactCover(M3);
		Polyomino[] polys = Polyomino.fromExactCover(region, partition);

		Polyomino.creerFenetre(new Configuration(polys, true));

		// Test ExactCoverUnique

		LinkedList<Polyomino> matos = Polyomino.genererLibres(5);

		boolean[][] region = new boolean[20][3];
		for (int i = 0; i < region.length; i++) {
			for (int j = 0; j < region[0].length; j++) {
				region[i][j] = true;
			}
		}

		int[][] diamond = { { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 },
				{ 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 }, { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, { 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 }, };
		boolean[][] region = new boolean[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (diamond[i][j] == 1) {
					region[i][j] = true;
				} else {
					region[i][j] = false;
				}
			}
		}

		Integer[][] M3 = Polyomino.toExactCoverUnique(region, matos);
		DancingLinks.displayExactCover(M3);

		LinkedList<Integer[]> partition = DancingLinks.findExactCover(M3);
		Polyomino[] polys = Polyomino.fromExactCoverUnique(region, partition);

		Image2dViewer fenetre = Polyomino.creerFenetre(new Configuration(polys, true));
		ExtracteurImage ext = new ExtracteurImage();
		String imgName = "pavage";
		ext.ecrire(fenetre.imgComponent,
				path + imgName + ".jpg");
	*/
	}

}