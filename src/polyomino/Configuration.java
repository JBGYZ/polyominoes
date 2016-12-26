package polyomino;

import java.awt.Color;
import java.util.Random;

public class Configuration {

	// A configurations is a set of parameters which are given to the display
	// functions in order to make visualization easier. It tells us what
	// polyominoes to display and where to put every one of them.

	public Polyomino[] polyominoes;
	public Point[] bottomLeft;
	public int xmax;
	public int ymax;
	public int sizeTiles;
	public int width;

	// Creates a configuration with all the parameters required

	public Configuration(Polyomino[] polyominoes, Point[] bottomLeft, int xmax, int ymax, int sizeTiles, int width) {
		this.polyominoes = polyominoes;
		this.bottomLeft = bottomLeft;
		this.xmax = xmax;
		this.ymax = ymax;
		this.sizeTiles = sizeTiles;
		this.width = width;
	}

	// Creates a configuration from a list of polyominoes, allowing or not
	// superposition

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
				x += p.width + 1;
			}
			this.ymax = Math.max(this.ymax, p.height);
		}
		if (superposition) {
			this.xmax = polyominoes[0].width;
		} else {
			this.xmax = x;
		}
		int screenSize = 13;
		int factor = 50;
		if (superposition) {
			this.sizeTiles = screenSize * factor / (Math.max(this.xmax, this.ymax));
		} else {
			this.sizeTiles = screenSize * factor / (Math.max(this.xmax, this.ymax));
		}
		this.width = this.sizeTiles / 10;
	}

	// Generates a random mixed color

	public static Color generateRandomColor(Color mix) {
		Random random = new Random();
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);
		if (mix != null) {
			red = (red + mix.getRed()) / 2;
			green = (green + mix.getGreen()) / 2;
			blue = (blue + mix.getBlue()) / 2;
		}
		Color color = new Color(red, green, blue);
		return color;
	}

	// Creates a window to visualize a configuration

	public Image2dViewer createWindow() {
		Color[] colors = { Color.red, Color.yellow, Color.green, Color.blue, Color.gray, Color.cyan, Color.magenta,
				Color.orange, Color.lightGray };
		Image2d img = new Image2d(this.xmax * this.sizeTiles, this.ymax * this.sizeTiles);
		for (int i = 0; i < this.polyominoes.length; i++) {
			Polyomino P = this.polyominoes[i];
			int xmin = this.bottomLeft[i].x;
			int ymin = this.bottomLeft[i].y;
			Color col = colors[i % colors.length];
			// Color mix = new Color(100, 100, 150);
			// Color col = generateRandomColor(mix);
			P.addPolygonAndEdges(img, this.width, col, this.sizeTiles, xmin, ymin, this.ymax);
		}
		Image2dViewer window = new Image2dViewer(img);
		window.setSize(this.sizeTiles * this.xmax, this.sizeTiles * this.ymax + 23);
		window.setLocationRelativeTo(null);
		return window;
	}
}
