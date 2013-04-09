package level;

import io.LayoutLoader;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import data.Files;

/**
 * Map layout. Drawn beneath actors. Stores level layout data.
 * @author Milo
 * @since 8 April 2013
 */
public class Layout {
	
	/** The test map file **/
	public static final String TEST_MAP = Files.MAPS + "testmap.map";
	
	/** The tile map. **/
	private Image[][] tiles;
	
	/** The start location for the player on this map **/
	private Point playerStartLocation = new Point(1, 1);
	
	/**
	 * Creates a new map and fills it with tiles.
	 */
	public Layout(LayoutLoader loader) {
		try {
			tiles = loader.loadMap(TEST_MAP);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** @return The start location for the player on this map **/
	public Point getStartLocation() { return playerStartLocation; }
	
	/**
	 * Gets the tile to draw at a certain location
	 * @param row - The x-coordinate of the desired tile
	 * @param column - The y-coordinate of the desired tile
	 * @return The tile at a given location, or null if the coordinates are invalid.
	 */
	public Image tileAt(int row, int column) {
		if (row >= 0 && row < tiles.length &&
			column >= 0 && column < tiles[0].length)
			return tiles[row][column];
		return null;
	}
}
