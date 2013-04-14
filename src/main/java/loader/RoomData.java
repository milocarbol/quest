package loader;

import java.awt.Image;
import java.util.List;

import entity.Feature;
import entity.actor.Monster;
import entity.actor.Player;

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
	
	/** The player on this map **/
	public final Player player;
	
	/** The monsters on this map **/
	public final List<Monster> monsters;
	
	/**
	 * Creates a new set of room data.
	 * @param tiles - The tiles in the room
	 * @param features - The features in the room.
	 * @param player - The player in this room.
	 * @param monsters - The monsters in this room.
	 */
	public RoomData(Image[][] tiles,
					Feature[][] features,
					Player player,
					List<Monster> monsters) {
		this.tiles = tiles;
		this.features = features;
		this.player = player;
		this.monsters = monsters;
	}
}
