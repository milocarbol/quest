package loader;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import entity.Feature;
import entity.actor.Monster;

/**
 * Data structure containing room data (floor tiles, features, etc.).
 * @author Milo Carbol
 * @since 11 April 2013
 */
public class RoomData {

	/** The tiles in the room **/
	public final Image[][] tiles;
	
	/** The features in the room **/
	public final Feature[][] features;
	
	/** The start location for the player on this map **/
	public final Point playerStartLocation;
	
	/** The monsters on this map **/
	public final List<Monster> monsters;
	
	/**
	 * Creates a new set of room data.
	 * @param tiles - The tiles in the room
	 * @param features - The features in the room.
	 * @param playerStartLocation - The place where the player starts.
	 * @param monsters - The monsters in this room.
	 */
	public RoomData(Image[][] tiles,
					Feature[][] features,
					Point playerStartLocation,
					List<Monster> monsters) {
		this.tiles = tiles;
		this.features = features;
		this.playerStartLocation = playerStartLocation;
		this.monsters = monsters;
	}
}
