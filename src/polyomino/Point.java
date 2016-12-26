package polyomino;

import java.util.LinkedList;

public class Point {
	public int x, y;

	public Point() {
		this.x = 0;
		this.y = 0;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getx() {
		return this.x;
	}

	public int gety() {
		return this.y;
	}

	public boolean equals(Point P) {
		return (P.x == this.x && P.y == this.y);
	}

	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	public LinkedList<Point> neighbors() {
		LinkedList<Point> nei = new LinkedList<Point>();
		nei.add(new Point(this.x + 1, this.y));
		nei.add(new Point(this.x - 1, this.y));
		nei.add(new Point(this.x, this.y + 1));
		nei.add(new Point(this.x, this.y - 1));
		return nei;
	}
}