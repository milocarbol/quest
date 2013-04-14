package loader;

import java.awt.Image;

import data.Images;

/**
 * Checks whether a normal image or an edged variant should be used based on a feature's surroundings.
 * @author Milo Carbol
 * @since 13 April 2013
 */
public class ImageTypeChecker {

	/** The grid of feature type strings **/
	private String[][] featureTypes;
	
	/**
	 * Creates a new ImageTypeChecker.
	 * @param featureTypes - The grid of feature type strings.
	 */
	public ImageTypeChecker(String[][] featureTypes) {
		this.featureTypes = featureTypes;
	}
	
	/**
	 * Computes what image should be used: an edged variant or a regular one.
	 * @param featureType - The feature type we're checking
	 * @param column - The feature's x-coordinate
	 * @param row - The feature's y-coordinate
	 * @return The image we should use
	 */
	public Image computeImage(String featureType, int column, int row) {
		if (Images.EDGED.contains(featureTypes[column][row]))
			return ImageLoader.loadImage(featureTypes[column][row] + getPositionString(featureTypes[column][row], column, row));
		else
			return ImageLoader.loadImage(featureTypes[column][row]);
	}
	
	/**
	 * Returns the correct suffix for edged features.
	 * @param featureType - The feature base type (i.e. "water")
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return The correct suffix for edged features
	 */
	private String getPositionString(String featureType, int column, int row) {
		if (isTopLeft(featureType, column, row))
			return "_tl";
		else if (isTopEdge(featureType, column, row))
			return "_t";
		else if (isTopRight(featureType, column, row))
			return "_tr";
		else if (isRightEdge(featureType, column, row))
			return "_r";
		else if (isBottomRight(featureType, column, row))
			return "_br";
		else if (isBottomEdge(featureType, column, row))
			return "_b";
		else if (isBottomLeft(featureType, column, row))
			return "_bl";
		else if (isLeftEdge(featureType, column, row))
			return "_l";
		else if (isInverseTopLeft(featureType, column, row))
			return "_itl";
		else if (isInverseTopRight(featureType, column, row))
			return "_itr";
		else if (isInverseBottomRight(featureType, column, row))
			return "_ibr";
		else if (isInverseBottomLeft(featureType, column, row))
			return "_ibl";
		else
			return "";
	}
	
	/**
	 * Checks if a feature is the top left corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is the top left corner of the set
	 */
	private boolean isTopLeft(String featureType, int column, int row) {
		return isPosition(featureType, column, row, false, true, true, false);
	}
	
	/**
	 * Checks if a feature is a top edge of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is a top edge of the set
	 */
	private boolean isTopEdge(String featureType, int column, int row) {
		return isPosition(featureType, column, row, false, true, true, true);
	}
	
	/**
	 * Checks if a feature is the top right corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is the top right corner of the set
	 */
	private boolean isTopRight(String featureType, int column, int row) {
		return isPosition(featureType, column, row, false, false, true, true);
	}
	
	/**
	 * Checks if a feature is a right edge of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is a right edge of the set
	 */
	private boolean isRightEdge(String featureType, int column, int row) {
		return isPosition(featureType, column, row, true, false, true, true);
	}
	
	/**
	 * Checks if a feature is the bottom right corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is the bottom right corner of the set
	 */
	private boolean isBottomRight(String featureType, int column, int row) {
		return isPosition(featureType, column, row, true, false, false, true);
	}
	
	/**
	 * Checks if a feature is a bottom edge of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is a bottom edge of the set
	 */
	private boolean isBottomEdge(String featureType, int column, int row) {
		return isPosition(featureType, column, row, true, true, false, true);
	}

	/**
	 * Checks if a feature is the bottom left corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is the bottom left corner of the set
	 */
	private boolean isBottomLeft(String featureType, int column, int row) {
		return isPosition(featureType, column, row, true, true, false, false);
	}

	/**
	 * Checks if a feature is a left edge of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is a left edge of the set
	 */
	private boolean isLeftEdge(String featureType, int column, int row) {
		return isPosition(featureType, column, row, true, true, true, false);
	}
	
	/**
	 * Checks if a feature is an inverse top left corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is an inverse top left corner of the set
	 */
	private boolean isInverseTopLeft(String featureType, int column, int row) {
		return isPosition(featureType, column, row, true, true, true, true, false, true, true, true);
	}
	
	/**
	 * Checks if a feature is an inverse top right corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is an inverse top right corner of the set
	 */
	private boolean isInverseTopRight(String featureType, int column, int row) {
		return isPosition(featureType, column, row, true, true, true, true, true, true, false, true);
	}
	
	/**
	 * Checks if a feature is an inverse bottom left corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is an inverse bottom left corner of the set
	 */
	private boolean isInverseBottomLeft(String featureType, int column, int row) {
		return isPosition(featureType, column, row, true, true, false, true, true, true, true, true);
	}
	
	/**
	 * Checks if a feature is an inverse bottom right corner of a set of those features.
	 * @param featureType - The feature type string
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @return True if the feature is an inverse bottom right corner of the set
	 */
	private boolean isInverseBottomRight(String featureType, int column, int row) {
		return isPosition(featureType, column, row, false, true, true, true, true, true, true, true);
	}
	
	/**
	 * Checks if a feature's surroundings matches the parameters.
	 * @param featureType - The feature's type
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param top - If the feature above should match this feature type
	 * @param right - If the feature to the right should match this feature type
	 * @param bottom - If the feature below should match this feature type
	 * @param left - If the feature to the left should match this feature type
	 * @return True if the feature's surroundings match the parameters
	 */
	private boolean isPosition(	String featureType, int column, int row,
								boolean top, boolean right, boolean bottom, boolean left) {
		return	featureTypes[column][row - 1].equals(featureType) == top &&
				featureTypes[column + 1][row].equals(featureType) == right &&
				featureTypes[column][row + 1].equals(featureType) == bottom &&
				featureTypes[column - 1][row].equals(featureType) == left;
	}
	
	/**
	 * Checks if a feature's surroundings matches the parameters.
	 * @param featureType - The feature's type
	 * @param column - The x-coordinate of the feature
	 * @param row - The y-coordinate of the feature
	 * @param topLeft - If the feature above and to the left should match this feature type
	 * @param top - If the feature above should match this feature type
	 * @param topRight - If the feature above and to the right should match this feature type
	 * @param right - If the feature to the right should match this feature type
	 * @param bottomRight - If the feature below and to the right should match this feature type
	 * @param bottom - If the feature below should match this feature type
	 * @param bottomLeft - If the feature below and to the left should match this feature type
	 * @param left - If the feature to the left should match this feature type
	 * @return True if the feature's surroundings match the parameters
	 */
	private boolean isPosition(	String featureType, int column, int row,
						boolean topLeft, boolean top, boolean topRight,
						boolean right, boolean bottomRight, boolean bottom,
						boolean bottomLeft, boolean left) {
		return	featureTypes[column - 1][row - 1].equals(featureType) == topLeft &&
				featureTypes[column][row - 1].equals(featureType) == top &&
				featureTypes[column + 1][row - 1].equals(featureType) == topRight &&
				featureTypes[column + 1][row].equals(featureType) == right &&
				featureTypes[column + 1][row + 1].equals(featureType) == bottomRight &&
				featureTypes[column][row + 1].equals(featureType) == bottom &&
				featureTypes[column - 1][row + 1].equals(featureType) == bottomLeft &&
				featureTypes[column - 1][row].equals(featureType) == left;
	}
}
