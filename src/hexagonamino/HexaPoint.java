package hexagonamino;

import java.util.LinkedList;

public class HexaPoint {
	
	public int x, y;

	public HexaPoint() {
		this.x = 0;
		this.y = 0;
	}

	public HexaPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getx() {
		return this.x;
	}

	public int gety() {
		return this.y;
	}

	public boolean equals(HexaPoint P) {
		return (P.x == this.x && P.y == this.y);
	}

	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}
	
	public LinkedList<HexaPoint> neighbors() {
		LinkedList<HexaPoint> nei = new LinkedList<HexaPoint>();
		nei.add(new HexaPoint(this.x, this.y + 1));
		nei.add(new HexaPoint(this.x - 1, this.y + 1));
		nei.add(new HexaPoint(this.x - 1, this.y));
		nei.add(new HexaPoint(this.x, this.y - 1));
		nei.add(new HexaPoint(this.x + 1, this.y - 1));
		nei.add(new HexaPoint(this.x + 1, this.y));
		
		
		
		
		return nei;
	}
	
	public boolean onBoard(int N){
		return (this.x >= 0 && this.y >= 0 && this.x < N && this.y < N);
	}
	
	public boolean onBoard(int Nx, int Ny){
		return (this.x >= 0 && this.y >= 0 && this.x < Nx && this.y < Ny);
	}
}