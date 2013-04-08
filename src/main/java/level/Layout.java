package level;

import gui.GameWindow;

import java.awt.Color;
import java.awt.Point;

/**
 * Map layout. Drawn beneath actors. Stores level layout data.
 * @author Milo
 * @since 8 April 2013
 */
public class Layout {
	
	/** The tile map. TODO change to images **/
	private Color[][] tiles = new Color[GameWindow.GRID_X_SPACES][GameWindow.GRID_Y_SPACES];
	
	/** The start location for the player on this map **/
	private Point playerStartLocation = new Point(1, 1);
	
	/**
	 * Creates a new map and fills it with tiles.
	 * TODO Make this read from a file.
	 */
	public Layout() {
		for (int row = 0; row < tiles.length; row++)
			for (int column = 0; column < tiles[row].length; column++)
				if (row == 0 ||
					row == tiles.length - 1 ||
					column == 0 ||
					column == tiles[row].length - 1)
					tiles[row][column] = Color.darkGray;
				else
					tiles[row][column] = Color.lightGray;
	}
	
	/** @return The start location for the player on this map **/
	public Point getStartLocation() { return playerStartLocation; }
	
	/**
	 * Gets the tile to draw at a certain location
	 * @param x - The x-coordinate of the desired tile
	 * @param y - The y-coordinate of the desired tile
	 * @return The tile at a given location, or a blank one if the coordinates are invalid.
	 */
	public Color tileAt(int x, int y) {
		if (x >= 0 && x < tiles.length &&
			y >= 0 && y < tiles[0].length)
			return tiles[x][y];
		return Color.black;
	}
}
