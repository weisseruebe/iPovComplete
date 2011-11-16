package de.rettig.ipov.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

import de.rettig.ipov.ImageConverter;
import junit.framework.TestCase;

public class ImageConverterTest extends TestCase {
	
	@Test
	public void testConvert() throws IOException{
		BufferedImage image = ImageIO.read(new File("./images/firstBlack.bmp"));
		byte[] ret = ImageConverter.convert(image);
		byte[] firstBlack = new byte[64*2];
		firstBlack[0] = (byte) 0xFF;
		firstBlack[1] = (byte) 0xFF;
		firstBlack[2] = 1;
		Assert.assertArrayEquals(firstBlack, ret);
	}
	
}
