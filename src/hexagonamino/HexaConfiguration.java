package hexagonamino;

import java.awt.Color;
import java.util.Random;

import polyomino.Image2d;
import polyomino.Image2dViewer;

public class HexaConfiguration {

	// A configurations is a set of parameters which are given to the display
	// functions in order to make visualization easier. It tells us what
	// polyominoes to display and where to put every one of them.

	public Hexagonamino[] hexagonaminoes;
	public HexaPoint[] bottomLeft;
	public int xmax;
	public int ymax;
	public int sizeTiles;
	public int width;

	// Creates a configuration with all the parameters required

	public HexaConfiguration(Hexagonamino[] hexagonaminoes, HexaPoint[] bottomLeft, int xmax, int ymax, int sizeTiles, int width) {
		this.hexagonaminoes = hexagonaminoes;
		this.bottomLeft = bottomLeft;
		this.xmax = xmax;
		this.ymax = ymax;
		this.sizeTiles = sizeTiles;
		this.width = width;
	}

	// Creates a configuration from a list of polyominoes, allowing or not
	// superposition

	public HexaConfiguration(Hexagonamino[] hexagonaminoes, boolean superposition) {
		this.hexagonaminoes = hexagonaminoes;
		int N = this.hexagonaminoes.length;
		this.bottomLeft = new HexaPoint[N];
		for (int i = 0; i < N; i++) {
			this.bottomLeft[i] = new HexaPoint();
		}
		int x = 0;
		this.ymax = 0;
		for (int i = 0; i < N; i++) {
			Hexagonamino p = this.hexagonaminoes[i];
			this.bottomLeft[i].x = (int) (x + 0.7*Math.sin(Math.PI/3) * p.height);
			this.bottomLeft[i].y = 0;
			if (!superposition) {
				x += p.width + Math.sin(Math.PI/3) * p.height + 1;
			}
			this.ymax = Math.max(this.ymax, p.height);
		}
		this.ymax = (int) (1.5*ymax);
		
		if (superposition) {
			this.xmax = hexagonaminoes[0].width;
		} else {
			this.xmax = x;
		}
		int screenSize = 13;
		int factor = 130;
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
		for (int i = 0; i < this.hexagonaminoes.length; i++) {
			Hexagonamino P = this.hexagonaminoes[i];
			int xmin = this.bottomLeft[i].x;
			int ymin = this.bottomLeft[i].y;
			Color col = colors[i % colors.length];
			Color mix = null;
			//Color col = generateRandomColor(mix);
			P.addPolygonAndEdges(img, this.width, col, this.sizeTiles, xmin, ymin, this.ymax);
		}
		Image2dViewer window = new Image2dViewer(img);
		window.setSize(this.sizeTiles * this.xmax, this.sizeTiles * this.ymax + 23);
		window.setLocationRelativeTo(null);
		return window;
	}
}
