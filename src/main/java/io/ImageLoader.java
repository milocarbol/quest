package io;

import java.awt.Image;
import java.awt.Toolkit;

import data.Files;

/**
 * Loads images from files.
 * @author Milo
 * @since 9 April 2013
 */
public class ImageLoader {
	
	public static final String IMAGE_EXTENSION = ".png";
	
	/** The toolkit to use to load images **/
	private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	/**
	 * Loads an image from the images directory.
	 * @param fileName - The name of the .PNG file, without the extension.
	 * @return The loaded image.
	 */
	public static Image loadImage(String fileName) {
		return toolkit.getImage(Files.IMAGES + fileName + IMAGE_EXTENSION);
	}
}
