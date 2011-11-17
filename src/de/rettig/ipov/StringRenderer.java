package de.rettig.ipov;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class StringRenderer {

	private BufferedImage image;
	private Graphics graphics;
	
	public StringRenderer(){
		image = new BufferedImage(64,16,BufferedImage.TYPE_BYTE_BINARY);
		graphics = image.getGraphics();
	}	
	
	public BufferedImage getBitmap(String string){
		graphics.setColor(java.awt.Color.WHITE);
		graphics.fillRect(0, 0, 64, 16);
		graphics.setColor(java.awt.Color.BLACK);
		graphics.setFont(new Font("Arial", Font.BOLD, 10));
		graphics.drawString(string, 0, 12);
	
		return image;
	}
	
	public static void main(String[] args) {
		StringRenderer stringRenderer = new StringRenderer();
		BufferedImage img = stringRenderer.getBitmap("Hallo");
		try {
			ImageIO.write(img, "png", new File("test.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
