package loader;

import java.awt.Image;
import java.awt.Toolkit;

import data.Files;
import data.Images;

/**
 * Loads images from files.
 * @author Milo
 * @since 9 April 2013
 */
public class ImageLoader {
	
	/** The image extension that all images use **/
	public static final String IMAGE_EXTENSION = ".png";
	
	/** The toolkit to use to load images **/
	private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	/**
	 * Regex that matches image names that are specific rather than generic.
	 * Example: "floor_00" or "floor_stone_00" will match, but "floor" won't.
	 */
	private static String specificImageRegex = "[a-z]+(_[a-z]+)?_[0-9]{2}";
	
	/**
	 * Loads an image of the type specified ("floor", "floor_stone", etc.)
	 * If a specific image is provided (i.e. "floor_00"), it will load that one.
	 * @param type - The type name ("floor", etc.), or the full file name ("floor_00").
	 * @return - The loaded image.
	 */
	public static Image loadImage(String type) {
		if (Images.TYPE_COUNTS.get(type) == null || type.matches(specificImageRegex)) {
			return fetchImage(type);
		}
		else {
			int specific = (int)(Math.random()*Images.TYPE_COUNTS.get(type));
			String specificString;
			if (specific < 10)
				specificString = "0" + specific;
			else
				specificString = "" + specific;
			
			return fetchImage(type + "_" + specificString);
		}
	}
	
	/**
	 * Loads an image from a file.
	 * @param fileName - The file name, without the extension.
	 * @return - The loaded image.
	 */
	private static Image fetchImage(String fileName) {
		return toolkit.getImage(Files.IMAGES + fileName + IMAGE_EXTENSION);
	}
}
