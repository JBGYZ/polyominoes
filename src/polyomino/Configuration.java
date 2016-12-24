package polyomino;

public class Configuration {

	public Polyomino[] polyominoes;
	public Point[] bottomLeft;
	public int xmax;
	public int ymax;
	public int tailleTuiles;
	public int width;

	public Configuration(Polyomino[] polyominoes, Point[] bottomLeft, int xmax, int ymax, int tailleTuiles, int width) {
		this.polyominoes = polyominoes;
		this.bottomLeft = bottomLeft;
		this.xmax = xmax;
		this.ymax = ymax;
		this.tailleTuiles = tailleTuiles;
		this.width = width;
	}

	public Configuration(Polyomino[] polyominoes, boolean superposition) {
		this.polyominoes = polyominoes;
		int N = this.polyominoes.length;
		this.bottomLeft = new Point[N];
		for (int i = 0; i < N; i++) {
			this.bottomLeft[i] = new Point();
		}
		int x = 0;
		this.ymax = 0;
		for (int i = 0; i < N; i++) {
			Polyomino p = this.polyominoes[i];
			this.bottomLeft[i].x = x;
			this.bottomLeft[i].y = 0;
			if (!superposition) {
				x += p.largeur + 1;
			}
			this.ymax = Math.max(this.ymax, p.hauteur);
		}
		if (superposition) {
			this.xmax = polyominoes[0].largeur;
		} else {
			this.xmax = x;
		}
		int tailleEcran = 13;
		int facteur = 100;
		if (superposition) {
			this.tailleTuiles = tailleEcran * facteur / (Math.max(this.xmax, this.ymax));
		} else {
			this.tailleTuiles = tailleEcran * facteur / (Math.max(this.xmax, this.ymax));
		}
		this.width = this.tailleTuiles / 10;
	}
}
