package rcp.manticora.services;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.xerces.impl.dv.util.Base64;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;


public class ImageTest {

	@Test
	public void testImagePuf() throws IOException {
		Display display = new Display();
		final Image image = new Image(display, "c:\\red-dot.png");
		
		ImageData imageData = image.getImageData();
		byte[] data = imageData.data;
		String imageString = new String(Base64.encode(data));
		System.out.println("III> " + imageString);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] {imageData};
		loader.save(bos, SWT.IMAGE_PNG);
		byte[] imageBytes = bos.toByteArray();
		System.out.println("YYY> " + new String(imageBytes));
		
		BufferedImage img = ImageIO.read(new File("c:\\red-dot.png"));
        BufferedImage newImg;
        String imgstr;
        imgstr = encodeToString(img, "png");
        System.out.println(imgstr);
	}
	
	
	/**
     * Encode image to string
     * @param image The image to encode
     * @param type jpeg, bmp, ...
     * @return encoded string
     */
	public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            
            System.out.println("BBB> " + new String(imageBytes));

//            BASE64Encoder encoder = new BASE64Encoder();
            imageString = Base64.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
	
	/**
     * Decode string to image
     * @param imageString The string to decode
     * @return decoded image
     */
    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
        	imageByte = Base64.decode(imageString);
//            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
