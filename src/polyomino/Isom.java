package polyomino;

public class Isom {

	public static void afficherTuiles(boolean[][] tuiles) {
		String s = "";
		for (int j = tuiles[0].length-1; j >=0; j--) {
			for (int i = 0; i < tuiles.length; i++){
				if (tuiles[i][j]) {
					s += "1";
				} else {
					s += "0";
				}
			}
			System.out.println(s);
			s = "";
		}
	}
	
	public static void main(String[] args){
		boolean[][] tuiles = {{true, false, false}, {false, false, true}, {true, true, true}, {false, true, true}};
		afficherTuiles(tuiles);
		System.out.println("Rotation d'angle 0");
		afficherTuiles(Polyomino.rotation(tuiles,0));
		System.out.println("Rotation d'angle pi/2");
		afficherTuiles(Polyomino.rotation(tuiles,1));
		System.out.println("Rotation d'angle pi");
		afficherTuiles(Polyomino.rotation(tuiles,2));
		System.out.println("Rotation d'angle 3pi/2");
		afficherTuiles(Polyomino.rotation(tuiles,3));
		System.out.println("Symétrie par rapport à x");
		afficherTuiles(Polyomino.symetrieX(tuiles));
	}

}
