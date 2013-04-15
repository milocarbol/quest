package level;


import java.awt.Image;
import java.io.IOException;
import java.util.List;

import loader.IRoomLoader;
import loader.RoomData;
import control.Game;
import data.Files;
import entity.Feature;
import entity.actor.Monster;
import entity.actor.Player;
import gui.GameWindow;

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
	
	/** Features of the room **/
	private Feature[][] features;
	
	/** The start location for the player on this map **/
	private Player player;
	
	/** The monsters on this map **/
	private List<Monster> monsters;
	
	/**
	 * Creates a new room and fills it with tiles and features.
	 * @throws RuntimeException if the tiles and features loaded from the file don't match, or if the room can't be loaded.
	 */
	public Room(IRoomLoader loader, Game game) {
		try {
			RoomData roomData = loader.loadRoom();
			tiles = roomData.tiles;
			features = roomData.features;
			player = roomData.player;
			monsters = roomData.monsters;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** @return The player on this map **/
	public Player getPlayer() { return player; }
	
	/** @return The monsters on this map **/
	public List<Monster> getMonsters() { return monsters; }
	
	/**
	 * Gets the tile to draw at a certain location
	 * @param column - The x-coordinate of the desired tile
	 * @param row - The y-coordinate of the desired tile
	 * @return The tile at a given location, or null if the coordinates are invalid.
	 */
	public Image tileAt(int column, int row) {
		if (tileIsValid(column, row))
			return tiles[column][row];
		return null;
	}
	
	/**
	 * Gets the image for a feature to draw at a certain location
	 * @param column - The x-coordinate of the desired feature
	 * @param row - The y-coordinate of the desired feature
	 * @return The image for the feature at a given location, or null if there isn't one.
	 */
	public Image featureImageAt(int column, int row) {
		if (tileIsValid(column, row) && features[column][row] != null)
			return features[column][row].getImage();
		return null;
	}
	
	/**
	 * Gets the feature at a certain location
	 * @param column - The x-coordinate of the desired feature
	 * @param row - The y-coordinate of the desired feature
	 * @return The feature at a given location, or null if the coordinates are invalid.
	 */
	public Feature featureAt(int column, int row) {
		if (tileIsValid(column, row))
			return features[column][row];
		return null;
	}
	
	private boolean tileIsValid(int column, int row) {
		return	column >= 0 && column < GameWindow.GRID_COLUMNS &&
				row >= 0 && row < GameWindow.GRID_ROWS;
	}
}
