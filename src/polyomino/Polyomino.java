package polyomino;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Polyomino {
	public boolean[][] tuiles;
	
	public Polyomino(boolean[][] tuiles) {
		this.tuiles = tuiles;
	}
	
	public Polyomino(String s) {
		//Ex : [(0,0), (0,4), (1,0), (1,1), (1,2), (1,3), (1,4), (2,0), (2,4)]
		//Parsing de la chaîne en LinkedList
		LinkedList<Integer[]> tuilesList = new LinkedList<Integer[]>();
		
		int k=1; //Indice courant dans la chaîne : on commence à la première parenthèse
		while(k<s.length()-1) {
			//On commence chaque boucle à un début de parenthèses
			k++;
			int x=0, y=0;
			while(s.charAt(k) != ',') {
				x = x*10+Integer.parseInt(Character.toString(s.charAt(k)));
				k++;
			}
			k++; //On passe la virgule de (_,_)
			while(s.charAt(k) != ')') {
				y = y*10+Integer.parseInt(Character.toString(s.charAt(k)));
				k++;
			}
			Integer[] tuile = {x,y};
			tuilesList.add(tuile);
			//On regarde si on est arrivés à la fin
			k++;
			if(s.charAt(k) == ',') k+=2;
			else break;
		}
		
		//Transformation de la liste en boolean[][]
		int xmax = 0, ymax = 0;
		for(Integer[] tuile : tuilesList) {
			xmax = Math.max(xmax, tuile[0]);
			ymax = Math.max(ymax, tuile[1]);
		}
		boolean[][] tableauTuiles = new boolean[xmax+1][ymax+1];
		for(Integer[] tuile : tuilesList) {
			tableauTuiles[tuile[0]][tuile[1]] = true;
		}
		this.tuiles = tableauTuiles;
	}
	
	public static Polyomino[] importer(String fichier) {
		LinkedList<Polyomino> liste = new LinkedList<Polyomino>();
		try{
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(fichier)));
			String ligne;
			while ((ligne=buffer.readLine())!=null){
				liste.add(new Polyomino(ligne));
			}
			buffer.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		return liste.toArray(new Polyomino[0]);
	}
	
	public void addPolygonAndEdges(Image2d img, int width, Color color, int tailleTuiles, int xmax, int ymax) {
		// Ajoute les Edge et les carrés de la tuile dans l'image img
		for(int i=0; i<tuiles.length; i++) {
			for(int j=0; j<tuiles[i].length; j++) {
				if(tuiles[i][j]) {
					int[] xcoords = {i*tailleTuiles,
							i*tailleTuiles,
							(i+1)*tailleTuiles,
							(i+1)*tailleTuiles},
						ycoords = {(ymax-j)*tailleTuiles,
							(ymax-(j+1))*tailleTuiles,
							(ymax-(j+1))*tailleTuiles,
							(ymax-j)*tailleTuiles};
						img.addPolygon(xcoords, ycoords, color);
					if(i==0 || !tuiles[i-1][j]) {
						System.out.println("a");
						img.addEdge(i*tailleTuiles,(ymax-j)*tailleTuiles,i*tailleTuiles,(ymax-(j+1))*tailleTuiles,width);
					}
					if(j==0 || !tuiles[i][j-1]) {
						System.out.println("b");
						img.addEdge(i*tailleTuiles,(ymax-j)*tailleTuiles,(i+1)*tailleTuiles,(ymax-j)*tailleTuiles, width);
					}
					if(i==tuiles.length-1 || !tuiles[i+1][j]) {
						System.out.println("c");
						img.addEdge((i+1)*tailleTuiles,(ymax-j)*tailleTuiles,(i+1)*tailleTuiles,(ymax-(j+1))*tailleTuiles,width);
					}
					if(j==tuiles[i].length-1 || !tuiles[i][j+1]) {
						System.out.println("d");
						img.addEdge(i*tailleTuiles,(ymax-(j+1))*tailleTuiles,(i+1)*tailleTuiles,(ymax-(j+1))*tailleTuiles,width);
					}
				}
			}
		}
	}
	
	public Polyomino translate(int dx, int dy) {
		
		return null;
	}
	
	public void afficheConsole() {
		for(boolean[] bTab : tuiles) {
			for(boolean b : bTab) {
				System.out.print(b ? "O" : " ");
			}
			System.out.println();
		}
	}
	
	public static void creerFenetre(Polyomino[] polyominoes, int tailleTuiles, int width) {
		// On détermine la taille de l'image
		Color[] colors = {Color.red, Color.yellow, Color.green, Color.blue, Color.gray, Color.cyan, Color.magenta, Color.orange, Color.lightGray};
		int xmax = 0, ymax = 0;
		for(Polyomino p : polyominoes) {
			xmax = Math.max(xmax, p.tuiles.length);
			ymax = Math.max(ymax, p.tuiles[0].length);
		}
		Image2d img = new Image2d(xmax*tailleTuiles, ymax*tailleTuiles);
		int k=0;
		for(Polyomino p : polyominoes) {
			p.addPolygonAndEdges(img, width, colors[k%colors.length], tailleTuiles, xmax, ymax);
			k++;
		}
		
		@SuppressWarnings("unused")
		Image2dViewer fenetre = new Image2dViewer(img);
	}
	
	public static void main(String[] args) {
		String chemin_fichier = "C:/Users/Clement/Documents/Polytechnique/INF421 (Algorithmique)/polyominoesINF421.txt";
		Polyomino[] tab = importer(chemin_fichier);
		for(Polyomino P : tab) {
			System.out.println();
			P.afficheConsole();
		}
		Polyomino[] tab2 = {tab[1]};
		creerFenetre(tab2, 100, 2);
	}
}
