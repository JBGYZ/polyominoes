package polyomino;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ExtracteurImage {
	
	public void ecrire(JComponent pan, String chemin) {
		File f = new File(chemin); 
		// pan graphics are saved in the buffer
		BufferedImage buffer = new BufferedImage(
				pan.getPreferredSize().width,
				pan.getPreferredSize().height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = buffer.getGraphics();
		pan.paint(g);
		// the buffer is saved in the image file
		try {
			ImageIO.write(buffer, "JPG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
