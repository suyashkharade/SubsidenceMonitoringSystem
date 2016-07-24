package impose;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImposeImageMain {
	
	
	
	static File path;// base path of the images

	public static void main(String args[]) throws IOException
	{
			// load source images
			
		
		    BufferedImage overlay = ImageIO.read(new File(path,"/home/aniket/Multi_Inter_Phase_Image__.png"));
			BufferedImage image = ImageIO.read(new File(path,  "/home/aniket/Multi_Inter_Mag_Image__.png"));
            
			// create the new image, canvas size is the max. of both image sizes
			int w = Math.max(image.getWidth(), overlay.getWidth());
			int h = Math.max(image.getHeight(), overlay.getHeight());
			BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

			// paint both images, preserving the alpha channels
			Graphics g = combined.getGraphics();
			
			g.drawImage(image,0, 0, null);
			g.drawImage(overlay, 0, 0, null);
            
			// Save as new image
			ImageIO.write(combined, "PNG", new File(path,"/home/aniket/Su3bytecombined.png"));
	}
}
