package de.rettig.ipov;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageConverter {
	static final int H = 16;
	static final int W = 64;
	static final int BLACK = -16777216;
	static final int WHITE = -1;
	
	public static void printPixelARGB(int pixel) {
	    int alpha = (pixel >> 24) & 0xff;
	    int red =   (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue =  (pixel) & 0xff;
	    System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
	  }
	
	
	public static byte[] convert(BufferedImage image){
		if(image.getHeight()!=H){
			throw new IllegalArgumentException("Imageheight must be 16");
		}
		byte[] buffer = new byte[2*W];
		int bufferPos = 0;
		for (int x=0;x<image.getWidth();x++){
			for (int y=0;y<image.getHeight();y+=8){
				for (int yB=0;yB<8;yB++){
					int rgb = image.getRGB(x, yB+y);
					if (rgb == BLACK){
						byte mask = (byte) (128 >> yB);
						buffer[bufferPos] |= mask;
					}
				}
				bufferPos++;
			}	
		}
		return buffer;
	}
	
	/****
	 * Creates a String represantation of the image which can be used to initialize a byte array
	 * in the arduino source code
	 * @param image
	 * @return
	 */
	public static String createCArrayString(BufferedImage image){
		byte[] buf = convert(image);
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=0;i<buf.length;i++){
			stringBuffer.append((buf[i] & 0xff) +",");
			if (i%2==1){
				stringBuffer.append("\n");
			}
		}
		return stringBuffer.toString();
	}
	
	public static void main(String[] args) {
		try {
			byte[] b = convert(ImageIO.read(new File("./images/auge.bmp")));
			for (byte bb:b){
				System.out.println(bb);
				
			}
				
			//System.out.println(createCArrayString(ImageIO.read(new File("./images/auge.bmp"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
