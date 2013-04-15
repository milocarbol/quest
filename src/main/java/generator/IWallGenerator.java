package generator;

import java.awt.Point;
import java.util.List;

/**
 * Generates a random wall pattern.
 * @author Milo Carbol
 * @since 14 April 2013
 */
public interface IWallGenerator {

	/**
	 * Generates a random wall pattern.
	 * @param wallType - The type of wall to use
	 * @return The list of wall locations
	 */
	public List<Point> generateWalls(String wallType);
}
