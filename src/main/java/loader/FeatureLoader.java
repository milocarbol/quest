package loader;

import control.Game;
import data.Images;
import entity.Feature;
import gui.GameWindow;

/**
 * Loads features from strings.
 * @author Milo Carbol
 * @since 14 April 2013
 */
public class FeatureLoader {

	/** The game these features are for **/
	Game game;
	
	/**
	 * Creates a new feature loader.
	 * @param game - The game these features are for
	 */
	public FeatureLoader(Game game) {
		this.game = game;
	}
	
	/**
	 * Creates the true feature grid from a string grid.
	 * @param featureGrid - The string grid
	 * @return The true grid of Feature objects
	 */
	public Feature[][] createFeatures(String[][] featureGrid) {
		ImageTypeChecker imageTypeChecker = new ImageTypeChecker(featureGrid);
		
		Feature[][] features = new Feature[GameWindow.GRID_COLUMNS][GameWindow.GRID_ROWS];
		for (int row = 0; row < GameWindow.GRID_ROWS; row++)
			for (int column = 0; column < GameWindow.GRID_COLUMNS; column++)
				if (!featureGrid[column][row].equals(Images.NULL_TILE))
					features[column][row] = new Feature(column, row, imageTypeChecker.computeImage(featureGrid[column][row], column, row), null, game);
		return features;
	}
}
