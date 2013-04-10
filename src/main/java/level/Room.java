package level;

import io.RoomLoader;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import data.Files;

/**
 * Map layout for a room. Drawn beneath actors. Stores room layout data.
 * @author Milo
 * @since 8 April 2013
 */
public class Room {
	
	/** The test map file **/
	public static final String TEST_ROOM = Files.MAPS + "testmap.map";
	
	/** The tile map. **/
	private Image[][] tiles;
	
	/** The start location for the player on this map **/
	private Point playerStartLocation = new Point(1, 1);
	
	/**
	 * Creates a new room and fills it with tiles.
	 */
	public Room(RoomLoader loader) {
		try {
			tiles = loader.loadRoom(TEST_ROOM);
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
