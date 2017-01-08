package polyomino;

import java.util.LinkedList;
import java.util.Random;

class Data {

	public Data U, D, L, R;
	public int i, j;
	public Column C;

	public Data() {
		U = null;
		D = null;
		L = null;
		R = null;
		C = null;
		i = -1;
		j = -1;
	}

	// Returns the list describing the elements covered by the subset to which the Data this belongs
	
	public LinkedList<Integer> toList() {
		LinkedList<Integer> setList = new LinkedList<Integer>();
		setList.add(this.C.N);
		for (Data t = this.R; !(t == this); t = t.R) {
			setList.add(t.C.N);
		}
		return setList;
	}
	
	// Returns the binary array describing the elements covered by the subset to which the Data this belongs

	public Integer[] toArray(int cardX) {
		LinkedList<Integer> setList = new LinkedList<Integer>();
		setList.add(this.C.N);
		for (Data t = this.R; !(t == this); t = t.R) {
			setList.add(t.C.N);
		}
		Integer[] setArray = new Integer[cardX];
		for (int i = 0; i < cardX; i++) {
			setArray[i] = 0;
		}
		for (int i : setList) {
			setArray[i] = 1;
		}
		return setArray;
	}

}

class Column extends Data {
	public int S;
	public int N;

	public Column() {
		U = null;
		D = null;
		L = null;
		R = null;
		C = null;
		i = -1;
		j = -1;
		S = 0;
		N = -1;
	}
}

public class DancingLinks {

	public Column H;

	// Generates a DancingLinks data structure from the matrix of an ExactCover problem

	public DancingLinks(Integer[][] M) {
		int cardC = M.length;
		int cardX = M[0].length;
		// we create a data matrix containing :
		// - in the first line, the column objects
		// - in the following line one data object for each coefficient of M, zero or 1 alike
		Data[][] dataMatrix = new Data[cardC + 1][cardX];
		for (int j = 0; j < cardX; j++) {
			Column c = new Column();
			c.i = 0;
			c.j = j;
			c.S = 0;
			c.N = j;
			c.C = c;
			dataMatrix[0][j] = c;
			for (int i = 1; i < cardC + 1; i++) {
				Data d = new Data();
				d.i = i;
				d.j = j;
				d.C = c;
				dataMatrix[i][j] = d;
			}
		}
		// we fill out every single link in the grid (up, down, left and right for every data)
		for (int j = 0; j < cardX; j++) {
			for (int i = 0; i < cardC + 1; i++) {
				Data d0 = dataMatrix[i][j];
				// left
				Data d1 = dataMatrix[i][Math.floorMod(j - 1, cardX)];
				d0.L = d1;
				d1.R = d0;
				// right
				Data d2 = dataMatrix[i][Math.floorMod(j + 1, cardX)];
				d0.R = d2;
				d2.L = d0;
				// up
				Data d3 = dataMatrix[Math.floorMod(i - 1, cardC + 1)][j];
				d0.U = d3;
				d3.D = d0;
				// down
				Data d4 = dataMatrix[Math.floorMod(i + 1, cardC + 1)][j];
				d0.D = d4;
				d4.U = d0;
			}
		}
		// we delete the data corresponding to zero coefficients of M, and at the same time we update the column sums
		for (int j = 0; j < cardX; j++) {
			for (int i = 1; i < cardC + 1; i++) {
				if (M[i - 1][j] == 1) {
					Column c = (Column) dataMatrix[0][j];
					c.S++;
				} else {
					Data d = dataMatrix[i][j];
					d.U.D = d.D;
					d.D.U = d.U;
					d.R.L = d.L;
					d.L.R = d.R;
				}
			}
		}
		// we create the root
		this.H = new Column();
		this.H.R = dataMatrix[0][0];
		this.H.L = dataMatrix[0][cardX - 1];
		this.H.L.R = this.H;
		this.H.R.L = this.H;
	}

