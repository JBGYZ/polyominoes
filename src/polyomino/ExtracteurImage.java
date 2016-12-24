package polyomino;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ExtracteurImage {
	
	public void ecrire(JComponent pan, String chemin) {
		File f = new File(chemin); //On ouvre le fichier image a creer
		//On va faire dessiner Image2dComponent et enregistrer
		//le r�sultat dans le BufferImage
		BufferedImage buffer = new BufferedImage(
				pan.getPreferredSize().width,
				pan.getPreferredSize().height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = buffer.getGraphics();
		pan.paint(g);
		//On �crit le BufferImage dans le fichier image
		try {
			ImageIO.write(buffer, "JPG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
