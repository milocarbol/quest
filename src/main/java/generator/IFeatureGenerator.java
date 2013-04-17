package generator;

import java.awt.Point;
import java.util.List;

/**
 * Generates a random feature pattern.
 * @author Milo Carbol
 * @since 14 April 2013
 */
public interface IFeatureGenerator {

	/**
	 * Generates a random pattern of the feature.
	 * @param featureType - The type of feature to use
	 * @return The list of locations to place that feature
	 */
	public List<Point> generateFeatures(String featureType);
}