	public static void coverColumn(Data x) {
		x.R.L = x.L;
		x.L.R = x.R;
		for (Data t = x.D; !(t == x); t = t.D) {
			for (Data y = t.R; !(y == t); y = y.R) {
				y.D.U = y.U;
				y.U.D = y.D;
				y.C.S--;
			}
		}
	}

	public static void uncoverColumn(Data x) {
		for (Data t = x.U; !(t == x); t = t.U) {
			for (Data y = t.L; !(y == t); y = y.L) {
				y.D.U = y;
				y.U.D = y;
				y.C.S++;
			}
		}
		x.R.L = x;
		x.L.R = x;
	}

	// Finds the least frequently covered element

	public static Data leastFrequent(Data H) {
		int m = Integer.MAX_VALUE;
		Data xTmp = null;
		for (Data x = H.R; !(x == H); x = x.R) {
			if (x.C.S < m) {
				xTmp = x;
				m = x.C.S;
			}
		}
		if (m == Integer.MAX_VALUE) {
			return H.R;
		}
		return xTmp;
	}

	// Returns the solution to the ExactCover problem as a list of partitions
	// Every partition is represented as a list of data objets, each data object being located on the line of a subset that belons to the partition
	// To get that subset, just apply toList or toArray to the data object
	
	public static LinkedList<LinkedList<Data>> exactCoverData(Data H) {
		if (H.R == H) {
			// it means there is no element to cover, we just return an empty partition
			LinkedList<LinkedList<Data>> partitions = new LinkedList<LinkedList<Data>>();
			LinkedList<Data> P = new LinkedList<Data>();
			partitions.add(P);
			return partitions;
		}
		LinkedList<LinkedList<Data>> partitions = new LinkedList<LinkedList<Data>>();
		Data x = leastFrequent(H); // first element to cover
		coverColumn(x.C);
		for (Data t = x.U; !(t == x); t = t.U) {
			for (Data y = t.L; !(y == t); y = y.L) {
				coverColumn(y.C);
			}
			for (LinkedList<Data> P : exactCoverData(H)) {
				P.add(t);
				partitions.add(P);
			}
			for (Data y = t.R; !(y == t); y = y.R) {
				uncoverColumn(y.C);
			}
		}
		uncoverColumn(x.C);
		/*
		int s = 0;
		for (LinkedList<Data> P : partitions){
			s = Math.max(s, P.size());
		}
		if (s > 0 && (s%10==0)){
			System.out.println("Taille maximum d'une partition : " + s);
		}
		*/
		return partitions;
	}
	
	
	// Returns the solution to the ExactCover problem as a list of partitions, each partitions being a list of binary arrays
	
	public static LinkedList<LinkedList<Integer[]>> exactCover(Integer[][] M){
		System.out.print("We search for the exact covers of M with the DancingLinks implementation of algorithm X : ");
		int cardX = M[0].length;
		DancingLinks D = new DancingLinks(M);
		LinkedList<LinkedList<Data>> partitionsData = exactCoverData(D.H);
		LinkedList<LinkedList<Integer[]>> partitions = new LinkedList<LinkedList<Integer[]>>();
		for (LinkedList<Data> Pdata : partitionsData){
			LinkedList<Integer[]> P = new LinkedList<Integer[]>();
			for (Data t : Pdata){
				P.add(t.toArray(cardX));
			}
			partitions.add(P);
		}
		System.out.println("done");
		System.out.println("There are " + partitions.size() + " partitions of M in total.");
		return partitions;
	}
	
	// Returns a random partition, for graphic displaying

	public static LinkedList<Integer[]> findExactCover(Integer[][] M) {
		LinkedList<LinkedList<Integer[]>> partitions = exactCover(M);
		if (partitions.size() == 0){
			throw new Error("There is no partition");
		}
		Random randomGenerator = new Random();
		int k = randomGenerator.nextInt(partitions.size());
		LinkedList<Integer[]> P = partitions.get(k);
		return P;
	}
	
	

}
